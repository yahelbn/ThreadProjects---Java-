import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




public class Table100PrintRowRow {
	
private static RowOfTable RowOfTable = new RowOfTable();
private static int numOfRow=1;

	public static int getNumOfRow() {
	return numOfRow;
}

public static void setNumOfRow(int numOfRow) {
	Table100PrintRowRow.numOfRow = numOfRow+1;
}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		// Create and launch 100 threads
		for (int i = 1; i <= 10; i++) {
			executor.execute(new Multiply(i));
			Thread.sleep(10);	
			
		}
		executor.shutdown();
		// Wait until all tasks are finished
		while (!executor.isTerminated()) {
		}
		
	}

	// A thread for adding a penny to the RowOfTable
	public static class Multiply implements Runnable {
		private int row;
		public void run() {
			try {
				
				RowOfTable.MultiplyRow(row);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public Multiply(int index) {
		this.row=index;
		}
		
	}

	// An inner class for RowOfTable
	public static class RowOfTable {
		private static Lock lock = new ReentrantLock(); // Create a lock
		private Condition condition = lock.newCondition();

		public void MultiplyRow(int numOfRow) throws InterruptedException {
			lock.lock(); // Acquire the lock
			
				int j=1;
			while(j<=10) {
				System.out.print((numOfRow*j)+" ");
				j++;	
			}
			
			System.out.println();
			

			
			lock.unlock();
		
			
			
			
		
		}
	}
}
