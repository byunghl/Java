package com.byunghl.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Server implements Observerable {

	private ServerSocket serverSocket = null;
	private Socket socket;
	private int portNumber;
	
	private ArrayList<Observer> observers;
	private Vector<UserInfo> vc;
	
	public Server() {
		observers = new ArrayList<Observer>();
		vc = new Vector<UserInfo>();
		
	}
	
	// will be called by GUI
	public void startServer() {
		try {
			serverSocket = new ServerSocket(portNumber);
			
			ConnectionMessage message = new ConnectionMessage("Server started!\nServer is running. PORT:"+portNumber+"\n", true);
			notifyObservers(message);
			
			if(serverSocket != null) {
				connection();
			}
		
		}catch(IOException e) {
			
		}	
	}
	
	private void connection() {
		Runnable r = new ConnectionThread(socket, serverSocket,observers, vc);
		Thread th = new Thread(r);
		th.start();
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
	
	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}


	// DO I REALLY NEED THIS METHOD?
	public void statusChanged(Message message) {
		notifyObservers(message);
	}
	
	
}
