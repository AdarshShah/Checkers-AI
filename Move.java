import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/*
 * Move object is used to create OR Graph that enables to keep track of sequence of moves possible from currecnt position
*/
public class Move {
	int row,col,dice;
	Move parent;
	Set<Move> children;
	
	/*
	 * Copy Constructor
	 */public Move(int row, int col, int dice, Move parent) {
		super();
		this.row = row;
		this.col = col;
		this.dice = dice;
		this.parent = parent;
		this.children=new HashSet<Move>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	/*
	 * Two Move Objects are equal only if their row and cols are equal
	 */@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	/*
	 * For Debugging Purpose
	 * Displays all the move sequences possible
	 */public void display(int x) {
		String str="";
		for(int i=0;i<x;i++) str+="\t";
		System.out.println(str+row+" "+col);
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			Move move = (Move) iterator.next();
			move.display(x+1);
		}
	}

	@Override
	public String toString() {
		return "Move [row=" + row + ", col=" + col + ", dice=" + dice + "]";
	}
	
	
	
}
