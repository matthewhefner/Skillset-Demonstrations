import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class BurningShip extends PApplet{
	private final int scale = 25;
	private int maxIteration = 50;
	private float[] center;
	private int scroll;
	
	public void setup() {
		//noLoop();
		colorMode(HSB);
		scroll = 1;
		center = new float[2];
		center[1] = 500;
	}
	
	public void settings(){
		//fullScreen();
		size((int) (300 * 3.5), (int) 300 * 2);
		//size(4096, 2160);
	}
	
	public void draw(){
		loadPixels();
		produceFractal();

	}
	
	public void mousePressed() {
		//save("burningShip/" + frameCount + ".png");
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		BurningShip sketch = new BurningShip();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public CN f(CN z) {
		return CN.subtract(CN.pow(z, 3), new CN(1, 0));
	}
	
	public CN fp(CN z) {
		return CN.multiply(CN.pow(z, 2), new CN(3, 0));
	}
	
    public void keyPressed()
    {
    		switch(key) {
    		case 'w':
    			center[1] -=  scale * pow(2, - scroll + 1);
    			break;
    		case 's':
    			center[1] +=  scale *pow(2, - scroll + 1);
    			break;
    			
    		case 'a':
    			center[0] -= scale * pow(2, - scroll + 1);
    			break;
    		case 'd':
    			center[0] += scale * pow(2, - scroll + 1);
    			break;
    		case '+':
    			scroll++;
    			if (scroll > 0) {
    				//maxIteration = scale * (int) pow(2, scroll);
    			}
    			break;
    		case '-':
    			scroll--;
    			if (scroll > 0) {
    				//maxIteration = scale * (int) pow(2, scroll);
    			}
    			break;
    		}

    }
	
	public void produceFractal() {
		//CN c = CN.multiply(new CN(0.7885,0),  CN.exp(new CN(0, ((float) frameCount) / 200)));
		//CN c = CN.multiply(new CN((float) mouseX / width, 0),  CN.exp(new CN(0, 2 * PI * mouseY / height)));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float ix = (float) (((float) i / width) * (3.5) - (1.75)) * pow(2, - scroll) + center[0] / width;
				float jx = (float) (((float) j / height) * (2) - (1)) * pow(2, - scroll) + center[1] / height;
				  CN z = new CN(ix, jx);
				  //pixels[i + width * j] = iterateNewton(z);
				  /*
				  if (i % (width / 4) == 0 && j == 0) {
					  System.out.println(z.toString());
				  }
				  */
				  pixels[i + width * j] = burningShip(new CN(0, 0), z);
			}
		}
		updatePixels();
		textSize(32);
		fill(255);
		//text("C = " + nf((float) c.getRe(),1,2) + " + " 
		//		+ nf((float) c.getIm(),1,2) + "i", 2 * width / 5, height);
		//System.out.println("Fractal Produced.");
	}
	
	public int burningShip(CN z, CN c) {
		int iteration = 0;
		double zx = z.getRe();
		double zy = z.getIm();
		while (zx * zx + zy * zy < pow(2, 2)  &&  iteration < maxIteration) 
	    {
			float xtemp = (float) (zx*zx - zy*zy + c.getRe());
			zy = abs((float) (2*zx*zy)) + c.getIm(); //abs returns the absolute value
			zx = abs(xtemp);
	        iteration = iteration + 1;
	    }
		int colInt = iteration * 255 / maxIteration;
		return color(((int)map(colInt, 0, 255, 200, 55) + 200) % 255, 255, map(colInt, 0, 255, 255, 0));
	}
}
