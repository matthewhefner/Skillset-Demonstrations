import java.io.File;
import java.util.Scanner;

public class Closest {
	private static final int b = 200;
	private static Node[][] grid = new Node[b][b];
	private static double minimum;
	
	public static void main(String[] args) {
		minimum = 2;
		File f = new File("points.txt");
		try {
			Scanner sk = new Scanner(f);
			int count = 0;
			while (sk.hasNext()) {
				insert(Double.parseDouble(sk.next()), Double.parseDouble(sk.next()));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		check();
	}
	
	public static void insert(double x, double y) {
		Node cursor = grid[(int) (x * b)][(int) (y * b)];
		if(cursor == null) {
			grid[(int) (x * b)][(int) (y * b)] = new Node(x, y, null);
		} else {
			for (; cursor.getNext() != null; cursor = cursor.getNext());
			cursor.setNext(new Node(x, y, null));
		}
	}
	
	public static void check() {
		for (int i = 0; i < b; i++) {
			for (int j = 0; j < b; j++) {
				Node cursor = grid[i][j];
				if(cursor == null) {
					return;
				} else {
					for (; cursor != null; cursor = cursor.getNext()) {
						Node after = cursor.getNext();
						for (; after != null; after = after.getNext()) {
							compare(cursor, after);
						}
						if (j > 0) {
							//top
							after = grid[i][j - 1];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (i > 0 && j > 0) {
							//top left
							after = grid[i - 1][j - 1];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (i > 0) {
							//left
							after = grid[i - 1][j];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (i > 0 && j < b - 1) {
							//bottom left
							after = grid[i - 1][j + 1];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (j < b - 1) {
							//bottom
							after = grid[i][j + 1];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (i < b - 1 && j < b - 1) {
							//bottom right
							after = grid[i + 1][j + 1];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (i < b - 1) {
							//right
							after = grid[i + 1][j];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
						if (j > 0 && i < b - 1) {
							//top right
							after = grid[i + 1][j - 1];
							for (; after != null; after = after.getNext()) {
								compare(cursor, after);
							}
						}
					}
				}
			}	
		}
		System.out.println(minimum);
	}
	
	public static void compare(Node n, Node m) {
		double difference = Math.sqrt(Math.pow(m.getxValue() - n.getxValue(), 2) +  Math.pow(m.getyValue() - n.getyValue(), 2));
		if (difference < minimum) {
			minimum = difference;
		}
	}
}
