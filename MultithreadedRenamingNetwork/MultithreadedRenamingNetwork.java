import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedRenamingNetwork {
	
	/** 
	 * Thread which will send itself through the renaming network the specified number of times
	 */
	private static class RenameMyselfThread extends Thread {
		
		/** Current id of this thread */
		private int threadId;
		
		public RenameMyselfThread() {
			threadId = -1;
		}
		
		@Override
		public void run() {
			
			int i = 1;
			
			// Complete the renaming process n times
			while (i <= n) {
				
				int prevId = threadId;
				
				// Visit the renaming network and allow it to give you a new ID
				threadId = renameNetwork.visit(threadId);
				
				// Atomically increment A[id]
				A[threadId].incrementAndGet();
				i++;
				// Generate a random number between 0 and 10 to sleep for
				long random = (long) (Math.random() * 10.0);
				try {
					Thread.sleep(random);
				} catch (InterruptedException e) {
					System.out.println("Failed to sleep for the alloted time period.");
				}
			}
		}
	}
	
	/** Number of renaming rounds each thread should go through */
	private static int n;
	
	/** Number of threads */
	private static int p;
	
	/** Integer array to keep track of how often each id has been used */
	private static AtomicInteger[] A;
	
	/** The renaming network we will utilize */
	private static RenameNetwork renameNetwork;
	
	/** Running threads */
	private static ArrayList<RenameMyselfThread> activeThreads;
	
	/**
	 * Process command line arguments.
	 * 
	 * @param args Command line arguments
	 * @return {@code true} if the arguments match the program requirements
	 */
	private static boolean opts(String[] args) {
		
		if (args.length != 2) {
			System.out.println("Please pass the correct number of parameters.");
			return false;
		}
		
		p = Integer.parseInt(args[0]);
		
		if (p < 1) {
			System.out.println("Number of threads must be greater than or equal to 1.");
			return false;
		}
		
		n = Integer.parseInt(args[1]);
		
		if (n < 1) {
			System.out.println("Number of renaming rounds must be greater than or equal to 1.");
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		
		if (!opts(args)) {
			return;
		}
		
		// Initalize the list of threads
		activeThreads = new ArrayList<>();
		
		// Initialize the renaming network which is dependent on the number of threads
		renameNetwork = new RenameNetwork(p);
		
		// Initialize the integer array with the range of ids defined by the renaming network
		A = new AtomicInteger[renameNetwork.getUpperBound()];
		
		for (int i = 0; i < A.length; i++) {
			A[i] = new AtomicInteger(0);
		}
		
		for (int i = 0; i < p; i++) {
			RenameMyselfThread newThread = new RenameMyselfThread();
			activeThreads.add(newThread);
		}
		
		// Start all threads
		for (RenameMyselfThread t : activeThreads) {
			t.start();
		}
		
		// Wait for all threads to finish
		for (RenameMyselfThread t : activeThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {}
		}
		
		boolean success = true;
		int sum = 0;
		
		// Ensure that the renaming network worked as described
		for (int i = 0; i < A.length; i++) {
			
			int val = A[i].get();
			
			if (val > n) {
				success = false;
			}
			
			sum+=val;
		}
		
		if (success && sum == (p*n)) {
			System.out.println("Conditions have been met. Success!");
		} else {
			System.out.println("Failure :(");
		}
	}
}
