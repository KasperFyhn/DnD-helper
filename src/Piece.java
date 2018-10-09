import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class Piece extends Component {
	
	private String name = "Ø"; //the name of the piece
	
	private Color color = Color.BLACK;
	
	private int tileSize;
	private int pieceSize;
	private int pieceInset;
	private int posX = -1; //the x-coord of the piece
	private int posY = -1; //the y-coord of the piece

	boolean canMove = false;
	
	public Piece(String name, int tileSize) {
		
		this.name = name;
		pieceSize = tileSize - tileSize / 10;
		pieceInset = tileSize / 20;
		
	}
	
	public Piece(String name, int posX, int posY, int tileSize) {
		
		this.name = name;
		this.posX = posX;
		this.posY = posY;
		this.tileSize = tileSize;
		pieceSize = tileSize - tileSize / 10;
		pieceInset = tileSize / 20;
			
	} //end Piece constructor
	
	public void paintComponent(Graphics g) {
		g.setColor(color);
		g.fillOval(posX * tileSize + pieceInset, posY * tileSize + pieceInset, pieceSize, pieceSize);
		g.setColor(Color.WHITE);
		g.drawString(name, posX * tileSize + tileSize / 3, posY * tileSize + tileSize / 2);
	}
	
	public void setName(String name) {
		this.name = name;	
	}
	
	public String getName() {
		return name;	
	}
	
	public void setColor(Color color) {
		this.color = color;	
	}
	
	public Color getColor() {
		return color;	
	}
	
	/**
	 * Give new x- and y-coords to the piece. 
	 * @param posX
	 * @param posY
	 * @return a boolean value to evaluate if the piece was moved or not
	 */
	public void setPosition(int posX, int posY) {	
		this.posX = posX;
		this.posY = posY;
	}
	
	/** 
	 * @return the current x-coords of the piece. Returns -1 if the piece is not placed.
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * @return the current y-coords of the piece. Returns -1 if the piece is not placed.
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * @return true if the piece is placed on a grid and false if it is not.
	 */
	public boolean isPlaced() {
		if(posX == -1 && posY == -1)
			return false;
		else
			return true;
	}
	
	/**
	 * Sets the position of the piece to (-1, -1). This makes the Piece class regard the piece
	 * as not placed on the board.
	 */
	public void displace() {
		
		posX = -1;
		posY = -1;
		
	}
		
} //end class Piece