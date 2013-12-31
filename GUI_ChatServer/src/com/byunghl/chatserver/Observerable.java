package com.byunghl.chatserver;

public interface Observerable {
	public void addObserver(Observer o);
	public void deleteObserver(Observer o);
	public void notifyObservers(Message message);
}
