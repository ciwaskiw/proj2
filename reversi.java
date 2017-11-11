// (placeholder team name): Michael Crouch (113581236), Jin Liu, Chris Iwaskiw

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * computer player for reversi on a custom board
 **/
public class reversi {
	protected static HashMap<Point, Integer> board = new HashMap<Point, Integer>();
	
	/*
	 * reads the next board from a scanner
	 * (scanner should be set to stdin)
	 */
	static void processInput(Scanner input) {
		for(int i = 0; i < 8; i++) {
			board.put(new Point(i+3,0) , input.nextInt());
		}
		for(int i = 0; i < 10; i++) {
			board.put(new Point(i+2,1) , input.nextInt());
		}
		for(int i = 0; i < 12; i++) {
			board.put(new Point(i+1,2) , input.nextInt());
		}
		for(int i = 0; i < 14; i++) {
			board.put(new Point(i,3) , input.nextInt());
		}
		for(int i = 0; i < 14; i++) {
			board.put(new Point(i,4) , input.nextInt());
		}
		for(int i = 0; i < 12; i++) {
			board.put(new Point(i+1,5) , input.nextInt());
		}
		for(int i = 0; i < 10; i++) {
			board.put(new Point(i+2,6) , input.nextInt());
		}
		for(int i = 0; i < 8; i++) {
			board.put(new Point(i+3,7) , input.nextInt());
		}
		
	}


	/*
	 * gets the legal moves and tokens taken
	 * 'disk' - Our piece on the board at one end of our move
	 * 'moves' - map of possible moves and the number of pieces taken
	 *  
	 */
	private static void getLegalMoves(Point disk, HashMap<Point, Integer> moves) {
		System.out.println("getLegalMoves called");
		int cur_x = disk.x;
		int cur_y = disk.y;
		int deltaX = 20, deltaY = 20;
		int score = 0;
		
		// check each direction
		for(int i=0;i<4;i++){
			switch(i){
			case 0: deltaX = -1; //left
					deltaY = 0;
					break;
			case 1: deltaX = 0; //up
					deltaY = -1;
					break;
			case 2: deltaX = 1; //right
					deltaY = 0;
					break;
			case 3: deltaX = 0; //down
					deltaY = 1;
					break;
			default:break;		//shouldn't happen 
			}
			
			//go over every '2' piece, increasing score, and record the location of the first empty space
			score = 0;
			Point next = new Point(cur_x+deltaX, cur_y+deltaY);
			while(board.containsKey(next) && board.get(next) == 2) {
				Point possible_move = new Point(next.x+deltaX, next.y+deltaY);
				score++;
			
				if(board.containsKey(possible_move)&& board.get(possible_move) == 0) {
					System.out.println("legal move at (" + possible_move.x + "," + possible_move.y + ")");
					moves.put(possible_move, score); 
				}
				next = possible_move;
			}
		}
		
	}
	
	/*
	 * Quick setup for getLegalMoves()
	 */
	private static HashMap<Point, Integer> legalMoves(){
		HashMap<Point, Integer> moves = new HashMap<Point, Integer>();
		Set<Point> disks = board.keySet();
		for(Point disk: disks) {
			if(board.get(disk) == 1) {
				getLegalMoves(disk,moves);
			}
		}
		return moves;
	}

	/*
	 * Main
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		processInput(input);
		HashMap<Point, Integer> result;
		result = legalMoves();
		System.out.println(result.toString());
	}

}
