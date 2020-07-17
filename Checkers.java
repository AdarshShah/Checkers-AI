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

	int board[][] = new int[8][8];
	Set<Move> visited = new HashSet<Move>();

	public Checkers(Checkers c) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				this.board[i][j] = -1 * c.board[i][j];
		}
	}

	public Checkers() {
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

	@Override
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

	public List<int[]> randomAgent() {
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

	public int findBestMoves(Move m) {
		if (visited.contains(m)) {
			return 0;
		}
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

	public boolean performMove(Move m, int score) {
		if (score == 0)
			return true;
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
			if (recursiveFunc(p, score - 2)) {
				board[m.row][m.col] = 0;
				board[(m.row + p.row) / 2][(m.col + p.col) / 2] = 0;
				board[p.row][p.col] = -1;
				return true;
			}
			visited.remove(p);
		}
		visited.remove(m);
		return false;
	}

	public boolean recursiveFunc(Move m, int score) {
		if (score == 0) {
			board[m.row][m.col] = -1;
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
	
	public boolean performOppnMove(Move m, int score, Move end) {
		if(visited.contains(end)) return true;
		if (score == 0)
			return true;
		if (score == 1) {
			Move p = new ArrayList<Move>(m.children).stream().filter(a->a.equals(end)).findAny().get();
			if(p==null) return false;
			board[m.row][m.col] = 0;
			board[p.row][p.col] = -1;
			return true;
		}
		List<Move> list = m.children.stream().filter(mv -> Math.abs(mv.row - m.row) >= 2).collect(Collectors.toList());
		visited.add(m);
		for (Move p : list) {
			if(visited.contains(p)) continue;
			visited.add(p);
			if (recursiveOppnFunc(p, score - 2, end)) {
				board[m.row][m.col] = 0;
				board[(m.row + p.row) / 2][(m.col + p.col) / 2] = 0;
				board[p.row][p.col] = -1;
				return true;
			}
			visited.remove(p);
		}
		visited.remove(m);
		return false;
	}
	
	public boolean recursiveOppnFunc(Move m, int score,Move end) {
		if(visited.contains(end)) return true;
		if (score == 0){
			board[m.row][m.col] = -1;
			return true;
		}
		for (Move child : m.children) {
			if(visited.contains(child)) continue;
			visited.add(child);
			int x = board[(m.row + child.row) / 2][(m.col + child.col) / 2];
			board[(m.row + child.row) / 2][(m.col + child.col) / 2] = 0;
			if (recursiveOppnFunc(child, score - 2,end)) {
				return true;
			}
			board[(m.row + child.row) / 2][(m.col + child.col) / 2] = 1;
			visited.remove(child);
		}
		return false;
	}

	public static void main(String[] args) {
//		Checkers game = new Checkers();
//		game.board[1][1]=1;game.board[2][2]=0;game.board[3][1]=1;
//		System.out.println(game);
//		
//		int[] move = game.randomAgent().get(0);
//		Move m =new Move(move[0],move[1],game.board[move[0]][move[1]],null);
//		int score = game.findBestMoves(m);		
//		game.visited.clear();
//		m.display(0);
//		game.performMove(m,score);
//		game.visited.clear();
//		System.out.println(game);
//		Checkers oppnGame = new Checkers(game);
//		System.out.println(game);
//		System.out.println(oppnGame);
		playGame();
	}

	static void playGame() {
		Checkers player = new Checkers();
		boolean turn = true;
		boolean gameOn = true;
		int black = 12;
		int white = 12;
		while (gameOn) {
			turn = !turn;
			System.out.println(player);
			int move[] = player.randomAgent().get(0);
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
