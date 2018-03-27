package ch.fhnw.sheepMaw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Game> games = new ArrayList<Game>();
	private static final int maxClientsCount = 50;
	private static Server server;
	private static final ServerThread[] threads = new ServerThread[50];

	@SuppressWarnings("resource")
	@Override
	public void run() {

		// open server socket
		Socket socket = null;
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(40000);
			System.out.println("Server started...");
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				socket = serverSocket.accept();
				System.out.println("client connected");
				int i = 0;

				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new ServerThread(socket, threads, this))
								.start();
						break;
					}
				}

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}

	}

	public static Server getServer() {
		return server;
	}

	public static void setServer(Server server) {
		Server.server = server;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

}
