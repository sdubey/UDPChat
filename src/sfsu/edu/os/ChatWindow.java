package sfsu.edu.os;

import java.net.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Creating various components
	JButton btnSend = new JButton("Send");
	JCheckBox exitChat = new JCheckBox("Exit Chat !!");
	JTextArea curText = new JTextArea("");
	JTextArea chatList = new JTextArea(" ");
	JPanel bottomPanel = new JPanel();
	JPanel topPanel = new JPanel();
	JPanel holdAll = new JPanel();

	// Creating sender and receiver
	SenderThread objSender;
	ReceiverThread objReceiver;

	/**
	 * The constructor.
	 */
	public ChatWindow(UDPComm objComm) {

		objSender = new SenderThread(objComm);
		objReceiver = new ReceiverThread();

		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(exitChat);
		bottomPanel.add(btnSend);

		topPanel.setLayout(new FlowLayout());
		topPanel.add(chatList);
		topPanel.add(curText);

		// Customizing components
		curText.setPreferredSize(new Dimension(350, 100));
		curText.setLineWrap(true);
		curText.setWrapStyleWord(true);

		chatList.setPreferredSize(new Dimension(350, 350));
		chatList.setEditable(false);
		chatList.setLineWrap(true);
		chatList.setWrapStyleWord(true);

		bottomPanel.setPreferredSize(new Dimension(350, 100));

		holdAll.setLayout(new BorderLayout());
		holdAll.add(bottomPanel, BorderLayout.SOUTH);
		holdAll.add(topPanel, BorderLayout.CENTER);

		getContentPane().add(holdAll, BorderLayout.CENTER);

		btnSend.addActionListener(this);
		exitChat.addActionListener(this);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		int portSender = 9875;
		int portReceiver = 9876;
		String host = "localhost";

		if (args.length < 1) {
			System.out.println("Usage: UDPClient " + "Now using host = " + host
					+ ", SenderPort# = " + portSender + "ReceiverPort is "
					+ portReceiver);
		}
		// Get the port number to use from the command line
		else {

			portSender = Integer.valueOf(args[0]).intValue();
			portReceiver = Integer.valueOf(args[1]).intValue();
			System.out.println("Usage: UDPClient " + "Now using host = " + host
					+ ", Port# = " + portSender + portSender
					+ "ReceiverPort is " + portReceiver);
		}

		// Get the IP address of the local machine - we will use this as the
		// address to send the data to
		try {
			UDPComm objComm = new UDPComm();
			objComm.setSenderPort(portSender);
			objComm.setReceiverPort(portReceiver);
			objComm.setHost("localhost");
			objComm.PrepareConnToSend();

			ChatWindow myApplication = new ChatWindow(objComm);
			myApplication.setLocation(10, 10);
			myApplication.setSize(400, 600);
			myApplication.setVisible(true);
		}

		catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Each non abstract class that implements the ActionListener must have this
	 * method.
	 * 
	 * @param e
	 *            the action event.
	 */
	public void actionPerformed(ActionEvent e) {
		String dataTosend = " ";
		// byte [] data=new byte[1024];
		if (e.getSource() == btnSend) {

			System.out.println("curText.getText().trim() -> "
					+ curText.getText().trim());
			byte[] data = curText.getText().trim().getBytes();
			chatList.setText(chatList.getText() + "\nme :"
					+ curText.getText().trim());
			objSender.btnPressed(data);
			curText.setText("");
		}

		if (e.getSource() == exitChat) {
			curText.setText("Exiting chat bye. !!!");

		}
	}

}
