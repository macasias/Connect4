import java.util.Random;
import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

public class alphabeta_MoodyCasias extends AIModule
{
	public final double INFINITY = 1000.0;
	public final double BIG = 10.0;

	public final int NUM_DIRECTIONS = 8;

	public final int DIRECTION_UP    		= 0;
	public final int DIRECTION_LEFT  		= 1;
	public final int DIRECTION_DOWN  		= 2;
	public final int DIRECTION_RIGHT 		= 3;
	public final int DIRECTION_UP_LEFT 		= 4;
	public final int DIRECTION_DOWN_LEFT 	= 5;
	public final int DIRECTION_DOWN_RIGHT 	= 6;
	public final int DIRECTION_UP_RIGHT 	= 7;

	public final Point DIR_RIGHT = new Point(1, 0);
	public final Point DIR_LEFT = new Point(-1, 0);

	class Chain 
	{
		private Point start;
		private Point direction;
		private final GameStateModule state;
		private int me;

		public Chain (Point start, Point direction, final GameStateModule state, int me) throws Exception {
			this.start = new Point(start);
			this.direction = new Point(direction);
			this.state = state;
			this.me = me;

			if (!inBounds(start.x + 3 * direction.x, start.y + 3 * direction.y))
				throw new Exception();
		}

		public Chain (Point start, int direction, final GameStateModule state, int me) throws Exception {
			this(start, null, state, me);		
			switch (direction) {
				case DIRECTION_UP:
					this.direction = new Point(0, 1);
					break;
				case DIRECTION_LEFT:
					this.direction = new Point(-1, 0);
					break;
				case DIRECTION_DOWN:
					this.direction = new Point(0, -1);
					break;
				case DIRECTION_RIGHT:
					this.direction = new Point(1, 0);
					break;
				case DIRECTION_UP_LEFT:
					this.direction = new Point(-1, 1);
					break;
				case DIRECTION_DOWN_LEFT:
					this.direction = new Point(-1, -1);
					break;
				case DIRECTION_DOWN_RIGHT:
					this.direction = new Point(1, -1);
					break;
				case DIRECTION_UP_RIGHT:
					this.direction = new Point(1, 1);
					break;
			}
		}

		private boolean inBounds(int x, int y) {
			if (x >= 0 && x < state.getWidth()) {
				if (y >= 0 && y < state.getHeight())
					return true;
			}

			return false;
		}

		public int numOpen() {
			int num = 0;
			Point p = new Point(start);
			for (int i = 0; i < 4; i++) {
				if (state.getAt(p.x, p.y) == 0)
					num++;
				else if (state.getAt(p.x, p.y) != me)
					return 0;

				p.x += direction.x;
				p.y += direction.y;
			}

			return num;
		}

		public int chainLength() {
			int length = 0;
			Point p = new Point(start);
			for (int i = 0; i < 4; i++) {
				if (state.getAt(p.x, p.y) == me)
					length++;

				p.x += direction.x;
				p.y += direction.y;
			}

			return length;
		}

		public int movesToComplete() {
			int moves = 0;
			Point p = new Point(start);
			for (int i = 0; i < 4; i++) {
				if (state.getAt(p.x, p.y) == 0) {
					if (p.x == start.x)
						moves++;
					else {
						for (int o = p.y; o >= 0; o--) {
							if (state.getAt(p.x, o) == 0)
								moves++;
							else
								continue;
						}
					}
				}

				p.x += direction.x;
				p.y += direction.y;
			}

			return moves;
		}

		public double value() {
			if (chainLength() == 4)
				return INFINITY;
			else if (chainLength() == 3 && (direction.equals(DIR_RIGHT) || direction.equals(DIR_LEFT))) {
				Point preStart = new Point(start.x - direction.x, start.y - direction.y);
				if (state.getAt(preStart.x, preStart.y) == 0)
					return BIG;
			}

			return 1.0 / Math.pow((double) movesToComplete(), 2);
		}

		public boolean contains(Point point) {
			Point p = new Point(start);
			for (int i = 0; i < 4; i++) {
				if (p.equals(point))
					return true;

				p.x += direction.x;
				p.y += direction.y;
			}

			return false;
		}

		public boolean connectedToStart(Point point) {
			Point p = new Point(start);
			for (int i = 0; i < 4; i++) {
				if (p.equals(point))
					return true;

				if (state.getAt(p.x, p.y) != me)
					return false;

				p.x += direction.x;
				p.y += direction.y;
			}

			return false;
		}

		public Point getStart() {
			return start;
		}

		public Point getDirection() {
			return direction;
		}
	}

	private int me;
	private int enemy;

	private int depth;

	private Random random;

	public void getNextMove(final GameStateModule state) {
		me = state.getActivePlayer();
		enemy = (me == 1) ? 2 : 1;
		random = new Random(System.currentTimeMillis());
      
      
		try {
			depth = 1;
         int lastBestMove = 0;
         
			while (true){
				chosenMove = alphabeta(state, depth++, -INFINITY, INFINITY, lastBestMove);
            lastBestMove = chosenMove;
         }
		} catch (Exception e) {
			chosenMove = (chosenMove == -1) ? 0 : chosenMove;
		}
	}

	private int alphabeta(final GameStateModule state, int depth, double alpha, double beta, 
                           int firstMove) throws Exception {
		if (depth == 0) throw new Exception();
      
      int[] nextMoveOrder = new int[7];
      double[] allMoveValues = new double[7];

		int move = 0;
		double max = -INFINITY;

		ArrayList<Double> values = new ArrayList<Double>();
      
      if (state.canMakeMove(firstMove)){
   
         final GameStateModule newState = state.copy();
   	   newState.makeMove(firstMove);
   	   double value = beta(newState, depth - 1, alpha, beta);
   	   values.add(value);
   
   	   if (value > max) {
   		   max = value;
   	   	move = firstMove;
   	   }
            
         alpha = Math.max( alpha, max );
      }


      for(int i = 0; i < state.getWidth(); i++){
   		if (terminate) throw new Exception();
        
         if( i == firstMove ){} //skip this move, already did it        
         
         else{
   			if (!state.canMakeMove(i)) continue;
   
   			final GameStateModule newState = state.copy();
   			newState.makeMove(i);
   			double value = beta(newState, depth - 1, alpha, beta);
   			values.add(value);
   
   			if (value > max) {
   				max = value;
   				move = i;
   			}
            
            alpha = Math.max( alpha, max );
            
            if(max > beta){
               break; 
            }
         }
		}


		double first = values.get(0); //if all values have the same outcome
		boolean different = false;
		for (Double value : values) {
			if (value != first) {
				different = true;
				break;
			}
		} 

		if (!different) { //then play a random move (so its' not in column 0 every time
			ArrayList<Integer> legalMoves = new ArrayList<Integer>(); 
			for (int i = 0; i < state.getWidth(); i++) {
				if (state.canMakeMove(i))
					legalMoves.add(i);
			}

			move = legalMoves.get(Math.abs(random.nextInt()) % legalMoves.size());
		}

		return move;
	}

	private double alpha(final GameStateModule state, int depth, double alpha, double beta) throws Exception {
		if (state.isGameOver()) return -INFINITY;
		if (depth == 0) return getValue(state);

		double max = -INFINITY;
   outerloop:
		for (int i = 0; i < state.getWidth(); i++) {
			if (terminate) throw new Exception();
			if (!state.canMakeMove(i)) continue;

			final GameStateModule newState = state.copy();
			newState.makeMove(i);
			double value = beta(newState, depth - 1, alpha, beta);
			max = Math.max(max, value);
         alpha = Math.max(alpha, max);
         if(max > beta){
            break outerloop;
         }
		}

		return max;
	}

	private double beta(final GameStateModule state, int depth, double alpha, double beta) throws Exception {
		if (state.isGameOver()) return INFINITY;
		if (depth == 0) return getValue(state);

		double min = INFINITY;

		for (int i = 0; i < state.getWidth(); i++) {
			if (terminate) throw new Exception();
			if (!state.canMakeMove(i)) continue;
			
			final GameStateModule newState = state.copy();
			newState.makeMove(i);
			double value = alpha(newState, depth - 1, alpha, beta);
			min = Math.min(min, value);
         beta = Math.min(beta, min);
         
         if(min < alpha){
            break;
         }
		}

		return min;
	}

	private double getValue(final GameStateModule state) throws Exception {
		ArrayList<Chain> myChains = getChains(state, me);
		ArrayList<Chain> enemyChains = getChains(state, enemy);

		double myValue = 0.0;
		for (Chain chain : myChains)
			myValue += chain.value();

		double enemyValue = 0.0;
		for (Chain chain : enemyChains)
			enemyValue += chain.value();

		return myValue - enemyValue;
	}

	private ArrayList<Chain> getChains(final GameStateModule state, int player) throws Exception {
		ArrayList<Chain> chains = new ArrayList<Chain>();

		for (int i = 0; i < NUM_DIRECTIONS; i++) {
			ArrayList<Chain> current = new ArrayList<Chain>();

			Point startLocation = null;
			Point stepDirection1 = null;
			Point stepDirection2 = null;
			Point scanDirection = null;
			Point direction = null;
			int iterations = 1;

			switch (i) {
				case DIRECTION_UP:
					startLocation = new Point(0, 0);
					stepDirection1 = new Point(0, 1);
					scanDirection = new Point(1, 0);
					direction = new Point(0, 1);
					break;
				case DIRECTION_LEFT:
					startLocation = new Point(state.getWidth() - 1, 0);
					stepDirection1 = new Point(-1, 0);
					scanDirection = new Point(0, 1);
					direction = new Point(-1, 0);
					break;
				case DIRECTION_DOWN:
					startLocation = new Point(0, state.getHeight() - 1);
					stepDirection1 = new Point(0, -1);
					scanDirection = new Point(1, 0);
					direction = new Point(0, -1);
					break;
				case DIRECTION_RIGHT:
					startLocation = new Point(0, 0);
					stepDirection1 = new Point(1, 0);
					scanDirection = new Point(0, 1);
					direction = new Point(1, 0);
					break;
				case DIRECTION_UP_LEFT:
					startLocation = new Point(state.getWidth() - 1, 0);
					stepDirection1 = new Point(-1, 0);
					stepDirection2 = new Point(0, 1);
					scanDirection = new Point(1, 1);
					direction = new Point(-1, 1);
					iterations = 2;
					break;
				case DIRECTION_DOWN_LEFT:
					startLocation = new Point(state.getWidth() - 1, state.getHeight() - 1);
					stepDirection1 = new Point(0, -1);
					stepDirection2 = new Point(-1, 0);
					scanDirection = new Point(-1, 1);
					direction = new Point(-1, -1);
					iterations = 2;
					break;
				case DIRECTION_DOWN_RIGHT:
					startLocation = new Point(0, state.getHeight() - 1);
					stepDirection1 = new Point(0, -1);
					stepDirection2 = new Point(1, 0);
					scanDirection = new Point(1, 1);
					direction = new Point(1, -1);
					iterations = 2;
					break;
				case DIRECTION_UP_RIGHT:
					startLocation = new Point(0, 0);
					stepDirection1 = new Point(0, 1);
					stepDirection2 = new Point(1, 0);
					scanDirection = new Point(1, -1);
					direction = new Point(1, 1);
					iterations = 2;
					break;
			}


			for (int o = 0; o < iterations; o++) {
				Point stepDirection = (o == 0) ? new Point(stepDirection1) : new Point(stepDirection2);
				if (o > 0) {
					startLocation.x += stepDirection.x;
					startLocation.y += stepDirection.y;
				}

				while (startLocation.x >= 0 && startLocation.x < state.getWidth() && startLocation.y >= 0 && startLocation.y < state.getHeight()) {
					if (terminate) throw new Exception();

					Point currentLocation = new Point(startLocation);

					while (currentLocation.x >= 0 && currentLocation.x < state.getWidth() && currentLocation.y >= 0 && currentLocation.y < state.getHeight()) {
						if (terminate) throw new Exception();

						if (state.getAt(currentLocation.x, currentLocation.y) == player) {
							boolean newchain = true;
							for (Chain chain : current) {
								if (terminate) throw new Exception();

								if (chain.connectedToStart(currentLocation)) {
									newchain = false;
									break;
								}
							}

							if (newchain) {
								try {
									Chain chain = new Chain(currentLocation, direction, state, player);
									if (chain.chainLength() == 4) {
										chains.clear();
										chains.add(chain);
										return chains;
									} else if (chain.numOpen() > 0) {
										current.add(chain);
									}
								} catch (Exception e) {
									// Do nothing
								}
							}
						}

						currentLocation.x += scanDirection.x;
						currentLocation.y += scanDirection.y;
					}

					startLocation.x += stepDirection.x;
					startLocation.y += stepDirection.y;
				}
			}

			chains.addAll(current);
		}

		return chains;
	}
}
