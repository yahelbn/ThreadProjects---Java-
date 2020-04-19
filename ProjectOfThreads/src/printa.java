
public class printa implements Runnable {
	char a ;

	public printa() {
		a = 'a';
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println("Thread a: "+a);
		}
		
		System.out.println();
	}

}
