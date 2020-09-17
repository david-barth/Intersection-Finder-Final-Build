package intersections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RectangleTest {

	
	
	//Tests for Rectangle Creation:
	@Test 
	public void testForSingleNegativeParam() throws Exception {		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Rectangle negativeRectangle = new Rectangle(200, 200, -10, 100);
		});
	}
	
	@Test
	public void testForMultipleNegativeParams() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Rectangle negativeRectangle = new Rectangle(-200, -200, -10, 100);
		});
	}
	
	@Test
	public void testForAllNegativeParams() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Rectangle negativeRectangle = new Rectangle(-200, -200, -10, -100);
		});
	}
	
	@Test
	public void testForSingleZeroParam() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Rectangle negativeRectangle = new Rectangle(0, 200, 10, 100);
		});
	}
	
	@Test
	public void testForMultipleZeroParams() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Rectangle negativeRectangle = new Rectangle(0, 0, 0, 100);
		});
	}
	
	@Test
	public void testForAllZeroParams() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Rectangle negativeRectangle = new Rectangle(0, 0, 0, 0);
		});
	}
	
	
	//Tests for isIntersectionUnique():
	@Test
	public void testForUniqueBasedOnCoordinates() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		Rectangle rectangle2 = new Rectangle(30, 30, 50, 100);

		boolean isIntersectionUnique = rectangle1.isIntersectionUnique(rectangle2); 
		assertTrue(isIntersectionUnique);
	}
	
	@Test
	public void testForUniqueBasedOnContributors() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		ArrayList<Integer> contributors1 = new ArrayList<Integer>(); 
		contributors1.add(1); 
		contributors1.add(2); 
		rectangle1.setContributors(contributors1);
		
		Rectangle rectangle2 = new Rectangle(20, 30, 40, 100);
		ArrayList<Integer> contributors2 = new ArrayList<Integer>(); 
		contributors2.add(1); 
		contributors2.add(3);
		rectangle2.setContributors(contributors2);
		
		boolean isIntersectionUnique = rectangle1.isIntersectionUnique(rectangle2); 
		assertTrue(isIntersectionUnique);
		
	}
	
	@Test 
	public void testForNonUniqueBasedOnCoordinates() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		Rectangle rectangle2 = new Rectangle(20, 30, 40, 100);

		boolean isIntersectionUnique = rectangle1.isIntersectionUnique(rectangle2); 
		assertFalse(isIntersectionUnique);
	}
	
	@Test 
	public void testForNonUniqueBasedOnContributors() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		ArrayList<Integer> contributors1 = new ArrayList<Integer>(); 
		contributors1.add(1); 
		contributors1.add(2); 
		rectangle1.setContributors(contributors1);
		
		Rectangle rectangle2 = new Rectangle(20, 30, 40, 100);
		ArrayList<Integer> contributors2 = new ArrayList<Integer>(); 
		contributors2.add(1); 
		contributors2.add(2);
		rectangle2.setContributors(contributors2);
		
		boolean isIntersectionUnique = rectangle1.isIntersectionUnique(rectangle2); 
		assertFalse(isIntersectionUnique);
	}
	
	@Test
	public void testForNullRectangleInput() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		Rectangle nullRectangle = null;

		Assertions.assertThrows(NullPointerException.class, () -> {
			rectangle1.isIntersectionUnique(nullRectangle); 
		});
	}
	
	
	//Tests for addUniqueContributors(): 
	@Test
	public void testForAdditionToEmptyList() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		ArrayList<Integer> contributors1 = new ArrayList<Integer>();
		rectangle1.setContributors(contributors1);
		
		ArrayList<Integer> newContributors = new ArrayList<Integer>();
		newContributors.add(1);
		newContributors.add(2); 
		rectangle1.addUniqueContributors(newContributors);
		
		
		for (int i = 0; i < newContributors.size(); i++) {
			int testContributor = rectangle1.getContributors().get(i); 
			int expectedContributor = newContributors.get(i); 
			
			assertTrue(expectedContributor == testContributor);  
		}
	}
	
	@Test
	public void testForAdditionToOccupiedList() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		ArrayList<Integer> contributors1 = new ArrayList<Integer>();
		contributors1.add(1);
		contributors1.add(2); 
		rectangle1.setContributors(contributors1);
		ArrayList<Integer> newContributors = new ArrayList<Integer>();
		newContributors.add(3);
		newContributors.add(4); 
		rectangle1.addUniqueContributors(newContributors);
		
		ArrayList<Integer> expectedList = new ArrayList<Integer>(); 
		expectedList.add(1); 
		expectedList.add(2); 
		expectedList.add(3); 
		expectedList.add(4); 
		
		for (int i = 1; i < expectedList.size(); i++) {
			int testContributor = rectangle1.getContributors().get(i); 
			int expectedContributor = expectedList.get(i); 
			
			assertTrue(expectedContributor == testContributor);  
		}
	}
	

	
	//Tests for addInitialContributors(): 
	@Test 
	public void testForAdditionOfContributors() throws Exception {
		Rectangle rectangle1 = new Rectangle(20, 30, 40, 100);
		
		rectangle1.addInitialContributors(1, 2);
		
		for (int i = 1; i <= 2; i++) {
			assertTrue(i == rectangle1.getContributors().get(i - 1));
		}
	}
	
}
