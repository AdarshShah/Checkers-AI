import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	/*
	 * Checkers object is used as controller. This class is used for presentation
	 * only. The business logic is present entirely in Checkers class
	 */
	/*
	 * Maintains Computer's game state
	 */
	Checkers game = new Checkers();
	/*
	 * Maintains Player's Game State
	 */
	Checkers oppnGame = null;
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
					panels.removeAll();
					panels.add(pieces);
				} else if (game.board[i][j] == 1) {
					pieces = new JLabel(new ImageIcon("WhitePawn.png"));
					panels = board[i][j];
					panels.removeAll();
					panels.add(pieces);
				} else {
					panels = board[i][j];
					panels.removeAll();
				}
			}
		}
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
		
		/*
		 * The Player's game state is updated at this point
		 */
		oppnGame = new Checkers(game);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == (JPanel) chessPiece.getParent()) {
					m = new Move(i, j, oppnGame.board[i][j], null);
					row = i;
					col = j;
				}
			}
		}
		oppnGame.visited.clear();
		oppnGame.findBestMoves(m);
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
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j].equals(c)) {
					Move m = new Move(row, col, -1, null);
					Move end = new Move(i, j, 0, null);
					oppnGame.visited.clear();
					int score = oppnGame.findBestMoves(m);
					oppnGame.visited.clear();
					oppnGame.visited.add(m);
					/*
					 * Checks if the move performed by player is valid. if so update else undo.
					 */if (oppnGame.performMove(m, score, end) & !m.equals(end)) {
						game = new Checkers(oppnGame);
						game.visited.clear();
						int max = game.AllMoves().get(0)[2];
						int count = (int)game.AllMoves().stream().filter(m2->m2[2]==max).count();
						int[] move = game.AllMoves().stream().filter(m1->m1[2]==max).collect(Collectors.toList()).get(Math.abs(new Random().nextInt())%count);
						Move black = new Move(move[0], move[1], -1, null);
						game.visited.clear();
						score = game.findBestMoves(black);
						game.visited.clear();
						game.performMove(black, score);
						break;
					}
				}
			}
		}
		System.out.println(game);
		updateBoard();
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
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
