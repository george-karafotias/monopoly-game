import java.awt.BorderLayout;
import javax.swing.JLabel;

public class GoToJailSquarePanel extends SquarePanel {

	private static final long serialVersionUID = 1L;

	public GoToJailSquarePanel() {
		super();
		type = GOTOJAIL;
		
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel goToJailLabel = new JLabel("GO TO JAIL");
		goToJailLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(goToJailLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}
}
