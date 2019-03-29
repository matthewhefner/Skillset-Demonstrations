import processing.core.*;
import processing.event.MouseEvent;
import java.math.*;

public class Brownian extends PApplet{
	//Helium Agents
	private double[][] agentsHe;
	private double probHe;
	//Hydrogen Agents
	private double[][] agentsH;
	private double probH;
	//Nitrogen Agents
	private double[][] agentsN;
	private double probN;
	private double[] avgs = new double[3];
	// Diffusion Coefficients
	private double DHe = 6.28 * Math.pow(10, -5);
	private double DH = 4.5 * Math.pow(10, -5);
	private double DN = 1.88 * Math.pow(10, -5);
	private double dt = Math.pow(10, -5);
	private double dX = Math.pow(10, -4);
	private double[][] bounds = {{-1.5 * Math.pow(10, -2), 1.5 * Math.pow(10, -2)}, 
			{-1.5 * Math.pow(10, -2), 1.5 * Math.pow(10, -2)}};
	private float seconds;
	private PImage img;
	
	public void setup() {
		background(0);
		probHe = DHe * dt / Math.pow(dX, 2);
		agentsHe = new double[1000][2];
		probH = DH * dt / Math.pow(dX, 2);
		agentsH = new double[1000][2];
		probN = DN * dt / Math.pow(dX, 2);
		agentsN = new double[1000][2];
		frameRate(500);
		img = loadImage("b.png");
	}
	
	public void settings(){
		size(800, 800, P2D);
	}
	
	public void draw(){
		image(img, 0, 0);
		seconds = (float) (dt * frameCount);
		walk(agentsHe, probHe, 1);
		walk(agentsH, probH, 0);
		walk(agentsN, probN, 2);
		drawRadius(0);
		drawRadius(1);
		drawRadius(2);
		stroke(255);
		strokeWeight(3);
		textSize(32);
		fill(255);
		text("t = " + String.valueOf(seconds), width / 2 - 70, height - 5);
		line(width - 50, height-40, width - 150, height - 40);
		textSize(24);
		text("150 Micrometers", width - 200, height - 5);
		println("Time: " + seconds + " " + avgs[0]+ " " + avgs[1]+ " " + avgs[2]);
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		Brownian sketch = new Brownian();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public void walk(double[][] agents, double prob, int k) {
		strokeWeight(5);
		noFill();
		if (k == 0) {
			stroke(255, 0, 0);
		} else if (k == 1) {
			stroke(0, 0, 255);
		} else if (k == 2) {
			stroke(0, 255, 0);
		}
		avgs[k] = 0;
		for (int j = 0; j < agents.length; j++) {
			double[] p = agents[j];
			float r = random(1);
			float r2 = random(1);
			if (r2 < 0.5){
				if (r < prob) {
					agents[j][0] -= dX;
				} else if (r < 2 * prob){
					agents[j][0] += dX;
				}
			} else {
				if (r < prob) {
					agents[j][1] -= dX;
				} else if (r < 2 * prob){
					agents[j][1] += dX;
				}
			}
			avgs[k] += Math.sqrt(Math.pow(agents[j][0], 2) + Math.pow(agents[j][1], 2));
			//println(agents[j]);
			point(map((float) agents[j][0], (float) bounds[0][0], (float) bounds[0][1], 0, width),
					map((float) agents[j][1], (float) bounds[1][0], (float) bounds[1][1], 0, height));
		}
		avgs[k] /= agents.length;
	}
	
	public void drawRadius(int k) {
		if (k == 0) {
			stroke(100, 0, 0);
		} else if (k == 1) {
			stroke(0, 0, 100);
		} else if (k == 2) {
			stroke(0, 100, 0);
		}
		circle(map(0, (float) bounds[0][0], (float) bounds[0][1], 0, width), 
				map(0, (float) bounds[0][0], (float) bounds[0][1], 0, width), 
				map((float) avgs[k], 0, (float) bounds[0][1], 0, width));
	}
}
