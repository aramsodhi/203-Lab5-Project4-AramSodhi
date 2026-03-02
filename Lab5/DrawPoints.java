import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class DrawPoints extends PApplet {

	String s;

	public void settings() {
    	size(500, 500);
		s = Paths.get("").toAbsolutePath().getFileName().toString();
	}
  
	public void setup() {
    	background(128);
    	noLoop();
  	}

  	public void draw() {
		// You should create a stream of Points using either a list or stream builder.
		Stream.Builder<Point> builder = Stream.builder();

		try (Scanner scanner = new Scanner(new File("positions.txt"))) {
			while (scanner.hasNextLine()) {
				// Each line contains comma and space separated x, y, and z values
				// You will add a Point to the stream builder/list for each line
				String line = scanner.nextLine();

				String[] split_line = line.split(", ");

				double x = Double.parseDouble(split_line[0]);
				double y = Double.parseDouble(split_line[1]);
				double z = Double.parseDouble(split_line[2]);

				Point point = new Point(x, y, z);

				builder.add(point);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Initialize the stream
		Stream<Point> pointStream = builder.build();

		// Process the stream
		List<Point> pointList = pointStream
				.filter(point -> point.z <= 2.0)
				.map(point -> new Point(point.x *= 0.5, point.y *= 0.5, point.z *= 0.5))
				.map(point -> new Point(point.x - 150.0, point.y - 562.0, point.z))
				.map(point -> new Point(point.x, -point.y, point.z))
				.toList();

		// Display the stream
		for (Point p : pointList){
			ellipse((int) p.x, (int) p.y, 1, 1);
			fill(126, 126, 126);
			text(s, 0, 500);
		}
  	}

  	public static void main(String[] args) {
      PApplet.main("DrawPoints");
   }
}
