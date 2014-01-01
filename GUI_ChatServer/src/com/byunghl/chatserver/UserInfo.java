package com.byunghl.chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class UserInfo extends Thread implements Observerable{
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private Socket userSocket;
	private Vector<UserInfo> userVector;
	private ArrayList<Observer> observers;
	
	private String nickName;;
	private ThreadMessage message;
	
	public UserInfo(Socket soc, ArrayList<Observer> al, Vector<UserInfo> vc) {
		this.nickName="";
		this.message = new ThreadMessage();
		this.userSocket = soc;
		this.observers = al;
		this.userVector = vc;
		
		userNetwork();
	}
	
	public void userNetwork() {
		try {
			inputStream = userSocket.getInputStream();
			dis = new DataInputStream(inputStream);
			outputStream = userSocket.getOutputStream();
			dos = new DataOutputStream(outputStream);
		
			nickName = dis.readUTF();
			message.setMessage("USER ID " + nickName + " is conncted to server.\n");
			notifyObservers(message);
			sendMessage("You are successfully connected to server");
			
		} catch(Exception e) {
			message.setMessage("Stream setting error occurs\n");
			notifyObservers(message);
		}
	}
	
	public void inMessage(String str) {
		message.setMessage("Message from user:" + str + "\n");
		broadCast(str);
	}
	
	public void broadCast(String str) {
		for(int i =0; i < userVector.size(); i++){
			UserInfo temp = (UserInfo)userVector.elementAt(i);
			temp.sendMessage(nickName+ " : " + str);
		}
	}
	
	public void sendMessage(String str) {
		try {
			dos.writeUTF(str);
		}catch(IOException e) {
			message.setMessage("Error occur while sending a message\n");
			notifyObservers(message);
		}
	}
	
	public void run() {
	
		while(true) {
			try {
				String msg = dis.readUTF();
				inMessage(msg);
			}catch(IOException e) {
				try {
					dos.close();
					dis.close();
					userSocket.close();
					userVector.removeElement(this);
					message.setMessage(userVector.size() + "number of people are in chatroom ");
					notifyObservers(message);
					message.setMessage("Returend disconnected user's resource\n");
				}catch(Exception ee) {
					
				}
			}
		}
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		int i = observers.indexOf(o);
		if(i>=0) {
			observers.remove(i);
		}
		
	}

	@Override
	public void notifyObservers(Message message) {
		for(int i =0; i < observers.size(); i++) {
			Observer observer = (Observer)observers.get(i);
			observer.update(message);
		}
		
	}

}
