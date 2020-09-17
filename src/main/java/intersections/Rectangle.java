package intersections;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Rectangle Class:  Object representation of rectangle coordinates considered by this program. 
 * 
 * Exists in 2 variants: 
 * 
 * 	1. Initial Rectangle: Instance represents rectangle coordinates found directly from client JSON file.  Marked uniquely by an id field. 
 * 
 * 	2. Intersection Rectangle: Instance represents intersection of initial or other intersection rectangles.  Marked by list of contributing initial rectangles. 
 * 
 * */


public class Rectangle {

	//Rectangle spatial parameters: 
	private int deltaX; 
	private int deltaY; 
	private int x; 
	private int y; 
	
	//Unique Id of initial contributing rectangle: 
	private final int id;
	
	//List of contributing initial input rectangles: 
	private ArrayList<Integer> contributors; 
	
	
	//Constructor for initial rectangle from JSON file (id present): 
	public Rectangle(int deltaX, int deltaY, int x, int y, int id) {
		
		//Check for negative or 0 rectangle parameter values: 
		if (deltaX <= 0 || deltaY <= 0 || x <= 0 || y <= 0 || id < 0) 
			throw new IllegalArgumentException("Negative or 0 value parameter refused");
		
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	
	//Constructor for intersection rectangle (contributors listeD): 
	public Rectangle(int deltaX, int deltaY, int x, int y) {
		this(deltaX, deltaY, x, y, 0);
		this.contributors = new ArrayList<Integer>(); 
	}


	public int getDeltaX() {
		return deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Integer> getContributors() {
		return contributors; 
	}
	
	//TESTING PURPOSES ONLY! (This would not be used in production code in this class): 
	public void setContributors(ArrayList<Integer> newContributors) {
		this.contributors = newContributors; 
	}
	
	
	/**
	 * Determines if intersection between two rectangles is made of unique contributors. 
	 * Uniqueness of intersection is determined by initial contributors, even if spatially degenerate.  
	 * 
	 * @param rectangle2, rectangle to compare current instance to. 
	 * 
	 * @return true, intersection contains unique contributors. 
	 * @return false, intersection contains no unique contributors. 
	 * */
	public boolean isIntersectionUnique(Rectangle rectangle2) {
		if (this.x == rectangle2.getX() && 
			this.y == rectangle2.getY()	&&
			this.deltaX == rectangle2.getDeltaX() && 
			this.deltaY == rectangle2.getDeltaY() && 
			!areContributorsUnique(rectangle2))
			
			return false; 
		
		else 
			return true; 
	}
	
	
	/**
	 * Adds any unique contributors from another rectangle's contributor list to the instance's contributors. 
	 * 
	 * @param contributorList, the list of contributors from another rectangle. 
	 * 
	 * */
	public void addUniqueContributors(ArrayList<Integer> contributorList) {
		//Compare contributors from both lists: 
		if (contributorList != null) {
			for (Integer contributor : contributorList) {
				
				//Add contributor as unique if list is empty: 
				if (contributors.size() == 0)
					contributors.add(contributor);
				
				//Add contributor only if it is not in current list: 
				else if (isContributorUnique(contributor))
					contributors.add(contributor); 
			}
		}  
		Collections.sort(contributors);
	}
	
	
	/**
	 * Adds id fields of two initial input rectangles to intersection rectangles. 
	 * Method is only applied to intersection rectangles comprised purely of initial rectangles. 
	 * 
	 * @param id1, id value from 1st input rectangle. 
	 * @param id2, id value from 2nd input rectangle. 
	 * 
	 * */
	public void addInitialContributors(Integer id1, Integer id2) {
		contributors.add(id1); 
		contributors.add(id2);
	}
	
	
	/**
	 * Determines if current instance contains unique contributors relative to another rectangle. 
	 * 
	 * @param rectangle, a second rectangle for contributor comparison. 
	 * 
	 * @return true, at least one contributor is unique both rectagnles. 
	 * @return false, no contributors are unique among both rectangles.
	 * */
	private boolean areContributorsUnique(Rectangle rectangle) {
		//Scan across contributors of input rectangle and compare to instance contributors: 
		for (Integer contributor : rectangle.getContributors()) {
			boolean isUnique = isContributorUnique(contributor);
			if (isUnique)
				return true; 
		}
		
		return false; 
	}
	
	
	/**
	 * Determines if external contributor exists in current instance list of contributors. 
	 * 
	 * @param contributor, a contributor of an external rectangle. 
	 * 
	 * @return true, contributor not found in current instance contributors. 
	 * @return false, contributor found in current instance contributors. 
	 * 
	 * */
	private boolean isContributorUnique(Integer contributor) {
		Collections.sort(contributors); 
		int searchItem = Collections.binarySearch(contributors, contributor); 
		if (searchItem < 0) 
			return true; 
		else
			return false; 
	}
	
	
}
