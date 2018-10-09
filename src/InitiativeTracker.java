import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.*;

/**
 * This panel is a tool for keeping track of characters' and monsters' HP
 * and initiative in e.g. DnD.
 * @author Kasper Fyhn Jacobsen
 */
public class InitiativeTracker extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public InitiativeTracker() {
		
		setBorder( BorderFactory.createEtchedBorder());
		setPreferredSize( new Dimension(450, 400) );
		setLayout( new BorderLayout() );
		
		//add keylistener to the text field for new character panes
		newCharName.addKeyListener( new KeyListener() {
			@Override
			public void keyPressed(KeyEvent evt) {
				int key = evt.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
						addCharacterPane();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent evt) {
			}		
		});
		
		//add listeners to top panel buttons
		newCharacterButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCharacterPane();
			}		
		});
		
		sortButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				middlePanel.removeAll();			
				Collections.sort(characters);				
				for(CharacterPane character : characters) {
					middlePanel.add(character);
				}			
				revalidate();
			}		
		});
		
		saveButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				savePreset();
				revalidate();
			}		
		});
		
		loadButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadPreset("./presetTest.txt");
				revalidate();
			}		
		});
	
		//add stuff to the top panel and add the top panel
		newCharName.setPreferredSize( new Dimension(150, 30) ); 
		topPanel.add(newCharName);		
		topPanel.add(newCharacterButton);
		topPanel.add(sortButton);
		topPanel.add(saveButton);
		topPanel.add(loadButton);
		add(topPanel, BorderLayout.NORTH);
		
		//add stuff to the middle panel and add the middle panel
		middlePanel.setLayout( new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(15);
		add(scrollPane);
	}

	private LinkedList<CharacterPane> characters = new LinkedList<CharacterPane>(); //the list which holds all characters
	
	private JPanel topPanel = new JPanel( new FlowLayout() ); //the panel for holding the text field and "ADD"-button.
	private JButton newCharacterButton = new JButton("ADD");
	private JButton sortButton = new JButton("SORT");
	private JButton saveButton = new JButton("SAVE");
	private JButton loadButton = new JButton("LOAD");
	private JTextField newCharName = new JTextField(""); //a text field in which you write the name for the new character
	
	private JPanel middlePanel = new JPanel(); //the panel in which the character panes go
	private JScrollPane scrollPane = new JScrollPane(middlePanel); //and then it is put into a scroll pane
	
	/**
	 * 
	 * @return true if the preset was successfully written to the file and false if not
	 */
	public boolean savePreset() {
		try {
			File presetFile = new File("presetTest.txt");
			//"C:\\Users\\Kasper Fyhn Jacobsen\\eclipse-workspace\\DnD Helper\\bin\\preset.txt");			
			PrintWriter writer = new PrintWriter(presetFile, "UTF-8");
			
			for(CharacterPane character : characters) {
				writer.println(character.name.getText());
				writer.println(character.initiative.getText());
				writer.println(character.hitPoints.getText());
			}
			
			writer.close();			
			return true;
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean loadPreset(String path) {
		try {
			File presetFile = new File(path);		
			FileReader reader = new FileReader(presetFile);
			Scanner input = new Scanner(reader);
			
			while(input.hasNext()) {
				try {
					CharacterPane newCharacter = new CharacterPane(input.nextLine());
					newCharacter.initiative.setText(input.nextLine());
					newCharacter.hitPoints.setText(input.nextLine());
					addCharacterPane(newCharacter);
				}catch(NoSuchElementException e) {
					System.out.println("Unexpected end of file!");
				}
			}
			input.close();			
			return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the LinkedList of character panes. Be wary if indexing directly as it can very easily result
	 * in a null pointer exception.
	 * @return the LinkedList of character panes
	 */
	public LinkedList<CharacterPane> getCharacterPanes(){
		return characters;
	}
	
	/**
	 * This subroutine retrieves the string from the newCharacter text field and makes a new
	 * Character object with the retrieved name and adds it to the LinkedList of characters. 
	 */
	public void addCharacterPane() {
		
		//test if there is actually a string in the text field
		if(newCharName.getText().isEmpty()) {
			return;
		}

		CharacterPane newCharacter = new CharacterPane(newCharName.getText()); //get the name
		newCharName.setText(""); //delete the name from the text field
		characters.add(newCharacter); //add the new character both to the LinkedList	...	
		middlePanel.add(newCharacter); //... and the panel
		
		revalidate(); //update the whole panel
		
		//set the scrollbar on the bottom
		JScrollBar sb = scrollPane.getVerticalScrollBar();
		sb.setValue( sb.getMaximum() );	
	}
	
	/**
	 * This subroutine adds a new character pane to the initiative tracker with the info given
	 * from a character pane.
	 * @param a character pane
	 */
	public void addCharacterPane(CharacterPane character) {
		
		//make sure that there is a name to add to the pane
		if(character.name == null) {
			System.out.println("The character sheet does not have a name!");
			return;
		}

		CharacterPane newCharacter = new CharacterPane(character.name.getText()); //get the name
		newCharacter.initiative.setText(character.initiative.getText()); //set the hit points
		newCharacter.hitPoints.setText(character.hitPoints.getText()); //set the hit points
		characters.add(newCharacter); //add the new character both to the LinkedList	...	
		middlePanel.add(newCharacter); //... and the panel
		
		revalidate(); //update the whole panel
		
		//set the scrollbar on the bottom
		JScrollBar sb = scrollPane.getVerticalScrollBar();
		sb.setValue( sb.getMaximum() );	
	}
	
	/**
	 * This subroutine adds a new character pane to the initiative tracker with the info given
	 * from a character sheet.
	 * @param a character sheet
	 */
	public void addCharacterPane(CharacterSheet character) {
		
		//make sure that there is a name to add to the pane
		if(character.name == null) {
			System.out.println("The character sheet does not have a name!");
			return;
		}

		CharacterPane newCharacter = new CharacterPane(character.name); //get the name
		newCharacter.changeHitPoints(character.hitPoints); //set the hit points
		characters.add(newCharacter); //add the new character both to the LinkedList	...	
		middlePanel.add(newCharacter); //... and the panel
		
		revalidate(); //update the whole panel
		
		//set the scrollbar on the bottom
		JScrollBar sb = scrollPane.getVerticalScrollBar();
		sb.setValue( sb.getMaximum() );	
	}
		
	/**
	 * Removes a character from the panel and from the LinkedList of characters.
	 * @param character
	 */
	public void removeCharacterPane(CharacterPane character) {		
		characters.remove(character); //remove the character from both the LinkedList ...
		middlePanel.remove(character); //...and from the panel.
		revalidate(); //revalidate the object structure 
		repaint(); //is needed for the case in which the last character pane is deleted	
	}
	
	/**
	 * This inner class defines the character panes which go into the main panel as a long list of characters
	 * as they are made.
	 */	
	private class CharacterPane extends JPanel implements Comparable<CharacterPane> {
		
		private static final long serialVersionUID = 1L;
		
		private JLabel name = new JLabel();
		private JLabel initiativeLabel = new JLabel("Init:");
		private JTextField initiative = new JTextField("");
		private JLabel hitPointsLabel = new JLabel("HP:");
		private JTextField hitPoints = new JTextField("0");;
		
		private CharacterPane(String name) {			
			//set the name of the character pane
			this.name.setText(name);
			
			setMaximumSize( new Dimension(450, 40) );
			
			//add the action listeners to the buttons, change their look and add them to the button panel
			JButton[] buttons = {plusOneButton, plusFiveButton, plusTenButton,
					minusOneButton, minusFiveButton, minusTenButton};	
			int[] btnVals = {1, 5, 10, -1, -5, -10};
			Font buttonFont = new Font("Arial", Font.PLAIN, 9);	
			for(int i = 0; i < 6; i++) {
				int val = btnVals[i];
				buttons[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						changeHitPoints(val);			
					}
				});
				buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
				buttons[i].setFont(buttonFont);
				buttonPanel.add(buttons[i]);
			}

			//add all components in the right order and fix their borders, sizes etc.
			setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
			setBorder(BorderFactory.createEtchedBorder());
			
			add(this.name);
			this.name.setFont( new Font("Arial Bold", Font.ITALIC, 15));
			this.name.setPreferredSize( new Dimension(150, 40) );
			
			add(initiativeLabel);
			initiativeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			
			add(initiative);
			initiative.setHorizontalAlignment(JTextField.CENTER);
			initiative.setPreferredSize( new Dimension(30, 30) );
			initiative.setMaximumSize( new Dimension(30, 30) );
			
			add(hitPointsLabel);
			hitPointsLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			
			add(hitPoints);
			hitPoints.setHorizontalAlignment(JTextField.CENTER);
			hitPoints.setPreferredSize( new Dimension(30, 30) );
			hitPoints.setMaximumSize( new Dimension(30, 30) );
			
			add(buttonPanel);
			buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			
			add(deleteCharacterButton);
			deleteCharacterButton.addActionListener(deleteListener);
			deleteCharacterButton.setBorder(BorderFactory.createRaisedBevelBorder());
		}
		
		/**
		 * Returns the index of the object in the private LinkedList of characters.
		 * @return the index of the object in the private LinkedList of characters
		 */
		private int getIndex() {			
			return characters.indexOf(this);		
		}
		
		@Override
		public int compareTo(CharacterPane other) {
			
			int valueThis;
			int valueOther;
			
			//try to parse the values to to int; if not possible, set value to 0
    		try {
    			valueThis = Integer.parseInt(this.initiative.getText());
    		}
    		catch(NumberFormatException e) {
    			valueThis = 0;
    		}
    		try {
    			valueOther = Integer.parseInt(other.initiative.getText());
    		}
    		catch(NumberFormatException e) {
    			valueOther = 0;
    		}
    		
    		if(valueThis > valueOther) 
    			return -1;			
    		else if(valueThis < valueOther)
    			return 1;
    		else
    			return 0;
		}
		
		/**
		 * Adds or substracts a given value to an integer in the textfield and then returns true. If there is not
		 * an integer in the text field, the method returns false.
		 * @param number a positive number for addition or negative number for substraction
		 * @return true if any arithmetic operations were actually made and false if none were made
		 */
		private boolean changeHitPoints(int number) {				
			try {
				int currentHP = Integer.parseInt(hitPoints.getText());
				int updatedHP = currentHP + number;
				hitPoints.setText(Integer.toString(updatedHP));
				return true;
			}
			catch(NumberFormatException e) {
				return false;
			}					
		}
		
		//buttons to update HP
		private JPanel buttonPanel = new JPanel( new GridLayout(2, 3) );
		private JButton plusOneButton = new JButton("+ 1");
		private JButton plusFiveButton = new JButton("+ 5");
		private JButton plusTenButton = new JButton("+ 10");		
		private JButton minusOneButton = new JButton("- 1");
		private JButton minusFiveButton = new JButton("- 5");
		private JButton minusTenButton = new JButton("- 10");

		//button to delete a character pane
		private JButton deleteCharacterButton = new JButton("DELETE");
		private ActionListener deleteListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = getIndex();
				removeCharacterPane(characters.get(index));
			}
		};
		
	} //end inner class
}
