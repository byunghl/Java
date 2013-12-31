package com.byunghl.chatserver;

public class ConnectionMessage implements Message {
	
	private String message;
	private Boolean isConnected;
	
	public ConnectionMessage(String message, boolean isConnected) {
		super();
		this.message = message;
		this.isConnected = isConnected;
	}

	public String getMessage() {
		return message;
	}

	public Boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
