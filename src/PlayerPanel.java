import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import core.Player;

public class PlayerPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Player player;
	private JLabel amountLabel;
	private JLabel propertiesLabel;
	private JLabel winsLabel;

	public PlayerPanel(Player player) {
		this.player = player;
		this.setLayout(new BorderLayout());
		
		JPanel p1ayerNamePanel = new JPanel();
		p1ayerNamePanel.setBackground(player.getColor());
		JLabel playerNameLabel = new JLabel(player.getName());
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
		p1ayerNamePanel.add(playerNameLabel);
	
		JPanel playerInfoPanel = new JPanel();
		playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
		playerInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		amountLabel = new JLabel("AMOUNT: " + String.valueOf(player.getAmount()));
		propertiesLabel = new JLabel("PROPERTIES: " + String.valueOf(player.getProperties().size()));
		winsLabel = new JLabel("WINS: " + String.valueOf(player.getWins()));
		playerInfoPanel.add(amountLabel);
		playerInfoPanel.add(propertiesLabel);
		playerInfoPanel.add(winsLabel);
		
		this.add(p1ayerNamePanel, BorderLayout.NORTH);
		this.add(playerInfoPanel, BorderLayout.CENTER);
	}
	
	public void refresh() {
		amountLabel.setText("AMOUNT: " + String.valueOf(player.getAmount()));
		propertiesLabel.setText("PROPERTIES: " + String.valueOf(player.getProperties().size()));
		winsLabel.setText("WINS: " + String.valueOf(player.getWins()));
	}
}
