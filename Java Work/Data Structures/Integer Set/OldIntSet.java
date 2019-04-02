/*
 * This class maintains a set of integers. 
 */
public class IntSet {
	private int [] set;		// An array to represernt the set. This is always maintained in sorted order.
	private int capacity;	// The maximum allocated memory for the set.
	private int size;		// The number of elements currently stored in the set.
	
	public IntSet() {
		size = 0;
		capacity = 10;
		set = new int [capacity];
	}
	
	public IntSet(int _cap) {
		size = 0;
		capacity = _cap;
		set = new int [capacity];
	}
	
	/* Find if a key is present in the set. Returns -1 if the key is not present, otherwse returns the position in the set.*/
	public boolean find(int key) {
		// binary search
		int low = 0, high = size-1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (set[mid] == key) return true;
			if (key > set[mid]) low = mid + 1;
			else high = mid - 1;
		}
		return false;
	}
	
	/* Insert a key into the set. */
	public void insert(int key) {
		// Make sure that the key is not present.
		assert (!find(key));
		
		// Reached capacity? Then expand.
		if (size == capacity) {
			int [] oldset = set;
			set = new int [2 * capacity];
			for (int i = 0; i < capacity; i++)
				set[i] = oldset[i];
			capacity = 2 * capacity;
		}
		
		// Perform insert.
		int i;
		for (i = size - 1; i >= 0 && set[i] > key; i--)
			set[i+1] = set[i];
		set[i+1] = key;
		size++;
	}
	
	/* Remove a key from the set. */
	public void remove(int key) {
		// Make sure that the key is present.
		assert (find(key));
		
		// Find the position of the key.
		int pos;
		for (pos = 0; pos < size && set[pos] != key; pos++);
		
		// Perform remove.
		for (; pos < size; pos++)
			set[pos] = set[pos+1];
		size--;
	}
	
	/* Print the contents of the set in sorted order. */
	public void print() {
		for (int i = 0; i < size; i++)
			System.out.print(set[i] + " ");
		System.out.println();
	}
}
