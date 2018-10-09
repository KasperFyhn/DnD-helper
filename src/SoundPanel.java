import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This panel is a tool to play sound effects loaded from a folder containing
 * the sound effects that are needed. 
 * @author Kasper Fyhn Jacobsen
 *
 */
public class SoundPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	
	public SoundPanel() {
		
		List<File> files = loadAllSoundFiles();
		
		for(File file : files) {
			addSoundEffectButton(file);
			
		}
		
		setBorder( BorderFactory.createEtchedBorder());
		setPreferredSize( new Dimension(350,400) );
		setLayout( new FlowLayout() );
		
		for(JButton button: buttons) {
			add(button);
		}		
	}
	
	/**
	 * This private method loads all .wav files from the folder "Sound effects" and returns
	 * a list of these sound files.
	 * @return
	 */
	private List<File> loadAllSoundFiles() {		
		List<File> files = null;
		
		try {
			 files = Files.walk(Paths.get("Sound effects"))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());			
		}
		catch(IOException e) {
			e.printStackTrace();
		}	
		
		//filter out all files that are not .wav
		for(int i = 0; i < files.size(); i++) {
			if(! files.get(i).getName().endsWith(".wav")) {
				files.remove(i);
			}
		}
		
		return files;
	}
	
	private void addSoundEffectButton(File file) {
		//set the name of the button to be the file name w/o file extension and in upper case letters
		String name = file.getName();
		name = name.replace(".wav", "").toUpperCase();
		
		//add the .wav file as a sound effect
		SoundEffect snd = new SoundEffect(file);
		
		//add the sound effect to a button with an action listener
		JButton btn = new JButton(name);
		btn.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            snd.playOrStop();
	         }
		});		
		buttons.add(btn);		
	}
	
	private class SoundEffect {
		
		private Clip clip;
		
		SoundEffect(File file) {
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
				System.out.println("Loading " + file.getName());
				// Get a clip resource
				clip = AudioSystem.getClip();
				// Open audio clip and load samples from the audio input stream.
				clip.open(audioInputStream);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
			   
		// Play or Re-play the sound effect from the beginning, by rewinding.
		public void playOrStop() {
			if (clip.isRunning())
				clip.stop();   // Stop the player if it is still running
			else {
				clip.setFramePosition(0); // rewind to the beginning
				clip.start();     // Start playing
			}
		}
	}

}