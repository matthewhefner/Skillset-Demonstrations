import processing.core.*;

/**
 * This program demonstrates the diffusion of Helium, Hydrogen, and Nitrogen in
 * water by utilizing the Processing Java API for animation and returns
 * statistics regarding the experiment
 * 
 * @author Matthew Hefner
 * 
 */
public class Brownian extends PApplet {
	// Helium Agents
	private double[][] agentsHe;
	// Hydrogen Agents
	private double[][] agentsH;
	// Nitrogen Agents
	private double[][] agentsN;
	// Average distances of each type
	private double[] avgs = new double[3];
	// Diffusion Coefficients
	private double DHe = 6.28 * Math.pow(10, -5);
	private double DH = 4.5 * Math.pow(10, -5);
	private double DN = 1.88 * Math.pow(10, -5);
	// Time step
	private double dt = Math.pow(10, -5);
	// Distance step
	private double dX = Math.pow(10, -5);
	// bounds of the screen in model space
	private double[][] bounds = { { -2 * Math.pow(10, -3), 2 * Math.pow(10, -3) },
			{ -2 * Math.pow(10, -3), 2 * Math.pow(10, -3) } };
	//Model time
	private float seconds;

	/**
	 * Sets up the screen and animation and initializes particle agents.
	 */
	public void setup() {
		background(0);
		// Initialize agents to origin
		agentsHe = new double[1000][2];
		agentsH = new double[1000][2];
		agentsN = new double[1000][2];
		// sets framerate of animation
		frameRate(500);
	}

	/**
	 * Sets screen size and initializes program frame.
	 */
	public void settings() {
		// pixel size of screen
		size(800, 800, P2D);
	}

	/**
	 * The loop that drives a Papplet program under the Processing Java API. Each
	 * loop of Draw is a time step.
	 */
	public void draw() {
		// draw background water color
		background(64, 164, 223);
		// number of passed seconds
		seconds = (float) (dt * frameCount);
		// walk each agent
		walk(agentsHe, 1);
		walk(agentsH, 0);
		walk(agentsN, 2);
		// draw the circles of each average radius
		drawRadius(0);
		drawRadius(1);
		drawRadius(2);
		// time stamp and scale
		stroke(255);
		strokeWeight(3);
		textSize(32);
		fill(255);
		text("t = " + String.valueOf(seconds), width / 2 - 70, height - 5);
		line(width - 100, height - 40, width - 200, height - 40);
		textSize(24);
		text("2 Millimeters", width - 225, height - 5);
		//Saves frames
		if (seconds == 0.75
				|| seconds == 0.25
				|| seconds == 0.5
				|| seconds == 1) {
			saveFrame(frameCount + ".png");
		}
		// stops at 1 second
		if (seconds == 1) {
			stop();
		}
	}

	/**
	 * Stops the program, calculates the minimum, maximum, and average distances of
	 * each particle type, and prints this information to the console.
	 */
	public void stop() {
		println("Time: " + seconds);
		double[] NDists = new double[agentsN.length];
		double[] HDists = new double[agentsH.length];
		double[] HeDists = new double[agentsHe.length];
		//calculate distances from locations
		for (int i = 0; i < agentsN.length; i++) {
			NDists[i] = Math.sqrt(Math.pow(agentsN[i][0], 2) 
					+ Math.pow(agentsN[i][1], 2));
		}
		for (int i = 0; i < agentsH.length; i++) {
			HDists[i] = Math.sqrt(Math.pow(agentsH[i][0], 2) 
					+ Math.pow(agentsH[i][1], 2));
		}
		for (int i = 0; i < agentsHe.length; i++) {
			HeDists[i] = Math.sqrt(Math.pow(agentsHe[i][0], 2) 
					+ Math.pow(agentsHe[i][1], 2));
		}
		println("Hydrogen:\n\tMinimum Distance: " + getMinValue(HDists));
		println("\tAverage Distance: " + getAvgValue(HDists));
		println("\tMaximum Distance: " + getMaxValue(HDists));
		println("Helium:\n\tMinimum Distance: " + getMinValue(HeDists));
		println("\tAverage Distance: " + getAvgValue(HeDists));
		println("\tMaximum Distance: " + getMaxValue(HeDists));
		println("Nitrogen:\n\tMinimum Distance: " + getMinValue(NDists));
		println("\tAverage Distance: " + getAvgValue(NDists));
		println("\tMaximum Distance: " + getMaxValue(NDists));
	}
	
	/**
	 * Calculates the distance between (0,0) and a point (x,y)
	 * @param x		The x coordinate
	 * @param y		The y coordinate
	 * @return		The distance to the origin
	 */
	public double getDist(double x, double y) {
		return Math.sqrt(Math.pow(x, 2) 
				+ Math.pow(y, 2));
	}

	/**
	 * Calculates the maximum value of an array of floating point numbers.
	 * @param numbers	The array of which the maximum is desired.
	 * @return			The maximum of that array.
	 */
	public double getMaxValue(double[] numbers) {
		double maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	/**
	 * Calculates the minimum value of an array of floating point numbers.
	 * @param numbers	The array of which the minimum is desired.
	 * @return			The minimum of that array.
	 */
	public double getMinValue(double[] numbers) {
		double minValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < minValue) {
				minValue = numbers[i];
			}
		}
		return minValue;
	}
	
	/**
	 * Calculates the average value of an array of floating point numbers.
	 * @param numbers	The array of which the average is desired.
	 * @return			The average of that array.
	 */
	public double getAvgValue(double[] numbers) {
		double avgValue = numbers[0];
		for (int i = 0; i < numbers.length; i++) {
			avgValue += numbers[i];
		}
		return avgValue / (double) numbers.length;
	}

	/**
	 * The required main method to drive any Java program. This declares and
	 * initializes the processing sketch using the processing API.
	 * 
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		// Making use of the processing library for Java
		String[] processingArgs = { "MySketch" };
		Brownian sketch = new Brownian();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	/**
	 * The probability distribution function given by Einstein's relationship.
	 * @param x		the distance from the origin
	 * @param D		the diffusion coefficient
	 * @return		the probability of being at that location
	 */
	public double einsteinPDF(double x, double D) {
		return Math.exp(-Math.pow(x, 2) / ((double) (4 * D * seconds)) 
				/ (Math.sqrt(4 * Math.PI * D) * Math.sqrt((double) seconds)));
	}

	/**
	 * This method takes an array of agent particle locations and steps them exactly
	 * one time step. It also calculates the average distance of a given particle
	 * type's agents from the origin.
	 * 
	 * @param agents The array of particle agents locations
	 * @param prob   The probability of that particle type stepping
	 * @param k      The particle type
	 */
	public void walk(double[][] agents, int k) {
		// screen size of agent
		strokeWeight(5);
		noFill();
		double D = 0;
		// Color of each agent
		if (k == 0) {
			stroke(255, 0, 0);
			D = DH;
		} else if (k == 1) {
			stroke(0, 0, 255);
			D = DHe;
		} else if (k == 2) {
			stroke(0, 255, 0);
			D = DN;
		}
		// Initiate average to zero for each time step
		avgs[k] = 0;
		for (int j = 0; j < agents.length; j++) {
			// for each agent of type k
			// r is the probability of stepping at all
			float r = random(1);
			// r2 is the probability of stepping on the y or x axis
			float r2 = random(1);
			if (r2 < 0.25) {
				if (r < einsteinPDF(getDist(agents[j][0] - dX, agents[j][1]), D)) {
					// step left
					agents[j][0] -= dX;
				}
			} else if (r2 < 0.5) {
				if (r < einsteinPDF(getDist(agents[j][0] + dX, agents[j][1]), D)) {
					// step left
					agents[j][0] += dX;
				}
			} else if (r2 < 0.75) {
				if (r < einsteinPDF(getDist(agents[j][0], agents[j][1] - dX), D)) {
					// step up
					agents[j][1] -= dX;
				}
			} else {
				if (r < einsteinPDF(getDist(agents[j][0], agents[j][1] + dX), D)) {
					// step down
					agents[j][1] += dX;
				}
			}
			// calculate average distance
			avgs[k] += Math.sqrt(Math.pow(agents[j][0], 2) + Math.pow(agents[j][1], 2));
			// draw particle
			point(map((float) agents[j][0], (float) bounds[0][0], (float) bounds[0][1], 0, width),
					map((float) agents[j][1], (float) bounds[1][0], (float) bounds[1][1], 0, height));
		}
		// finish average distance calculation
		avgs[k] /= agents.length;
	}

	/**
	 * Draws a circle with a radius of the particle type's average distance from the
	 * origin.
	 * 
	 * @param k The particle type.
	 */
	public void drawRadius(int k) {
		// determine particle color
		if (k == 0) {
			stroke(100, 0, 0);
		} else if (k == 1) {
			stroke(0, 0, 100);
		} else if (k == 2) {
			stroke(0, 100, 0);
		}
		// draw circle
		circle(map(0, (float) bounds[0][0], (float) bounds[0][1], 0, width),
				map(0, (float) bounds[0][0], (float) bounds[0][1], 0, width),
				map((float) avgs[k], 0, (float) bounds[0][1], 0, width));
	}
}
