
public class SumOfNum implements Runnable {

	private int sum;
	
	public SumOfNum() {
		sum=0;
	}
	
	public void run() {
		for (int i = 0; i <= 10; i++) {
			sum += i;
			System.out.print("Thread1: "+sum+"\n");
		}
	}
	
}
