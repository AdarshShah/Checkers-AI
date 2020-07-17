import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/*
	This class can be used as a starting point for creating your Chess game project. The only piece that 
	has been coded is a white pawn...a lot done, more to do!
*/

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
	JLayeredPane layeredPane;
	JPanel chessBoard;
	JLabel chessPiece;
	int xAdjustment;
	int yAdjustment;
	int startX;
	int startY;
	int initialX;
	int initialY;
	JPanel panels;
	JLabel pieces;
	JPanel board[][] = new JPanel[8][8];
	int row, col;
	Checkers game = new Checkers();
	Move m;

	public ChessProject() {
		Dimension boardSize = new Dimension(600, 600);

		// Use a Layered Pane for this application
		layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane);
		layeredPane.setPreferredSize(boardSize);
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);

		// Add a checkers board to the Layered Pane
		chessBoard = new JPanel();
		layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
		chessBoard.setLayout(new GridLayout(8, 8));
		chessBoard.setPreferredSize(boardSize);
		chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

		for (int i = 0; i < 64; i++) {
			JPanel square = new JPanel(new BorderLayout());
			chessBoard.add(square);

			int row = (i / 8) % 2;
			if (row == 0)
				square.setBackground(i % 2 == 0 ? Color.white : Color.gray);
			else
				square.setBackground(i % 2 == 0 ? Color.gray : Color.white);
			board[i / 8][i % 8] = square;
		}

		updateBoard();
		System.out.println(game);
	}

	void updateBoard() {
		// Setting up the Checkers board.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (game.board[i][j] == -1) {
					pieces = new JLabel(new ImageIcon("BlackPawn.png"));
					panels = board[i][j];
					panels.add(pieces);
				} else if (game.board[i][j] == 1) {
					pieces = new JLabel(new ImageIcon("WhitePawn.png"));
					panels = board[i][j];
					panels.add(pieces);
				} else {
					panels = board[i][j];
					panels.removeAll();
				}
			}
			System.out.println(game);
		}
	}

	/*
	 * This method checks if there is a piece present on a particular square.
	 */
	private Boolean piecePresent(int x, int y) {
		Component c = chessBoard.findComponentAt(x, y);
		if (c instanceof JPanel) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * This is a method to check if a piece is a Black piece.
	 */
	private Boolean checkWhiteOponent(int newX, int newY) {
		Boolean oponent;
		Component c1 = chessBoard.findComponentAt(newX, newY);
		JLabel awaitingPiece = (JLabel) c1;
		String tmp1 = awaitingPiece.getIcon().toString();
		if (((tmp1.contains("Black")))) {
			oponent = true;
		} else {
			oponent = false;
		}
		return oponent;
	}

	/*
	 * This method is called when we press the Mouse. So we need to find out what
	 * piece we have selected. We may also not have selected a piece!
	 */
	public void mousePressed(MouseEvent e) {
		chessPiece = null;
		Component c = chessBoard.findComponentAt(e.getX(), e.getY());
		if (c instanceof JPanel)
			return;
		if (((JLabel) c).getIcon().toString().contains("Black"))
			return;
		Point parentLocation = c.getParent().getLocation();
		xAdjustment = parentLocation.x - e.getX();
		yAdjustment = parentLocation.y - e.getY();
		chessPiece = (JLabel) c;

		game = new Checkers(game);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == (JPanel) chessPiece.getParent()) {
					m = new Move(i, j, game.board[i][j], null);
					row = 1;
					col = j;
				}
			}
		}
		game.visited.clear();
		game.findBestMoves(m);
		initialX = e.getX();
		initialY = e.getY();
		startX = (e.getX() / 75);
		startY = (e.getY() / 75);
		chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
		chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
		layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
	}

	public void mouseDragged(MouseEvent me) {
		if (chessPiece == null)
			return;
		chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
	}

	/*
	 * This method is used when the Mouse is released...we need to make sure the
	 * move was valid before putting the piece back on the board.
	 */
	public void mouseReleased(MouseEvent e) {
		if (chessPiece == null)
			return;
		
		chessPiece.setVisible(false);
		Component c = chessBoard.findComponentAt(e.getX(), e.getY());
		String tmp = chessPiece.getIcon().toString();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j].equals(c)) {
					Move m = new Move(row, col, -1, null);
					Move end = new Move(i, j, 0, null);
					game.visited.clear();
					int score = game.findBestMoves(m);
					game.visited.clear();
					if (game.performOppnMove(m, score, end)) {
						game.board[m.row][m.col]=0;
						break;
					}
				}
			}
		}
		game = new Checkers(game);
		updateBoard();
		/*
		 * The only piece that has been enabled to move is a White Pawn...but we should
		 * really have this is a separate method somewhere...how would this work.
		 * 
		 * So a Pawn is able to move two squares forward one its first go but only one
		 * square after that. The Pawn is the only piece that cannot move backwards in
		 * chess...so be careful when committing a pawn forward. A Pawn is able to take
		 * any of the opponent’s pieces but they have to be one square forward and one
		 * square over, i.e. in a diagonal direction from the Pawns original position.
		 * If a Pawn makes it to the top of the other side, the Pawn can turn into any
		 * other piece, for demonstration purposes the Pawn here turns into a Queen.
		 */
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	/*
	 * Main method that gets the ball moving.
	 */
	public static void main(String[] args) {
		JFrame frame = new ChessProject();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
