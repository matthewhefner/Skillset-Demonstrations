public class IntStack {
	// May create private data here.
	private int[] arr;
	private int size;

	public IntStack() {
		// TODO: Code to initialize your stack.
		arr = new int[100];
		size = 0;
	}

	public void push(int x) {
		// TODO: Code to push an item x onto the stack. The stack will never contain more than 100 elements.
		arr[size] = x;
		size++;
	}

	public int pop() {
		// TODO: Code to pop and return an item from the top of the stack. If the stack is empty, return -1.
		if (size == 0) {
			return -1;
		}
		int x = arr[size - 1];
		size--;
		return x;
	}
}