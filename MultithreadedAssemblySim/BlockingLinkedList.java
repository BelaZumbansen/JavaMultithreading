import java.util.concurrent.Semaphore;

public class BlockingLinkedList<T> {
	
	public class Node {
		
		T obj;
		Node prev;
		
		public Node (T obj) {
			this.obj = obj;
			this.prev = null;
		}
	}
	
	private Semaphore empty;
	private Semaphore full;
	private Semaphore mutex;
	private Node tail;
	int maximumCapacity;
	
	public BlockingLinkedList (int maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
		
		full = new Semaphore(0);
		if (maximumCapacity != -1) {
			empty = new Semaphore(maximumCapacity);
		} else {
			empty = null;
		}
		mutex = new Semaphore(1);
		tail = null;
	}
	
	public void addNode(T newObj) throws InterruptedException {
	
		// Thread waits if the linked list is at full capacity
		if (maximumCapacity != -1) {
			empty.acquire();
		}
		
		// Thread acquires lock for altering the list
		mutex.acquire();
		
		// Thread appends a new node to the end of the linked list
		Node newNode = new Node(newObj);
		
		newNode.prev = tail;
		tail = newNode;
		
		// Release list lock
		mutex.release();
		// Register the addition of a new element, a thread waiting on full will be woken
		full.release();
	}
	
	public T removeLast() throws InterruptedException {
		
		// Thread waits if linked list is empty
		full.acquire();
		
		// Thread acquires lock for altering the list
		mutex.acquire();
		
		// Thread retrieves tail and updates tail to the second last element
		T rtn = tail.obj;
		tail = tail.prev;
		
		// Thread releases lock on the list
		mutex.release();
		
		// Register the deletion of an element if necessary
		if (maximumCapacity != -1) {
			empty.release();
		}
		
		return rtn;
	}
}
