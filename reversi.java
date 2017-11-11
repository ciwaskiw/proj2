import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class reversi {
	protected static HashMap<Point, Integer> board = new HashMap<Point, Integer>();
	
	static void processInput(Scanner input) {
		for(int i = 0; i < 8; i++) {
			board.put(new Point(0,i) , input.nextInt());
		}
		for(int i = 0; i < 10; i++) {
			board.put(new Point(1,i) , input.nextInt());
		}
		for(int i = 0; i < 12; i++) {
			board.put(new Point(2,i) , input.nextInt());
		}
		for(int i = 0; i < 14; i++) {
			board.put(new Point(3,i) , input.nextInt());
		}
		for(int i = 0; i < 14; i++) {
			board.put(new Point(4,i) , input.nextInt());
		}
		for(int i = 0; i < 12; i++) {
			board.put(new Point(5,i) , input.nextInt());
		}
		for(int i = 0; i < 10; i++) {
			board.put(new Point(6,i) , input.nextInt());
		}
		for(int i = 0; i < 8; i++) {
			board.put(new Point(7,i) , input.nextInt());
		}
		
	}


	private static void getLegalMoves(Point disk, Set<Point> moves) {
		System.out.println("getLegalMoves called");
		int cur_x = disk.x;
		int cur_y = disk.y;
		// sample code for adding possible move in ¡ü direction
		Point next = new Point(cur_x - 1, cur_y);

		while(board.containsKey(next) && board.get(next) == 2) {
			Point possible_move = new Point(next.x-1, next.y);
			if(board.containsKey(possible_move)) {
				if(board.get(possible_move) == 0) {
					System.out.println("legal move at (" + possible_move.x + "," + possible_move.y + ")");
					moves.add(possible_move);
					break;
				} 
			}
			System.out.println("searching for next");
			next = possible_move;
		}
		
		
	}
	
	private static Set<Point> legalMoves(){
		Set<Point> moves = new HashSet<>();
		Set<Point> disks = board.keySet();
		for(Point disk: disks) {
			if(board.get(disk) == 1) {
				getLegalMoves(disk,moves);
			}
		}
		return moves;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		processInput(input);
		HashSet<Point> result = new HashSet<>();
		getLegalMoves(new Point(6,7),result);
		System.out.println(result.toString());
	}

}
