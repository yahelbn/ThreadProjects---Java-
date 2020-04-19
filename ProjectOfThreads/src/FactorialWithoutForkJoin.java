
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FactorialWithoutForkJoin {

	public static final int AVAILABLE_PROCCESSORS = Runtime.getRuntime().availableProcessors();
	public static final int n = 5;
	public static final int threshold = n / AVAILABLE_PROCCESSORS;

	public static void main(String[] args) {
		int resultOfFactorial = 1;
		//System.out.println(AVAILABLE_PROCCESSORS);
		ExecutorService e = Executors.newFixedThreadPool(AVAILABLE_PROCCESSORS);
		Factorial[] fuckingShit = new Factorial[AVAILABLE_PROCCESSORS];
		int i = 0;
		for (i = 0; i < AVAILABLE_PROCCESSORS - 1; i++) {
			e.execute(fuckingShit[i] = new Factorial(i * threshold, (i + 1) * threshold));
		}
		e.execute(fuckingShit[i] = new Factorial(i * threshold, n));
		e.shutdown();
		while (!e.isTerminated());
		for (int j = 0; j < fuckingShit.length; j++) {
			resultOfFactorial *= fuckingShit[j].getResult();
		}
		
		System.out.println(resultOfFactorial);

	}

	public static class Factorial implements Runnable {
		private int low;
		private int high;
		private int result = 1;

		public Factorial(int low, int high) {
			this.low = low;
			this.high = high;
		}

		@Override
		public void run() {
			for (int i = low + 1; i <= high; i++) {
				result *= i;
			}
		}

		public int getResult() {
			return result;
		}
	}

}
