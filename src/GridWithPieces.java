import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is a component which draws a 2D grid with tiles of different types and colors,
 * thereby laying out a grid with different traits for a game depending on the needs. Moreover,
 * the inner class Piece makes it possible to lay out a number of pieces of which one at a time,
 * the so-called focus piece, can be moved around using the arrow key. Switch between focus pieces
 * with S and W.
 * @author kasperfyhnjacobsen
 */
public class GridWithPieces extends JPanel implements KeyListener {
	
	private int rows; //number of rows in the grid
	private int cols; //number of columns in the grid
	private int tileSize; //the size of the tiles which are always squares
	
	GridPanel parentWindow;
	
	/**
	 * Create a new grid with the input number of columns and rows and with the given size. The preferred size
	 * of the component is set according to these dimensions.
	 * @param rows
	 * @param cols
	 * @param tileSize
	 */
	public GridWithPieces(int rows, int cols, int tileSize) {
		
		this.rows = rows;
		this.cols = cols;
		this.tileSize = tileSize;
		
		this.occupiedTiles = new boolean[cols][rows]; //number of cols gives number of x coords
												 	  //number of rows gives number of y coords
		
		//set pieceSize and pieceInset to their standard values based on tileSize
		pieceSize = tileSize - tileSize / 10;
		pieceInset = tileSize / 20;
		
		//set the preferred size of the component to fit with the constructed grid
		setPreferredSize( new Dimension(cols * tileSize + 1, rows * tileSize + 1) );
		
		//set up key listening and set the component in focus in order for it to be able to listen to events
		addKeyListener(this);
		setFocusable(true);
		requestFocus();		
		
	}
	
	public void paintComponent (Graphics g) {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
			}
		}
		
		if(pieces != null) {
			for(Piece piece : pieces) {
				piece.paintComponent(g);
				if(piece == focusPiece) {
					g.setColor(Color.RED);
					g.drawOval(piece.getPosX() * tileSize, piece.getPosY() * tileSize, tileSize, tileSize);
					g.setColor(Color.BLACK);
				}
			}
		}
							
	}
	
	private boolean[][] occupiedTiles; //an array of boolean values to keep track of occupied tiles
	
	public void vacateTile(int x, int y) {
		
		occupiedTiles[x][y] = false;
		
	}
	
	public boolean occupyTile(int x, int y) {
		
		if(occupiedTiles[x][y] == true) {
			return false;
		}
		else {
			occupiedTiles[x][y] = true;
			return true;
		}		
	}
	
	private Piece[] pieces; //an array of pieces to move around on the grid
	private int noOfPieces;
	private int focusPieceIndex;
	private Piece focusPiece;
	
	private int pieceSize; //is set to be 90% of tileSize as a standard
	private int pieceInset; //is set to be 5% of tileSize as a standard
	
	/**
	 * Sets a number of standard pieces on the grid, starting from the top left corner and, then, filling
	 * the grid going right and jumping to the next row when a row is filled.
	 * @param noOfPieces
	 */
	public void setStandardPieces(int noOfPieces) {
		
		//first, check if too many pieces are called for. If so, return and give an error message.
		if(noOfPieces > (cols * rows)) {
			System.out.print("The input number of pieces exceeds the number of tiles on the boards!");
			return;
		}
		
		pieces = new Piece[noOfPieces]; //initialize a new array
		this.noOfPieces = noOfPieces; //store the number of pieces for later reference
		
		//place each piece on the grid
		for(int i = 0; i < pieces.length; i++) {
			pieces[i] = new Piece(Integer.toString(i + 1) ,i % cols, i / cols, tileSize);
			if(i > pieces.length / 2 - 1)
				pieces[i].setColor(Color.RED);
			occupyTile(pieces[i].getPosX(), pieces[i].getPosY());
		}		
	}
	
	/**
	 * Set the new focus piece based on an index in the range of legal piece indexes.
	 * @param newFocusPieceIndex
	 * @return a booelan value to evaluate if another piece has been made the new focus piece
	 */
	public boolean setFocusPiece(int newFocusPieceIndex) {
		
		//check if the new index is in the legal range
		if(newFocusPieceIndex < 0 || (newFocusPieceIndex > (noOfPieces - 1))) {
			return false;
		}
		
		if(focusPiece != null) {
			focusPiece.canMove = false;
		}
		
		//set the new index and the actual focus piece
		focusPieceIndex = newFocusPieceIndex;
		focusPiece = pieces[focusPieceIndex];
		focusPiece.canMove = true;
		
		if(parentWindow != null) {
			parentWindow.currentPiece.setText("  Current piece: " + focusPiece.getName());
		}

		return true;
		
	}
	
	/**
	 * Returns the current focus piece.
	 * @return
	 */
	public Piece getFocusPiece() {
		return focusPiece;
	}
	
	/**
	 * Move a piece from its current position, giving negative or positive x- and y-values. Negative values
	 * move the piece closer to the top left corner and positive values further from it.
	 * Before a piece can be moved, it is checked that a) the piece can move, b) that the new position is within
	 * the grid, and c) that the new tile is not occupied.
	 * @param piece that is to be moved
	 * @param x moves the piece on the x-axis. Negative moves left and positive moves right
	 * @param y moves the piece on the y-axis. Negative moves up and positive moves down
	 * @return a boolean value to evaluate if the piece was moved or not
	 */
	public boolean movePiece(Piece piece, int x, int y) {
		
		int newPosX = piece.getPosX() + x;
		int newPosY = piece.getPosY() + y;
		
		//if the piece cannot move, return false
		if( ! piece.canMove) {
			return false;
		}
		
		//if the piece is trying to move out of the grid, return false
		if(newPosX < 0 || newPosX > (cols - 1) || newPosY < 0 || newPosY > (rows - 1)) {
			System.out.println("Moving out of grid!");
			return false;
		}
		
		//if the tile is occupied, return false.
		else if (occupiedTiles[newPosX][newPosY]) {
			System.out.println("Tile is occupied!");
			return false;
		}
		
		//if the tile is not occupied, set the new position of the piece and the occupiedTiles accordingly
		//and return true		
		vacateTile(piece.getPosX(), piece.getPosY()); //first, set the previous tile to unoccupied
		piece.setPosition(newPosX, newPosY);
		occupyTile(newPosX, newPosY); //then, set the new tile as occupied
		return true;

	} //end movePiece
	
	/**
	 * Place a piece on the board. Before a piece can be placed, it is checked that a) that the new position 
	 * is within the grid, and b) that the new tile is not occupied.
	 * @param piece the piece that is to be (re)placed on the board
	 * @param newPosX new x-coords
	 * @param newPosY new y-coords
	 * @return a boolean value to evaluate if the piece was moved or not
	 */
	public boolean placePiece(Piece piece, int newPosX, int newPosY) {
		
		//if the piece is being placed out of the grid, return false
		if(newPosX < 0 || newPosX > (cols - 1) || newPosY < 0 || newPosY > (rows - 1)) {
			System.out.println("Placing outside grid!");
			return false;
		}
		
		//if the tile is occupied, return false.
		else if (occupiedTiles[newPosX][newPosY]) {
			System.out.println("Tile is occupied!");
			return false;
		}
		
		//set the new position of the piece and the occupiedTiles accordingly
		//and return true
		if(piece.isPlaced()) {
			vacateTile(piece.getPosX(), piece.getPosY()); //first, set the previous tile - if any - to unoccupied
		}
		piece.setPosition(newPosX, newPosY);
		occupyTile(newPosX, newPosY); //then, set the new tile as occupied
		return true;

	} //end placePiece
	
	public void removePiece(Piece piece) {
		vacateTile(piece.getPosX(), piece.getPosY());
		piece.displace();
	}

	/*
	 * Below are the key event handling methods which facilitate the possibility of moving pieces around with
	 * the arrow keys
	 */
	
	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			movePiece(focusPiece, 0, -1);
			repaint();
			break;
		case KeyEvent.VK_DOWN:
			movePiece(focusPiece, 0, 1);
			repaint();
			break;
		case KeyEvent.VK_RIGHT:
			movePiece(focusPiece, 1, 0);
			repaint();
			break;
		case KeyEvent.VK_LEFT:
			movePiece(focusPiece, -1, 0);
			repaint();
			break;
		case KeyEvent.VK_W:
			setFocusPiece(focusPieceIndex + 1);
			repaint();
			break;
		case KeyEvent.VK_S:
			setFocusPiece(focusPieceIndex - 1);
			repaint();
			break;
		case KeyEvent.VK_D:
			removePiece(focusPiece);
			repaint();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

} //end class Grid
