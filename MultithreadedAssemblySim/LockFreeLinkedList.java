import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeLinkedList<T> {
	
	public class Node {
		
		T obj;
		AtomicReference<Node> prev;
		AtomicBoolean markedPointer;
		
		public Node (T obj) {
			this.obj = obj;
			this.prev = new AtomicReference<Node>();
			this.markedPointer = new AtomicBoolean(false);
		}
	}
	
	private int maximumCapacity;
	private AtomicReference<Node> tail;
	private AtomicInteger size;
	private AtomicInteger emptySlots;
	
	public LockFreeLinkedList (int maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
		
		tail = new AtomicReference<Node>();
		size = new AtomicInteger(0);
		emptySlots = new AtomicInteger(maximumCapacity);
	}
	
	public void addNode(T newObj) throws InterruptedException {
		
		if (maximumCapacity != -1) {
			
			int curSlots;
			do {
				do {
					curSlots = emptySlots.get();
				} while (curSlots <= 0 && !Thread.currentThread().isInterrupted());
			} while(!emptySlots.compareAndSet(curSlots, curSlots - 1));
		}
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		Node newNode = new Node(newObj);
		Node curTail = null;
		boolean continueGoing = false;
		
		do {
			
			continueGoing = false;
			
			curTail = tail.get();
			
			if (curTail != null) {
				
				if (isLogicallyDeleted(curTail)) {
					continueGoing = true;
					continue;
				}
			}
			
			newNode.prev.set(curTail);
			continueGoing = !tail.compareAndSet(curTail, newNode);
		} while (continueGoing && !Thread.currentThread().isInterrupted());
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		size.incrementAndGet();
	}
	
	public boolean isLogicallyDeleted(Node node) {
		return node.markedPointer.get();
	}
	
	public boolean logicallyDelete(Node node) {
		return node.markedPointer.compareAndExchange(false, true);
	}
	
	public T removeLast() throws InterruptedException {
		
		int curSize;
		do {
			do {
				curSize = size.get();
			} while (curSize <= 0 && !Thread.currentThread().isInterrupted());
		} while(!size.compareAndSet(curSize, curSize - 1) && !Thread.currentThread().isInterrupted());
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		Node rtn     = null;
		Node newTail = null;
		boolean continueGoing = false;
		
		do {
			
			continueGoing = false;
			
			rtn = tail.get();
			
			if (rtn == null || !logicallyDelete(rtn)) {
				continueGoing = true;
				continue;
			}
			
			newTail = rtn.prev.get();
			continueGoing = !tail.compareAndSet(rtn, newTail);
			
		} while (continueGoing && !Thread.currentThread().isInterrupted());
		
		if (maximumCapacity != -1) {
			emptySlots.incrementAndGet();
		}
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		return rtn.obj;
	}
}
