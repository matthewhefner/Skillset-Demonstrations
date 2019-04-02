/*
 * This class maintains a set of integers. 
 */
public class IntSet {
	private Node head;
	private int size;		// The number of elements currently stored in the set.
	
	public IntSet() {
		head = null;
		size = 0;
	}
	
	public IntSet(int _cap) {
		head = null;
		size = 0;
	}
	
	/* Find if a key is present in the set. Returns -1 if the key is not present, otherwise returns the position in the set.*/
	public boolean find(int key) {
		for (Node cursor = head; cursor != null; cursor = cursor.getNext()) {
			if (cursor.getData() == key)
				return true;
		}
		return false;
	}
	
	/* Insert a key into the set. */
	public void insert(int key) {
		assert (!find(key));
		Node cursor = head;
		if (cursor != null) {
			for (; cursor.getNext() != null && cursor.getNext().getData() > key; cursor = cursor.getNext());
			cursor.setNext(new Node(key, cursor.getNext()));
		}
		else {
			head = new Node(key, null);
		}
		size++;
	}
	
	/* Remove a key from the set. */
	public void remove(int key) {
		assert (find(key));
		Node cursor = head;
		while (cursor.getData() != key) {
			cursor = cursor.getNext();
		}
		cursor.setNext(cursor.getNext().getNext());
		size--;
	}
	
	/* Print the contents of the set in sorted order. */
	public void print() {
		for (Node cursor = head; cursor != null; cursor = cursor.getNext()) {
			System.out.println(cursor.getData());
		}
	}
}
