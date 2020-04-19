
public class MainOfThreads {

	public static void main(String[] args) {
		
	
	MultipleNumbers mul=new MultipleNumbers();
	SumOfNum sum=new SumOfNum();
	
	
	
	Thread threadSum=new Thread(sum);
	Thread threadmul=new Thread(mul);
	
	
	
	threadSum.start();
	threadmul.start();
}
}