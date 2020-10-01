
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
//import javax.sound.midi.*;
//import javax.sound.sampled.Line.Info;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

public class PianoListener {

	public PianoListener() throws MidiUnavailableException {
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		
		for(int i=0; i<infos.length; i++)
		{
			System.out.print(i + ": ");
		    System.out.println(infos[i].getName() + " - " + infos[i].getDescription());
		}
		
		
		MidiDevice inputDevice; //the ALLEGRO
		try {
			inputDevice = MidiSystem.getMidiDevice(infos[1]);
		} catch (MidiUnavailableException e) {
			System.out.println("ALLEGRO NOT PLUGGED IN");
			inputDevice = null;
			e.printStackTrace();
		}
//		try {
//			MidiDevice outputDevice = MidiSystem.getMidiDevice(infos[1]);
//		} catch (MidiUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Sequencer sequencer;
		
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			sequencer = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Transmitter transmitter;
		Receiver receiver;
		
		//////////////////////////////////SET UP FINISHED//////////////////////////////////////////////
		
		// Open a connection to your input device
		inputDevice.open();
		// Open a connection to the default sequencer (as specified by MidiSystem)
		sequencer.open();
		// Get the transmitter class from your input device
		transmitter = inputDevice.getTransmitter(); 
		// Get the receiver class from your sequencer
		receiver = sequencer.getReceiver();
		// Bind the transmitter to the receiver so the receiver gets input from the transmitter
		transmitter.setReceiver(receiver); 

		// Create a new sequence
		Sequence seq;
		try {
			seq = new Sequence(Sequence.PPQ, 24);
		} catch (InvalidMidiDataException e) {
			seq = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// And of course a track to record the input on
		Track currentTrack = seq.createTrack();
		// Do some sequencer settings
		try {
			sequencer.setSequence(seq);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sequencer.setTickPosition(0);
		sequencer.recordEnable(currentTrack, -1);
		// And start recording
		sequencer.startRecording();
		
		Transmitter converterTransmitter = sequencer.getTransmitter();
//		converterTransmitter.setReceiver(new DataStream());
		/////////////////////////////RECORDING///////////////////////////////////////////
		int trackSize = currentTrack.size();
		
		InDataStream dataStream = new InDataStream();
		dataStream.start();
		
		while(1==1) {
//			for(int i = 0; i < currentTrack.size(); i++) {
//				byte[] event;
//				event = currentTrack.get(i).getMessage().getMessage();
//				
//				System.out.println(Arrays.toString(event));
//				
//				
//			}
			
			if(currentTrack.size() != trackSize) {
				if(currentTrack.size() > 1) {
//					byte[] currentEvent;
//					currentEvent = currentTrack.get(currentTrack.size()-2).getMessage().getMessage();
//					System.out.println(Arrays.toString(currentEvent));
					
//					if(currentEvent[0] == -112)
//						System.out.println(getNote(currentEvent[1]));
					
					dataStream.offer(currentTrack.get(currentTrack.size()-2).getMessage());
					
//					MusicConverter.send(currentTrack.get(currentTrack.size()-2).getMessage());
				}
				
//				System.out.println();
				trackSize = currentTrack.size();
			}
			
//			if(currentTrack.size() > 200) 
//				break;
		}
//		System.out.println("Program Stopped");
		
		
		
		//////////////////////////////STOP RECORDING///////////////////////////////////////////////
		// Stop recording
//		if(sequencer.isRecording())
//		{
//		    // Tell sequencer to stop recording
//		    sequencer.stopRecording();
//
//		    // Retrieve the sequence containing the stuff you played on the MIDI instrument
//		    Sequence tmp = sequencer.getSequence();
//
//		    // Save to file
////		    try {
////				MidiSystem.write(tmp, 0, new File("MyMidiFile.mid"));
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		    
//		    //end program
//		    inputDevice.close();
//		    sequencer.close();
//		}
		
	}
	
	
	
	public static String getNote(byte note) {
//		System.out.println(note);
		int octave = (note /12) - 1;
		
		while(note > 11) {
			note -= 12;
		}
		
		switch(note) {
		case 0: return "C" + octave;
		case 1: return "C#" + octave;
		case 2: return "D" + octave;
		case 3: return "D#" + octave;
		case 4: return "E" + octave;
		case 5: return "F" + octave;
		case 6: return "F#" + octave;
		case 7: return "G" + octave;
		case 8: return "G#" + octave;
		case 9: return "A" + octave;
		case 10: return "A#" + octave;
		case 11: return "B" + octave;
		}
		
		return "getNote error";
	}
}

