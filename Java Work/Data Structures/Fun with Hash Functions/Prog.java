import java.lang.Integer;

public class Prog {

	public static void main(String [] args) {
		
		if (args.length != 2) {
			System.out.println("Please execute: java Prog <n> <p>");
			System.out.println("n is the input size, and p is the program number.");
			System.exit(0);
		}

		int n = Integer.parseInt(args[0]);
		int p = Integer.parseInt(args[1]);

		switch(p) {
			case 1: prog1(n);
							break;
			case 2: prog2(n);
							break;
			case 3: prog3(n);
							break;
			case 4: prog4(n);
							break;
			default: System.out.println("Invalid program number. Only 1-4.");
		}
	}

	private static void prog1(int n) {
		// TODO: Code to generate n keys that all get hashed to the same index using hash1.	
		for (int i = 0; i < n * n; i += n) {
			System.out.println(i);
		}
	}

	private static void prog2(int n) {
		// TODO: Code to generate n keys that all get hashed to the same index using hash2.	
		for (int i = 0; i < n; i += 1) {
			System.out.println(i);
		}
	}

	private static void prog3(int n) {
		// TODO: Code to generate n keys that all get hashed to the same index using hash3.	
		for (int i = 0; i < n; i += 1) {
			System.out.println(i * 128189);
		}
	}

	private static void prog4(int n) {
		// TODO: Code to generate n keys that all get hashed to the same index using hash4.	
		HashFunctions hashFunctions = new HashFunctions(n);
		int hashes[] = new int[n * n];
		int[] counts = new int[n];
		for (int i = 0; i < n * n; i += 1) {
			hashes[i] = hashFunctions.hash4(i);
			counts[hashFunctions.hash4(i)]++;
		}
		int maxIndex = 0;
		for (int i = 0; i < n; i += 1) {
			if (counts[i] > counts[maxIndex]) {
				maxIndex = i;
			}
		}
		int counter = 0;
		for (int i = 0; i < n * n; i += 1) {
			if (hashes[i] == maxIndex) {
				System.out.println(i);
				counter++;
			}
			if (counter == n) {
				break;
			}
		}
	}
}
