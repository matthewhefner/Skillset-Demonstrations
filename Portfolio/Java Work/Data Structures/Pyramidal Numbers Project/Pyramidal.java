import java.lang.Math;

public class Pyramidal {
	public static int[] pyramidals;
	
	public static void main(String [] args) {
		if (args.length != 1) {
			System.out.println("Please call the program with one argument.");
			System.out.println("$ java Pyramidal n");
			System.out.println("Exiting applicaiton.");
			System.exit(0);
		}
		int n = Integer.parseInt(args[0]); // This is the end point of the range.
		// TODO: Please write code to find for each number i in [1, n], whether it is a sum of at most 4 pyramidal numbers.
		// Please follow the output format given in the handout.
		generatePyramidals(n);
		for (int i = 1; i <= n; i++) {
			check(i);
		}
	}
	
	public static void check(int n) {
		superCheck2(n);
	}
	
	public static void superCheck2(int n) {
		//binary search; pyramidals are sorted! :)
		String output = n + " 0";
		boolean[] found = new boolean[4];
		int a = 0;
		int b = pyramidals.length - 1;
		int center = (b + a) / 2;
		while (a <= b) {
			center = (b + a) / 2;
			if (pyramidals[center] == n) {
				output = n + " 1 " + pyramidals[center];
				break;
			} else if (pyramidals[center] < n) {
				//Could possibly be a part of the sum
				//Start TWO
				if (!found[1]) {
					int a2 = 0;
					int b2 = pyramidals.length - 1;
					int center2 = (b2 + a2) / 2;
					while (a2 <= b2) {
						center2 = (b2 + a2) / 2;
						if (pyramidals[center] + pyramidals[center2] == n) {
							output = n + " 2 " + pyramidals[center] + " " + pyramidals[center2];
							found[1] = true;
							break;
						} else if (pyramidals[center] + pyramidals[center2] < n) {
							//Could possibly be a part of the sum
							//Start THREE
							if (!found[2]) {
								int a3 = 0;
								int b3 = pyramidals.length - 1;
								int center3 = (b3 + a3) / 2;
								while (a3 <= b3) {
									center3 = (b3 + a3) / 2;
									if (pyramidals[center] + pyramidals[center2] +  pyramidals[center3] == n) {
										output = n + " 3 " + pyramidals[center] + " " + pyramidals[center2] + " " + pyramidals[center3];
										found[2] = true;
										break;
									} else if (pyramidals[center] + pyramidals[center2] + pyramidals[center3] < n) {
										//Could possibly be a part of the sum
										//Start FOUR
										if (!found[3]) {
											int a4 = 0;
											int b4 = pyramidals.length - 1;
											int center4 = (b4 + a4) / 2;
											while (a4 <= b4) {
												center4 = (b4 + a4) / 2;
												if (pyramidals[center] + pyramidals[center2] +  pyramidals[center3] + pyramidals[center4] == n) {
													output = n + " 4 " + pyramidals[center] + " " + pyramidals[center2] + " " + pyramidals[center3] + " " + pyramidals[center4];
													found[3] = true;
													break;
												} else if (pyramidals[center] + pyramidals[center2] + pyramidals[center3] + pyramidals[center4] < n) {
													a4 = center4 + 1;
												} else {
													b4 = center4 - 1;
												}
											}
										}
										//End FOUR
										a3 = center3 + 1;
									} else {
										b3 = center3 - 1;
									}
								}
							}
							//End THREE
							a2 = center2 + 1;
						} else {
							b2 = center2 - 1;
						}
					}
				}
				//End TWO
				a = center + 1;
			} else {
				b = center - 1;
			}
		}
		System.out.println(output);
		return;
	}
	
	public static void generatePyramidals(int n) {
		int[] tempPyramidals = new int[n];
		int count = 0;
		int pyramidal = 1;
		while(pyramidal <= n) {
			tempPyramidals[count] = pyramidal;
			count++;
			pyramidal += (count + 1) * (count + 1);
		}
		pyramidals = new int[count];
		for (int i = 0; i < count; i++) {
			pyramidals[i] = tempPyramidals[i];
		}
	}
}
