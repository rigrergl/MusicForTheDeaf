import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;


public class MusicForTheDeafGUI {
	
	static SerialPort chosenPort; //Arduino port
	static PrintWriter output;
	static PianoListener pianoListener;
	static Scanner arduinoData;
	
	public static void main(String[] args) {
		System.out.println("started");
		
		// create and configure the window
				JFrame window = new JFrame();
				window.setTitle("Music for the Deaf GUI");
				window.setSize(800, 150);
				window.setLayout(new BorderLayout());
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// create a drop-down box and connect button, then place them at the top of the window
				JComboBox<String> outputPortList = new JComboBox<String>();
				JButton connectButton = new JButton("Connect Peripheral");
				JButton onOffButton = new JButton("On");
				JPanel topPanel = new JPanel();
				topPanel.add(outputPortList);
				topPanel.add(connectButton);
				topPanel.add(onOffButton);
				window.add(topPanel, BorderLayout.NORTH);
				
				// populate the drop-down box
				SerialPort[] portNames = SerialPort.getCommPorts();
				for(int i = 0; i < portNames.length; i++)
					outputPortList.addItem(portNames[i].getSystemPortName());
				
				window.setVisible(true);
				
				configureConnectButton(connectButton, outputPortList);
				configureOnOffButton(onOffButton);
				
				//create another drop-down box for the output port and a connectPiano button, then place them below the outputport
				JComboBox<String> inputPortList = new JComboBox<String>();
				JButton connectPianoButton = new JButton("Connect Piano");
				topPanel.add(inputPortList); topPanel.add(connectPianoButton);
				
				//populate input drop-down box
				Info[] infos = MidiSystem.getMidiDeviceInfo();
				for(int i = 0; i < infos.length; i++)
					inputPortList.addItem(infos[i].getName());
				
				
				//create the PianoListener
				try {
					pianoListener = new PianoListener();
				} catch (MidiUnavailableException e) {
					System.out.println("Failed to connect to Piano");
					e.printStackTrace();
				}
				
	}
	
	/**
	 * Configures an switch button that can be use to toggle some LEDs on the stip on and off
	 * @param onOffButton
	 */
	public static void configureOnOffButton(JButton onOffButton) {
		if(output == null) {
			System.out.println();
		}
		
		onOffButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				if(onOffButton.getText().equals("On")) {
					onOffButton.setText("Off");
//					output.print("348:0:0:10&");
//					output.flush();
					try {
						MidiPlayer.play();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					onOffButton.setText("On");
					output.print("348:0:0:0&");
					output.flush();
				}
			}
		});
	}
	
	/**
	 * Establishes connection with the selected Serial Comm Port and initializes the Thread for sending data out to the arduino
	 * @param connectButton
	 * @param portList
	 */
	private static void configureConnectButton(JButton connectButton, JComboBox portList) { //TODO REVISE METHOD OF SENDING DATA OUT TO ARDUINO
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
				if(connectButton.getText().equals("Connect Peripheral")) {
					// attempt to connect to the serial port
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(chosenPort.openPort()) {
						connectButton.setText("Disconnect Peripheral");
						portList.setEnabled(false);
						
						//create a new thread for sending data to the arduino
						Thread thread = new Thread(){
							@Override public void run() {
								try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
								
								 output = new PrintWriter(chosenPort.getOutputStream());
								 arduinoData = new Scanner(chosenPort.getInputStream());
								 MusicConverter.setPrintWriter(output);
								
								//enter infinite loop that sends text to the arduino
								while(true) {
//									output.print("on");
//									output.flush();
//									try{Thread.sleep(100);} catch(InterruptedException e) {e.printStackTrace();}
//									
//									output.print("off");
//									output.flush();
//									try{Thread.sleep(100);} catch(InterruptedException e) {e.printStackTrace();}
								}
							}
						};
						thread.start();
					}
					
				} else {
					// disconnect from the serial port
					chosenPort.closePort();
					portList.setEnabled(true);
					connectButton.setText("Connect");
				}
			}
		});
	}
}
