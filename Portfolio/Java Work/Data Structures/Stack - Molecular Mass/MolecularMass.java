import java.util.Scanner;	
import java.util.Arrays;

public class MolecularMass {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the molecule: ");
		String input = in.nextLine();
		int mass = getMass(input.toCharArray());
		System.out.println("The Molecular Mass of " + input + " is " + mass);
	}
	
	public static int getMass(char[] chars) {
		IntStack stack = new IntStack();
		int mass = 0;
		for (int i = 0; i < chars.length; i++) {
			switch(chars[i]) {
				case 'C':
					stack.push(12);
					break;
				case 'H':
					stack.push(1);
					break;
				case 'O':
					stack.push(16);
					break;
				case '(':
					stack.push(getMass(Arrays.copyOfRange(chars, i + 1, chars.length)));
					int advance = 0;
					int nested = 0;
					boolean forward = true;
					while (forward) {
						if (chars[i + advance] == '(') {
							nested++;
						} else if (chars[i + advance] == ')') {
							nested--;
						}
						if (chars[i + advance] == ')' && nested == 0){
							forward = false;
						} else {
							advance++;
						}
					}
					i += advance;
					break;
				case ')':
					for (int j = stack.pop(); j != -1; j = stack.pop()) {
						mass += j;
					}
					return mass;
				default: 
					//must be a number
					stack.push(stack.pop() * ((int) chars[i] - '0'));
			}
		}
		for (int j = stack.pop(); j != -1; j = stack.pop()) {
			mass += j;
		}
		return mass;
	}
}