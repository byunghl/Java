package com.byunghl.chatserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ServerGUI extends JFrame implements Observer{
	
	private static final long serialVersionUID = 1L;
	
	private static ServerGUI serverGUI = new ServerGUI();
	
	private Server server;
	private JPanel contentPane;
	private JTextField textField;
	private JButton startButton;
	private JTextArea textArea;
	
	private int portNumber;
	
	// Constructor
	private ServerGUI() {
		super("ChatServer");
		init();
		initGUI();
	}
	
	// Initialize member variables.
	private void init() {
		this.portNumber = 0;
		this.server= new Server();
	}
	
	// GUI Setter.
	private void initGUI() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		
		textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setRows(5);
		textArea.setEditable(false);
		scrollPane.setBounds(0,0,265, 254);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.setBounds(98, 264, 154, 37);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel portLabel = new JLabel("Port Number");
		portLabel.setBounds(12, 264, 98, 37);
		contentPane.add(portLabel);
		
		startButton = new JButton("Run Server");
		startButton.setBounds(0, 325, 264, 37);
		contentPane.add(startButton);
		
		// Process a event with anonymous class
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if(textField.getText().equals("") || textField.getText().length() == 0 ) {
					textArea.setText("Port Number is required!\nPlease input port number");;
					textField.requestFocus();
				} 
				else {
					try {
						portNumber = Integer.parseInt(textField.getText());
						server.setPortNumber(portNumber);
						server.addObserver(getInstance());
						server.startServer();
					} catch(Exception ex) {
						textArea.setText("Port number must be integer");
						textField.requestFocus();
					}
				}
				
			}
			
		});
		
		setVisible(true);
	}
	
	public void display(String msg, int choice) {
		if(choice == 1) {
			textArea.setText(msg);
			textField.setEditable(false);
			startButton.setText("Running");
			startButton.setEnabled(false);
		} else if(choice == 2) {
			textArea.append(msg);
		}
	}
		

	@Override
	public void update(Message message) {
		
		if(message instanceof ConnectionMessage) {
			String temp = ((ConnectionMessage) message).getMessage();
			display(temp, 1);
		}
		else if(message instanceof ThreadMessage) {
			String temp = ((ThreadMessage) message).getMessage();
			display(temp, 2);
		}
		
	}
	
	// Singleton pattern is applied. 
	public static ServerGUI getInstance() {
		return serverGUI;
	}

	
	
}

