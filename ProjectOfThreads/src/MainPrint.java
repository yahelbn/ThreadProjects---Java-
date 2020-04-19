
public class MainPrint {

public static void main(String[] args) throws InterruptedException {
	printa a=new printa();
	printb b=new printb();
	printc c=new printc();
	
	
	Thread threada=new Thread(a);
	Thread threadb=new Thread(b);
	Thread threadc=new Thread(c);

	
	threada.start();
	threada.join();
	threadb.start();
	threadb.join();
	
	threadc.start();

	
	
}

}
