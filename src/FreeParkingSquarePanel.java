import java.awt.BorderLayout;
import javax.swing.JLabel;

public class FreeParkingSquarePanel extends SquarePanel {

	private static final long serialVersionUID = 1L;

	public FreeParkingSquarePanel() {
		super();
		type = FREEPARKING;
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel freeParkingLabel = new JLabel("FREE PARKING");
		freeParkingLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(freeParkingLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}
}
