import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class NewtonsMethod extends PApplet{
	private final int scale = 1;
	private final double tolerance = 0.000001;
	private final CN[] roots = {new CN(1, 0), new CN(-.5, sqrt(3) / 2), new CN(-.5, -sqrt(3) / 2)};
	private final int[] colors = {color(255, 0, 0), color(0, 255, 0), color(0, 0, 255), color(0)};
	private final int maxIteration = 155;
	private int[] center;
	private int scroll;
	
	public void setup() {
		//noLoop();
		colorMode(HSB);
		scroll = 0;
		center = new int[2];
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
		save("out/" + frameCount + ".png");
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		NewtonsMethod sketch = new NewtonsMethod();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public CN f(CN z) {
		return CN.subtract(CN.pow(z, 3), new CN(1, 0));
	}
	
	public CN fp(CN z) {
		return CN.multiply(CN.pow(z, 2), new CN(3, 0));
	}
	
    public void innerMouseDragged()
    {

    }

    public void innerMousePressed()
    {

    }
	
    public void mouseWheel(MouseEvent event)
    {
        scroll -= event.getCount();
    }
	
	public void produceFractal() {
		//CN c = CN.multiply(new CN(0.7885,0),  CN.exp(new CN(-.3, 1)));
		CN c = CN.multiply(new CN((float) mouseX / width, 0),  CN.exp(new CN(0, 2 * PI * mouseY / height)));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float divisor = pow(2, 1 + (float) scroll / 10);
				float ix = (float) (((float) i / width) * (3.5 / divisor) - (1.75 / divisor));
				float jx = (float) (((float) j / height) * (2 / divisor) - (1 / divisor));
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
		textSize(32);
		fill(255);
		text("C = " + nf((float) c.getRe(),1,2) + " + " 
				+ nf((float) c.getIm(),1,2) + "i", 2 * width / 5, height);
		//System.out.println("Fractal Produced.");
	}
	
	public int iterateNewton(CN z) {
		for (int iteration = 0; iteration < maxIteration; iteration++) {
			z.subtract(CN.divide(f(z), fp(z)));
			for (int i = 0; i < 3; i++) {
				CN difference = CN.subtract(z, roots[i]);
				if (abs((float) difference.getRe()) < tolerance 
						&& abs((float) difference.getIm()) < tolerance) {
					return colors[i];
				}
			}
		}
		return colors[3];
	}
	
	public int iterateJulia(CN z, CN c) {
		int iteration = 0;
		double zx = z.getRe();
		double zy = z.getIm();
		while (zx * zx + zy * zy < 4  &&  iteration < maxIteration) 
	    {
	        double xtemp = zx * zx - zy * zy;
	        zy = 2 * zx * zy  + c.getIm(); 
	        zx = xtemp + c.getRe();
	        iteration = iteration + 1;
	    }
		int colInt = iteration * 255 / maxIteration;
		return color(colInt, 255, map(colInt, 0, 255, 150, 255));
	}
	
	public int iterateMultiJulia(CN z, CN c, int n) {
		int iteration = 0;
		float zx = (float) z.getRe();
		float zy = (float) z.getIm();
		while (zx * zx + zy * zy < 4  &&  iteration < maxIteration) 
	    {
			float xtmp = pow((zx * zx + zy * zy), (n / 2)) * cos(n * atan2(zy, zx)) 
					+ (float) c.getRe();
		    zy = pow((zx * zx + zy * zy), (n / 2)) * sin(n * atan2(zy, zx)) + (float) c.getIm();
		    zx = xtmp;
	        iteration = iteration + 1;
	    }
		int colInt = iteration * 255 / maxIteration;
		return color(colInt, 255, colInt);
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
		    // sqrt of inner term removed using log simplification rules.
		    float log_zn = log( x*x + y*y ) / 2;
		    float nu = log( log_zn / log(2) ) / log(2);
		    // Rearranging the potential function.
		    // Dividing log_zn by log(2) instead of log(N = 1<<8)
		    // because we want the entire palette to range from the
		    // center to radius 2, NOT our bailout radius.
		    iteration = (int) (iteration + 1 - nu);
		  }
		int colInt = iteration * 255 / maxIteration;
		return color(colInt, 255, colInt);
	}
}
