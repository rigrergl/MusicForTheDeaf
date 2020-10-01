package Tests;
import java.util.concurrent.TimeUnit;

public class SineTester {
	private double  startTime;
	private double currentTime; //in seconds
	
	public SineTester(){
		startTime = System.currentTimeMillis();
		currentTime = startTime / 1000;
	}
	
	public void update() {
		currentTime = (System.currentTimeMillis() - startTime) /1000; 
		System.out.println( (.5 * Math.sin(currentTime)) + .5);
	}
	
	public double getTime() {
		return currentTime;
	}
	
	public static void main(String[] args) {
		StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.point(0.5, 0.5);
		
		SineTester tester = new SineTester();
		while(1==1) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			StdDraw.clear();
			tester.update();
			StdDraw.point( 0.5 ,  (.5 * Math.sin(tester.getTime()) + .5 ));
			
		}
	}
}
