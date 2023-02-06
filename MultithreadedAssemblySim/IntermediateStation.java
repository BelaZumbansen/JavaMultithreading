
public abstract class IntermediateStation {
	
	BlockingLinkedList<Object> completedPartLs;
	
	public IntermediateStation(int maxCapacity) {
		completedPartLs = new BlockingLinkedList<Object>(maxCapacity);
	}
	
	public abstract void generatePart() throws InterruptedException;
	
	public Object retrieveIntermediatePart() throws InterruptedException {
		return completedPartLs.removeLast();
	}
}
