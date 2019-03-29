import processing.core.PApplet;
import java.math.BigDecimal;
import java.math.MathContext;

public class MandelbrotBigDecimal extends PApplet{
	private int maxIteration;
	private CNBigDecimal center;
	private BigDecimal zoom;
	private final MathContext scale = MathContext.DECIMAL32;
	private boolean prt1;
	private boolean prt2;
	private boolean prt3;
	private boolean prt4;
	
	public void setup() {
		//noLoop();
		colorMode(HSB);
		zoom = new BigDecimal(".5", scale);
		maxIteration = 25;
		center = new CNBigDecimal(new BigDecimal("-0.670116545164917", scale), 
				new BigDecimal("0.4580609361148835", scale));
		prt1 = true;
		prt2 = true;
		prt3 = true;
		prt4 = true;
	}
	
	public void settings(){
		//fullScreen();
		//size((int) (50 * 3.5), (int) 50 * 2);
		size(1920 / 2,1080 / 2);
	}
	
	public void draw(){
		loadPixels();
		thread("part1");
		thread("part2");
		thread("part3");
		thread("part4");
		while(prt1 || prt2 || prt3 || prt4) {
			delay(50);
		}
		updatePixels();
		prt1 = true;
		prt2 = true;
		prt3 = true;
		prt4 = true;
		zoom = zoom.multiply(new BigDecimal("1.01", scale), scale);	
		if (frameCount % 10 == 0)
			maxIteration += 1;
		filter(BLUR, (float) .1);
		save("man3/" + frameCount + ".png");
	}
	
	public void mousePressed() {
		//save("burningShip/" + frameCount + ".png");
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		MandelbrotBigDecimal sketch = new MandelbrotBigDecimal();
		PApplet.runSketch(processingArgs, sketch);
	}
	
	public void part1() {
		produceFractal(1);
	}
	
	public void part2() {
		produceFractal(2);
	}
	
	public void part3() {
		produceFractal(3);
	}
	
	public void part4() {
		produceFractal(4);
	}
	
	public void produceFractal(int n) {
		System.out.println("Frame: " + frameCount);
		System.out.println("Zoom: " + zoom);
		BigDecimal widthWidth = new BigDecimal(String.valueOf(width), scale);
		BigDecimal heightHeight = new BigDecimal(String.valueOf(height), scale);
		System.out.println("Big Decimal Width: " + widthWidth);
		System.out.println("Big Decimal Height: " + heightHeight);
		int iMin = 0;
		int jMin = 0;
		int iMax = 0;
		int jMax = 0;
		iMin = (n % 2) * (width / 2);
		iMax = iMin + (width / 2);
		jMin = ((n - 1) / 2) * (height / 2);
		jMax = jMin + (height / 2);
		for (int i = iMin; i < iMax; i++) {
			for (int j = jMin; j < jMax; j++) {
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
				
				pixels[i + width * j] = 
						burningShip(new CNBigDecimal(new BigDecimal("0"), 
								new BigDecimal("0")), z);
			}
		}
		switch(n) {
		case 1:
			prt1 = false;
			println("prt1");
			break;
		case 2:
			prt2 = false;
			println("prt2");
			break;
		case 3:
			prt3 = false;
			println("prt3");
			break;
		case 4:
			prt4 = false;
			println("prt4");
			break;
		}
	}

	public int burningShip(CNBigDecimal z, CNBigDecimal c) {
		int iteration = 0;
		BigDecimal zx = z.getRe();
		BigDecimal zy = z.getIm();
		BigDecimal zxSq = zx.multiply(zx, scale);
		BigDecimal zySq = zy.multiply(zy, scale);
		while (zx.multiply(zx, scale).add(zy.multiply(zy, scale), 
				scale).compareTo(new BigDecimal("1024", scale)) < 0  
				&&  iteration < maxIteration) 
	    {
			zxSq = zx.multiply(zx, scale);
			zySq = zy.multiply(zy, scale);
			BigDecimal xtemp = zxSq.subtract(zySq, scale).add(c.getRe(), scale);
			
			BigDecimal zyPreserve = zy;
			zy = new BigDecimal("2", scale);
			zy = zy.multiply(zx, scale);
			zy = zy.multiply(zyPreserve, scale);
			//zy = zy.abs(scale);
			zy = zy.add(c.getIm(), scale);
			zx = xtemp;
	        iteration = iteration + 1;
	        
	        /*
	        float xtemp = (float) (zx*zx - zy*zy + c.getRe());
			zy = abs((float) (2*zx*zy)) + c.getIm(); 
			//abs returns the absolute value
			zx = abs(xtemp);
	        iteration = iteration + 1;
	         */
	    }
		/*
		if ( iteration < maxIteration ) {
		    float log_zn = log( zxSq.floatValue() + zySq.floatValue() ) / 2;
		    float nu = log( log_zn / log(2) ) / log(2);
		    iteration = (int) (iteration + 1 - nu);
		  }
		  */
		int colInt = iteration * 255 / maxIteration;
		colInt = (int) (255 * (2 / (1.0 + Math.exp((float) -colInt / 255)) - 1));
		return color(colInt, 255, colInt);
	}
}
