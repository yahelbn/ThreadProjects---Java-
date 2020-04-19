
public class printb implements Runnable{
	char b ;

	public printb() {
		b = 'b';
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println("Thread b: "+b);
			Thread.yield();
		/*	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
		System.out.println();
	}
}
