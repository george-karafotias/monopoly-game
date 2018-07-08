import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import core.Player;

public abstract class SquarePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final int START = 1;
	public static final int PROPERTY = 2;
	public static final int TAX = 3;
	public static final int INJAIL = 4;
	public static final int CHANCE = 5;
	public static final int FREEPARKING = 6;
	public static final int GOTOJAIL = 7;
	
	protected int type;
	protected Dimension dimension = new Dimension(120,120);
	protected ArrayList<Player> playersIn;
	protected JPanel positionPanel;
	
	protected SquarePanel() {
		this.playersIn = new ArrayList<Player>();
		this.setPreferredSize(dimension);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.setBackground(Color.white);
	}

	public int getType() {
		return type;
	}
	
	protected void createPositionPanel() {
		positionPanel = new JPanel();
		positionPanel.setBackground(Color.white);
		positionPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
	}
	
	protected void updatePositionPanel() {
		positionPanel.removeAll();
		
		if (playersIn != null && playersIn.size() > 0) {
			positionPanel.setLayout(new GridLayout(1, playersIn.size()));
		}
		
		for (int i=0; i<playersIn.size(); i++) {
			JPanel playerPanel = new JPanel();
			playerPanel.setBackground(playersIn.get(i).getColor());
			positionPanel.add(playerPanel);
		}
		
		positionPanel.revalidate();
		positionPanel.repaint();
		this.validate();
	}
	
	public void enteredByPlayer(Player player) {
		playersIn.add(player);
		updatePositionPanel();
	}
	
	public void leftByPlayer(Player player) {
		playersIn.remove(player);
		updatePositionPanel();
	}
}
