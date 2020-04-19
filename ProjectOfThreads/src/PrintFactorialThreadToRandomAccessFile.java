import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintFactorialThreadToRandomAccessFile {
	private static Factorial factorialcalc = new Factorial();
	public static final String F_NAME = "1.dat";
	private static RandomAccessFile f;

	public static void main(String[] args) throws IOException, InterruptedException {
		// Create a thread pool with two
		
		// threads
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 1; i < 10; i++) {
			executor.execute(new CalcTheFactorial(i));
			executor.execute(new PrintDataTask());
				Thread.sleep(100);
		
		}
		executor.shutdown();
		
		System.out.println("Finish the process.");
		
		f.seek(0);
		System.out.println(f.readLong());
		f.close();
	}

	// A task for adding an amount to the account
	public static class CalcTheFactorial implements Runnable {
		private int index;

		public void run() {
			// Purposely delay it to let the withdraw method proceed
			factorialcalc.Calc(index);
		}

		public CalcTheFactorial(int index) {
			this.index = index;
		}
	}

	
	// A task for subtracting an amount from the account
	public static class PrintDataTask implements Runnable {

		public void run() {

			try {
		
				factorialcalc.PrintTheFacto();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// An inner class for account
	private static class Factorial { // Create a new lock
		private Lock lock = new ReentrantLock();
		// Create a condition
		private Condition newDeposit = lock.newCondition();
		private int index = 1;
		private int factorial = 1;
		private static boolean flag = true;

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getFactorial() {
			return factorial;
		}

		public void PrintTheFacto() throws IOException {
			lock.lock(); // Acquire the lock
			try {

				while (getIndex() < 10) {
//				System.out.println(getIndex());
					newDeposit.await();
				}
				if(isFlag() == true) {
					System.out.println("the factorial is:" + getFactorial()); //Print to console
					 f = new RandomAccessFile(F_NAME, "rw");
					f.seek(0);
					f.writeLong(getFactorial()); // Write into data file the factorial
					
					setFlag(false);
				}

			} catch (InterruptedException ex) {
				ex.printStackTrace();
			} finally {
				lock.unlock();
			} // Release the lock
		}

		public void Calc(int amount) {
			lock.lock(); // Acquire the lock
			try {
				int num = 1 + getIndex();
				setIndex(num);
				factorial *= amount;

				// Signal thread waiting on the condition
				newDeposit.signalAll();
			} finally {
				lock.unlock();
			} // Release the lock
		}
	}
}
