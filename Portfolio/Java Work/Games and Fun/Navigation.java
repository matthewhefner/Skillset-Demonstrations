import processing.core.*;
import processing.event.MouseEvent;

public class Navigation {
	PApplet parent;
	private PVector eye;
	public PVector getEye() {
		return eye;
	}

	public PVector getCenter() {
		return center;
	}

	public PVector getEyeSphere() {
		return eyeSphere;
	}

	private PVector center = new PVector(0, 0, 0);
	private PVector eyeSphere = new PVector(0,0,0);
	private int[] mouse = new int[2];
	private float speed;

	
	public Navigation(PApplet _parent, int w, int l) {
		parent = _parent;
		speed = 10;
		eye = new PVector((float) (w / 2.0),
				(float) (w / 2.0), 
				(float) ((w /2.0) / PApplet.tan((float) (PApplet.PI * 30.0 / 180.0))));
		float r = eye.mag();
		float theta = PApplet.acos(eye.y / r);
		float phi = PApplet.atan(eye.z / eye.x);
		eyeSphere.set(r, theta, phi);
		float x = eyeSphere.x * PApplet.sin(eyeSphere.y) * PApplet.cos(eyeSphere.z);
		float y = eyeSphere.x * PApplet.sin(eyeSphere.y) * PApplet.sin(eyeSphere.z);
		float z = eyeSphere.x * PApplet.cos(eyeSphere.y);
		eye.set(x, z, y);
	}
	
	public void d(){
		parent.camera(eye.x, 
			eye.y, 
			eye.z, 
			0, 0, 0, 0, -1, 0);
		parent.translate(center.x, center.y, center.z);
		parent.stroke(255);
		parent.strokeWeight(5);
		parent.line(0, 0, 0, -eye.x, 0, -eye.z);
	}
	
	public void movement() {
		if (parent.keyPressed && parent.key == PApplet.CODED) {
			PVector forward = new PVector(eye.x, 0, eye.z);
			forward.normalize();
			forward.mult(speed);
			if (parent.keyCode == PApplet.UP) {
				center.add(forward);
			} else if (parent.keyCode == PApplet.DOWN) {
				center.sub(forward);
			} else if (parent.keyCode == PApplet.LEFT) {
				//TODO
			} else if (parent.keyCode == PApplet.RIGHT) {
				//TODO
			}
		}
	}
	
	public void mP(){
		mouse[0] = parent.mouseX;
		mouse[1] = parent.mouseY;
	}
	
	public void mW(MouseEvent event) {
		eyeSphere.add(event.getCount() * 75, 
				0, 
				0);
		float x = eyeSphere.x * PApplet.sin(eyeSphere.y) * PApplet.cos(eyeSphere.z);
		float y = eyeSphere.x * PApplet.sin(eyeSphere.y) * PApplet.sin(eyeSphere.z);
		float z = eyeSphere.x * PApplet.cos(eyeSphere.y);
		eye.set(x, z, y);
	}
	
	public void mD(){
		eyeSphere.set(eyeSphere.x, 
				(eyeSphere.y + ((float) mouse[1] - parent.mouseY) / 50) % (2 * PApplet.PI), 
				(eyeSphere.z + ((float) mouse[0] - parent.mouseX) / 50) % (2 * PApplet.PI));
		float x = eyeSphere.x * PApplet.sin(eyeSphere.y) * PApplet.cos(eyeSphere.z);
		float y = eyeSphere.x * PApplet.sin(eyeSphere.y) * PApplet.sin(eyeSphere.z);
		float z = eyeSphere.x * PApplet.cos(eyeSphere.y);
		eye.set(x, z, y);
		
		mouse[0] = parent.mouseX;
		mouse[1] = parent.mouseY;
	}
}
