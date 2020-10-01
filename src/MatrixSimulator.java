import java.awt.Color;
import java.io.PrintWriter;

import Tests.StdDraw;

public class MatrixSimulator {
	public final class Pixel {
		int address;
		Color color;

		public Pixel(int address, Color color) {
			this.address = address;
			this.color = color;
		}
	}

	public final static int ROWS = 11; // they go from 0-10
	public final static int COLS = 44; // they go from 0-31

	public PrintWriter output;
	OutDataStream outStream;

	private Pixel[][] leds; // organized and accessed in (x, y) Cartesian Coordinate System

	public MatrixSimulator(PrintWriter output) {
		this.output = output;
		this.outStream = new OutDataStream(output, MusicForTheDeafGUI.arduinoData);
		outStream.start();
		leds = new Pixel[COLS][ROWS];
		int address = 0;
		for (int y = ROWS - 1; y >= 0; y--) {
			for (int x = 0; x < COLS; x++) {
				leds[x][y] = new Pixel(address, Color.BLACK);
				address++;
			}
		}

 StdDraw.setCanvasSize(COLS * 40, ROWS * 40);
		StdDraw.setXscale(0, COLS);
		StdDraw.setYscale(0, ROWS);

		StdDraw.clear(Color.BLACK);
// readStateString("");
	}

// public void readStateString(String stateString) {
// //resetting state of the matrix
// for(int x = 0; x < COLS; x++) {
// for(int y = 0; y < ROWS; y++) {
// leds[x][y] = Color.BLACK;
// }
// }
//
//
// //stateString format: a list of integer (each group of three represent an RGB value)
//
// /*
// * The order the data will be fed into the LEDs is the following
// * 0 1 2
// * 5 4 3
// * 8 7 6
// *
// */
// int x = 0;
// int y = 10;
// Scanner sc = new Scanner(stateString);
// while(sc.hasNext()) {
// int R = sc.nextInt();
// int G;
// int B;
//
// try{G = sc.nextInt();} catch (Exception e) {return;}
// try {B = sc.nextInt();} catch (Exception e) {return;}
//
// leds[x][y] = new Color(R, G, B);
// x += 1;
// if(x >= 32) {
// x = 0;
// y -= 1;
// }
// if(y < 0) {
// break;
// }
// }
// }

// public void draw() {
// /*
// * for the arduino version, you will
// * call FastLED.clear() to turn off all LEDs
// * then throw a single text line to the arduino
// *
// * The text would contain the info for turning on only the required LEDs in this format:
// * An LED-ID followed by R G B integers
// *
// * Ex: 2 255 255  255  3 0 255 0
// * The LED-ID is simply the count of this LED starting at the first LED = 0
// */
// int ledID = 0; //useless for now
//
// StdDraw.setPenColor(Color.BLACK);
// StdDraw.filledRectangle(COLS/2, ROWS/2, COLS, ROWS);
//
// for(int x = 0; x < COLS; x++) {
// for(int y = 0; y < ROWS; y++) {
// if(leds[x][y] != null && !leds[x][y].equals(Color.BLACK)) {
// StdDraw.setPenColor(leds[x][y].color);
// StdDraw.filledSquare(x + .5, y + .5, .5);
// }
// ledID += 1;
// }
// }
// }

	public void drawColumn(int xPos, int height) {

	}

// public void scaleTest() {
// int r = 0;
// int g = 0;
// int b = 0;
// for(int x = 0; x < COLS; x++) {
// for(int y = 0; y < ROWS; y++) {
// StdDraw.setPenColor(new Color(r, g, b));
// StdDraw.filledSquare(x + .5, y + .5, .5);
// r = g = b = b + 5;
// if(r >= 255)
// r = g = b = 0;
// }
// }
// }

	public Pixel[][] getLEDS() {
		return this.leds;
	}

	@Override
	public String toString() {
		String s = "";
		for (int y = ROWS - 1; y >= 0; y--) {
			for (int x = 0; x < COLS; x++) {
				s += leds[x][y].toString().substring(14);
			}
			s += "\n";
		}

		return s;
	}

	public synchronized void lightUpNote(int x, Color color, int velocity, int colorIndex) {
//		System.out.println(color.toString());
		leds[x][0].color = color;

		StdDraw.setPenColor(color);

		StdDraw.filledSquare(x + .5, 0 + .5, .5);

		int address = x;

		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
//		String message = address + ":" + r + ":" + g + ":" + b + ":" + velocity;
		String message = address + ":" + colorIndex + ":" + velocity + "&";

// System.out.println(message);

		if (this.output != null) {
			this.outStream.offer(message);
// System.out.println("message sent");
		}
	}

// public static void main(String[] args) {
//// MatrixSimulator sim = new MatrixSimulator();
//// sim.readStateString("0 0 255 0 255 0 255 0 0");
//// sim.draw();
//// sim.setLED(1, 1, Color.RED);
//// sim.setLED(2, 2, Color.GREEN);
//// sim.setLED(3, 3, Color.BLUE);
////
//// sim.setLED(0, 0, Color.yellow);
//// sim.setLED(0, 10, Color.yellow);
//// sim.setLED(31, 0, Color.yellow);
//// sim.setLED(31, 10, Color.yellow);
//
//// sim.draw();
//
//// sim.scaleTest();
//
//// System.out.println("Printing:");
//// System.out.println(sim.toString());
// }

}