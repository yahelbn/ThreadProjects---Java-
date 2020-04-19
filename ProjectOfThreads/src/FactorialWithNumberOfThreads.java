import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FactorialWithNumberOfThreads {
	
	public static boolean flag = false;
	public static long result = 1;
	private static Lock lock = new ReentrantLock();
	private static Condition cond = lock.newCondition();
	
	
	
	public static void main(String[] args) {
		int np = Runtime.getRuntime().availableProcessors();
		System.out.println("Num of Proccessors is : " + np);
		int n = 20;
		int j = 0;
		int[] arr = new int[n];
		
		for (int i = 1; i <= 20; i++) {
			arr[j] = i;
			j++;
		}
		
		for (int numberOfThreads = 1; numberOfThreads <= 1; numberOfThreads++) {
			long startTime = System.currentTimeMillis();
			result = factorialForTest(numberOfThreads, arr);
			long endTime = System.currentTimeMillis();
			
			System.out.println("\nTime with " + numberOfThreads + " threads: " + (endTime - startTime) + " ms.");
			
			
		}

	}
	
	
public static long factorialForTest(int numberOfThreads, int[] arr) {
	long resultSum[] = new long[numberOfThreads];
	ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
	int elementsInEachThread = arr.length / numberOfThreads;
	int i = 0;
	for (; i < numberOfThreads - 1; i++) {
		executor.execute(new factorialRunnable(i * elementsInEachThread, (i+1)* elementsInEachThread , arr , resultSum, i));
	}
	executor.execute(new factorialRunnable(i * elementsInEachThread, arr.length , arr , resultSum, i));
	executor.execute(new PrintSum(numberOfThreads));
	executor.shutdown();
	while(!executor.isTerminated()) {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	return result;
}	

public static class factorialRunnable implements Runnable{
	private int startRow, endRow, index;
	private int[] arr;
	private long[] arrRes;
	
	public factorialRunnable(int startRow, int endRow, int[] arr, long[] arrRes, int index) {
		this.startRow = startRow;
		this.endRow = endRow;
		this.arr = arr;
		this.arrRes = arrRes;
		this.index = index;
	}
	
	@Override
	public void run() {
		lock.lock();
		try {
			arrRes[index] = 1;
			for (int i = startRow; i < endRow; i++) {
				FactorialWithNumberOfThreads.result *= arr[i];
			}
			flag = true;
			cond.signalAll();
		} finally {
			lock.unlock();
		}
		
	}
	
}
public static class PrintSum implements Runnable{
	private int i;
	
	public PrintSum(int n) {
		this.i = n;
	}
	@Override
	public void run() {
		Thread.currentThread().setName("T" + i);
		i++;
		lock.lock();
		
		try {
			if(!flag)
				cond.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			System.out.println(Thread.currentThread().getName() + ": " + result);
		} finally {
			lock.unlock();
		}
		
	}
	
}
}