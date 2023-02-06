
import java.util.concurrent.atomic.AtomicBoolean;

public class Node {
	
	private volatile Node next;
	private int  nodeID;
	private AtomicBoolean deleted;
	
	public Node (int nodeID) {
		this.nodeID = nodeID;
		this.deleted = new AtomicBoolean(false);
	}
	
	public String toString() {
		return "" + nodeID;
	}
	
	public Node next() {
		return next;
	}
	
	public void setNext(Node next) {
		this.next = next;
	}
	
	public boolean isEqual(Node comparison) {
		return nodeID == comparison.nodeID;
	}
	
	public boolean isDeleted() {
		return deleted.get();
	}
	
	public void delete() {
		deleted.set(true);
	}
}

