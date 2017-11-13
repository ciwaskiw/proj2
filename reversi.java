// (placeholder team name): Michael Crouch (113581236), Jin Liu, Chris Iwaskiw

import java.awt.Point;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * computer player for reversi on a custom board
 **/
public class reversi {
	protected static HashMap<Point, Integer> board = new HashMap<Point, Integer>();
	
	/* Note: notice that positionValue[y][x] is the position value of point(x,y) */
	protected static int[][] positionValueTable = {
			{0, 0, 0, 120, -20, 20, 5, 5, 20, -20, 120, 0, 0, 0},
			{0, 0, 120, -40, -40, 0, 0, 0, 0, -40, -40, 120, 0, 0,},
			{0, 120, -40, -40, -5, 15, 18, 18, 15, -5, -40, -40, 120,0},
			{120, -20, -20, 0, 10, 13, 1, 1, 13, 10, 0, -20, -20, 120},
			{120, -20, -20, 0, 10, 13, 1, 1, 13, 10, 0, -20, -20, 120},
			{0, 120, -40, -40, -5, 15, 18, 18, 15, -5, -40, -40, 120,0},
			{0, 0, 120, -40, -40, 0, 0, 0, 0, -40, -40, 120, 0, 0,},
			{0, 0, 0, 120, -20, 20, 5, 5, 20, -20, 120, 0, 0, 0}
			};

/*	                  120   -20   20   5   5   20   -20   120
                     120  -40   -40   0    0   0   0    -40   -40   120	
                120  -40  -40   -5    15   18  18  15   -5    -40   -40   120
          120  -20   -20    0   10    13   1   1   13   10     0    -20   -20   120
          120  -20   -20    0   10    13   1   1   13   10     0    -20   -20   120
                120  -40   -40  -5    15   18  18  15   -5    -40   -40   120
                     120   -40  -40   0    0   0   0    -40   -40   120	
                           120  -20   20   5   5   20   -20   120
*/
	
/*	
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
		System.out.println("check (" + disk.x + "," + disk.y + ")getLegalMoves called");
		int cur_x = disk.x;
		int cur_y = disk.y;
		int deltaX = 20, deltaY = 20;
		int direction = 0;
		
		// check each direction
		for(int i=0;i<8;i++){
			switch(i){
			case 0: deltaX = -1; //left
					deltaY = 0;
					direction = 2;
					break;
			case 1: deltaX = 0; //up
					deltaY = -1;
					direction = 3;
					break;
			case 2: deltaX = 1; //right
					deltaY = 0;
					direction = 0;
					break;
			case 3: deltaX = 0; //down
					deltaY = 1;
					direction = 1;
					break;
			case 4: deltaX = -1;
					deltaY = -1; //Upper left
					direction = 6;
					break;
			case 5: deltaX = -1;
					deltaY = 1; //Bottom left
					direction = 7;
					break;
			case 6: deltaX = 1;
					deltaY = 1; //Bottom right
					direction = 4;
					break;
			case 7: deltaX = 1;
					deltaY = -1; //Upper right
					direction = 5;
					break;
			default:break;		//shouldn't happen 
			}
			
			Point next = new Point(cur_x+deltaX, cur_y+deltaY);
			while(board.containsKey(next) && board.get(next) == 2) {
				Point possible_move = new Point(next.x+deltaX, next.y+deltaY);
			
				if(board.containsKey(possible_move)&& board.get(possible_move) == 0) {
					System.out.println("legal move at (" + possible_move.x + "," + possible_move.y + ")");
					// the direction is the reverse direction of checking direction
					moves.put(possible_move, direction); 
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
	 * Make move at point p. **The method will be called only if the move is legal**
	 * @param direction : corresponding to direction value setting in getLegalMoves()
	 */
	private static void makeMove(Point move, int direction) {
		
		int cur_x = move.x;
		int cur_y = move.y;
		int deltaX = 20, deltaY = 20;
		board.put(move, 1);
		
		switch(direction){
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
			case 4: deltaX = -1;
					deltaY = -1; //Upper left
					break;
			case 5: deltaX = -1;
					deltaY = 1; //Bottom left
					break;
			case 6: deltaX = 1;
					deltaY = 1; //Bottom right
					break;
			case 7: deltaX = 1;
					deltaY = -1; //Upper right
					break;
			default:break;		//shouldn't happen 
			}
			
			Point next = new Point(cur_x + deltaX, cur_y + deltaY);
			while(board.get(next) != 1) {
				Point reverse = new Point(next.x, next.y);
				board.put(reverse, 1);
				next = new Point(next.x + deltaX, next.y + deltaY);
			}
		
	}
	
	
	/*
	 * Mobility Evaluation: Calculate the difference between number of possible moves
	 */
	private int diffInMobility() {
		int numOfPlayer = 0;
		int numOfOpponent = 0;
		for(Point disk: board.keySet()) {
			if(board.get(disk) == 1) {
				numOfPlayer ++;
			}else if(board.get(disk) == 2){
				numOfOpponent ++;
			}
		}
		
		return numOfPlayer - numOfOpponent;
	}
	
	/*
	 * According to preset position value table, calculate the difference between the position values.
	 */
	private int diffInPostionValue() {
		int myValue = 0;
		int opponentValue = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 14; j++) {
				Point position = new Point(j,i);
				if(board.get(position) == 1) {
					myValue += positionValueTable[i][j];
				}else if(board.get(position) == 2) {
					opponentValue += positionValueTable[i][j];
				}
			}
		}
		
		return myValue - opponentValue;
	}
	
	/*
	 * Main
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		processInput(input);
		HashMap<Point, Integer> result;
		result = legalMoves();

	}

}
