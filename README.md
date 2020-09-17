## Introduction
This project implements a recursive algorithm to calculate and report on the set of all intersections that exist in a set of input rectangles.  The program is written in Java (Java 11) and uses the [json.org](https://www.json.org/json-en.html) library to parse rectangles supplied via JSON file from the commandline.  Validation of the JSON file schema is provided via the library [json Schema](https://json-schema.org/) in order to ensure input file compliance with the program.  Output is reported through command line and project management is accomplished by [Apache Maven](https://maven.apache.org/).


## System Requirements
This program was tested on Windows 10 with Eclipse IDE (2020-03) and using [JUnit5](https://junit.org/junit5/).


## Download and Project Build

Via commandline: 

```
git clone https://github.com/david-barth/Intersection-Finder-Final-Build 
cd Intersection-Finder-Final-Build
```

To install and build the project ensure that Apache Maven is installed on the system.  In the root directory use the command: 

```
mvn clean install
```
 
This executes the build process and displays the unit tests that run.  Please not that the following messages will be output to the screen as part of the unit test suites: 

```
No file path provided.  A file path needs to be supplied
JSON format does not match the required format.  Please supply a correctly formatted JSON file
File type is not invalid. Please supply a JSON file
Negative rectangle size or coordinates.  Please use different coordinates
File format is wrong.  Please only use alphanumeric characters
The file was not found.  Please ensure file exists
JSON format does not match the required format.  Please supply a correctly formatted JSON file
Negative rectangle size or coordinates.  Please use different coordinates
```

These messages ensure that the error handling capabilities of the client App is functional and working.  The program is ready to use.  



## Directions for Use
By default, the program can be started using the following command on command line: 

```
mvn exec:java
```

This executes the program with a default file to show that the program is functioning normally.  The default output looks like this: 
 
 ```
 Inputs:
1: Rectangle at (100,100), delta_x=250,  delta_y=80
2: Rectangle at (120,200), delta_x=250,  delta_y=150
3: Rectangle at (140,160), delta_x=250,  delta_y=100
4: Rectangle at (160,140), delta_x=350,  delta_y=190
Intersections:
1: Between rectangle 1, and 3 at (140,160)), delta_x= 210,  delta_y= 20.
2: Between rectangle 1, and 4 at (160,140)), delta_x= 190,  delta_y= 40.
3: Between rectangle 2, and 3 at (140,200)), delta_x= 230,  delta_y= 60.
4: Between rectangle 2, and 4 at (160,200)), delta_x= 210,  delta_y= 130.
5: Between rectangle 3, and 4 at (160,160)), delta_x= 230,  delta_y= 100.
6: Between rectangle 1, 3, and 4 at (160,160)), delta_x= 190,  delta_y= 20.
7: Between rectangle 2, 3, and 4 at (160,200)), delta_x= 210,  delta_y= 60.
Program closing
 ```

This output is an example of what the program will output using JSON file that is properly validated and with rectangle coordinates that are valid.  To switch the input file to another file, in the command line within the root directory: 

```
 mvn exec:java -DargumentInput=inputFileName
```

JSON input files of the proper structures are placed in the following directory from the project root: 

```
cd .\src\main\resources\InputFiles
```



## Testing 
This program build contains 43 unit tests for various public methods of the source classes. The tests can be run with the command: 

```
mvn test
```

Or collectively with the build process, using: 

```
mvn install
```





##  Program
The program consists of 3 source classes: 

1. App.java
2. IntersectionReporter.java
3. Rectangle.java

#### App.java
This class holds the client level code for all command line arguments injected into the program.  Resource safety is done in 3 level: 

1. Ensure the input file is not null and exists on a valid path.  
2. Validate the type of file as JSON and validate the JSON file structure against the required Schema.  
3. Validate the rectangle coordinates are not 0 or negative. 

Rectangles are then created and given to the **IntersectionReporter**. 

#### IntersectionReporter.java
This class uses following algorithm to find intersections: 

1. Find all intersections of initial input triangles and generate rectangles for each intersection. 
2. Keep track of the initial contributing input rectangles for each intersection rectangles (called contributors). 
3. Recursively generate a new set of intersection rectangles from the first set of intersections. 
4. Add all unqiue contributors to each newly generated intersection (e.g. (1, 2) + (1, 3) ---> Overall Contributors to intersection => (1, 2, 3). 
5. Continue recursively generating intersections until the last set of intersections is found. 

Each successive recursion will produce intersection rectangles of greater numbers of contributors, giving "generations" of intersections.  Contributors are used to keep track of which initial rectangle contributes to the intersection. 


#### Rectangle.java
This class models and implements the rectangle coordinates considered by this program.  The class also contains methods for adding contributors uniquely to the contributor list of intersection Rectangles or to add initial contributors to an intersection rectangle generated from two initial rectangles.  There are 2 flavors of Rectangle considered by this program: 

1. Initial Rectangle:  This is marked by an "id" field. 
2. Intersection Rectangle: This is marked by a list called "contributors" which contains the id of contributing initial rectangles. 

Sub-classing can also be used, but this program makes this distinction via constructor choice. 

