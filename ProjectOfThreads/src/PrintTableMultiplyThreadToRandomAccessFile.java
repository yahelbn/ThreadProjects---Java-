import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PrintTableMultiplyThreadToRandomAccessFile {

	private static RowOfTable RowOfTable = new RowOfTable();
	private static int num = 1;
	public static final String F_NAME = "1.dat";

	public static int Table[][] = new int[10][10];
	private static RandomAccessFile f;
	private static boolean flag3=true;
	
	

	public static boolean isFlag3() {
		return flag3;
	}

	public static void setFlag3(boolean flag3) {
		PrintTableMultiplyThreadToRandomAccessFile.flag3 = flag3;
	}

	public static int getNum() {
		return num;
	}

	public static void setNum(int numOfRow) {
		PrintTableMultiplyThreadToRandomAccessFile.num = numOfRow + 1;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		ExecutorService executor = Executors.newCachedThreadPool();
		// Create and launch 100 threads
		for (int i = 1; i <= 10; i++) {
			executor.execute(new Multiply(i));  // create array of any row after multiply by i.
			Thread.sleep(1000);
			executor.execute(new PrintMat()); // Print every row immediately after make her.
			executor.execute(new BuildMatrix()); //Build matrix by taking every row and add it to the big matrix.
			executor.execute(new PrintMatrixToRandom()); // Write the big matrix into RandomAccessFile.
		Thread.sleep(1000);
			//executor.execute(new ReadMatrix());
			Thread.sleep(1000);
		}
		executor.shutdown();
		// Wait until all tasks are finished
		while (!executor.isTerminated()) {
		}

		System.out.println();

		System.out.println("Print matrix after create her by row after row with threads:");
		System.out.println();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(Table[i][j] + " ");

			}
			System.out.println();
		}
		System.out.println();
		

	
		System.out.println();
		System.out.println("Print matrix after read it from the RandomAccessFile:");
		System.out.println();
	
		System.out.println();
		f.seek(0);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(f.readInt());
				System.out.print(f.readChar());

			}
			System.out.print(f.readUTF());
		}
		f.close();

	}

	
	public static class PrintMat implements Runnable {

		@Override
		public void run() {
			try {
				RowOfTable.PrintRow();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static class BuildMatrix implements Runnable {

		@Override
		public void run() {
			try {
				RowOfTable.addRow();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			this.row = index;
		}

	}

	public static class PrintMatrixToRandom implements Runnable {

		public void run() {
			try {

				RowOfTable.PrintMatrixToRandomAccessFile();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	


	// An inner class for RowOfTable
	public static class RowOfTable {
		private static Lock lock = new ReentrantLock(); // Create a lock

		private Condition condition = lock.newCondition();
		private Condition condition2 = lock.newCondition();
		
		private int[] row;
		private boolean flag = true;
		private boolean flag2=true;
		
		private int currentRowAdding;

		public int getCurrentRowAdding() {
			return currentRowAdding;
		}

		public void setCurrentRowAdding(int currentRowAdding) {
			this.currentRowAdding = currentRowAdding;
		}

		public int[] getRow() {
			return row;
		}

		public void setRow(int[] row) {
			this.row = row;
		}

		public void MultiplyRow(int numOfRow) throws InterruptedException {

			// create 1 row every run

			lock.lock();
			int[] arr = new int[10];
			int i = 0;
			int j = 1;
			while (j <= 10) {
				arr[i] = numOfRow * j;
				setRow(arr);
				                                // [1,2,3,4,5,6,7,8,9,10]
				i++;
				j++;
			}

			flag = false;
			System.out.println();

			condition.signalAll();
			lock.unlock();

		}

		public void PrintRow() throws InterruptedException, IOException {
			lock.lock();
			try {

			while (flag == true) {
				condition.await();
			}
			// print 1 row to console

			for (int i = 0; i < 10; i++) {
				System.out.print(row[i] + " ");
			}

			// Print to random access file
			}finally {
				lock.unlock();

			}

		}

		public void addRow() throws InterruptedException, IOException {
			lock.lock();
			try {
				while (flag == true) {
					condition.await();
				} // print 1 row to console

				for (int j = 0; j < 10; j++) {
					Table[currentRowAdding][j] = row[j];
				}

				// Print to random access file
				int current;
				current = getCurrentRowAdding() + 1;
				setCurrentRowAdding(current);

				condition2.signalAll();
			} finally {
				lock.unlock();

			}
		}

		public void PrintMatrixToRandomAccessFile() throws InterruptedException, IOException {

			lock.lock();
			try {
				while (currentRowAdding < 10) {
					condition2.await();
				} // print 1 row to console
				f = new RandomAccessFile(F_NAME, "rw");
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						f.writeInt(Table[i][j]);
						f.writeChar(' ');

					}
					f.writeUTF("\n");
				}
				flag2=false;
				condition2.signalAll();
			} finally {
				lock.unlock();
			}
			// Print to random access file

		}
		
		


	}
}
