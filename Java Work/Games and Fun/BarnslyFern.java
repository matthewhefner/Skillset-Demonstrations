import processing.core.*;

public class BarnslyFern extends PApplet{
	private final int maxIteration = 63;
	
	/*
    Barnsley Fern for Processing 3.4
	 */

	// declaring variables x and y
	public double x, y;
	
	// creating canvas
	public void setup() {
	  background(255);
	  frameRate(500);
	}
	
	public void settings() {
		  size(600, 600);
	}

	/* setting stroke,  mapping canvas and then
	   plotting the points */
	public void drawPoint() {
	  stroke(34, 139, 34);
	  strokeWeight(1);
	  float px = map((float)x,(float) -2.1820,(float) 2.6558, 0, width);
	  float py = map((float)y, 0,(float) 9.9983, height, 0);
	  point(px, py);
	}
	
	/* algorithm for calculating value of (n+1)th
	   term of x and y based on the transformation
	   matrices */
	public void nextPoint() {
	  double nextX, nextY;
	  double r = random(1);
	  if (r < 0.01) {
	    nextX =  0;
	    nextY =  0.16 * y;
	  } else if (r < 0.86) {
	    nextX =  0.85 * x + 0.04 * y;
	    nextY = -0.04 * x + 0.85 * y + 1.6;
	  } else if (r < 0.93) {
	    nextX =  0.20 * x - 0.26 * y;
	    nextY =  0.23 * x + 0.22 * y + 1.6;
	  } else {
	    nextX = -0.15 * x + 0.28 * y;
	    nextY =  0.26 * x + 0.24 * y + 0.44;
	  }
	  x = nextX;
	  y = nextY;
	}
	
	/* iterate the plotting and calculation
	   functions over a loop */
	public void draw() {
	  for (int i = 0; i < 100; i++) {
	    drawPoint();
	    nextPoint();
	  }
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		BarnslyFern sketch = new BarnslyFern();
		PApplet.runSketch(processingArgs, sketch);
	}
	

}
