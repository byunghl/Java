package com.byunghl.chatserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ConnectionThread implements Runnable, Observerable {

	private Socket socket;
	private ServerSocket serverSocket;
	private Vector vc;
	private ArrayList<Observer> observers; 
	private String message;
	private ThreadMessage tm;
	
	
	public ConnectionThread(Socket socket, ServerSocket serverSocket, ArrayList<Observer> observers, Vector vc) {
		this.socket = socket;
		this.serverSocket = serverSocket;
		this.observers = observers;
		this.vc = vc;
		this.message = new String("");
		this.tm = new ThreadMessage();
	}
	
	@Override
	public void run() {
		while(true) {
			try{
				message = "\nWating for connections";
				setMessage(getMessage());
				socket = serverSocket.accept();
				message = "\nUser is connected";
				setMessage(getMessage());
			}catch(Exception ex){}
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

	private String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		tm.setMessage(this.message.toString());
		notifyObservers(tm);
	}
	
	// DO I REALLY NEED THIS METHOD?
	public void reportChange(Message message) {
		notifyObservers(message);
	}

	
	
}


