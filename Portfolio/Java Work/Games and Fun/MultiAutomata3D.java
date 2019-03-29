import processing.core.*;
import processing.event.MouseEvent;
import java.util.Arrays;

public class MultiAutomata3D extends PApplet{
	private boolean[][] active; //2d boolean array of iteration
	private boolean[][] newActive; //2d boolean array of next iteration
	private boolean[][] alive; //2d boolean array of iteration
	private boolean[][] newAlive; //2d boolean array of next iteration
	private int rows; //int # of rows
	private int cols; //int # of cols
	private boolean paused; //boolean toggles resume/pause
	private int[][] neighbors; //2d int array of neighbors
	private int[][] rulers; //2d int array of rulers
	private int[][] last; //2d int array storing the last ruler
	private int scaler; //int size of each cell when drawn to screen
	//
	private Navigation nav;
	PShape cat;
	
	public void setup() {
		background(0);
		cat = loadShape("new.obj");
		cat.scale((float) 0.1);
		//
		paused = true;
		scaler = 30;
		rows = 100;
		cols = 100;
		active = new boolean[rows][cols]; //2d boolean array of iteration
		newActive = new boolean[rows][cols]; //2d boolean array of next iteration
		alive = new boolean[rows][cols]; //2d boolean array of iteration
		newAlive = new boolean[rows][cols]; //2d boolean array of next iteration
		neighbors = new int[rows][cols]; //2d int array of neighbors
		rulers = new int[rows][cols]; //2d int array of rulers
		last = new int[rows][cols];

		nav = new Navigation(this, rows * scaler, cols * scaler);
	}
	
	public void settings(){
		fullScreen(P3D);
	}
	
	public void draw(){
		background(0);
		directionalLight(255, 100, 500, 1, 0, (float) 0.5);
		ambientLight(100, 100, 200);
		nav.movement();
		nav.d();
		translate(0, 0, 0);
		generation();
	}
	
	public void keyPressed() {
		if (key == ' ') {
			pauseProcess();
		} else if (key == 's') {
			startMoves();
		}
		if (keyCode == DELETE) {
			active = new boolean[rows][cols]; //2d boolean array of iteration
			newActive = new boolean[rows][cols]; //2d boolean array of next iteration
			alive = new boolean[rows][cols]; //2d boolean array of iteration
			newAlive = new boolean[rows][cols]; //2d boolean array of next iteration
			neighbors = new int[rows][cols]; //2d int array of neighbors
			rulers = new int[rows][cols]; //2d int array of rulers
			last = new int[rows][cols]; //2d int array of rulers
		}
	}

	public void pauseProcess() {
		paused = !paused;
		if (paused) {
			newActive = new boolean[rows][cols];
			newAlive = new boolean[rows][cols];
			for (int k = 0; k < rows; k++) {
				newActive[k] = Arrays.copyOf(active[k], active[k].length);
				newAlive[k] = Arrays.copyOf(alive[k], alive[k].length);
			}
		} else {
			for (int k = 0; k < rows; k++) {
				active[k] = Arrays.copyOf(newActive[k], newActive[k].length);
				alive[k] = Arrays.copyOf(newAlive[k], newAlive[k].length);
			}
			newActive = new boolean[rows][cols];
			newAlive = new boolean[rows][cols];
			fullCheck();
		}
	}
	
	public void startMoves() {
		for (int u = 0; u < rows; u++) {
			for (int v = 0; v < cols; v++) {
				if ( (u) % 50 == 0 && u != 0) {
					mouseHelper(u, v, 2);
				}
			}
		}
	}

	public void mouseHelper(int row, int col, int rul) {
		rulers[row][col] = rul;
		last[row][col] = rul;
		newActive[row][col] = true;
		newAlive[row][col] = true;
		newLife(row, col);
	}
	
	public void generation() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (active[i][j]) {
					pushMatrix();
					translate(i * scaler - (rows * scaler / 2), 
							neighbors[i][j] * scaler / 2, 
							j * scaler - (cols * scaler / 2));
					noStroke();
					fill(255);
					if ((neighbors[i][j] == 2 || neighbors[i][j] == 3) & alive[i][j]) {
						//Stay alive
						newActive[i][j] = true;
						newAlive[i][j] = true;
					} else if ((neighbors[i][j] == 3) & !alive[i][j]) {
						//New life.
						newLife(i,j);
						newActive[i][j] = true;
						newAlive[i][j] = true;
						shape(cat);
					} else if ((neighbors[i][j] > 6) & alive[i][j]) {
						if (rulers[i][j] == 0) {
							//Tie death, stays active
							newActive[i][j] = true;
							newAlive[i][j] = false;
							shape(cat);	
						} else {
							//Stays alive
							newActive[i][j] = true;
							newAlive[i][j] = true;
							//drawRect(i, j, 0);	
						}
					} else if (neighbors[i][j] == 0){
						//No longer active
						newActive[i][j] = false;
						newAlive[i][j] = false;
						shape(cat);
					} else {
						//Death; still active
						newActive[i][j] = true;
						newAlive[i][j] = false;
						shape(cat);	
					}
					popMatrix();
				}
			}
		}
		if (!paused) {
			for (int k = 0; k < rows; k++) {
				active[k] = Arrays.copyOf(newActive[k], newActive[k].length);
				alive[k] = Arrays.copyOf(newAlive[k], newAlive[k].length);
			}
			newActive = new boolean[rows][cols];
			newAlive = new boolean[rows][cols];
			
			fullCheck();
		}
	}

	public void newLife(int i, int j) {
		//Top Left
		if (i > 0 && j > 0) {
			newActive[i - 1][j - 1] = true;
		} else if (j > 0){
			newActive[rows - 1][j - 1] = true;
		}
		//Left
		if (i > 0) {
			newActive[i - 1][j] = true;
		} else {
			newActive[rows - 1][j] = true;
		}
		//Bottom Left
		if (i > 0 && j < cols - 1) {
			newActive[i - 1][j + 1] = true;
		} else if (j < cols - 1){
			newActive[rows - 1][j + 1] = true;
		}
		//Top
		if (j > 0) {
			newActive[i][j - 1] = true;
		}
		//Bottom
		if (j < cols - 1) {
			newActive[i][j + 1] = true;
		}
		//Top Right
		if (i < rows - 1 && j > 0) {
			newActive[i + 1][j - 1] = true;
		} else if (j > 0) {
			newActive[0][j - 1] = true;
		}
		if (i < rows - 1) {
			newActive[i + 1][j] = true;
		} else {
			newActive[0][j] = true;
		}
		if (i < rows - 1 && j < cols - 1) {
			newActive[i + 1][j + 1] = true;
		} else if (j < cols - 1) {
			newActive[0][j + 1] = true;
		}
	}
	
	public void fullCheck() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (active[i][j]) {
					neighborCheck(i, j);
				}
			}
		}
	}

	public void neighborCheck(int i, int j) {
		neighbors[i][j] = 0;
		int[] ruler = {0, 0, 0, 0, 0};
		//top left
		if (i > 0 && j > 0 && alive[i - 1][j - 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[i - 1][j - 1]] += 1;
		} else if (i == 0 && j > 0 && alive[rows - 1][j - 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[rows - 1][j - 1]] += 1;
		}
		//left
		if (i > 0 && alive[i - 1][j]) {
			neighbors[i][j] += 1;
			ruler[rulers[i - 1][j]] += 1;
		} else if (i == 0 && alive[rows - 1][j]) {
			neighbors[i][j] += 1;
			ruler[rulers[rows - 1][j]] += 1;
		}
		//bottom left
		if (i > 0 && j < cols - 1 && alive[i - 1][j + 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[i - 1][j + 1]] += 1;
		} else if (i == 0 && j < cols - 1 && alive[rows - 1][j + 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[rows - 1][j + 1]] += 1;
		}
		
		//top
		if (j > 0 && alive[i][j - 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[i][j - 1]] += 1;
		}
		//bottom
		if (j < cols - 1 && alive[i][j + 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[i][j + 1]] += 1;
		}
		
		//top right
		if (i < rows - 1 && j > 0 && alive[i + 1][j - 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[i + 1][j - 1]] += 1;
		} else if (i == rows - 1 && j > 0 && alive[0][j - 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[0][j - 1]] += 1;
		}
		//right
		if (i < rows - 1 && alive[i + 1][j]) {
			neighbors[i][j] += 1;
			ruler[rulers[i + 1][j]] += 1;
		} else if (i == rows - 1 && alive[0][j]) {
			neighbors[i][j] += 1;
			ruler[rulers[0][j]] += 1;
		}
		//bottom right
		if (i < rows - 1 && j < cols - 1 && alive[i + 1][j + 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[i + 1][j + 1]] += 1;
		} else if (i == rows - 1 && j < cols - 1 && alive[0][j + 1]) {
			neighbors[i][j] += 1;
			ruler[rulers[0][j + 1]] += 1;
		}
		int max = 0;
		int maxIndex = 0;
		int maxCount = 2;
		for (byte k = 1; k < 5; k++){
			if (ruler[k] > max) {
				max = ruler[k];
				maxIndex = k;
				maxCount = 1;
			} else if (ruler[k] == max) {
				maxCount++;
			}
		}
		if (maxCount == 1) {
			rulers[i][j] = maxIndex;
			last[i][j] = maxIndex;
		} else {
			rulers[i][j] = 0;
		}
	}
	
	public void mousePressed(){
		nav.mP();
	}
	
	public void mouseWheel(MouseEvent event) {
		nav.mW(event);
	}
	
	public void mouseDragged(){
		nav.mD();
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		MultiAutomata3D sketch = new MultiAutomata3D();
		PApplet.runSketch(processingArgs, sketch);
	}
}
