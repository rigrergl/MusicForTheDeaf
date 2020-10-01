import java.awt.Color;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.midi.MidiMessage;

//import org.jfugue.theory.Note;

public class MusicConverter {
// public static Set<Note> activeKeys = new HashSet<>();
	public static Integer pianoMinNote = 86;
	public static Integer pianoMaxValue;
	public static Integer key;
	public static PrintWriter output = MusicForTheDeafGUI.output;

	public static final byte NOTE_ON = -112;
	public static final byte NOTE_OFF = -128;
	public static final int MELODY_RANGE = 44; // 32 different notes can be displayed

	public static int minNote = 40; // default, C3

	static MatrixSimulator matrixSimulator = new MatrixSimulator(output);
	
	//Color variables
	static final Color A = new Color(198, 3, 126); //ROSE
	static final Color B = new Color(255, 0, 0);
	static final Color C = new Color(255, 99, 1);
	static final Color D = new Color(255, 145, 0);
	static final Color E = new Color(255, 199, 0);
	static final Color F = new Color(255, 255, 0);
	static final Color G = new Color(186, 200, 25);
	static final Color H = new Color(0, 142, 91);
	static final Color I = new Color(2, 151, 186);
	static final Color J = new Color(39, 114, 178);
	static final Color K = new Color(68, 78, 161);
	static final Color L = new Color(114, 70, 151);
	
//	static final Color A = blend(Color.RED, Color.MAGENTA);
//	static final Color B = Color.RED;
//	static final Color C = blend(Color.RED, Color.ORANGE);
//	static final Color D = Color.ORANGE;
//	static final Color E = blend(Color.YELLOW, Color.ORANGE);
//	static final Color F = Color.YELLOW;
//	static final Color G = blend(Color.YELLOW, Color.GREEN);
//	static final Color H = Color.GREEN;
//	static final Color I = blend(Color.BLUE, Color.GREEN);
//	static final Color J = Color.BLUE;
//	static final Color K = blend(Color.BLUE, Color.MAGENTA);
//	static final Color L = Color.MAGENTA;
	
	static final Color[] CHROMATIC_MAPPING = {A, B, C, D, E, F, G, H, I, J, K, L};
	static final Color[] FIFTHS_MAPPING_COLORS = {A, H, C, J, E, L, G, B, I, D, K, F};
	static Color[] currentColorMapping = FIFTHS_MAPPING_COLORS;
	
	static final int[] CHROMATIC_MAPPING_INDEXES = {0,1,2,3,4,5,6,7,8,9,10,11};
	static final int[] FIFTHS_MAPPING_INDEXES = {0, 7, 2, 9, 4, 11, 6, 1, 8, 3, 10, 5};
	static int[] currentColorMappingIndexes = FIFTHS_MAPPING_INDEXES;
//	static int[] currentColorMappingIndexes = CHROMATIC_MAPPING_INDEXES;
	
	
	static boolean sustain = false;
	static Queue<MidiMessage> sustainedMessages = new LinkedList<>();
	
	

	public static synchronized void send(MidiMessage message) {
		if (message == null)
			return;

		byte[] bytes = message.getMessage();
		System.out.println(Arrays.toString(bytes));
		
//		System.out.print(Arrays.toString(bytes) + " ");
		
//		System.out.println(Arrays.toString(bytes)); //-80
//		System.out.println("END OF MESSAGE");
		
		
		if(bytes[0] == -80) {
			if(sustain) {
				sustain = !sustain;
				while(sustainedMessages.peek() != null) {
					MidiMessage m = sustainedMessages.poll();
					byte note = m.getMessage()[1];
					matrixSimulator.lightUpNote(note - minNote, new Color(0, 0, 0), 127, 12);
				}
			}
			else
				sustain = !sustain;
			
			
			System.out.println(sustain);
			return;
		}
		
		byte note = bytes[1];
		byte velocity;
		if(bytes.length >= 3)
			velocity = bytes[2];
		else 
			return;
		
		if (note < minNote || note >= minNote + MELODY_RANGE) {
			//note is not considered melody
//			System.out.println("Note ignored");
		} else if (bytes[0] == NOTE_ON) {
			Color color = getColor(note);
			int colorIndex = getColorIndex(note);
			matrixSimulator.lightUpNote(note - minNote, color, velocity, colorIndex);
		}
		else if (bytes[0] == NOTE_OFF) {
			if(!sustain)
				matrixSimulator.lightUpNote(note - minNote, new Color(0, 0, 0), 127, 12);
			else //if(!sustainedMessages.contains(message))
				sustainedMessages.offer(message);
		}

//TODO
// String arduinoMessage = toArduinoMessage(message);
// if(output != null) {
// output.print(arduinoMessage);
// output.flush();
// }
	}
	

	public static void setPrintWriter(PrintWriter out) {
		output = out;
		matrixSimulator.output = output;
		matrixSimulator.outStream.output = output;
		matrixSimulator.outStream.arduinoData = MusicForTheDeafGUI.arduinoData;
	}

	
//	/**
//	 * Sends the message 
//	 * @param message
//	 * @return
//	 */
//	public static String sendNoteToArduino(MidiMessage message) {
//		byte[] bytes = message.getMessage();
//
//		System.out.println(Arrays.toString(bytes));
//		System.out.println();
//		
////		Color color = getColor();
//
//		if (bytes[0] == NOTE_ON) { // if it's a note on message
//			byte note = bytes[1];
//			String ledAddress = note - pianoMinNote + ":";
//			Color color = getColor(note);
//			String ledVal = color.getRed() + ":" + color.getGreen() + ":" + color.getBlue();
//
//			String finalMessage = ledAddress + "&";
//			return finalMessage;
//		} else if (bytes[0] == NOTE_OFF) {
//			byte note = bytes[1];
//			String ledAddress = note - pianoMinNote + ":";
//			String ledVal = "0:0:0";
//			return ledAddress + ledVal + "&";
//		}
//		return "";
//	}

	public static String getNote(byte note) {
// System.out.println(note);
		int octave = (note / 12) - 1;

		while (note > 11) {
			note -= 12;
		}

		switch (note) {
		case 0:
			return "C" + octave;
		case 1:
			return "C#" + octave;
		case 2:
			return "D" + octave;
		case 3:
			return "D#" + octave;
		case 4:
			return "E" + octave;
		case 5:
			return "F" + octave;
		case 6:
			return "F#" + octave;
		case 7:
			return "G" + octave;
		case 8:
			return "G#" + octave;
		case 9:
			return "A" + octave;
		case 10:
			return "A#" + octave;
		case 11:
			return "B" + octave;
		}

		return "getNote error";
	}
	
	public static byte getParentNote(byte note) {
		while (note > 11) {
			note -= 12;
		}
		
		return note;
	}
	
	public static Color getColor(byte note) { //TODO
//		System.out.println(note);
		note = getParentNote(note);
		return currentColorMapping[note];
		
	}
	
	public static int getColorIndex(byte note) {
		note = getParentNote(note);
		return currentColorMappingIndexes[note];
	}
	
	
	public static Color blend(Color c0, Color c1) {
	    double totalAlpha = c0.getAlpha() + c1.getAlpha();
	    double weight0 = c0.getAlpha() / totalAlpha;
	    double weight1 = c1.getAlpha() / totalAlpha;

	    double r = weight0 * c0.getRed() + weight1 * c1.getRed();
	    double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
	    double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
	    double a = Math.max(c0.getAlpha(), c1.getAlpha());

	    return new Color((int) r, (int) g, (int) b, (int) a);
	  }
	
}