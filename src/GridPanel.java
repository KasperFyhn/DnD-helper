import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GridPanel extends JFrame{
	
	JLabel currentPiece = new JLabel();
	
	public void run() {
		
		JPanel content = new JPanel();
		content.setLayout( new BorderLayout() );
		
		//create and set the grid preferences
		int rows = 8;
		int cols = 8;
		
		GridWithPieces grid = new GridWithPieces(rows, cols, 60);
		grid.parentWindow = this;
		
		JScrollPane sp = new JScrollPane(grid);
		sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		if(grid.getFocusPiece() != null)
		currentPiece = new JLabel("  Current piece: " + grid.getFocusPiece().getName());
		
		int noOfPieces = 16;
		grid.setStandardPieces(noOfPieces);
		
		int indent = 0;
		for(int i = noOfPieces - 1; i > noOfPieces / 2 - 1; i -= 4) {			
			for(int j = 0; j < 4; j++) {
				grid.setFocusPiece(i - j);
				grid.placePiece(grid.getFocusPiece(), 7 - j * 2 - indent, 7 - indent);
			}		
			indent++;			
		}
		
		indent = 0;
		for(int i = noOfPieces - noOfPieces / 2 - 1; i > 0; i -= 4) {			
			for(int j = 0; j < 4; j++) {
				grid.setFocusPiece(i - j);
				grid.placePiece(grid.getFocusPiece(), 7 - j * 2 - indent, 1 - indent);
			}		
			indent++;			
		}
		
		//create the text field for the current focus piece

		
		//create the text field to set a new focus piece
		JTextField newPiece = new JTextField("");
		newPiece.setEditable(true);
		
		//create a button to change the focus piece
		JButton changePiece = new JButton("Change piece");
		changePiece.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int newIndex = -1;
				try {
					newIndex = Integer.parseInt(newPiece.getText());
					newPiece.setText("");
					
					grid.setFocusPiece(newIndex - 1);
					currentPiece.setText("  Current piece: " + grid.getFocusPiece().getName());
					
					content.repaint();
				}
				catch(NumberFormatException e) {
					newPiece.setText("Illegal number!");
				}
				grid.requestFocus();
			}			
		});
		
		//create the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridLayout() );
		topPanel.add(currentPiece);
		topPanel.add(newPiece);
		topPanel.add(changePiece);

		content.add(topPanel, BorderLayout.NORTH);
		content.add(sp, BorderLayout.CENTER);
		
		JFrame mainWindow = new JFrame();
		mainWindow.setContentPane(content);
		mainWindow.setVisible(true);
		mainWindow.pack();
		mainWindow.setResizable(false);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}

