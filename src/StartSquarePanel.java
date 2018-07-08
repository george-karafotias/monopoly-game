import java.awt.BorderLayout;
import javax.swing.JLabel;

public class StartSquarePanel extends SquarePanel {
	
	private static final long serialVersionUID = 1L;

	public StartSquarePanel() {
		super();
		type = START;
		
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel startLabel = new JLabel("START");
		startLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(startLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}
}
