import processing.core.*;
import processing.event.MouseEvent;

import java.math.*;

public class RandomWalk extends PApplet{
	private PVector[] agents;
	private PVector eye;
	private PVector eyeSphere = new PVector(0,0,0);
	private float[] prob;
	private int[] mouse = new int[2];
	PShape cat;
	//private float[][] rotations;
	
	public void setup() {
		background(0);
		noStroke();
		cat = createShape(SPHERE, 10);
		prob = new float[2];
		prob[0] = (float) (1.0 / 3.0);
		prob[1] = (float) (2.0 / 3.0);
		agents = new PVector[1000];
		//rotations = new float[agents.length][3];
		eye = new PVector((float) (width / 2.0),
				(float) (height / 2.0), 
				(float) ((height/2.0) / tan((float) (PI*30.0 / 180.0))));
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new PVector(0, 0, 0);
		}
		float r = eye.mag();
		float theta = acos(eye.z / r);
		float phi = 0;
		eyeSphere.set(r, theta, phi);
		float x = eyeSphere.x * sin(eyeSphere.y) * cos(eyeSphere.z);
		float y = eyeSphere.x * sin(eyeSphere.y) * sin(eyeSphere.z);
		float z = eyeSphere.x * cos(eyeSphere.y);
		eye.set(x, y, z);
	}
	
	public void settings(){
		fullScreen(P3D);
	}
	
	public void draw(){
		//println(eye.mag());
		background(0);
		directionalLight(255, 100, 100, 1, (float) 0.5, 0);
		directionalLight(100, 255, 100, 0, 1, (float) 0.5);
		directionalLight(100, 100, 255, (float) 0.5, 0, 1);
		directionalLight(255, 100, 100, 1, (float) -0.5, 0);
		directionalLight(100, 255, 100, 0, 1, (float) -0.5);
		directionalLight(100, 100, 255, (float) -0.5, 0, 1);
		camera(eye.x, 
			eye.y, 
			eye.z, 
			0, 0, 0, 0, -1, 0);
		//translate(width / 2, height / 2, 0);
		translate(0, 0, 0);
		walk();
	}

	public void mousePressed(){
		mouse[0] = mouseX;
		mouse[1] = mouseY;
	}
	
	public void mouseWheel(MouseEvent event) {
		eyeSphere.add(event.getCount() * 75, 
				0, 
				0);
		float x = eyeSphere.x * sin(eyeSphere.y) * cos(eyeSphere.z);
		float y = eyeSphere.x * sin(eyeSphere.y) * sin(eyeSphere.z);
		float z = eyeSphere.x * cos(eyeSphere.y);
		eye.set(x, y, z);
	}
	
	public void mouseDragged(){
		eyeSphere.set(eyeSphere.x, 
				(eyeSphere.y + ((float) mouseX - mouse[0]) / 50) % (2 * PI), 
				0);
		float x = eyeSphere.x * sin(eyeSphere.y) * cos(eyeSphere.z);
		float y = eyeSphere.x * sin(eyeSphere.y) * sin(eyeSphere.z);
		float z = eyeSphere.x * cos(eyeSphere.y);
		eye.set(x, y, z);
		
		mouse[0] = mouseX;
		mouse[1] = mouseY;
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		RandomWalk sketch = new RandomWalk();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public void walk() {
		noStroke();
		fill(128);
		for (int j = 0; j < agents.length; j++) {
			PVector p = agents[j];
			float[] r = {random(1), random(1), random(1)};
			int[] step = {0, 0, 0};
			for (int i = 0; i < 3; i++) {
				if (r[i] < prob[0]) {
					step[i] += random(5);
				} else if (r[i] < prob[1]) {
					step[i] -= random(5);
				}
			}
			
			p.add(step[0], step[1], step[2]);
			pushMatrix();
			translate(p.x, p.y, p.z);

			shape(cat);
			popMatrix();
		}
	}
}
