import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import core.Player;
import core.Property;

public class PropertySquarePanel extends SquarePanel {

	private static final long serialVersionUID = 1L;
	
	private Property property;

	public PropertySquarePanel(Property property) {
		super();
		this.property = property;
		type = PROPERTY;
		
		this.setLayout(new BorderLayout());
		createPositionPanel();
		JLabel propertyLabel = new JLabel(property.getName() + " " + property.getValue());
		propertyLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(propertyLabel, BorderLayout.CENTER);
		this.add(positionPanel, BorderLayout.SOUTH);
	}

	public Property getProperty() {
		return property;
	}
	
	public void setOwner(Player owner) {
		this.setBorder(BorderFactory.createLineBorder(owner.getColor(), 2));
	}
}
