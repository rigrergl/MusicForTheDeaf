import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;


//package com.javacodegeeks.snippets.desktop;

/**
 * Credit to GeeksForGeeks user Ilias Tsagklis for providing the base of this class
 * @author Ilias Tsagklis and Rigre Jr
 *
 */
public class MidiPlayer {

   public static void play() throws Exception {

       // Obtains the default Sequencer connected to a default device.
       Sequencer sequencer = MidiSystem.getSequencer();

       // Opens the device, indicating that it should now acquire any
       // system resources it requires and become operational.
       sequencer.open();

       // create a stream from a file
       File file = new File("midiFiles/Hey-Jude-1.mid");
       InputStream is = new BufferedInputStream(new FileInputStream(file));

       // Sets the current sequence on which the sequencer operates.
       // The stream must point to MIDI file data.
       sequencer.setSequence(is);

       
       List<Transmitter> transmitters = sequencer.getTransmitters();
//       System.out.println( "transmitters " + transmitters.size());
       
       Transmitter dataStreamTrans = sequencer.getTransmitter();
       InDataStream dataStream = new InDataStream();
       dataStreamTrans.setReceiver(dataStream);
       dataStream.start();
//       sequencer.addMetaEventListener(dataStream);
       
    // Starts playback of the MIDI data in the currently loaded sequence.
       sequencer.start();																					
       System.out.println("started");
       
       
       Track[] tracks = sequencer.getSequence().getTracks();
       sequencer.getSequence().deleteTrack(tracks[0]);
       
       
//       System.out.println(tracks[0].ticks());
//       System.out.println(tracks[0].get(1).getTick());
       
       
   }

}