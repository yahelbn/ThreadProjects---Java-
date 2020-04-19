import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FactorialThreadToConsoleByExecute {
	private static Factorial Factorial = new Factorial();
	
	private static int factorial = 1;
	final static int numForFact=10;

	

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		// Create and launch 100 threads
		for (int i = 1; i <= numForFact; i++) {
			executor.execute(new Multiply(i));
			Thread.sleep(10);
		}
		executor.execute(new printFactorialToConsole());
		executor.shutdown();

		
		// Wait until all tasks are finished
		while (!executor.isTerminated()) {
		}
		System.exit(0);
		
	}

	// A thread for adding a penny to the Factorial
	public static class Multiply implements Runnable {
		private int num;

		public void run() {
			try {

				Factorial.MultiplyRow(num);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public Multiply(int index) {
			this.num = index;
		}

	}

	public static class printFactorialToConsole implements Runnable {
	

		public void run() {
			try {

				Factorial.printFactorial();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	
	}

	// An inner class for Factorial
	public static class Factorial {
		private static Lock lock = new ReentrantLock(); // Create a lock
		private Condition condition = lock.newCondition();
		private static int counter;
		private static int currentNum;
		

		public static int getCurrentNum() {
			return currentNum;
		}



		public static void setCurrentNum(int currentNum) {
			Factorial.currentNum = currentNum;
		}



		public void MultiplyRow(int num) throws InterruptedException {
			lock.lock(); 
			
			setCurrentNum(num);
			factorial *= num;
		
			condition.signalAll();
			lock.unlock();
		}
		
		
		
		public void printFactorial() throws InterruptedException {
			lock.lock(); 
			while(currentNum<numForFact) {
				condition.await();
			}
			System.out.println("The factorial is:" + factorial);
			
			lock.unlock();
		}
		

		
		
		
	}
}
