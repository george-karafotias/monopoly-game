import java.awt.BorderLayout;
import javax.swing.JLabel;

public class InJailSquarePanel extends SquarePanel {
	
	private static final long serialVersionUID = 1L;

	public InJailSquarePanel() {
		super();
		type = INJAIL;
		
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel inJailLabel = new JLabel("IN JAIL/VISIT");
		inJailLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(inJailLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}
}
