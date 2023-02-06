import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenameNetwork {
	
	/** Array of Booleans denoting the availability of a particular id */
	private AtomicBoolean[] ids;
	
	/** Upper bound of the integer range available to be used as ids*/
	private int upperBound;
	
	/** Random object for the network */
	private Random random;
	
	public RenameNetwork(int numThreads) {
		
		// Allow a range of numThreads*2 for the available ids
		upperBound = numThreads*2;
		
		// Initialize the availability array to all ids being available
		ids = new AtomicBoolean[upperBound];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = new AtomicBoolean(false);
		}
		random = new Random();
	}
	
	/**
	 * Visit the renaming network to gain a new unique id and free the previous id associated with the
	 * visiting thread.
	 */
	public int visit(int prevId) {
		
		int searchId;
		
		// Loop infinitely, due to the size of our range the thread will always eventually find a viable id
		while (true) {
			
			// Generate a random int between 0 (inclusive) and upperBound (exclusive)
			searchId = random.ints(0, upperBound)
					.findFirst()
					.getAsInt();
			
			// If the id is available, atomically set it to unavailable and assign this id to the visiting thread
			if (ids[searchId].compareAndSet(false, true)) {
				
				// Set the previous id of the thread as available
				if (prevId != -1) {
	 				ids[prevId].set(false);
				}
				return searchId;
			}
		}
	}
	
	public int getUpperBound() {
		return upperBound;
	}
}
