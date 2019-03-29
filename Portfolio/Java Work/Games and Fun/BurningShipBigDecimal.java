import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;

public class BurningShipBigDecimal extends PApplet{
	private int maxIteration;
	private CNBigDecimal center;
	private BigDecimal zoom;
	private final MathContext scale = MathContext.DECIMAL32;
	
	public void setup() {
		//noLoop();
		colorMode(HSB);
		zoom = new BigDecimal("1", scale);
		maxIteration = 30;
		center = new CNBigDecimal(new BigDecimal("-1.762", scale), new BigDecimal(".028", scale));
	}
	
	public void settings(){
		//fullScreen();
		size((int) (100 * 3.5), (int) 100 * 2);
		//size(1280, 720);
	}
	
	public void draw(){
		loadPixels();
		produceFractal();
		zoom = zoom.multiply(new BigDecimal("1.1", scale), scale);	
		if (frameCount % 1 == 0)
			maxIteration += 1;
		filter(BLUR, (float) 0.1);
		save("burningShip/" + frameCount + ".png");
	}
	
	public void mousePressed() {
		//save("burningShip/" + frameCount + ".png");
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		BurningShipBigDecimal sketch = new BurningShipBigDecimal();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public void produceFractal() {
		System.out.println("Frame: " + frameCount);
		System.out.println("Zoom: " + zoom);
		BigDecimal widthWidth = new BigDecimal(String.valueOf(width), scale);
		BigDecimal heightHeight = new BigDecimal(String.valueOf(height), scale);
		System.out.println("Big Decimal Width: " + widthWidth);
		System.out.println("Big Decimal Height: " + heightHeight);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//Map i to Re coordinate
				BigDecimal ii = new BigDecimal(String.valueOf(i), scale);
				ii = ii.divide(widthWidth, scale);
				ii = ii.multiply(new BigDecimal("3.5", scale), scale);
				ii = ii.subtract(new BigDecimal("2", scale), scale);
				ii = ii.divide(zoom, scale);
				ii = ii.add(center.getRe(), scale);
				
				//Map j to Im coordinate
				BigDecimal jj = new BigDecimal(String.valueOf(j), scale);
				jj = jj.divide(heightHeight, scale);
				jj = jj.multiply(new BigDecimal("2", scale), scale);
				jj = jj.subtract(new BigDecimal("1", scale), scale);
				jj = jj.divide(zoom, scale);
				jj = jj.subtract(center.getIm(), scale);
				
				BigDecimal iRE = new BigDecimal(ii.toString(), scale);
				BigDecimal jIM = new BigDecimal(jj.toString(), scale);
				
				CNBigDecimal z = new CNBigDecimal(iRE, jIM);
				
				//System.out.println("\tz: " + z.getRe() + " + " + z.getIm() + "i");
				
				pixels[i + width * j] = burningShip(new CNBigDecimal(new BigDecimal("0"), new BigDecimal("0")), z);
			}
		}
		updatePixels();
		textSize(32);
		fill(255);
		//text("C = " + nf((float) c.getRe(),1,2) + " + " 
		//		+ nf((float) c.getIm(),1,2) + "i", 2 * width / 5, height);
		//System.out.println("Fractal Produced.");
	}

	public int burningShip(CNBigDecimal z, CNBigDecimal c) {
		int iteration = 0;
		BigDecimal zx = z.getRe();
		BigDecimal zy = z.getIm();
		BigDecimal zxSq = zx.multiply(zx, scale);
		BigDecimal zySq = zy.multiply(zy, scale);
		while (zx.multiply(zx, scale).add(zy.multiply(zy, scale), scale).compareTo(new BigDecimal("4", scale)) < 0  
				&&  iteration < maxIteration) 
	    {
			zxSq = zx.multiply(zx, scale);
			zySq = zy.multiply(zy, scale);
			BigDecimal xtemp = zxSq.subtract(zySq, scale).add(c.getRe(), scale);
			
			BigDecimal zyPreserve = zy;
			zy = new BigDecimal("2", scale);
			zy = zy.multiply(zx, scale);
			zy = zy.multiply(zyPreserve, scale);
			zy = zy.abs(scale);
			zy = zy.add(c.getIm(), scale);
			zx = xtemp.abs(scale);
	        iteration = iteration + 1;
	        
	        /*
	        float xtemp = (float) (zx*zx - zy*zy + c.getRe());
			zy = abs((float) (2*zx*zy)) + c.getIm(); //abs returns the absolute value
			zx = abs(xtemp);
	        iteration = iteration + 1;
	         */
	    }
		int colInt = iteration * 255 / maxIteration;
		colInt = (int) (255 * (4 / (1.0 + Math.exp((float) -colInt / 255)) - 2));
		return color(map(colInt, 0, 255, 220, 100), 255, colInt);
	}
}
