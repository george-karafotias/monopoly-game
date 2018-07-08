import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import core.Player;
import core.Property;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final int defaultMessageDuration = 5000;
	
	private JLabel feedbackLabel;
	private ArrayList<SquarePanel> squares;
	private ArrayList<Player> players;
	private ArrayList<PlayerPanel> playerPanels;

	public MainWindow() {
		this.setSize(720,720);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		players = createPlayers();
		
		init();
	}
	
	private void init() {
		squares = createSquares();
		this.add(createSouthPanel(squares), BorderLayout.SOUTH);
		this.add(createWestPanel(squares), BorderLayout.WEST);
		this.add(createNorthPanel(squares), BorderLayout.NORTH);
		this.add(createEastPanel(squares), BorderLayout.EAST);
		this.add(createCenterPanel(players), BorderLayout.CENTER);
		
		startGame(squares, players);
		
		if (askForNewGame()) {
			resetPlayers();
			init();
		}
	}
	
	private void startGame(ArrayList<SquarePanel> squares, ArrayList<Player> players) {
		boolean gameIsOver = false;
		int whoIsPlaying = new Random().nextInt(players.size());
		int rounds = 0;
		
		if (players != null && players.size() > 0) {
			for (int i=0; i<players.size(); i++) {
				movePlayerToNewPosition(players.get(i), 0);
				refreshPlayerPanel(players.get(i));
			}
		}
		
		while (!gameIsOver) {
			feedbackLabel.setText("PLAYER " + players.get(whoIsPlaying).getName() + " IS PLAYING...");
			delay(4000);
			rollDice(players.get(whoIsPlaying), squares);
			delay(2000);
			move(players.get(whoIsPlaying), squares);
			
			whoIsPlaying = (whoIsPlaying == 0) ? 1 : 0;
			rounds++;
			gameIsOver = evaluateGameOver(rounds);
		}
		
		announceWinner();
	}
	
	private boolean evaluateGameOver(int rounds) {
		if (rounds >= 100)
			return true;
		
		if (players != null && players.size() > 0) {
			for (int i=0; i<players.size(); i++) {
				if (players.get(i).getAmount() < 0)
					return true;
			}
		}
		return false;
	}
	
	private void announceWinner() {
		if (players != null && players.size() > 0) {
			Player winner = null;
			double maxAmount = -99999;
			for (int i=0; i<players.size(); i++) {
				if (players.get(i).getAmount() > maxAmount) {
					maxAmount = players.get(i).getAmount();
					winner = players.get(i);
				}	
			}
			if (winner != null) {
				winner.youWon();
				displayMessageDialog(winner.getName() + " is the WINNER!!!", defaultMessageDuration);
			}
		}
	}
	
	private boolean askForNewGame() {
		int response = JOptionPane.showConfirmDialog(this, "Do you want to play another game?");
		if (response == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}
	
	private void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception ex) {
			
		}
	}
	
	private void movePlayerToNewPosition(Player player, int dice) {
		int playerCurrentPosition = player.getPosition();
		//squares.get(playerCurrentPosition).setBackground(Color.white);
		squares.get(playerCurrentPosition).leftByPlayer(player);
		if (playerCurrentPosition + dice >= squares.size()) {
			evaluateStartPass(player);
		}
		int playerNewPosition = (playerCurrentPosition + dice)%squares.size();
		player.setPosition(playerNewPosition);
		//squares.get(playerNewPosition).setBackground(player.getColor());
		squares.get(playerNewPosition).enteredByPlayer(player);
	}
	
	private void rollDice(Player player, ArrayList<SquarePanel> squares) {
		int dice = new Random().nextInt(6) + 1;
		feedbackLabel.setText(player.getName() + ": " + String.valueOf(dice));
		
		if (player.isInJail()) {
			evaluateInJail(player, dice);
			return;
		}
		
		movePlayerToNewPosition(player, dice);
	}
	
	private void displayMessageDialog(String message, int duration) {
		JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = pane.createDialog(this, "");
        Timer timer = new Timer(duration, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
	}
	
	private void evaluateStartPass(Player player) {
		player.setAmount(player.getAmount() + 200);
		refreshPlayerPanel(player);
	}
	
	private void evaluateChance(Player player) {
		int whatToDo = new Random().nextInt(2);
		if (whatToDo == 0) {
			int amountToGain = ((int)((20 - 1) * Math.random() + 1)) * 10;
			player.setAmount(player.getAmount() + amountToGain);
			displayMessageDialog(player.getName() + " GAINED " + amountToGain + "!", defaultMessageDuration);
		} else {
			int amountToLose = ((int)((30 - 1) * Math.random() + 1)) * 10;
			player.setAmount(player.getAmount() - amountToLose);
			displayMessageDialog(player.getName() + " LOST " + amountToLose + ".", defaultMessageDuration);
		}
		refreshPlayerPanel(player);
	} 
	
	private void evaluateProperty(Player player, PropertySquarePanel square) {
		Property property = square.getProperty();
		
		if (playerOwnsProperty(player, property)) return;
		
		if (!playerCanBuyProperty(player, property)) {
			payRentToPropertyOwner(player, property);
			return;
		}
		
		if (player.getAmount() >= property.getValue()) {
			if (!player.isAuto()) {
				int response = JOptionPane.showConfirmDialog(this, "Do you want to pay " + square.getProperty().getValue() + " and buy property " + square.getProperty().getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					playerBoughtProperty(player, square);
				}
			} else {
				int response = new Random().nextInt(2);
				if (response == 1) {
					playerBoughtProperty(player, square);
				}
			}
		}
	}
	
	private void evaluateTax(Player player) {
		int tax = (int)(player.getAmount() * 0.1);
		player.setAmount(player.getAmount() - tax);
		displayMessageDialog(player.getName() + " IS PAYING " + tax + " AS A TAX.", defaultMessageDuration);
		refreshPlayerPanel(player);
	}
	
	private void evaluateGoToJail(Player player) {
		int playerNewPosition = -1;
		if (squares != null && squares.size() > 0) {
			int i = 0;
			while (i<squares.size() && playerNewPosition == -1) {
				if (squares.get(i).getType() == SquarePanel.INJAIL)
					playerNewPosition = i;
				i++;
			}
		}
		if (playerNewPosition != -1) {
			//squares.get(player.getPosition()).setBackground(Color.white);
			squares.get(player.getPosition()).leftByPlayer(player);
			player.setPosition(playerNewPosition);
			player.goToJail();
			//squares.get(playerNewPosition).setBackground(player.getColor());
			squares.get(playerNewPosition).enteredByPlayer(player);
		}
	}
	
	private void evaluateInJail(Player player, int dice) {
		int fine = 50;
		if (player.howLongInJail() >= 3) {
			payFineAndGoOutOfJail(player, fine, dice);
		} else {
			if (dice == 6) {
				player.outOfJail();
				movePlayerToNewPosition(player, dice);
			} else {
				int response = JOptionPane.showConfirmDialog(this, "Do you want to pay " + fine + " and get out of jail?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					payFineAndGoOutOfJail(player, fine, dice);
				}
			}
		}
		refreshPlayerPanel(player);
	}
	
	private void payFineAndGoOutOfJail(Player player, int fine, int dice) {
		player.setAmount(player.getAmount() - fine);
		player.outOfJail();
		movePlayerToNewPosition(player, dice);
	}
	
	private void payRentToPropertyOwner(Player player, Property property) {
		Player owner = getOwnerOfProperty(property);
		if (owner != null) {
			player.setAmount(player.getAmount() - property.getValue());
			owner.setAmount(owner.getAmount() + property.getValue());
			displayMessageDialog(player.getName() + " IS PAYING " + property.getValue() + " RENT TO PLAYER " + owner.getName() + ".", defaultMessageDuration);
			refreshPlayerPanel(player);
			refreshPlayerPanel(owner);
		}
	}
	
	private Player getOwnerOfProperty(Property property) {
		if (players != null && players.size() > 0) {
			for (int i=0; i<players.size(); i++) {
				if (playerOwnsProperty(players.get(i), property))
					return players.get(i);
			}
		}
		return null;
	}
	
	private boolean playerOwnsProperty(Player player, Property property) {
		ArrayList<Property> playerProperties = player.getProperties();
		if (playerProperties != null && playerProperties.size() > 0) {
			for (int i=0; i<playerProperties.size(); i++) {
				if (playerProperties.get(i) == property)
					return true;
			}
		}
		return false;
	}
	
	private boolean playerCanBuyProperty(Player player, Property property) {
		if (players != null && players.size() > 0) {
			for (int i=0; i<players.size(); i++) {
				if (players.get(i) != player) {
					if (playerOwnsProperty(players.get(i), property))
						return false;
				}
			}
		}
		return true;
	}
	
	private void playerBoughtProperty(Player player, PropertySquarePanel propertyPanel) {
		Property property = propertyPanel.getProperty();
		player.addProperty(property);
		player.setAmount(player.getAmount() - property.getValue());
		propertyPanel.setOwner(player);
		refreshPlayerPanel(player);
	}
	
	private void refreshPlayerPanel(Player player) {
		int playerIndex = players.indexOf(player);
		playerPanels.get(playerIndex).refresh();
	}
	
	private void move(Player player, ArrayList<SquarePanel> squares) {
		SquarePanel currentPlayerPanel = squares.get(player.getPosition());
		int currentSquare = currentPlayerPanel.getType();
		if (currentSquare == SquarePanel.PROPERTY) {
			evaluateProperty(player, (PropertySquarePanel)currentPlayerPanel);
		} else if (currentSquare == SquarePanel.CHANCE) {
			evaluateChance(player);
		} else if (currentSquare == SquarePanel.TAX) {
			evaluateTax(player);
		} else if (currentSquare == SquarePanel.GOTOJAIL) {
			evaluateGoToJail(player);
		}
	}
	
	private ArrayList<SquarePanel> createSquares() {
		ArrayList<SquarePanel> squaresList = new ArrayList<SquarePanel>();
		
		squaresList.add(new StartSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("ARISTOTELOUS", 60)));
		squaresList.add(new PropertySquarePanel(new Property("EYZONON", 60)));
		squaresList.add(new TaxSquarePanel(10));
		squaresList.add(new PropertySquarePanel(new Property("KANARI", 80)));
		squaresList.add(new InJailSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("KONPOLEOS", 100)));
		squaresList.add(new PropertySquarePanel(new Property("KAMARA", 120)));
		squaresList.add(new ChanceSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("MITROPOLEOS", 160)));
		squaresList.add(new FreeParkingSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("PANORMOU", 220)));
		squaresList.add(new ChanceSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("PAPADAKI", 260)));
		squaresList.add(new PropertySquarePanel(new Property("KONTE", 260)));
		squaresList.add(new GoToJailSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("SKONDRA", 320)));
		squaresList.add(new PropertySquarePanel(new Property("SOUKATZIDI", 350)));
		squaresList.add(new ChanceSquarePanel());
		squaresList.add(new PropertySquarePanel(new Property("PANORAMA", 400)));
		
		return squaresList;
	}
	
	private JPanel createSouthPanel(ArrayList<SquarePanel> squares) {
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1,6));
		for (int i=5; i>=0; i--)
			southPanel.add(squares.get(i));
		return southPanel;
	}
	
	private JPanel createWestPanel(ArrayList<SquarePanel> squares) {
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(4,1));
		for (int i=9; i>5; i--)
			westPanel.add(squares.get(i));
		return westPanel;
	}
	
	private JPanel createNorthPanel(ArrayList<SquarePanel> squares) {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1,6));
		for (int i=10; i<=15; i++)
			northPanel.add(squares.get(i));
		return northPanel;
	}
	
	private JPanel createEastPanel(ArrayList<SquarePanel> squares) {
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(4,1));
		for (int i=16; i<20; i++)
			eastPanel.add(squares.get(i));
		return eastPanel;
	}
	
	private JPanel createCenterPanel(ArrayList<Player> players) {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2,1));
		
		JLabel monopolyLabel = new JLabel("MONOPOLY");
		monopolyLabel.setHorizontalAlignment(JLabel.CENTER);
		monopolyLabel.setFont(new Font("Arial", Font.BOLD, 28));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		
		JPanel playersPanel = new JPanel();
		playersPanel.setLayout(new GridLayout(1,players.size()));
		playerPanels = new ArrayList<PlayerPanel>();
		for (int i=0; i<players.size(); i++) {
			PlayerPanel playerPanel = new PlayerPanel(players.get(i));
			playerPanels.add(playerPanel);
			playersPanel.add(playerPanel);
		}
			
		JPanel feedbackPanel = new JPanel();
		feedbackLabel = new JLabel("WAITING TO START...");
		feedbackLabel.setForeground(Color.red);
		feedbackLabel.setFont(new Font("Arial", Font.BOLD, 18));
		feedbackPanel.add(feedbackLabel);
		
		infoPanel.add(playersPanel);
		infoPanel.add(feedbackPanel);
		
		centerPanel.add(monopolyLabel);
		centerPanel.add(infoPanel);
		
		return centerPanel;
	}
	
	private ArrayList<Player> createPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		Player player1 = new Player("YOU", Color.green, false);
		Player player2 = new Player("AUTO", Color.cyan, true);
		players.add(player1);
		players.add(player2);
		return players;
	}
	
	private void resetPlayers() {
		if (players != null && players.size() > 0) {
			for (int i=0; i<players.size(); i++)
				players.get(i).reset();
		}
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
}
