
public abstract class IntermediateStation_V2 {
	
	LockFreeLinkedList<Object> completedPartLs;
	
	public IntermediateStation_V2(int maxCapacity) {
		completedPartLs = new LockFreeLinkedList<Object>(maxCapacity);
	}
	
	public abstract void generatePart() throws InterruptedException;
	
	public Object retrieveIntermediatePart() throws InterruptedException {
		return completedPartLs.removeLast();
	}
}
