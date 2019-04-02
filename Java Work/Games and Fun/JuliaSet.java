import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class JuliaSet extends PApplet{
	private final int maxIteration = 32;
	
	public void setup() {
		//noLoop();
		colorMode(HSB);
	}
	
	public void settings(){
		//fullScreen();
		//size((int) (3000 * 3.5), (int) 3000 * 2);
		size(386, 216);
	}
	
	public void draw(){
		loadPixels();
		produceFractal();
		//save("standard-2_images_halves/" + frameCount + ".png");
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		JuliaSet sketch = new JuliaSet();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public void produceFractal() {
		CN c = CN.multiply(new CN(0.7885,0),  CN.exp(new CN(0, ((float) frameCount + .5) / 200)));
		if (frameCount == 1263) {
			exit();
		}
		//CN c = CN.multiply(new CN((float) mouseX / width, 0),  CN.exp(new CN(0, 2 * PI * mouseY / height)));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float ix = (float) (((float) i / width) * (3.5) - (1.75));
				float jx = (float) (((float) j / height) * (2) - (1));
				  CN z = new CN(ix, jx);
				  //pixels[i + width * j] = iterateNewton(z);
				  /*
				  if (i % (width / 4) == 0 && j == 0) {
					  System.out.println(z.toString());
				  }
				  */
				  pixels[i + width * j] = iterateMultiJulia(z, c, 4);
			}
		}
		updatePixels();
		textSize(32);
		fill(255);
		text("C = " + nf((float) c.getRe(),1,2) + " + " 
				+ nf((float) c.getIm(),1,2) + "i", 2 * width / 5, height);
		//System.out.println("Fractal Produced.");
	}
	
	public int iterateJulia(CN z, CN c) {
		int iteration = 0;
		double zx = z.getRe();
		double zy = z.getIm();
		while (zx * zx + zy * zy < pow(2, 10)  &&  iteration < maxIteration) 
	    {
	        double xtemp = zx * zx - zy * zy;
	        zy = 2 * zx * zy  + c.getIm(); 
	        zx = xtemp + c.getRe();
	        iteration = iteration + 1;
	    }
		int colInt = iteration * 255 / maxIteration;
		return color(colInt, 255, map(colInt, 0, 255, 100, 255));
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
		return color(colInt, 255, map(colInt, 0, 255, 50, 255));
	}
}
