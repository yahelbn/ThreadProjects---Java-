
public class MultipleNumbers implements Runnable {
	private int mult;
	public MultipleNumbers() {
		mult=1;
	}

	@Override
	public void run() {
		for (int i = 1; i <= 10; i++) {
			mult *= i;
			System.out.print("Thread2: "+mult + "\n");
			
		}
	}

}
