package intersections;


//Local imports and Java language imports: 
import java.util.ArrayList;


/**
 * IntersectionReporter Class:  Algorithmic class that calculates and outputs on the set of all existing intersections among a set of rectangles from a client JSON file. 
 * 
 * Algorithm Process: 
 * 
 * 		1. Generate initial intersection rectangles from initial rectangles. 
 * 
 * 		2. Recurse and generate a new set of intersection rectangles from all previous set of intersection rectangles. 
 * 
 * 		3. Continue until all intersection rectangles have been found among set of initial rectangles. 
 * 
 * 
 * Nature of Recursion: A deeper level of recursion represents intersections comprised of larger numbers of contributing initial rectangles. 
 * 
 * */



public class IntersectionReporter {

	//Initial rectangles from JSON
	private ArrayList<Rectangle> initialRectangles; 
	
	//Intersections found by algorithm
	private ArrayList<Rectangle> allIntersections; 
	
	//Last intersection generated at a level of recursion
	private int lastIntersection; 
	
	public IntersectionReporter(ArrayList<Rectangle> initialRectangles) {
		//Check for Null list: 
		if (initialRectangles == null) 
			throw new NullPointerException("Null list of rectangles not accepted"); 
		
		//Check for null rectangles in list: 
		for (Rectangle rectangle : initialRectangles) {
			if (rectangle == null)
				throw new NullPointerException("At least 1 rectangle is null in list"); 
		}
		
		this.initialRectangles = initialRectangles; 
		this.allIntersections = new ArrayList<Rectangle>();
		this.lastIntersection = 0; 
	}
	
	
	/**
	 * Client level method to recursively finds all intersections of initial rectangles given to Reporter instance. 
	 * 
	 * @return allIntersections, the set of all intersections found for all input rectangles from JSON file. 
	 * */
	public ArrayList<Rectangle> getIntersections() {
		//Generate initial rectangles: 
		generateIntersections(initialRectangles, 0);
		boolean noIntersection = noIntersections(0); 
		
		//Generate further rectangles until all intersection rectangles are found: 
		getIntersections(noIntersection, this.lastIntersection - 1);
		
		return allIntersections; 
	}
	
	
	/**
	 * Recursive portion of algorithm that finds intersections comprised of more than 2 contributing rectangles (contributors). 
	 * 
	 * @param noIntersections, boolean value that reports whether or not the current set of intersection rectangles have more intersections. 
	 * 
	 * @param lastIntersection, index of the last intersection rectangle from the previous recursive calculation of sets.  
	 * 
	 * */
	private void getIntersections(boolean noIntersections, int lastIntersection) {
		//Base case: No more intersections left to calculate: 
		if (noIntersections)
			return; 
		
		//Intersections still exist: 
		if (!noIntersections) {
			//Generate intersections using last intersection index as reference point: 
			int oldLast = lastIntersection; 
			generateIntersections(allIntersections, oldLast);
			
			//Determine if more intersections exist in current set of intersections: 
			noIntersections = noIntersections(oldLast); 
			getIntersections(noIntersections, this.lastIntersection - 1);
		}
	}
	
	
	/**
	 * 
	 * Iteratively prints out x, y, deltaX, and deltaY parameters of input rectangles. 
	 * 
	 * */
	public void printInputs() {
		System.out.println("Inputs: ");
		for (Rectangle rectangle : initialRectangles) {
			System.out.println(rectangle.getId() + ": Rectangle at " + 
							 "(" + rectangle.getX() + "," + rectangle.getY() + "),"
							  + " delta_x=" + rectangle.getDeltaX() + ", "
							  + " delta_y=" + rectangle.getDeltaY());
		}
	}
	
	
	/**
	 * Iteratively prints out all intersection rectangle parameters (x, y, deltaX, deltaY), and all contributing rectangles per intersection. 
	 * 
	 * @param allIntersections, the list of all intersection rectangles found among a set of input rectangles. 
	 * 
	 * */
	public void printIntersections(ArrayList<Rectangle> allIntersections) {
		System.out.println("Intersections: ");
		for (int i = 0; i < allIntersections.size(); i++) {
			Rectangle rectangle = allIntersections.get(i); 
			StringBuilder outputBuilder = new StringBuilder();
			
			//Append contributing rectangles to output string: 
			outputBuilder.append("" + (i + 1) + ": Between rectangle");
			ArrayList<Integer> contributors = rectangle.getContributors(); 
			for (Integer contributor : contributors) {
				if (contributors.indexOf(contributor) == contributors.size() - 1)
					outputBuilder.append(" and " + contributor); 
				else 
					outputBuilder.append(" " + contributor + ","); 
			}
			
			//Append rectangle parameter information to output string: 
			outputBuilder.append(" at (" + rectangle.getX() + "," + rectangle.getY() + ")"
			        +  "), " + "delta_x= " + rectangle.getDeltaX()
			        +  ",  delta_y= " + rectangle.getDeltaY() + ".");

			String output = outputBuilder.toString(); 
			System.out.println(output);
		}
	}
	
	
	/**
	 * Calculates parameters of an intersection rectangle between 2 rectangles and returns a new rectangle instance: 
	 * Bottom-right points calculated for intersection calculations. 
	 * 
	 *  @param rectangle1, 1st rectangle. 
	 *  @param rectangle2, 2nd rectangle. 
	 *  
	 *  @return newRectangle, the intersection rectangle of two rectangles. 
	 * */
	public Rectangle findIntersection(Rectangle rectangle1, Rectangle rectangle2) {
		//Check for Null rectangles: 
		if (rectangle1 == null || rectangle2 == null)
			throw new IllegalArgumentException("Null inputs are not accepted");
		
		//rectangle1 coordinates: 
		int x1 = rectangle1.getX(); 
		int y1 = rectangle1.getY(); 
		int x2 = rectangle1.getX() + rectangle1.getDeltaX(); 
		int y2 = rectangle1.getY() + rectangle1.getDeltaY();
		
		//rectangle2 coordinates: 
		int x3 = rectangle2.getX(); 
		int y3 = rectangle2.getY(); 
		int x4 = rectangle2.getDeltaX() + rectangle2.getX();
		int y4 = rectangle2.getDeltaY() + rectangle2.getY(); 
		
		
		//Intersecting rectangle coordinates: 
		int x5 = Math.max(x1, x3);
		int y5 = Math.max(y1, y3); 
		int x6 = Math.min(x2, x4); 
		int y6 = Math.min(y2, y4); 
		int deltaX = x6 - x5; 
		int deltaY = y6 - y5; 
		
		
		//Prevent creation of "0 area" or negative area intersection triangles: 
		if (deltaX <= 0 || deltaY <= 0) throw new IllegalArgumentException("Negative overlap not allowed");  
		
		//Add intersection rectangle to list: 
		Rectangle newRectangle = new Rectangle(deltaX, deltaY, x5, y5); 
		if (rectangle1.getContributors() == null && rectangle2.getContributors() == null) {
			newRectangle.addInitialContributors(rectangle1.getId(), rectangle2.getId());
		} 
			
		return newRectangle; 
	}
	
	
	/**
	 * Determines if 2 rectangles overlap with each other or not. 
	 * Variable names indicate spatial orientations of points. 
	 * 
	 * @param rectangle1, 1st rectangle. 
	 * @param rectangle2, 2nd rectangle. 
	 * 
	 * @return true, overlap exists. 
	 * @return false, no overlap exists. 
	 * 
	 * */
	public boolean overlapPresent(Rectangle rectangle1, Rectangle rectangle2) {
		//Null Input Check: 
		if (rectangle1 == null || rectangle2 == null)
			throw new IllegalArgumentException("Null rectangle inputs not accepted");
		
		
		//Rectangle 1 coordinates: 
		int R1LeftX = rectangle1.getX();
		int R1RightX = R1LeftX + rectangle1.getDeltaX(); 
		int R1BottomY = rectangle1.getY(); 
		int R1TopY = R1BottomY + rectangle1.getDeltaY();  
	
		//Rectangle 2 coordinates: 
		int R2LeftX = rectangle2.getX();
		int R2RightX = R2LeftX + rectangle2.getDeltaX(); 
		int R2BottomY = rectangle2.getY(); 
		int R2TopY = R2BottomY + rectangle2.getDeltaY();  
	
	
		//Determine if at least one set of sides overlap between two rectangles: 
		if (R1LeftX > R2RightX || R1RightX < R2LeftX || R1TopY < R2BottomY || R1BottomY > R2TopY) {
			return false; 
		} else {
			return true; 
		}
	}
	
	
	/**
	 * Iteratively determines if intersections exist in the overall set of rectangles. 
	 * 
	 * @param lastIntersection, index of the last intersection generated in previous recursion. 
	 * 
	 * @return true, an intersection was found between two rectangles of intersection set. 
	 * @return false, no intersections found among intersection set rectangles. 
	 * */
	
	private boolean noIntersections(int lastIntersection) {
		int endingPoint = allIntersections.size();
		
		//Iterate from last intersection generated to end of list: 
		for (int i = lastIntersection; i < endingPoint; i++) {
			Rectangle rectangle1 = allIntersections.get(i); 
			
			//Iterate from next rectangle from rectangle1 to avoid redundant comparisons (AnB / BnA intersections): 
			for (int j = i + 1; j < endingPoint; j++) {
				Rectangle rectangle2 = allIntersections.get(j);
				
				//Check if overlap exists between two rectangles: 
				if (overlapPresent(rectangle1, rectangle2)) 
					return false; 
			}
		}
		return true; 
	}
	
	
	
	/**
	 * Generates intersections between rectangles in a set, using the last intersection index as reference. 
	 * 
	 * @param rectangleList, the list of rectangles to generate intersections from. 
	 * @param lastIntersection, index of most recent intersection to start iterating from. 
	 * 
	 * 
	 * */
	private void generateIntersections(ArrayList<Rectangle> rectangleList, int lastIntersection) {
		int endPoint = rectangleList.size(); 
		
		//Iterate from last intersection point to end of set: 
		for (int i = lastIntersection; i < endPoint; i++) {
			
			Rectangle rectangle1 =  rectangleList.get(i);
			int startPoint = rectangle1.getId();

			//Iteratively compare rectangles starting from next rectangle to rectangle1: 
			for (int j = startPoint; j < endPoint; j++) {
				Rectangle rectangle2 = rectangleList.get(j);
				
				//Create new intersection rectangle if overlap is possible: 
				if (overlapPresent(rectangle1, rectangle2)) {
					Rectangle newRectangle = findIntersection(rectangle1, rectangle2);
					
					//Only add rectangle to intersections if unique contributors exist for rectangle:  
					if (isUnique(newRectangle)) {
						newRectangle.addUniqueContributors(rectangle1.getContributors());
						newRectangle.addUniqueContributors(rectangle2.getContributors());
						allIntersections.add(newRectangle);
						this.lastIntersection++;
					}
						
				}
			}
		}
	}
	
	
	/**
	 * Iteratively determines if an intersection rectangle is unique among all intersection rectangles.  
	 * Comparison is done using parameters and contributing initial rectangles. 
	 * 
	 * @param newRectangle, a new intersection rectangle generated from 2 rectangles. 
	 * 
	 * @return true, the intersection rectangle is unique in the set. 
	 * @return false, there already exists an intersection in the set. 
	 * 
	 * */
	private boolean isUnique(Rectangle newRectangle) {
		for (Rectangle rectangle : allIntersections) {
			if (!rectangle.isIntersectionUnique(newRectangle))
				return false; 
		}
		return true; 
	}
	
	
	
}
