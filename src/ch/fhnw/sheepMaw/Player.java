package ch.fhnw.sheepMaw;

import java.io.Serializable;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String userId;
	private String ip;
	private int port;
	private boolean server;

	public Player(String userName, boolean server, String userId, String ip,
			int i) {
		this.setUserName(userName);
		this.server = server;
		this.setUserId(userId);
		this.setIp(ip);
		this.setPort(i);
	}

	public Player(String userName) {
		this.setUserName(userName);
	}

	public Player(String userName, String userId) {
		this.setUserId(userId);
		this.setUserName(userName);
	}

	public String checkRole(boolean server) {
		if (this.server == true) {
			return "server";
		} else {
			return "client";
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
