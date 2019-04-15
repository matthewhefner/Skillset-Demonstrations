/**
 * This is a string set data structure, that is implemented as a Hash Table.
 * This data structure supports operations insert, find and print - that insert
 * a new String, finds a String key and prints the contents of the data
 * structure resp.
 */
public class StringSet {

	StringNode[] table; // Hash table - collisions resolved through chaining.
	int numelements; // Number of elements actually stored in the structure.
	int size; // Allocated memory (size of the hash table).

	/**
	 * Constructor: initializes numelements, size and initial table size.
	 */
	public StringSet() {
		numelements = 0;
		size = 100;
		table = new StringNode[size];
	}

	/*
	 * inserts a new key into the set. Inserts it at the head of the linked list
	 * given by its hash value.
	 */
	public void insert(String key) {
		if (find(key)) {
			return;
		}
		if (numelements == size) {
			StringNode[] oldTable = table.clone();
			table = new StringNode[size * 2 + 1];
			size = size * 2 + 1;
			numelements = 0;
			for (int i = 0; i < oldTable.length; i++) {
				for (StringNode cursor = oldTable[i]; cursor != null; cursor = cursor.getNext()) {
					insert(cursor.getKey());
				}
			}
			insert(key);
			//For garbage collection:
			oldTable = null; 
			return;
		}
		int h = hash(key);
		StringNode cursor = table[h];
		if (cursor == null) {
			table[h] = new StringNode(key, null);
		} else {
			for (; cursor.getNext() != null; cursor = cursor.getNext());
			cursor.setNext(new StringNode(key, null));
		}
		numelements++;
	}

	/*
	 * finds if a String key is present in the data structure. Returns true if
	 * found, else false.
	 */
	public boolean find(String key) {
		int h = hash(key);
		StringNode cursor = table[h];
		if (cursor == null) {
			return false;
		} else {
			for (; cursor != null; cursor = cursor.getNext()) {
				if (cursor.getKey().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Prints the contents of the hash table.
	 */
	public void print() {
		for (int i = 0; i < table.length; i++) {
			for (StringNode cursor = table[i]; cursor != null; cursor = cursor.getNext()) {
				System.out.println(cursor.getKey());
			}
		}
	}

	/*
	 * The hash function that returns the index into the hash table for a string k.
	 */
	private int hash(String k) {
		int h = 0;
		int x = 47777;
		int m = 81401;
		for (int i = 0; i < k.length(); i++) {
			h = (h * x + (int) k.charAt(i)) % m;
			if (h < 0) {
				// account for computer scientists mistakenly calling
				// "%" modulus ;)
				h += m;
			}
		}
		return h % size;
	}

}
