import java.awt.BorderLayout;
import javax.swing.JLabel;

public class ChanceSquarePanel extends SquarePanel {

	private static final long serialVersionUID = 1L;

	public ChanceSquarePanel() {
		super();
		type = CHANCE;
		
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel chanceLabel = new JLabel("CHANCE?");
		chanceLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(chanceLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}
}
