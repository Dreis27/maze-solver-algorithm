import java.util.ArrayList;

public class MazeSquare extends GameSquare
{
	private GameBoard board;			// A reference to the GameBoard this square is part of.
	private boolean target;				// true if this square is the target of the search.
	private boolean visited = false;	// Keeps track of whether the square has already been visited
	private ArrayList<MazeSquare> shortestPath = null; // A list to store the shortest path in
	/**
	 * Create a new GameSquare, which can be placed on a GameBoard.
	 * 
	 * @param x the x co-ordinate of this square on the game board.
	 * @param y the y co-ordinate of this square on the game board.
	 * @param board the GameBoard that this square resides on.
	 */
	public MazeSquare(int x, int y, GameBoard board)
	{
		super(x, y);
		this.board = board;
	}

	/**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the end point for the search.
	 * 
	 */	
    public void leftClicked()
	{
		board.reset(1); // remove both highlighting from the squares and target square if leftClicked
		this.setHighlight(true); // highlight the target square so user knows it's selected
		this.target = true;
		setShortestPath(null); // clear the shortest path
	}
    
    /**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the start point for the search. 
	 */	
	public void rightClicked() 
	{ 
		board.reset(0); // remove highlighting from the squares if rightClicked
		findShortestPath();
	}

	/**
	 * A method that is invoked when a reset() method is called on GameBoard.
	 * 
	 * @param n An unspecified value that matches that provided in the call to GameBoard reset()
	 */
	public void reset(int n)
	{
		setHighlight(false);
		if(n==1) setTarget(false);

	}

	// Getter methods
	public boolean isTarget(){
		return target;
	}

	public ArrayList<MazeSquare> getShortestPath() {
		return shortestPath;
	}

	public boolean isVisited(){
		return visited;
	}

	// Setter methods
	public void setVisited(boolean isVisited){
		this.visited = isVisited;
	}

	public void setTarget(boolean isTarget){
		this.target = isTarget;
	}

	public void setShortestPath(ArrayList<MazeSquare> newShortestPath) {
		this.shortestPath = newShortestPath;
	}

	// Recursive DFS algorithm to find the shortest path
	public void depthFirstSearch(MazeSquare current, ArrayList<MazeSquare> path) {

		int x = current.getXLocation();
		int y = current.getYLocation();

		if(current.isVisited()) return;

		current.setVisited(true);
		path.add(current);

		/* If a path to the target is found,
		 * compare it to the current shortest path,
		 * and modify the current path accordingly.
		 */
		if(current.isTarget()){
			if (getShortestPath() == null || path.size() < getShortestPath().size()) {
				setShortestPath(new ArrayList<>(path));
			}
		} else {
			// get neighbours of the current square
			MazeSquare[] neighbours = {
				(MazeSquare) board.getSquareAt(x-1, y), // left neighbour
				(MazeSquare) board.getSquareAt(x+1, y), // right neighbour
				(MazeSquare) board.getSquareAt(x, y-1), // below neighbour
				(MazeSquare) board.getSquareAt(x, y+1) }; // above neighbour

			for (int i = 0; i < neighbours.length; i++) {
				// call depth first search on a neighbor if it exists 
				if (!current.getWall(i) && neighbours[i] != null) {
					depthFirstSearch(neighbours[i], path);
				}
			}
		}

		// backtracking
		current.setVisited(false);
		path.remove(path.size() - 1);
	}

	// Finction to find andprint the shortest path
	public ArrayList<MazeSquare> findShortestPath() {

		ArrayList<MazeSquare> path = new ArrayList<>();
		depthFirstSearch(this, path);

		// Print the shortest path to the console
		try {
			System.out.println("The shortest path to the target:");

			for(int i = 0; i<shortestPath.size(); i++){
				System.out.print("(" + getShortestPath().get(i).getXLocation() + ", " + getShortestPath().get(i).getYLocation() + ")"); // print the path to the target

				// get the square to highlight
				GameSquare square = board.getSquareAt(getShortestPath().get(i).getXLocation(), getShortestPath().get(i).getYLocation());

				square.setHighlight(true); // paint the square on the shortest path

				if(!(i == shortestPath.size()-1)){
					System.out.print(" -> ");
				} else {
					System.out.print("\n");
				}
			}
			// print the length of the path
			System.out.println("Path length: " + getShortestPath().size() + " squares\n");
			return shortestPath;

		// handle case where path doesn't exist
		} catch (NullPointerException e) {

			System.out.println("The path doesn't exist!\n");
			return null;
		}
	}
}
