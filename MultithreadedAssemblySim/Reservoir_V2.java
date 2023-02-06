
public class Reservoir_V2 {
	
	LockFreeLinkedList<Object> partLs;
	int p;
	
	public Reservoir_V2(int maxCapacity) {
		partLs = new LockFreeLinkedList<Object>(maxCapacity);
	}
	
	public Object retrievePart() throws InterruptedException {
		return partLs.removeLast();
	}
	
	public void generatePart() throws InterruptedException {
		partLs.addNode(new Object());
	}
}
