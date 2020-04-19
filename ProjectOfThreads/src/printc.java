public class printc implements Runnable{
	char c ;

	public printc() {
	c='c';
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println("Thread c: "+c );
		
		}
		System.out.println();
	}
}