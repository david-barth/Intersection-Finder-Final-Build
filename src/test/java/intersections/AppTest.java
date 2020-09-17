package intersections;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppTest {
	
	
	private final static String RESOURCEURI = "C:\\Users\\dzb10\\Programming\\Java\\Java_MasterClass\\intersectionsProject\\src\\main\\resources\\"; 
	
	//Tests for isFilePathValid(): 
	@Test
	public void testValidationForValidPath() throws Exception {
		boolean isPathValid = App.isFilePathValid(RESOURCEURI + "\\NormalCases\\testRectanglesOriginals.json");  
		
		assertTrue(isPathValid);
	}
	
	
	@Test
	public void testValidationForNull() throws Exception {
		boolean isPathValid = App.isFilePathValid(null); 
		
		assertFalse(isPathValid);
	}
	
	@Test
	public void testValidationForBadPathFormat() throws Exception {
		boolean isPathValid = App.isFilePathValid(RESOURCEURI + "@.d..dw;?.json"); 
		
		assertFalse(isPathValid);
	}
	
	
	
	//Tests for isFileCorrect(): 
	@Test
	public void testForNoFile() throws Exception {
		boolean doesFileExist = App.isFileCorrect(RESOURCEURI + "NonExistingFile.json"); 
		
		assertFalse(doesFileExist);
	}
	
	@Test 
	public void testForJSONType() throws Exception {
		boolean isFileJSON = App.isFileCorrect(RESOURCEURI + "\\NormalCases\\testRectanglesOriginal.json"); 
	
		assertTrue(isFileJSON);
	}
	
	@Test 
	public void testForWrongType() throws Exception {
		boolean isFileJSON = App.isFileCorrect(RESOURCEURI + "\\ErrorFiles\\wrongFileType.txt"); 
		
		assertFalse(isFileJSON);
	}
	
	@Test 
	public void testForGoodFormat() throws Exception {
		boolean isFormatCorrect = App.isFileCorrect(RESOURCEURI + "\\NormalCases\\testRectanglesDuplicates.json"); 
		
		assertTrue(isFormatCorrect);
	}
	
	@Test 
	public void testForBadFormat() throws Exception {
		boolean isFormatGood = App.isFileCorrect(RESOURCEURI + "\\ErrorFiles\\testBadFormat.json"); 
		
		assertFalse(isFormatGood);
	}
	
	@Test
	public void testForEmptyJSON() throws Exception {
		boolean isThereJSON = App.isFileCorrect(RESOURCEURI + "\\ErrorFiles\\EmptyJSON.json"); 
	
		assertFalse(isThereJSON);
	}
	
	
	//Tests for createRectangles(): 
	@Test
	public void testForCorrectRectangleSet() throws Exception {
		FileReader jsonReader = new FileReader(RESOURCEURI + "\\NormalCases\\testRectanglesOriginal.json");
		JSONTokener tokener = new JSONTokener(jsonReader); 
		JSONObject parser = new JSONObject(tokener); 
		JSONArray rectangleCoordinates = parser.getJSONArray("rects");
		ArrayList<Rectangle> testRectangles = App.createRectangles(rectangleCoordinates);
		
		ArrayList<Rectangle> expectedRectangles = new ArrayList<Rectangle>(); 
		expectedRectangles.add(new Rectangle(250, 80, 100, 100)); 
		expectedRectangles.add(new Rectangle(250, 150, 120, 200)); 
		expectedRectangles.add(new Rectangle(250, 100, 140, 160)); 
		expectedRectangles.add(new Rectangle(350, 190, 160, 140)); 

		for (int i = 0; i < expectedRectangles.size(); i++) {
			Rectangle expectedRectangle = expectedRectangles.get(i); 
			Rectangle testRectangle  = testRectangles.get(i); 
			assertEquals(expectedRectangle.getDeltaX(), testRectangle.getDeltaX());
			assertEquals(expectedRectangle.getDeltaY(), testRectangle.getDeltaY());
			assertEquals(expectedRectangle.getX(), testRectangle.getX());
			assertEquals(expectedRectangle.getY(), testRectangle.getY());
		}
	}
	
	@Test
	public void testForNegativeRectangleParam() throws Exception {
		FileReader jsonReader = new FileReader(RESOURCEURI + "\\ErrorFiles\\negativeRectangles.json");
		JSONTokener tokener = new JSONTokener(jsonReader); 
		JSONObject parser = new JSONObject(tokener); 
		JSONArray rectangleCoordinates = parser.getJSONArray("rects");
		ArrayList<Rectangle> testRectangles = App.createRectangles(rectangleCoordinates);
	
		assertNull(testRectangles);
	}
	
	@Test
	public void testForZeroParam() throws Exception {
		FileReader jsonReader = new FileReader(RESOURCEURI + "\\ErrorFiles\\zeroParameterRectangles.json");
		JSONTokener tokener = new JSONTokener(jsonReader); 
		JSONObject parser = new JSONObject(tokener); 
		JSONArray rectangleCoordinates = parser.getJSONArray("rects");
		ArrayList<Rectangle> testRectangles = App.createRectangles(rectangleCoordinates);
	
		assertNull(testRectangles);
	}
	
	
	//Tests for reduceToTen(): 
	@Test
	public void testForReductionToTen() throws Exception {
		FileReader jsonReader = new FileReader(RESOURCEURI + "\\ErrorFiles\\moreThanTenRectangles.json");
		JSONTokener tokener = new JSONTokener(jsonReader); 
		JSONObject parser = new JSONObject(tokener); 
		JSONArray rectangleCoordinates = parser.getJSONArray("rects");
		ArrayList<Rectangle> testRectangles = App.createRectangles(rectangleCoordinates);
		
		int beforeReductionSize = testRectangles.size(); 
		int afterReductionSize = App.reduceToTen(testRectangles).size(); 
		
		assertTrue(beforeReductionSize > 10);
		assertTrue(afterReductionSize <= 10); 
	}
	
}