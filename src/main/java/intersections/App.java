package intersections;

//Java Language Imports and local imports: 
import java.io.FileReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.io.FileNotFoundException;


//External Library classes: 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;




/**
 * Program: Take command line input JSON file of rectangle coordinates and display all calculated intersections among rectangles.  
 * 
 * Resource Safe: JSON input checked at varying levels for proper file type, proper file formatting, and appropriate rectangle parameters. 
 * 
 * Error Handling: Address and catch various exceptions that is used to close program before crash occurs. (Error codes used) 
 * 
 * Input Safety: Handled at lower levels of program structure. 
 * 
 * Unit Tests: Provided to ensure code is working with various input files to test different use cases. 
 * 
 * Readability: Full English names used for coding style to enforce maximum readability.  (Apologies if Verbose) 
 * 
 * */


public class App {
	
	
	private static final String SCHEMAURI = "C:\\Users\\dzb10\\Programming\\Java\\Java_MasterClass\\intersectionsProject\\src\\main\\resources\\Schema\\"; 
	
    public static void main(String[] args) {
    	try {
    		//Resource Safety 1: Check for null Input, incorrect file name format: 
    		String jsonFilePath = args[0]; 
    		if (!isFilePathValid(jsonFilePath))
    			System.exit(404);

			//Resource Safety 2: Check for existence of JSON file, file is not null, correct JSON schema.  
    		
    		if (!isFileCorrect(jsonFilePath))
    			System.exit(400); 
    		
    		FileReader jsonReader = new FileReader(jsonFilePath);
			JSONTokener tokener = new JSONTokener(jsonReader); 
			JSONObject parser = new JSONObject(tokener); 
			
			
			//Resource Safety 3: Check for existence of  Negative X/Y + Delta Values, only 1 rectangle specified:   
			JSONArray rectangleCoordinates = parser.getJSONArray("rects");
			ArrayList<Rectangle> rectangles = createRectangles(rectangleCoordinates);
			if (rectangles == null || rectangles.size() == 1)
				System.exit(400);
			
			//Reduce Rectangle set size if necessary: 
			else if (rectangles.size() > 10)
				rectangles = reduceToTen(rectangles); 
			
			
			//Find intersections and print inputs + intersections:  
			IntersectionReporter reporter = new IntersectionReporter(rectangles);
			ArrayList<Rectangle> intersections = reporter.getIntersections();
			reporter.printInputs();
			reporter.printIntersections(intersections);
			
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			System.out.println("Program closing");
		}
    }    
    
    
    
    
    /**
     * Validates JSON file path to ensure input file exists and input to program is not null.  
     * 
     * @param jsonFilePath, The file path submitted for validation from the command line. 
     * 
     * @return true, The file path points to a valid JSON file. 
     * 
     * @return false, The file path is null or is a valid file path.
     * 
     * */
    public static boolean isFilePathValid(String jsonFilePath) {
		try {
			Paths.get(jsonFilePath);
		} catch(InvalidPathException exception) {
			System.out.println("File format is wrong.  Please only use alphanumeric characters");
			return false; 
		} catch (NullPointerException cxception) {
			System.out.println("No file path provided.  A file path needs to be supplied");
			return false;  
		} 
		
		return true; 
    }
     
    
    
    /**
     * Validates the existence of the specified JSON file, it's type, and whether or not the file conforms to the required JSON schema format. 
     * 
     * @param jsonFilePath, A string of the relative file path to the specified JSON file
     * 
     * @return true, The file is JSON and conforms to the required schema format. 
     * 
     * @return false, The file isn't JSON, doesn't exist or does not conform to the schema format. 
     */
    public static boolean isFileCorrect(String jsonFilePath) {
    	try {
        	FileReader testReader = new FileReader(jsonFilePath);
    		JSONTokener testTokener = new JSONTokener(testReader);
    		JSONObject JSON = new JSONObject(testTokener);
    		FileReader schemaReader = new FileReader(SCHEMAURI + "JSONSchema.json"); 
    		JSONObject schemaJSON = new JSONObject(new JSONTokener(schemaReader)); 
    		Schema JSONSchema = SchemaLoader.load(schemaJSON);
    		JSONSchema.validate(JSON);
    	} catch (FileNotFoundException exception) {
    		System.out.println("The file was not found.  Please ensure file exists");
    		return false;  
    	} catch (JSONException exception) {
    		System.out.println("File type is not invalid. Please supply a JSON file");
    		return false; 
    	} catch (ValidationException exception) {
    		System.out.println("JSON format does not match the required format.  Please supply a correctly formatted JSON file");
    		return false; 
    	}
    	
    	return true; 
    }
    
   
    /**
     * 
     * Converts a formatted JSON array into a set of Rectangle objects with coordinates (x, y) and dimensions deltaX and deltaY. 
     * 
     * @param coordinates, A JSON array of x, y, deltaX, and deltaY rectangle values extracted from the input JSON file.
     * 
     * @return rectangles, An array list of rectangle objects that can be processed by an instance of IntersectionReporter. 
     * 
     * 
     * */
    public static ArrayList<Rectangle> createRectangles(JSONArray coordinates) {
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>(); 
		for (int i = 0; i < coordinates.length(); i++) {
			JSONObject rectangleInfo = coordinates.getJSONObject(i); 
			int deltaX = rectangleInfo.getInt("delta_x"); 
			int deltaY = rectangleInfo.getInt("delta_y"); 
			int x = rectangleInfo.getInt("x"); 
			int y = rectangleInfo.getInt("y");
			if (!checkCoordinates(deltaX, deltaY, x, y)) 
				return null; 
			Rectangle rectangle = new Rectangle(deltaX, deltaY, x, y, i + 1); 
			rectangles.add(rectangle); 
		}
		return rectangles; 
	}
    
    
    /**
     * Checks rectangle parameters deltaX, deltaY, x, and y to see if they are negative or 0.  
     * @param deltaX, Rectangle width 
     * @param deltaY, Rectangle height
     * @param x, x-coordinate of bottom-right Rectangle point. 
     * @param y, y-coordinate of bottom-right Rectangle point.
     * 
     * @return true, all coordinates are greater than 0. 
     * @return false, at least one coordinate is negative or 0. 
     * */
    
    private static boolean checkCoordinates(int deltaX, int deltaY, int x, int y) {
    	try { 
    		if (deltaX <= 0 || deltaY <= 0 || x <= 0 || y <= 0)
    			throw new InvalidAlgorithmParameterException(); 
		} catch (InvalidAlgorithmParameterException exception) {
			System.out.println("Negative rectangle size or coordinates.  Please use different coordinates");
			return false; 
		}
    	return true; 
    }
    
    
    /**
     * Reduces size of rectangle set to a set of 10 rectangles. 
     * 
     * @param rectangles, the original array list of Rectangle instances. 
     * 
     * @return reducedRectangles, the reduced array list of Rectangle instances. 
     * 
     * */
    public static ArrayList<Rectangle> reduceToTen(ArrayList<Rectangle> rectangles) {
    	ArrayList<Rectangle> reducedRectangles = new ArrayList<Rectangle>(); 
    	
    	for (int i = 0; i < 9; i++) {
    		Rectangle rectangle = rectangles.get(i); 
    		reducedRectangles.add(rectangle); 
    	}
    	
    	return reducedRectangles; 
    }
    
}
