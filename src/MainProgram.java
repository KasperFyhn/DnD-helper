import javax.swing.*;

public class MainProgram {

	static JPanel content;
	static InitiativeTracker initiativeTracker;
	static SoundPanel soundPanel;
	
	public static void main(String[] args) {
		
		content = new JPanel();
		content.setLayout( new BoxLayout(content, BoxLayout.X_AXIS) );
		content.setBorder( BorderFactory.createEmptyBorder());
		
		initiativeTracker = new InitiativeTracker();
		content.add(initiativeTracker);
		
		soundPanel = new SoundPanel();
		content.add(soundPanel);
		
		/*
		InitiativeTracker test1 = new InitiativeTracker();
		content.add(test1);
		*/
		
		JFrame mainWindow = new JFrame("DnD Helper v0.1");
		mainWindow.setContentPane( new JScrollPane(content) );
		mainWindow.pack();
		mainWindow.setLocation(500, 200);
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
