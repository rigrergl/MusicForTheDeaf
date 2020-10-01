import java.io.PrintWriter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class OutDataStream extends Thread {
	PrintWriter output;
	Queue<String> q;
	Scanner arduinoData;
	Thread dimmer;

	public OutDataStream(PrintWriter output, Scanner arduinoData) {
		this.arduinoData = arduinoData;
		this.output = output;
		q = new LinkedList<>();//new PriorityQueue<>(new StringComparator());

		dimmer = new Dimmer(this);
	}

	public synchronized void offer(String s) {
		if (arduinoData != null && output != null)
			q.offer(s);
//		System.out.println(s);
	}
	
	public synchronized void offer(String s, boolean dim) {
		if (arduinoData != null && output != null && q.size() == 0)
			q.offer(s);
	}

	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		while(arduinoData == null) {}
//		String waiting = arduinoData.next();
//		System.out.println("Waiting: " + waiting);
		
		dimmer.start();
		while (1 == 1) {
			synchronized (OutDataStream.class) {
				if (arduinoData != null && output != null) {
					if (q.peek() != null) {
						String s = q.poll();
						if(!s.equals("D"))
						System.out.println(s);

//						while(arduinoData.hasNext()) {
//							arduinoData.next();
//							System.out.println("clearing");
//						}
//						
						output.write(s);
//						output.write("1:0:0:1");
						output.flush();

//						try {
//							Thread.sleep(100);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}

//						System.out.println( startTime);

//						while (1 == 1) {
//							String next = arduinoData.next();
//							if (next.equals("done")) {
//								System.out.println(next);
//								break;
//							}
//						}

//						System.out.println("before");
						long startTime = System.currentTimeMillis();
						String serial = arduinoData.next();
						double arduinoDelay = System.currentTimeMillis() - startTime;
						System.out.println(arduinoDelay);
						
//						System.out.println("after");

//						System.out.println("Serial Data: " + serial);
//						System.out.println();

//						try {
//							Thread.sleep(50);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}

//						System.out.println(System.currentTimeMillis());
					}
//					else System.out.println("peek is null");
				}
//				else 
//					System.out.println("one is null");
			}
		}
	}

	private class Dimmer extends Thread {
		OutDataStream stream;

		public Dimmer(OutDataStream stream) {
			this.stream = stream;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (true) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stream.offer("D");
			}

		}

	}
	
//	static private class StringComparator implements Comparator<String>{
//		public int compare(String a, String b) {
//			if(a.length() <= 0 || b.length() <= 0)
//				return 0;
//			if(a.charAt(0) == 'D')
//				return 1;
//			if(b.charAt(0) == 'D')
//				return -1;
//			return 0;
//		}
//	}
//	
//	public static void main(String[] args) {
//		PriorityQueue<String> test = new PriorityQueue<>(new StringComparator());
//		test.offer("D"); test.offer("dfsdf");
//		
//		while(test.peek() != null) {
//			System.out.println(test.poll());
//		}
//	}
}
