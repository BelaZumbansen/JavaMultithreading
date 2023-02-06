
public class Reservoir {
	
	BlockingLinkedList<Object> partLs;
	int p;
	
	public Reservoir(int maxCapacity) {
		partLs = new BlockingLinkedList<Object>(maxCapacity);
	}
	
	public Object retrievePart() throws InterruptedException {
		return partLs.removeLast();
	}
	
	public void generatePart() throws InterruptedException {
		partLs.addNode(new Object());
	}
}
