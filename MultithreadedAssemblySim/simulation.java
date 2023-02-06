import java.util.ArrayList;
import java.util.Random;

public class simulation {
	
	private static class IntermediateStation_1 extends IntermediateStation {

		public IntermediateStation_1(int maxCapacity) {
			super(maxCapacity);
		}

		@Override
		public void generatePart() throws InterruptedException {
			
			// Retrieve required parts
			reservoir2.retrievePart();
			intermediateStation2.retrieveIntermediatePart();
			
			// Register a new completed item
			completedPartLs.addNode(new Object());
		}
	}
	
	private static class IntermediateStation_2 extends IntermediateStation {

		public IntermediateStation_2(int maxCapacity) {
			super(maxCapacity);
		}

		@Override
		public void generatePart() throws InterruptedException {
			
			// Retrieve required parts
			reservoir1.retrievePart();
			
			// Register a new completed item
			completedPartLs.addNode(new Object());
		}
	}
	
	private static class IntermediateStation_3 extends IntermediateStation {

		public IntermediateStation_3(int maxCapacity) {
			super(maxCapacity);
		}

		@Override
		public void generatePart() throws InterruptedException {
			
			// Retrieve required parts
			intermediateStation2.retrieveIntermediatePart();
			intermediateStation4.retrieveIntermediatePart();
			
			// Register a new completed item
			completedPartLs.addNode(new Object());
		}	
	}
	
	private static class IntermediateStation_4 extends IntermediateStation {

		public IntermediateStation_4(int maxCapacity) {
			super(maxCapacity);
		}

		@Override
		public void generatePart() throws InterruptedException {
			
			// Retrieve required parts
			reservoir2.retrievePart();
			reservoir3.retrievePart();
			
			// Register a new completed item
			completedPartLs.addNode(new Object());
		}
	}
	
	private static class IntermediateStation_5 extends IntermediateStation {

		public IntermediateStation_5(int maxCapacity) {
			super(maxCapacity);
		}

		@Override
		public void generatePart() throws InterruptedException {
			
			// Retrieve required parts
			reservoir3.retrievePart();
			intermediateStation4.retrieveIntermediatePart();
			
			// Register a new completed item
			completedPartLs.addNode(new Object());
		}
	}
	
	private static class FinalProductAssemblyThread extends Thread {
		
		@Override
		public void run() {
			
			// If you have not created all the final products continue looping
			while (assembledProductCnt < k) {
				
				try {
					
					// Retrieve all required parts
					intermediateStation1.retrieveIntermediatePart();
					intermediateStation2.retrieveIntermediatePart();
					intermediateStation3.retrieveIntermediatePart();
					intermediateStation5.retrieveIntermediatePart();
				} catch (InterruptedException e) {
					return;
				}

				// New item has been created
				assembledProductCnt++;
			}
				
			run = false;
		}
	}
	
	private static class AssemblyStationThread extends Thread {
		
		/** Station */
		private IntermediateStation intermediateStation;
		private long generationRate;
		
		public AssemblyStationThread(IntermediateStation intermediateStation, long generationRate) {
			this.intermediateStation = intermediateStation;
			this.generationRate = generationRate;
		}
		
		@Override
		public void run() {
			
			while (run) {
				
				// Generate parts at a specified interval until the main thread sends an interrupt indicating that the process is complete
				try {
					intermediateStation.generatePart();
					Thread.sleep(generationRate);
				} catch (InterruptedException e) {
					// Exit thread
					return;
				}
			}
		}
	}
	
	private static class ReservoirThread extends Thread {
		
		/** Reservoir */
		private Reservoir reservoir;
		private long generationRate;
		
		public ReservoirThread(Reservoir reservoir, long generationRate) {
			this.reservoir = reservoir;
			this.generationRate = generationRate;
		}
		
		@Override
		public void run() {
			
			while (run) {
				
				// Generate parts at a specified interval until the main thread sends an interrupt indicating that the process is complete
				try {
					reservoir.generatePart();
					Thread.sleep(generationRate);
				} catch (InterruptedException e) {
					// Exit thread
					return;
				}
			}
		}
	}
	
	/*
	 * Execution variables
	 */
	private static float p;
	private static int c;
	private static int assembledProductCnt;
	private static int k;
	
	/*
	 * 3 Reservoirs 
	 */
	private static Reservoir reservoir1;
	private static Reservoir reservoir2;
	private static Reservoir reservoir3;
	
	/*
	 * 5 Intermediate stations
	 */
	private static IntermediateStation_1 intermediateStation1;
	private static IntermediateStation_2 intermediateStation2;
	private static IntermediateStation_3 intermediateStation3;
	private static IntermediateStation_4 intermediateStation4;
	private static IntermediateStation_5 intermediateStation5;
	
	private static volatile boolean run;
	
	/**
	 * Handle command line parameters
	 */
	public static boolean opts(String[] args) {
		
		try {
			
			if (args.length != 3) {
				System.out.println("Please pass 3 parameters as requested.");
				return false;
			}
			
			p = Float.parseFloat(args[0]);
			
			if (p <= 1 || p >= 30) {
				System.out.println("Please pass parameter p such that 1 < p < 30");
				return false;
			}
			
			c = Integer.parseInt(args[1]);
			
			k = Integer.parseInt(args[2]);
			
			if (k <= 1000) {
				System.out.println("Please pass parameter k such that k > 1000");
				return false;
			}
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		// Process options
		if (!opts(args)) {
			return;
		}
		
		assembledProductCnt = 0;
		run = true;
		
		// Initialize each intermediate station with the maximum capacity
		intermediateStation1 = new IntermediateStation_1(c);
		intermediateStation2 = new IntermediateStation_2(c);
		intermediateStation3 = new IntermediateStation_3(c);
		intermediateStation4 = new IntermediateStation_4(c);
		intermediateStation5 = new IntermediateStation_5(c);
		
		// Initialize each reservoir 
		reservoir1 = new Reservoir(-1);
		reservoir2 = new Reservoir(-1);
		reservoir3 = new Reservoir(-1);
		
		Random generator = new Random(1000);
		// Generate a random generation rate between 0 and p for each thread and initialize the thread
		long generationRate = (long) (generator.nextDouble()*p);
		AssemblyStationThread station1Thread = new AssemblyStationThread(intermediateStation1, generationRate);
		generationRate = (long) (generator.nextDouble()*p);
		AssemblyStationThread station2Thread = new AssemblyStationThread(intermediateStation2, generationRate);
		generationRate = (long) (generator.nextDouble()*p);
		AssemblyStationThread station3Thread = new AssemblyStationThread(intermediateStation3, generationRate);
		generationRate = (long) (generator.nextDouble()*p);
		AssemblyStationThread station4Thread = new AssemblyStationThread(intermediateStation4, generationRate);
		generationRate = (long) (generator.nextDouble()*p);
		AssemblyStationThread station5Thread = new AssemblyStationThread(intermediateStation5, generationRate);
		
		generationRate = (long) (generator.nextDouble()*p);
		ReservoirThread reservoir1Thread = new ReservoirThread(reservoir1, generationRate);
		generationRate = (long) (generator.nextDouble()*p);
		ReservoirThread reservoir2Thread = new ReservoirThread(reservoir2, generationRate);
		generationRate = (long) (generator.nextDouble()*p);
		ReservoirThread reservoir3Thread = new ReservoirThread(reservoir3, generationRate);
		
		// Initialize the Final Assembly thread
		FinalProductAssemblyThread finalAssemblyThread = new FinalProductAssemblyThread();
		
		// start timer
		long simulationStart = System.currentTimeMillis();
		
		// Start station threads
		station1Thread.start();
		station2Thread.start();
		station3Thread.start();
		station4Thread.start();
		station5Thread.start();
		
		// Start reservoir threads
		reservoir1Thread.start();
		reservoir2Thread.start();
		reservoir3Thread.start();
		
		// Start final assembly thread
		finalAssemblyThread.start();
		
		// Wait for Final assembly thread to complete
		try {
			finalAssemblyThread.join();
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		
		// Send an interrupt to each thread still executing
		station1Thread.interrupt();
		station2Thread.interrupt();
		station3Thread.interrupt();
		station4Thread.interrupt();
		station5Thread.interrupt();
		reservoir1Thread.interrupt();
		reservoir2Thread.interrupt();
		reservoir3Thread.interrupt();
		
		// Stop timer
		long simulationEnd = System.currentTimeMillis();
		
		System.out.println("Simulation with {p, c, k} = {" + p + ", " + c + ", " + k + "} was: " 
		+ (simulationEnd - simulationStart) + "ms");
	}	
}
