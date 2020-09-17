package intersections;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntersectionReporterTest {

	
	//Tests for Instance Creation: 
	@Test
	public void testForNullList() throws Exception {
		Assertions.assertThrows(NullPointerException.class, () -> {
			IntersectionReporter nullListReporter = new IntersectionReporter(null); 
		});
	}
	
	@Test
	public void testForOneNullRectangle() throws Exception {		
		ArrayList<Rectangle> nullRectangleList = new ArrayList<Rectangle>(); 
		nullRectangleList.add(null); 
		nullRectangleList.add(new Rectangle(350, 190, 160, 140)); 
		nullRectangleList.add(new Rectangle(350, 190, 160, 140)); 

		Assertions.assertThrows(NullPointerException.class, () -> {
			IntersectionReporter nullListReporter = new IntersectionReporter(null); 
		});
	}
	
	@Test
	public void testForMultipleNullRectangles() throws Exception {
		ArrayList<Rectangle> nullRectangleList = new ArrayList<Rectangle>(); 
		nullRectangleList.add(null); 
		nullRectangleList.add(null); 
		nullRectangleList.add(null); 

		Assertions.assertThrows(NullPointerException.class, () -> {
			IntersectionReporter nullListReporter = new IntersectionReporter(null); 
		});
	}
	

	
	//Tests for getIntersections(): 
	public void testAlgorithmForCorrectCoordinates() throws Exception {
		ArrayList<Rectangle> testRectangles = new ArrayList<Rectangle>(); 
		testRectangles.add(new Rectangle(250, 80, 100, 100)); 
		testRectangles.add(new Rectangle(250, 150, 120, 200)); 
		testRectangles.add(new Rectangle(250, 100, 140, 160)); 
		testRectangles.add(new Rectangle(350, 190, 160, 140)); 
		IntersectionReporter testReporter = new IntersectionReporter(testRectangles); 
		
		int[] resultXValues = {140, 160, 140, 160, 160, 160, 160};
		int[] resultYValues = {160, 140, 200, 200, 160, 160, 200};
		int[] resultDeltaXValues = {210, 190, 230, 210, 230, 190, 210};
		int[] resultDeltaYValues = {20, 40, 60, 130, 100, 20, 60};
		ArrayList<Rectangle> testResults = testReporter.getIntersections();
		
		for (int i = 0; i < resultXValues.length; i++) {
			int expectedX = resultXValues[i]; 
			int expectedY = resultYValues[i]; 
			int expectedDeltaX = resultDeltaXValues[i]; 
			int expectedDeltaY = resultDeltaYValues[i]; 
			
			assertTrue(expectedX == testResults.get(i).getX());
			assertTrue(expectedY == testResults.get(i).getY());
			assertTrue(expectedDeltaX == testResults.get(i).getDeltaX());
			assertTrue(expectedDeltaY == testResults.get(i).getDeltaY());
		}
	}
	
	
	public void testAlgorithmForCorrectContributors() throws Exception {
		ArrayList<Rectangle> testRectangles = new ArrayList<Rectangle>(); 
		testRectangles.add(new Rectangle(250, 80, 100, 100)); 
		testRectangles.add(new Rectangle(250, 150, 120, 200)); 
		testRectangles.add(new Rectangle(250, 100, 140, 160)); 
		testRectangles.add(new Rectangle(350, 190, 160, 140)); 
		IntersectionReporter testReporter = new IntersectionReporter(testRectangles); 
		
		int[][] expectedContributors = {{1, 3}, {1, 4}, {2, 3}, {2, 4}, {3, 4}, {1, 3, 4}, {2, 3, 4}}; 
		ArrayList<Rectangle> testResults = testReporter.getIntersections();
		
		for (int i = 0; i < expectedContributors.length; i++) {
			ArrayList<Integer> testContributors = testResults.get(i).getContributors();
			
			for (int j = 0; j < expectedContributors[i].length; j++) {
				assertTrue(testContributors.get(j) == expectedContributors[i][j]);
			}
		}
	}
	
	
	
	
	
	//Tests for findIntersections(): 
	@Test 
	public void testForNormalIntersectionCreation() throws Exception {
		Rectangle rectangle1 = new Rectangle(250, 80, 100, 100);
		Rectangle rectangle2 = new Rectangle(250, 100, 140, 160); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		Rectangle expectedRectangle = new Rectangle(210, 20, 140, 160);
		IntersectionReporter testReporter = new IntersectionReporter(testList);
		
		Rectangle testRectangle = testReporter.findIntersection(rectangle1, rectangle2); 
		
		assertTrue(testRectangle.getX() == expectedRectangle.getX()); 
		assertTrue(testRectangle.getY() == expectedRectangle.getY()); 
		assertTrue(testRectangle.getDeltaX() == expectedRectangle.getDeltaX()); 
		assertTrue(testRectangle.getDeltaY() == expectedRectangle.getDeltaY()); 
	}
	
	@Test
	public void testForOneNullRectangleInput() throws Exception {
		Rectangle rectangle1 = new Rectangle(250, 80, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testReporter.findIntersection(rectangle1, null); 
		});
	}
	
	@Test
	public void testForTwoNullRectangleInputs() throws Exception {
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testReporter.findIntersection(null, null); 
		});
	}
	
	//Negative Overlap Case: Specific case where deltaX or deltaY is a negative value, representing erroneous overlap: 
	@Test 
	public void testForNegativeOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(250, 80, 100, 100);
		Rectangle rectangle2 = new Rectangle(250, 150, 120, 200); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testReporter.findIntersection(rectangle1, rectangle2); 
		});
	}
	
	
	
	
	//Tests for overlapPresent():
	
	//Rectangle 1 is on top of rectangle 2: 
	//Overlap Case: 
	@Test
	public void testForUpwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 100, 100);
		Rectangle rectangle2 = new Rectangle(100, 200, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertTrue(isOverlapping);
	}

	//No Overlap Case: 
	@Test
	public void testForNoUpwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 100, 300);
		Rectangle rectangle2 = new Rectangle(100, 100, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertFalse(isOverlapping);
	}
	
	
	//Rectangle 1 is on bottom of rectangle 2: 	
	//Overlap Case: 
	@Test
	public void testForDownwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 200, 100, 100);
		Rectangle rectangle2 = new Rectangle(100, 100, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertTrue(isOverlapping);
	}

	//No Overlap Case: 
	@Test
	public void testForNoDownwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 100, 100);
		Rectangle rectangle2 = new Rectangle(100, 100, 100, 300); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertFalse(isOverlapping);
	}
	
	
	//Rectangle 1 is on left side of Rectangle 2: 
	//Overlap Case: 
	@Test
	public void testForLeftwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(200, 100, 100, 100);
		Rectangle rectangle2 = new Rectangle(100, 100, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertTrue(isOverlapping);
	}

	//No Overlap Case: 
	@Test
	public void testForNoLeftwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 100, 100);
		Rectangle rectangle2 = new Rectangle(100, 100, 300, 300); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertFalse(isOverlapping);
	}
	
	
	//Rectangle 1 is on right side of Rectangle 2: 
	//Overlap Case: 
	@Test
	public void testForRightwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 100, 100);
		Rectangle rectangle2 = new Rectangle(200, 100, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertTrue(isOverlapping);
	}

	//No Overlap Case: 
	@Test
	public void testForNoRightwardOverlap() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 300, 100);
		Rectangle rectangle2 = new Rectangle(100, 100, 100, 100); 
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		boolean isOverlapping = testReporter.overlapPresent(rectangle1, rectangle2); 
		
		assertFalse(isOverlapping);
	}
	
	@Test
	public void testForOneNullInput() throws Exception {
		Rectangle rectangle1 = new Rectangle(100, 100, 300, 100);
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testReporter.overlapPresent(rectangle1, null); 
		});
	}
	

	public void testForTwoNullInputs() throws Exception {
		ArrayList<Rectangle> testList = new ArrayList<Rectangle>();
		IntersectionReporter testReporter = new IntersectionReporter(testList);
	
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testReporter.overlapPresent(null, null); 
		});
	}
	
}
