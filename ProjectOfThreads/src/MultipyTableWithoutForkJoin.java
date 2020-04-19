
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultipyTableWithoutForkJoin {

	public static final int AVAILABLE_PROCCESSORS = Runtime.getRuntime().availableProcessors();
	public static final int n = 10;
	public static final int threshold = n / AVAILABLE_PROCCESSORS;
	public static boolean check = true;

	public static void main(String[] args) {
		int[][] multiplyResultMat = new int[10][10];
		
		ExecutorService e = Executors.newFixedThreadPool(AVAILABLE_PROCCESSORS);
		MultiTable[] mult = new MultiTable[AVAILABLE_PROCCESSORS];
		int i = 0;
		for (i = 0; i < mult.length - 1; i++) {
			e.execute(mult[i] = new MultiTable(i * threshold, (i + 1) * threshold, multiplyResultMat));// for on row (0,1),(1,2) ,..... row 1 row 2 row 3...
		}
		e.execute(mult[i] = new MultiTable(i * threshold, n, multiplyResultMat));// the last row ...
		e.shutdown();
		while (!e.isTerminated())
			;
		for (int j = 0; j < multiplyResultMat.length; j++) {
			for (int k = 0; k < multiplyResultMat[0].length; k++) {
				System.out.printf("%-4s ", multiplyResultMat[j][k]);
			}
			System.out.println();
		}

	}

		
	
//	public static double[][] multiplyMatrix(int numberOfThreads, double[][] m1, double[][] m2) {
//		double[][] result = new double[m1.length][m2[0].length];
//		// Create a fixed thread pool with the specified number of threads
//		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
//		// Submit runnable tasks to the executor
//		int rowsInEachThread = m1.length / numberOfThreads;
//		int i = 0;
//		for (; i < numberOfThreads - 1; i++) {
//			executor.execute(new Task(i * rowsInEachThread, (i + 1) * rowsInEachThread, m1, m2, result));
//		}
//		executor.execute(new Task(i * rowsInEachThread, m1.length, m1, m2, result));
//		// The resulting rows for the last thread
//		// Shut down the executor
//		executor.shutdown();
//		// Wait until all tasks are finished
//		while (!executor.isTerminated()) {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException ex) {
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
	
//	private int startRow, endRow;
//	private double[][] m1;
//	private double[][] m2;
//	private double[][] result;
//
//	Task(int startRow, int endRow, double[][] m1, double[][] m2, double[][] result) {
//		this.startRow = startRow;
//		this.endRow = endRow;
//		this.m1 = m1;
//		this.m2 = m2;
//		this.result = result;
//	}
//
//	public void run() {
//		for (int i = startRow; i < endRow; i++)
//			for (int j = 0; j < result.length; j++)
//				for (int k = 0; k < result[0].length; k++)
//					result[i][j] += m1[i][k] * m2[k][j];
//	}
	
	
	public static class MultiTable implements Runnable {
		private int low;
		private int high;
		private int[][] result;

		public MultiTable(int low, int high, int[][] result) {
			this.low = low;
			this.high = high;
			this.result = result;
		}

		@Override
		public void run() {

			
			for (int i = low + 1; i <= high; i++) {
				for (int j = 1; j <= 10; j++) {
					result[i-1][j-1] = i * j;		
				}
			}
		}

	}

}
