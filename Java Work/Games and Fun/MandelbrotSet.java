import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class MandelbrotSet extends PApplet{
	private final int scale = 25;
	private int maxIteration = 255;
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
	}
	
	public void draw(){
		loadPixels();
		produceFractal();
		//save("out/" + frameCount + ".png");
	}
	
	public void mousePressed() {
		float ix = (float) (((float) mouseX / width) * (3.5) - (1.75)) * pow(2, - scroll) + center[0] / width;
		float jx = (float) (((float) mouseY / height) * (2) - (1)) * pow(2, - scroll) + center[1] / height;
		CN z = new CN(ix, jx);
		println(z);
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		MandelbrotSet sketch = new MandelbrotSet();
		PApplet.runSketch(processingArgs, sketch);
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
    				maxIteration = scale * (int) pow(2, scroll);
    			}
    			break;
    		case '-':
    			scroll--;
    			if (scroll > 0) {
    				maxIteration = scale * (int) pow(2, scroll);
    			}
    			break;
    		}

    }
	
	public void produceFractal() {
		//CN c = CN.multiply(new CN(0.7885,0),  CN.exp(new CN(-.3, 1)));
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
				  pixels[i + width * j] = mandelbrot(z);
			}
		}
		updatePixels();
		//System.out.println("Fractal Produced.");
	}
	
	public int mandelbrot(CN z) {
		int iteration = 0;
		float x0 = (float) z.getRe();
		float y0 = (float) z.getIm();
		float x = 0;
		float y = 0;
		while (x * x + y * y < 4  &&  iteration < maxIteration) 
	    {
		    float xtemp = x*x - y*y + x0;
    	    y = 2*x*y + y0;
    	    x = xtemp;
    	    iteration = iteration + 1;
	    }
		if ( iteration < maxIteration ) {
		    float log_zn = log( x*x + y*y ) / 2;
		    float nu = log( log_zn / log(2) ) / log(2);
		    iteration = (int) (iteration + 1 - nu);
		  }
		int colInt = iteration * 255 / maxIteration;
		return color(colInt, 255, map(colInt, 0, 255, 150, 255));
	}
}
