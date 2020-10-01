
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoCommTest {
	
	static SerialPort chosenPort;

	public static void main(String[] args) {
		
		// create and configure the window
		JFrame window = new JFrame();
		window.setTitle("Sensor Graph GUI");
		window.setSize(400, 75);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create a drop-down box and connect button, then place them at the top of the window
		JComboBox<String> portList = new JComboBox<String>();
		JButton connectButton = new JButton("Connect");
		JPanel topPanel = new JPanel();
		topPanel.add(portList);
		topPanel.add(connectButton);
		window.add(topPanel, BorderLayout.NORTH);
		
		// populate the drop-down box
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(int i = 0; i < portNames.length; i++)
			portList.addItem(portNames[i].getSystemPortName());
		
		
		// configure the connect button
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
				if(connectButton.getText().equals("Connect")) {
					// attempt to connect to the serial port
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(chosenPort.openPort()) {
						connectButton.setText("Disconnect");
						portList.setEnabled(false);
						
						//create a new thread for sending data to the arduino
						Thread thread = new Thread(){
							@Override public void run() {
								try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
								
								PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
								
								//enter infinite loop that sends text to the arduino
								while(true) {
									output.print("on");
									output.flush();
									try{Thread.sleep(100);} catch(InterruptedException e) {e.printStackTrace();}
									
									output.print("off");
									output.flush();
									try{Thread.sleep(100);} catch(InterruptedException e) {e.printStackTrace();}
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
		
		// show the window
		window.setVisible(true);
	}

}