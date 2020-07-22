import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Checkers {
	
	/*
	 * Computer is assigned Black piece. Player is assigned White piece.
	 * Player always takes the first turn
	 * The checkers game state is represented as board. 
	 * Black piece : -1 
	 * White piece: 1 
	 * Empty cell : 0
	 */
	int board[][] = new int[8][8];
	
	/*
	 * Used in Depth First Search to keep track of cells visited
	 */Set<Move> visited = new HashSet<Move>();

	/*
	 * A copy constructor which replaces black pieces with white and vice versa
	 */public Checkers(Checkers c) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				this.board[i][j] = -1 * c.board[i][j];
		}
	}

	/*
	 * Default Constructor initializing Checkers board to initial condition
	 */public Checkers() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0)
					board[i][j] = -1;
			}
		}
		for (int i = 5; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0)
					board[i][j] = 1;
			}
		}
	}

	/*
	 * For Debugging purpose only
	 */@Override
	public String toString() {
		String str = "";
		for (int[] row : board) {
			for (int i : row) {
				str += "\t" + i;
			}
			str = str + "\n";
		}
		return str;
	}

	/*
	 * This method returns a randomly generated legal move
	 */public Move randomAgent() {
		int[] arr = AllMoves().get(Math.abs(new Random().nextInt())%AllMoves().size());
		Move m = new Move(arr[0],arr[1],-1,null);
		return m;
	}
	 
	/*
	 * This method generates all legal moves possible and sorts the moves based on the
	 * score. The score is defined as the longest distance a piece on current
	 * position can move.
	 */public List<int[]> AllMoves() {
		ArrayList<int[]> allMoves = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == -1) {
					Move m = new Move(i, j, board[i][j], null);
					allMoves.add(new int[] { i, j, findBestMoves(m) });
					visited.clear();
				}
			}
		}
		allMoves = (ArrayList<int[]>) allMoves.stream().sorted((a, b) -> {
			return b[2] - a[2];
		}).collect(Collectors.toList());
		return allMoves;
	}

	/*
	 * This is a Greedy Best First Search Algorithm which calculates best score that can be
	 * obtained by current position. The neighbors of current position are examined to 
	 * determine whether piece can advance in this direction or not. If possible, the 
	 * piece is advanced and the procedure is repeated further. The value returned is 
	 * the length of the longest path. This value is defined as score of that move.
	 */public int findBestMoves(Move m) {
		visited.add(m);
		Move a = null;
		int score = 0;

		try {
			if (board[m.row + 1][m.col + 1] == 0 & board[m.row][m.col] == -1) {
				a = new Move(m.row + 1, m.col + 1, board[m.row + 1][m.col + 1], m);
				if(!visited.contains(a)) {
					visited.add(a);
					score = Math.max(score, 1);
					visited.remove(a);
				}
			} else if (board[m.row + 1][m.col + 1] == 1 & board[m.row + 2][m.col + 2] == 0) {
				a = new Move(m.row + 2, m.col + 2, board[m.row + 2][m.col + 2], m);
				if(!visited.contains(a)) {
					visited.add(a);
					score = Math.max(score, 2 + findBestMoves(a));
					visited.remove(a);
				}
			}
			if (a != null)
				m.children.add(a);
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		Move b = null;
		try {
			if (board[m.row + 1][m.col - 1] == 0 & board[m.row][m.col] == -1) {
				b = new Move(m.row + 1, m.col - 1, board[m.row + 1][m.col - 1], m);
				if(!visited.contains(b)) {
					visited.add(b);
					score = Math.max(score, 1);
					visited.remove(b);
				}
			} else if (board[m.row + 1][m.col - 1] == 1 & board[m.row + 2][m.col - 2] == 0) {
				b = new Move(m.row + 2, m.col - 2, board[m.row + 2][m.col - 2], m);
				if(!visited.contains(b)) {
					visited.add(b);
					score = Math.max(score, 2 + findBestMoves(b));
					visited.remove(b);
				}
			}
			if (b != null)
				m.children.add(b);
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		Move c = null;
		try {
			if (board[m.row - 1][m.col - 1] == 0 & board[m.row][m.col] == -1) {
				c = new Move(m.row - 1, m.col - 1, board[m.row - 1][m.col - 1], m);
				if(!visited.contains(c)) {
					visited.add(c);
					score = Math.max(score, 1);
					visited.remove(c);
				}
			} else if (board[m.row - 1][m.col - 1] == 1 & board[m.row - 2][m.col - 2] == 0) {
				c = new Move(m.row - 2, m.col - 2, board[m.row - 2][m.col - 2], m);
				if(!visited.contains(c)) {
					visited.add(c);
					score = Math.max(score, 2 + findBestMoves(c));
					visited.remove(c);
				}
			}
			if (c != null)
				m.children.add(c);
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		Move d = null;
		try {
			if (board[m.row - 1][m.col + 1] == 0 & board[m.row][m.col] == -1) {
				d = new Move(m.row - 1, m.col + 1, board[m.row - 1][m.col + 1], m);
				if(!visited.contains(d)) {
					visited.add(d);
					score = Math.max(score, 1);
					visited.remove(d);
				}
			} else if (board[m.row - 1][m.col + 1] == 1 & board[m.row - 2][m.col + 2] == 0) {
				d = new Move(m.row - 2, m.col + 2, board[m.row - 2][m.col + 2], m);
				if(!visited.contains(d)) {
					visited.add(d);
					score = Math.max(score, 2 + findBestMoves(d));
					visited.remove(d);
				}
			}
			if (d != null)
				m.children.add(d);
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		return score;
	}
	 
	/*
	 * This method performs move from current position m till required score is not
	 * achieved. This method is used to perform automated moves by computer.
	 */public boolean performMove(Move m, int score) {
		 System.out.println(m+" "+score);
		if (score == 0) {
			board[m.row][m.col] = -1;
			return true;
		}
		if (score == 1) {
			Move p = new ArrayList<Move>(m.children).get(Math.abs(new Random().nextInt())%m.children.size());
			board[m.row][m.col] = 0;
			board[p.row][p.col] = -1;
			return true;
		}
		List<Move> list = m.children.stream().filter(mv -> Math.abs(mv.row - m.row) >= 2).collect(Collectors.toList());
		visited.add(m);
		for (Move p : list) {
			if(visited.contains(p)) continue;
			visited.add(p);
			if (performMove(p, score - 2)) {
				board[m.row][m.col] = 0;
				board[(m.row + p.row) / 2][(m.col + p.col) / 2] = 0;
				return true;
			}
			visited.remove(p);
		}
		visited.remove(m);
		return false;
	}
	
	/*
	 * This function is used to implement canceling opponent's pieces recursively.
	 */public boolean recursiveFunc(Move m, int score) {
		if (score == 0) {
			board[m.row][m.col] = -1;
			System.out.println(m);
			return true;
		}
		for (Move child : m.children) {
			if(visited.contains(child)) continue;
			visited.add(child);
			int x = board[(m.row + child.row) / 2][(m.col + child.col) / 2];
			board[(m.row + child.row) / 2][(m.col + child.col) / 2] = 0;
			if (recursiveFunc(child, score - 2)) {
				return true;
			}
			board[(m.row + child.row) / 2][(m.col + child.col) / 2] = 1;
			visited.remove(child);
		}
		return false;
	}
	
	/*
	 * This function is used to implement moves performed by Player.
	 */public boolean performMove(Move m, int score, Move end) {
		if(m.equals(end)) {
			System.out.println("correct move");
			board[m.row][m.col]=-1;
			return true;
		}
		else if(score==0) return false;
		else if(score==1) {
			board[m.row][m.col]=0;
			for(Move child : m.children) {
				if(!visited.contains(child)) {
					visited.add(child);
					if(performMove(child, score-1, end)) return true;
				}
				visited.remove(child);
			}
			board[m.row][m.col]=-1;
		}else if(score>1) {
			board[m.row][m.col]=0;
			for(Move child : m.children) {
				if(!visited.contains(child)) {
					visited.add(child);
					board[(m.row+child.row)/2][(m.col+child.col)/2]=0;
					if(performMove(child, score-2, end)) return true;
					board[(m.row+child.row)/2][(m.col+child.col)/2]=1;
				}
				visited.remove(child);
			}
		}
		return false;
	}
	
	/*
	 * An array BestMove[] of size 3 is used as result. 
	 * BestMove[0] : row
	 * BestMove[1] : col 
	 * BestMove[2] : score
	 */
	public int[] twoLevelsDeep() {
		int BestMove[] = null;
		int max=Integer.MIN_VALUE;
		List<int[]> moves = AllMoves();
		for(int[] m : moves) {
			Move mv1 = new Move(m[0], m[1], board[m[0]][m[1]], null);
			visited.clear();
			int score = findBestMoves(mv1);
			Checkers game = new Checkers(new Checkers(this));
			//Computer's First Move 
			game.performMove(mv1, score);
			
			game =  new Checkers(new Checkers(this));
			int move[] = game.AllMoves().get(0);
			Move mv2 = new Move(move[0], move[1], game.board[move[0]][move[1]], null);
			game.visited.clear();
			score -= game.findBestMoves(mv2);
			game.visited.clear();
			//Player's Second Move
			game.performMove(mv2, score);
			
			game =  new Checkers(new Checkers(this));
			move = game.AllMoves().get(0);
			Move mv3 = new Move(move[0], move[1], game.board[move[0]][move[1]], null);
			game.visited.clear();
			score += game.findBestMoves(mv3);
			game.visited.clear();
			//Computer's Second Move
			game.performMove(mv3, score);
			
			if(score>=max) BestMove=m;
		}
		
		return BestMove;
	}
	
	public static void main(String[] args) {
		playGame();
		
	}

	/*
	 * For Debugging purpose only This function demonstrates Game playing Computer
	 * VS Computer
	 */static void playGame() {
		Checkers player = new Checkers();
		boolean turn = true;
		boolean gameOn = true;
		int black = 12;
		int white = 12;
		while (gameOn) {
			turn = !turn;
			System.out.println(player);
			int move[] = player.AllMoves().get(0);
			Move m = new Move(move[0], move[1], player.board[move[0]][move[1]], null);
			int score = player.findBestMoves(m);
			player.visited.clear();
			player.performMove(m, score);
			player.visited.clear();
			if (turn) {
				white -= score / 2;
			} else {
				black -= score / 2;
			}
			System.out.println(score + " " + black + " " + white);
			if (black <= 0 | white <= 0) {
				System.out.println(player);
				break;
			}
			player = new Checkers(player);
		}
		if (turn) {
			System.out.println("Player 2 wins");
		} else {
			System.out.println("Player 1 wins");
		}
	}
	

}
