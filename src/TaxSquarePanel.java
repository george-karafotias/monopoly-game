import java.awt.BorderLayout;
import javax.swing.JLabel;

public class TaxSquarePanel extends SquarePanel {

	private static final long serialVersionUID = 1L;

	public TaxSquarePanel(int percentage) {
		super();
		type = TAX;
		
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel taxLabel = new JLabel("INCOME TAX " + percentage + "%");
		taxLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(taxLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}
}
