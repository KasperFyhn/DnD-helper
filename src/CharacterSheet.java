import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class CharacterSheet extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	String name;
	int hitPoints;
	
	//basic information on the character	
	JPanel headPanel = new JPanel( new GridLayout(2, 3) );
	SheetField[] headInfo = {
			new SheetField("Name"),
			new SheetField("Level"),
			new SheetField("Class"),
			new SheetField("Race"),
			new SheetField("Alignment"),
			new SheetField("Background"),			
	};

	//attributes
	JPanel attribPanel = new JPanel();
	SheetField[] attribInfo = {
			new SheetField("Strength"),
			new SheetField("Dexterity"),
			new SheetField("Constitution"),
			new SheetField("Intelligence"),
			new SheetField("Wisdom"),
			new SheetField("Charisma"),			
	};
	
	//skills
	JPanel skillsPanel = new JPanel();
	SheetField[] skillsInfo = {
			new SheetField("Acrobatics"),
			new SheetField("Animal Handling"),
			new SheetField("Arcana"),
			new SheetField("Athletics"),
			new SheetField("Deception"),
			new SheetField("History"),
			new SheetField("Insight"),
			new SheetField("Intimidation"),
			new SheetField("Investigation"),
			new SheetField("Medicine"),
			new SheetField("Nature"),
			new SheetField("Perception"),	
	};
	
	public CharacterSheet(String name) {
		
		for(SheetField field : headInfo) {
			headPanel.add(field);
		}
		headPanel.setMaximumSize( new Dimension(600, 40));
		add(headPanel);
		
		for(SheetField field : attribInfo) {
			attribPanel.add(field);
		}
		attribPanel.setLayout( new BoxLayout(attribPanel, BoxLayout.Y_AXIS) );
		add(attribPanel);

		
		setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
		setPreferredSize( new Dimension(450, 400) );

		
	}
	

	
	private static class SheetField extends JPanel {		

		private JLabel label = new JLabel();
		private JTextField value = new JTextField();
		private static Dimension fieldSize = new Dimension(100, 20);
		
		SheetField(String label){
			//set the label name
			setLabel(label);
			//fix layout
			value.setPreferredSize(fieldSize);
			value.setMaximumSize(fieldSize);
			value.setAlignmentX(RIGHT_ALIGNMENT);

			this.label.setAlignmentX(LEFT_ALIGNMENT);		
			
			setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
			setBorder( BorderFactory.createEmptyBorder(0, 3, 0, 3));
			setMaximumSize( new Dimension(190, 30));
			
			//add components
			add(this.label);
			add(Box.createHorizontalGlue());
			add(this.value);
		}
		
		public void setLabel(String newLabel) {
			label.setText(newLabel);
		}
		
		public int valueToInt() {			
			String str = value.getText();
			int value;			
			try {
				value = Integer.parseInt(str);
			}
			catch(NumberFormatException e) {
				value = 0;
			}
			return value;
		}
		
		public String valueToString() {			
			return value.getText();
		}
		
		
		
	}

}
