

public class LockFreeLinkedList {
	
	/**
	 * Thread class which traverses the Linked List printing the elements in order.
	 */
	private static class TraverserThread extends Thread {
		
		/** The Node the traverser is currently on */
		private Node curNode;
		
		@Override
		public void run() {
			
			// Begin the traversal at the head
			curNode = head;
			
			do {
				
				// Make sure the node you are currently on was not deleted prior to the iteration
				while (curNode.isDeleted()) {
					
					// If the node was deleted, move on to the next
					curNode = curNode.next();
				}
				
				// Print out the node
				System.out.println(curNode.toString());
				
				// Attempt to sleep the thread for 50ms
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
				
				// Move the pointer forwards
				curNode = curNode.next();
			} while (!isHead(curNode) || keepLooping);
			// Continue this loop until the looping variable is false and we have reached the head
		}
	}
	
	/**
	 * Thread class which traverses the Linked List and removes elements under the correct conditions
	 */
	private static class RemoverThread extends Thread {
		
		/** Prior node as required for the removal algorithm*/
		private Node prior;
		
		/** Current node in the traversal */
		private Node curNode;
		
		@Override
		public void run() {
			
			// Begin the traversal at the head
			curNode = head;
			
			do {
				
				// Sleep the thread for 10ms
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				
				// Check whether the current node is the head
				if (isHead(curNode)) {
					
					prior = null;
					
					// Continue the traversal
					curNode = curNode.next();
				} else {
					
					// Generate a random number between 0.0 and 1
					double random = Math.random();
					
					// 40% chance
					if (random < 0.4) {
						
						if (prior == null) {
							prior = curNode;
						}
						
						// Continue the traversal
						curNode = curNode.next();
					} 
					// 10% chance 
					else if (random < 0.5) {
						
						if (prior != null) {
							
							
							Node toDelete = prior.next();
							
							// Delete all nodes between prior and current
							while (!toDelete.isEqual(curNode)) {
								toDelete.delete();
								toDelete = toDelete.next();
							}
							
							prior.setNext(toDelete);
							System.out.println("Cutting from " + prior.toString() +  " to " + curNode.toString());
							
							curNode = curNode.next();
						}
					}
				}
			} while (!isHead(curNode) || keepLooping);
			// Continue this loop until the looping variable is false and we have reached the head
		}
	}
	
	/**
	 * Thread class which traverses the Linked List and adds elements at a k percentage chance
	 */
	private static class AdderThread extends Thread {
		
		private Node curNode;
		
		@Override
		public void run() {
			
			// Begin the traversal at the head 
			curNode = head;
			
			do {
				
				// Sleep the thread for 10ms
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				
				// Generate a random number between 0.0 < k < 1
				double random = Math.random();
				
				// (k*100)% chance
				if (random < k) {
					
					// Generate new nodes
					Node newNode1 = new Node(nextID);
					nextID++;
					Node newNode2 = new Node(nextID);
					nextID++;
					Node newNode3 = new Node(nextID);
					nextID++;
					
					newNode1.setNext(newNode2);
					newNode2.setNext(newNode3);
					
					Node next = curNode.next();
					curNode.setNext(newNode1);
					
					while (next.isDeleted()) {
						next = next.next();
					}
					
					newNode3.setNext(next);
					
					curNode = curNode.next();
					
					System.out.println("Adding nodes after " + curNode.toString());
				} else {
					
					curNode = curNode.next();
					
					while (curNode.isDeleted()) {
						curNode = curNode.next();
					}
				}
			} while (!isHead(curNode) || keepLooping);
			// Continue this loop until the looping variable is false and we have reached the head
		}
	}
	
	/** Static variables */
	public  static Node   			head;
	private static double 			k;
	private static int    			nextID;
	private static volatile boolean keepLooping;
	
	
	// process command-line options
	public static void opts(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Please pass only one parameter. A double k such that 0.0 < k < 1");
		}
		
		k = Double.parseDouble(args[0]);
		
		if (k <= 0.0 || k >= 1) {
			System.out.println("Please pass parameter k such that 0.0 < k < 1");
		}
	}
	
	public static void main(String[] args) {
		
		//process options
		opts(args);
		
		keepLooping = true;
		
		constructLinkedList();
		
		TraverserThread traverserThread = new TraverserThread();
		AdderThread 	adderThread     = new AdderThread();
		RemoverThread   removerThread   = new RemoverThread();
		
		traverserThread.start();
		adderThread.start();
		removerThread.start();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		
		keepLooping = false;
		
		try {
			traverserThread.join();
		} catch (InterruptedException e) {}
		
		try {
			adderThread.join();
		} catch (InterruptedException e) {}
		
		try {
			removerThread.join();
		} catch (InterruptedException e) {}
		
		System.out.println("\n" + head.toString());
		
		Node next = head.next();
		
		while (!next.isEqual(head)) {
			System.out.println(next.toString());
			next = next.next();
		}
	}
	
	/**
	 * Construct the first 20 elements of the linked list as a circular list
	 */
	private static void constructLinkedList() {
		
		head = new Node(nextID);
		Node prevNd = head;
		nextID++;
		
		// Create the elements
		for (int i = 2; i <= 20; i++) {
			
			Node curNd = new Node(nextID);
			nextID++;
			prevNd.setNext(curNd);
			
			prevNd = curNd;
		}
		
		prevNd.setNext(head);
	}
	
	public static boolean isHead(Node checkNode) {
		return head.isEqual(checkNode);
	}
	
	public static int incrementId() {
		int rtn = nextID;
		nextID++;
		return rtn;
	}
}
