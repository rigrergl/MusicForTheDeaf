import java.util.LinkedList;
import java.util.Queue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

//
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.PriorityQueue;
//import java.util.Scanner;
//
//import javax.sound.midi.MetaEventListener;
//import javax.sound.midi.MetaMessage;
//import javax.sound.midi.MidiMessage;
//import javax.sound.midi.Receiver;
//
//
///**
// * A DataStream Object is intended to be created all at once before anything is sent to the Arduino
// * An object of this class will be created for playback from a file, not from live playback
// *-------------
// * String Events will follow the following format:
// * 
// * TimeStamp //TODO
// * 
// * @author Rigre Jr
// *
// */
//
//public class DataStream implements Receiver, MetaEventListener{
//	Receiver receiver;
//	boolean active;
//	
//	public DataStream() {
//		this.active = true;
//	}
//	
//	@Override
//	public void send(MidiMessage message, long timestamp) {
//		if(this.active)
//			MusicConverter.send(message);
//	}
//	
//	@Override
//	public void close() {}
//	
//	@Override
//	public void meta(MetaMessage meta) {
//		//TODO
//	}
//	
//	public boolean setActive(boolean b) {
//		boolean prev = this.active;
//		this.active = b;
//		return prev;
//	}
//}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	//Receiver Methods
////	public void send(MidiMessage message, long timeStamp) {
//////		byte[] msg = message.getMessage();
////		
//////		if(msg[1] == 10 || msg[1] == 9)
//////			System.out.println("Drums");
////		
//////		//a two byte MidiMessage means an intrument change, the second byte represents the instrument ***Not for sure
//////		if(msg.length == 2)
//////				System.out.println(msg[1]);
////		
////		
//////		Note note = null;
//////		if(msg.length >= 3)
//////			note = new Note(msg[1]);
//////		if(note != null) {
//////			System.out.print(msg[1] + " " + note.toString());
//////			if(msg.length >= 4)
//////				System.out.print(" " + msg[3]);
//////			System.out.println();
//////		}
////	}
////	
////	public void close() {
////		//TODO
////	}
////	///
////	
////	//Meta Event Listener Method
////	public void meta(MetaMessage meta) {
////		System.out.println("MetaMEssage received");
////		
////		byte[] data = meta.getData();
////		
////		System.out.println(meta.getType() + " ");
////		System.out.println(Arrays.toString(data));
////		
////	}
////	
////	
////	private class EventComparator implements Comparator<String>{
////		//Note: This comparator imposes orderings that are inconsistent with equals
////		
////		@Override
////		public int compare(String event1, String event2) { //TODO TEST THIS METHOD
////			Scanner sc1 = new Scanner(event1);
////			Scanner sc2 = new Scanner(event2);
////			
////			Integer timeStamp1 = sc1.nextInt();
////			Integer timeStamp2 = sc2.nextInt();
////			sc1.close(); sc2.close();
////			
////			return Integer.compare(timeStamp1, timeStamp2);
////		}
////		
////		@Override
////		public boolean equals(Object obj) {
////			return false; //this method will not be used for this project
////		}
////	}
////	
////}
//

public class InDataStream extends Thread implements Receiver{
	Queue<MidiMessage> messages;

	public InDataStream() {
		messages = new LinkedList();
	}

	public void run() {
//		MidiMessage pedal;
//		try {
//			pedal = new ShortMessage(-80);
//		} catch (InvalidMidiDataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//		MusicCo
		
		
		while (1 == 1) {
//			if (messages.size() > 0) {
				MusicConverter.send(messages.poll());
//			}
		}
	}

	public synchronized void offer(MidiMessage message) {
		messages.offer(message);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(MidiMessage arg0, long arg1) {
		this.offer(arg0);
		
	}
}
