package ch.fhnw.sheepMaw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * 
 * this class is the main server class to handle input from client handle new rounds distribute cards ...
  * @author Nicolas Sax
 *
 */
public class ServerThread extends Thread {
	Logik gameLogik = new Logik();
	private GameInput gameInput = new GameInput();
	private ObjectOutputStream outServer;
	private ObjectInputStream inServer;
	private Socket socket = null;
	private final ServerThread[] threads;
	private int maxClientsCount;
	private Server server;
	private boolean disconnect = false;

	/**
	 * this method creates a new serverthread
	 * @param s
	 * @param threads
	 */
	public ServerThread(Socket s, ServerThread[] threads, Server server) {
		this.threads = threads;
		this.maxClientsCount = threads.length;
		this.socket = s;
		this.setServer(server);

	}

	/**
	 * this method creates a new game for the current client and writes the
	 * whole game list back to all connected clients
	 * 
	 * @param sentObject
	 *            gameInput object received from client
	 * @author Nicolas Sax
	 */
	private void createGame(GameInput sentObject) {
		// generate random unique game id
		String gameID = UUID.randomUUID().toString();
		// get the server object
		Server server = this.getServer();
		// get servers games list
		ArrayList<Game> games = server.getGames();
		// create new game object with random game id and current threads player
		// as creator
		Game newGame = new Game(gameID, this.gameInput.getPlayer());
		// add the new game to the games list and write it back to the server
		ArrayList<Player> gamePlayers = new ArrayList<Player>();
		gamePlayers.add(this.gameInput.getPlayer());
		newGame.setPlayers(gamePlayers);
		games.add(newGame);
		server.setGames(games);
		this.gameInput.setGame(newGame);
		// update all gameInput objects with adding the new game to the list
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				threads[i].gameInput.setGames(server.getGames());
				threads[i].gameInput.setAction("gameAdded");
				try {
					threads[i].outServer.writeObject(threads[i].gameInput);
					threads[i].outServer.flush();
					threads[i].outServer.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// send success response that the game was created
		this.gameInput.setAction("gameCreated");
		try {
			this.outServer.writeObject(this.gameInput);
			this.outServer.flush();
			this.outServer.reset();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * this method removes the current user from the players list of the server
	 * and writes that list back to all connected clients
	 * 
	 * @param sentObject
	 *            gameInput object received from client
	 * @author Nicolas Sax
	 */
	private void leaveServer() {
		Server server = this.getServer();

		ArrayList<Player> players = server.getPlayers();
		for (int j = 0; j < players.size(); j++) {
			if (players.get(j).getUserId()
					.equals(this.gameInput.getPlayer().getUserId())) {
				players.remove(j);
				break;
			}
		}

		server.setPlayers(players);
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null && threads[i] != this) {
				threads[i].gameInput.setPlayers(server.getPlayers());
				threads[i].gameInput.setAction("playerLeft");
				try {
					threads[i].outServer.writeObject(threads[i].gameInput);
					threads[i].outServer.flush();
					threads[i].outServer.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// disconnect from the server

		}
	}

	/**
	 * 
	 * this method creates a random user id for the newly connected user and
	 * adds him to the players list of the server this list will be written back
	 * to all clients
	 * 
	 * @param sentObject
	 *            gameInput object received from client
	 * @author Nicolas Sax
	 */
	private void joinServer(GameInput sentObject) {

		Server server = this.getServer();
		String userId = UUID.randomUUID().toString();

		Player newPlayer = new Player(sentObject.getPlayer().getUserName(),
				userId);
		ArrayList<Player> players = null;
		if (server.getPlayers().isEmpty()) {
			players = new ArrayList<Player>();
		} else {
			players = server.getPlayers();
		}
		players.add(newPlayer);
		server.setPlayers(players);

		this.gameInput.setPlayer(newPlayer);

		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				threads[i].gameInput.setGames(server.getGames());
				threads[i].gameInput.setPlayers(server.getPlayers());
				threads[i].gameInput.setAction("serverJoined");
				try {
					threads[i].outServer.writeObject(threads[i].gameInput);
					threads[i].outServer.flush();
					threads[i].outServer.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * this method removes all cards from the playground and calculates the point for the specific player
	 * @param sentObject
	 * @author Nicolas Sax
	 */
	private void passCards(GameInput sentObject) {
		Game sentGame = sentObject.getGame();
		int maxPoints = 0;
		Player mostPoints = null;
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();

				if (currentGame.getGameId().equals(sentGame.getGameId())) {
					try {

						ArrayList<Card> playgrndCards = currentGame
								.getPlaygroundCards();
						playgrndCards.clear();
						currentGame.setPlaygroundCards(playgrndCards);
						if (currentGame
								.getLast_played()
								.getUserId()
								.equals(threads[i].gameInput.getPlayer()
										.getUserId())) {
							currentGame.setPoints(currentGame.getPoints()
									+ currentGame.getCalcPoints());
						}
						System.out.println("Punkte Spieler "
								+ this.gameInput.getPlayer().getUserName()
								+ ": " + currentGame.getPoints());
						currentGame.setCalcPoints(0);
						currentGame
								.setActive_user(getNextActivePlayer(currentGame));
						if (currentGame.getPoints() > maxPoints) {
							maxPoints = currentGame.getPoints();
							mostPoints = threads[i].gameInput.getPlayer();
						}
						threads[i].gameInput.setAction("cardsPassed");

						threads[i].gameInput.setGame(currentGame);
						threads[i].outServer.writeObject(threads[i].gameInput);
						threads[i].outServer.flush();
						threads[i].outServer.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (maxPoints >= 350) {
			finishGame(maxPoints, mostPoints, sentGame);
		}

	}

	/**
	 * this method sends the gameFinished command to all players in a game
	 * it is called when one player gets 350 or more points 
	 * 
	 * @param maxPoints
	 * @param mostPoints
	 * @param sentGame
	 */
	private void finishGame(int maxPoints, Player mostPoints, Game sentGame) {
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();

				if (currentGame.getGameId().equals(sentGame.getGameId())) {
					threads[i].gameInput.setAction("gameFinished");
					threads[i].gameInput.setGame(currentGame);
					try {
						threads[i].outServer.writeObject(threads[i].gameInput);
						threads[i].outServer.flush();
						threads[i].outServer.reset();
					} catch (IOException e) {
				
						e.printStackTrace();
					}

				}
			}
		}

	}

	private int calcRoundPoints(Game sentGame) {
		int calcRoundPoints = 0;
		int maxHandCards = 0;
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();

				if (currentGame.getGameId().equals(sentGame.getGameId())) {
					if (currentGame.getPlayerCards().size() > maxHandCards) {
						maxHandCards = currentGame.getPlayerCards().size();
					}
				}

			}
		}
		int opponentPoints = 5 * maxHandCards;
		calcRoundPoints = opponentPoints;

		return calcRoundPoints;
	}

	/**
	 * this method gets for each player in a game new cards
	 * additionally it calculates all points and adds them to the specific player
	 * 
	 * @param sentGame
	 */
	private void newRound(Game sentGame) {
		int noOfPlayers = this.gameInput.getPlayers().size();
		HandleCards hc = new HandleCards(noOfPlayers);
		ArrayList<HashMap<Integer, Card>> playersCards = hc.playersCards;
		int players = 0;
		int maxPoints = 0;
		Player mostPoints = null;
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();

				if (currentGame.getGameId().equals(sentGame.getGameId())) {

					if (threads[i].gameInput.getPlayer().getUserId()
							.equals(sentGame.getActive_user().getUserId())) {
						currentGame.setPoints(calcRoundPoints(sentGame)
								+ currentGame.getCalcPoints()
								+ currentGame.getPoints());
					} else {
						currentGame.setPoints(currentGame.getCalcPoints()
								+ currentGame.getPoints());
					}
					if (currentGame.getPoints() > maxPoints) {
						maxPoints = currentGame.getPoints();
						mostPoints = threads[i].gameInput.getPlayer();
					}

					threads[i].gameInput.setAction("cardsPlayed");

					ArrayList<Card> playgrndCards = currentGame
							.getPlaygroundCards();
					playgrndCards.clear();
					currentGame.setPlaygroundCards(playgrndCards);

					currentGame
							.setActive_user(leastPoints(sentGame));
					currentGame.setCalcPoints(0);
					
					currentGame.setLast_played(this.gameInput.getPlayer());
					HashMap<Integer, Card> playerCards = playersCards
							.get(players);
					players++;
					currentGame.setPlayerCards(playerCards);

					threads[i].gameInput.setGame(currentGame);
					try {
						threads[i].outServer.writeObject(threads[i].gameInput);
						threads[i].outServer.flush();
						threads[i].outServer.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (maxPoints >= 350) {
			finishGame(maxPoints, mostPoints, sentGame);
		}
	}
	private Player leastPoints(Game sentGame){
		Player mostPoints = null;
		int minPoints = 9999999;
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();

				if (currentGame.getPoints() < minPoints) {
					minPoints = currentGame.getPoints();
					mostPoints = threads[i].gameInput.getPlayer();
				}
			}
		}
		return mostPoints;
	}
	/**
	 * this method is calling the game logic and checks if the played cards are valid
	 * if they are so, they will be removed from the players hand and be added to the game table
	 * if they ar invalid nothing will happen and the player will receive a notPlayable action
	 * @param sentObject
	 * @author Nicolas Sax
	 */
	private void playCards(GameInput sentObject) {
		Game sentGame = sentObject.getGame();
		Game myGame = this.gameInput.getGame();
		HashMap<Integer, Card> playerCards = myGame.getPlayerCards();

		boolean valid = false;
		Game playedGame = this.gameInput.getGame();
		playedGame.setPlayedCards(sentObject.getGame().getPlayedCards());

		valid = gameLogik.checkPlayable(playedGame);
		ArrayList<Integer> deckValues = new ArrayList<Integer>();
		if (valid) {
			// cards playable

			for (Card c : sentGame.getPlayedCards()) {
				deckValues.add(c.getDeckValue());

			}
			ArrayList<Integer> cardKeySet = new ArrayList<Integer>();
			for (int k : playerCards.keySet()) {
				cardKeySet.add(k);
			}
			HashMap<Integer, Card> newPlayerCards = new HashMap<Integer, Card>();
			int cardNo = 0;
			for (int k : cardKeySet) {
				int deckValue = playerCards.get(k).getDeckValue();
				if (!(deckValues.contains(deckValue))) {

					newPlayerCards.put(cardNo, playerCards.get(k));
					cardNo++;
				}
			}

			myGame.setPlayerCards(newPlayerCards);
			this.gameInput.setGame(myGame);
			if (myGame.getPlayerCards().size() == 0) {
				newRound(sentGame);

			} else {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						Game currentGame = threads[i].gameInput.getGame();
						if (currentGame.getGameId()
								.equals(sentGame.getGameId())) {
							currentGame.setCalcPoints(currentGame
									.getCalcPoints()
									+ calcPoints(sentGame.getPlayedCards()));

							try {

								currentGame.setPlaygroundCards(sentGame
										.getPlayedCards());
								currentGame.setLast_played(this.gameInput
										.getPlayer());
								currentGame
										.setActive_user(getNextActivePlayer(currentGame));
								threads[i].gameInput.setAction("cardsPlayed");

								threads[i].gameInput.setGame(currentGame);
								threads[i].outServer
										.writeObject(threads[i].gameInput);
								threads[i].outServer.flush();
								threads[i].outServer.reset();

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {

			try {
				this.gameInput.setAction("notPlayable");
				this.outServer.writeObject(this.gameInput);
				this.outServer.flush();
				this.outServer.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * this method calculates all points of an arraylist of cards together
	 * 
	 * @param playedCards
	 * @return calcPoints
	 * @author Nicolas Sax
	 */
	private int calcPoints(ArrayList<Card> playedCards) {
		int calcPoints = 0;
		for (Card c : playedCards) {
			calcPoints = calcPoints + c.getRankValue();
		}
		return calcPoints;

	}

	/**
	 * this method informs all players of one game that one player left
	 * @param sentObject
	 * @author Nicolas Sax
	 */
	private void leaveGame(GameInput sentObject) {
		Game myGame = this.gameInput.getGame();
		Server server = this.getServer();

		ArrayList<Game> games = (ArrayList<Game>) server.getGames();
		for (int j = 0; j < games.size(); j++) {
			if (games.get(j).getGameId().equals(myGame.getGameId())) {
				games.remove(j);
				break;
			}
		}

		server.setGames(games);
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();

				if (currentGame.getGameId().equals(myGame.getGameId())) {
					threads[i].gameInput.setGames(server.getGames());
					threads[i].gameInput.setGame(null);
					if (threads[i] == this) {
						threads[i].gameInput.setAction("gameLeft");

					} else {

						threads[i].gameInput.setAction("opponentLeft");

					}
					try {

						threads[i].outServer.writeObject(threads[i].gameInput);
						threads[i].outServer.flush();
						threads[i].outServer.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	/**
	 * 
	 * This method retrieves the next player
	 * 
	 * @param game
	 * @return next active player
	 * @author Nicolas Sax
	 */
	private Player getNextActivePlayer(Game game) {
		Player active_user = game.getActive_user();
		int i = 0;
		int noOfPlayers = game.getPlayers().size();
		int activeIndex = 0;
		for (Player p : game.getPlayers()) {
			if (active_user.getUserId().equals(p.getUserId())) {
				activeIndex = i;
			}
			i++;
		}
		if (activeIndex + 1 == noOfPlayers) {
			activeIndex = 0;
		} else {
			activeIndex++;
		}

		return game.getPlayers().get(activeIndex);
	}

	/**
	 * 
	 * this method lets a user join a game as soon the user joined, the cards
	 * will be randomly distributed to the players. each player in the game that
	 * has been joined receives the start command. the creator of the game will
	 * be set as active user and is so allowed to start the game
	 * 
	 * @param sentObject
	 *            gameInput object received from client
	 * @author Nicolas Sax
	 */
	private void joinGame(GameInput sentObject) {
		Game sentGame = sentObject.getGame();
		
		this.gameInput.setGame(sentGame);
		this.gameInput.setAction("gameJoined");
		Player gameCreator = sentGame.getGameCreator();
		try {
			this.outServer.writeObject(this.gameInput);
			this.outServer.flush();
			this.outServer.reset();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		int noOfPlayers = this.gameInput.getPlayers().size();
		HandleCards hc = new HandleCards(noOfPlayers);
		ArrayList<HashMap<Integer, Card>> playersCards = hc.playersCards;
		int players = 0;
		for (int i = 0; i < maxClientsCount; i++) {
			if (threads[i] != null) {
				Game currentGame = threads[i].gameInput.getGame();
				if(currentGame != null){
				ArrayList<Player> gamePlayers = currentGame.getPlayers();
				gamePlayers.add(this.gameInput.getPlayer());
				if (currentGame.getGameId().equals(sentGame.getGameId())) {
					HashMap<Integer, Card> playerCards = playersCards
							.get(players);
					threads[i].gameInput.setAction("gameStarted");
					currentGame.setPlayerCards(playerCards);
					currentGame.setPlayers(gamePlayers);
					currentGame.setActive_user(gameCreator);
					threads[i].gameInput.setGame(currentGame);
					players++;
					try {
						threads[i].outServer.writeObject(threads[i].gameInput);
						threads[i].outServer.flush();
						threads[i].outServer.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				}
			}
		}
		
	}

	/** 
	 * start thread to communicate with one client
	 * @see java.lang.Thread#run()
	 * @author Nicolas Sax
	 **/
	public void run() {

		try {
			outServer = new ObjectOutputStream(socket.getOutputStream());
			outServer.flush();
			inServer = new ObjectInputStream(socket.getInputStream());

			Object o = null;
			//get input from client as log as it does not disconnect
			while (true && !disconnect) {
				// get gameInput Object from server

				o = inServer.readObject();
				System.out.println(o);
				if (o instanceof GameInput) {
					GameInput sentObject = (GameInput) o;
					if (sentObject.getAction() != "") {
						System.out.println(sentObject.getAction());
						if (sentObject.getAction().equals("leaveServer")) {

							disconnect = true;
							break;
						} else if (sentObject.getAction().equals("leaveGame")) {
							leaveGame(sentObject);
						} else if (sentObject.getAction().equals("joinServer")) {
							joinServer(sentObject);
						} else if (sentObject.getAction().equals("createGame")) {
							createGame(sentObject);
						} else if (sentObject.getAction().equals("joinGame")) {
							joinGame(sentObject);
						} else if (sentObject.getAction().equals("playCards")) {
							playCards(sentObject);

						} else if (sentObject.getAction().equals("passCards")) {
							passCards(sentObject);

						} 

					}

				}

			}
			leaveServer();
			this.gameInput.setAction("serverLeft");
			outServer.writeObject(gameInput);
			for (int i = 0; i < maxClientsCount; i++) {
				if (threads[i] == this) {
					threads[i] = null;
				}
			}
			inServer.close();
			outServer.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				System.out.println("Connection Closing..");

				if (inServer != null) {

					inServer.close();
					System.out.println(" Socket Input Stream Closed");

				}

				if (outServer != null) {
					outServer.close();
					System.out.println("Socket Out Closed");
				}
				if (socket != null) {
					socket.close();
					System.out.println("Socket Closed");
					this.interrupt();

				}

			} catch (IOException ie) {
				System.out.println("Socket Close Error");
			}
		}// end finally
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
