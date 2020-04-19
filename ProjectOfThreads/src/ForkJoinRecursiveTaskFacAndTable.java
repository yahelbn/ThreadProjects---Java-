import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ForkJoinRecursiveTaskFacAndTable {
	public static final int AVAILABLE_PROCCESSORS = Runtime.getRuntime().availableProcessors();
	public static final int n = 10;
	public static final int threshold = n / AVAILABLE_PROCCESSORS;

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		FactorialRecursiveTask factorialTask = new FactorialRecursiveTask(n);
		long result = pool.invoke(factorialTask);
		System.out.printf("%d Factorail: %d\n", n, result);
		System.out.printf("\n1 to %d Multiplication Table:\n", n);
		MultiplicationTableRecursiveAction multTableAction = new MultiplicationTableRecursiveAction(n);
		pool.invoke(multTableAction);
	}
}

class FactorialRecursiveTask extends RecursiveTask<Long> {
	/**
	 *  */
	private static final long serialVersionUID = 1L;
	private int high;
	private int low;

	public FactorialRecursiveTask(int n) {
		this(n, 1);
	}

	public FactorialRecursiveTask(int high, int low) {
		this.high = high;
		this.low = low;
	}

	@Override
	protected Long compute() {
		if (high - low < ForkJoinRecursiveTaskFacAndTable.threshold) {
			long factorial = 1;
			for (int i = low; i <= high; i++) {
				factorial *= i;
			}
			return factorial;
		}
		int middle = (high + low) / 2;
		RecursiveTask<Long> t1 = new FactorialRecursiveTask(middle, low);
		RecursiveTask<Long> t2 = new FactorialRecursiveTask(high, middle + 1);
		invokeAll(t1, t2);
		return t1.join() * t2.join();
	}
}

class MultiplicationTableRecursiveAction extends RecursiveAction {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private int high;
	private int low;

	public MultiplicationTableRecursiveAction(int lastRow) {
		this(lastRow, 1);
	}

	public MultiplicationTableRecursiveAction(int high, int low) {
		this.high = high;
		this.low = low;
	}

	@Override
	protected void compute() {
		if (high - low < ForkJoinRecursiveTaskFacAndTable.threshold) {
			for (int i = low; i <= high; i++) {
				for (int j = 1; j <= 10; j++) {
					System.out.printf("%-4s ", i * j);
				}
				System.out.println();
			}
			return;
		}
		int mid = (high + low) / 2;
		RecursiveAction t1 = new MultiplicationTableRecursiveAction(mid, low);
		RecursiveAction t2 = new MultiplicationTableRecursiveAction(high, mid + 1);
		invokeAll(t1);
		invokeAll(t2);
	}
}
