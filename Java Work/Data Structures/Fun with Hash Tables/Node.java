/**
 * Super-duper basic Node class!
 * @author Matthew Hefner
 *
 */
public class Node {
	private double xValue;
	private double yValue;
	private Node next;
	
	/**
	 * Constructor for a new node with a given value and given next node.
	 * @param _value
	 * @param _next
	 */
	public Node(double _xValue, double _yValue, Node _next) {
		xValue = _xValue;
		yValue = _yValue;
		next = _next;
	}

	public double getxValue() {
		return xValue;
	}



	public void setxValue(double xValue) {
		this.xValue = xValue;
	}



	public double getyValue() {
		return yValue;
	}



	public void setyValue(double yValue) {
		this.yValue = yValue;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}
}
