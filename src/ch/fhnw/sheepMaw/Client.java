package ch.fhnw.sheepMaw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client implements Runnable {

	private ObjectOutputStream outClient;
	private ObjectInputStream inClient;
	private Socket socket; // Verbindung
	public PlayerOverviewGUI poGUI = null;
	private GameInput gameInput;
	public static GameTableGUI gameTable;

	public static LoginGUI lGui;

	public Client(LoginGUI loginGUI) {
		Client.lGui = loginGUI;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the gameInput
	 */
	public GameInput getGameInput() {
		return gameInput;
	}

	/**
	 * @param gameInput
	 *            the gameInput to set
	 */
	public void setGameInput(GameInput gameInput) {
		this.gameInput = gameInput;
	}

	@Override
	public void run() {

		receiveOutputfromServer();

	}
	//Start Server if "Start Server" pressed in joinGameGUI Class
	public void createServer() {
		new Thread(new Server()).start();

	}

	// --------------------------------------Methoden f???r
	// Client--------------------------------------------------------

	/**
	 * @author Oliver Ostermann
	 * @throws IOException
	 *             receiveOutputfromServer()
	 * 
	 *             inClient (ObjectInputStream) listens to all Byte-Streams received
	 *             from the server. readObject is used to accept Byte-Streams and parse them
	 *             back into Objects.
	 * 
	 *             Object o can only be an instance of the GameInput class for further handling.
	 * @throws
	 */

	private void receiveOutputfromServer() {

		try {
			Object o = null;
			/** read stuff until we receive the disconnect command
			 * 
			 */
			while (true) {
				o = inClient.readObject();
				System.out.println(o);
				if (o instanceof GameInput) {

					GameInput receivedObject = (GameInput) o;

					System.out.println(receivedObject.getAction());
					if (receivedObject.getAction().equals("serverJoined")
							|| receivedObject.getAction().equals("playerLeft")
							|| receivedObject.getAction().equals("gameLeft")) {
						if( receivedObject.getAction().equals("gameLeft")){
							poGUI.newGameButton.setEnabled(true);
						}
						reloadGameOverview(receivedObject);
						

					} else if (receivedObject.getAction()
							.equals("opponentLeft")) {
						poGUI.newGameButton.setEnabled(true);
						poGUI.setVisible(true);
						gameTable.setVisible(false);

						reloadGameOverview(receivedObject);
					} else if (receivedObject.getAction().equals("gameAdded")) {

						poGUI.gameView.removeAll();

						Game[] games = new Game[receivedObject.getGames()
								.size()];
						int i = 0;
						for (Game g : receivedObject.getGames()) {
							if (!g.getGameCreator()
									.getUserId()
									.equals(receivedObject.getPlayer()
											.getUserId())) {

								System.out.println(g.getGameCreator()
										.getUserName() + "'s Game");
								games[i] = g;
								// poGUI.addToGameOverview(g.getGameCreator().getUserName()
								// + "'s Game", g.getGameId());
							}
							i++;
						}
						poGUI.gameView.setListData(games);

						poGUI.gameView.revalidate();
					} else if (receivedObject.getAction().equals("gameCreated")) {
						poGUI.newGameButton.setEnabled(false);
						poGUI.repaint();
					}
					
					/**gameStarted
					 * 
					 * this command instantiates the GameTableGUI. The player is then presented with a "playable" GUI
					 */

					else if (receivedObject.getAction().equals("gameStarted")) {

						gameTable = new GameTableGUI(receivedObject.getPlayer()
								.getUserName(), receivedObject, this);
						poGUI.setVisible(false);
					}
					 /**cardsPlayed:
					 * after the drawn cards were excepted in the logic of the server, the cards are
					 * painted in the headPanel of GameTableGui.
					 * 
					 * Depending on whose turn, the buttons play and pass are enabled or disabled.
					 * Playground cards are finally put into the gameTable headPanel.
					 * 
					 * 
					 */
					
					else if (receivedObject.getAction().equals("cardsPlayed")) {

						gameTable.cardDump.removeAll();
						gameTable.gameInput = receivedObject;
						if (receivedObject.getGame().getPlayerCards().size() == 17) {
							gameTable.deck = new Deck(receivedObject.getGame().getPlayers().size());
						}

						if (!receivedObject.getGame().getActive_user()
								.getUserId()
								.equals(receivedObject.getPlayer().getUserId())) {
							gameTable.play.setEnabled(false);
							gameTable.pass.setEnabled(false);
							gameTable.action.setText("WAITING FOR OPPONENT...");
						} else {
							gameTable.action.setText("YOUR TURN!");
							gameTable.play.setEnabled(true);
							gameTable.pass.setEnabled(true);
						}
						gameTable.points.setText("Points: "
								+ receivedObject.getGame().getPoints());
						gameTable.handPanel(receivedObject.getGame());
						for (int i = 0; i < receivedObject.getGame()
								.getPlaygroundCards().size(); i++) {
							int dV = receivedObject.getGame()
									.getPlaygroundCards().get(i).getDeckValue();
							gameTable.revalidatePlayField(dV);

						}

						gameTable.repaint();
						GameTableGUI.hand_Panel.revalidate();
					}
					/**
					 * The new active user is set.
					 * 
					 * Play and Pass are either disabled or enabled depending on whose turn.
					 * 
					 * If passed the points are revalidated in the gametable.
					 */
					else if (receivedObject.getAction().equals("cardsPassed")) {

						gameTable.cardDump.removeAll();
						gameTable.gameInput = receivedObject;

						if (!receivedObject.getGame().getActive_user()
								.getUserId()
								.equals(receivedObject.getPlayer().getUserId())) {
							gameTable.action.setText("WAITING FOR OPPONENT...");
							gameTable.play.setEnabled(false);
							gameTable.pass.setEnabled(false);
						} else {
							gameTable.action.setText("YOUR TURN!");
							gameTable.play.setEnabled(true);
							gameTable.pass.setEnabled(true);
						}
						gameTable.handPanel(receivedObject.getGame());
						for (int i = 0; i < receivedObject.getGame()
								.getPlaygroundCards().size(); i++) {
							int dV = receivedObject.getGame()
									.getPlaygroundCards().get(i).getDeckValue();
							gameTable.revalidatePlayField(dV);

						}
						gameTable.points.setText("Points: "
								+ receivedObject.getGame().getPoints());

						gameTable.repaint();
						GameTableGUI.hand_Panel.revalidate();
					}
					
					/**gameFinished:
					 * As soon as someone won the game, the play and pass button in the GameTableGUI is
					 * disabled and on the GUI the winner is displayed.
					 */
					
					else if (receivedObject.getAction().equals("gameFinished")) {
						poGUI.newGameButton.setEnabled(true);
						gameTable.cardDump.removeAll();
						gameTable.gameInput = receivedObject;

						gameTable.play.setEnabled(false);
						gameTable.pass.setEnabled(false);

						gameTable.handPanel(receivedObject.getGame());
						for (int i = 0; i < receivedObject.getGame()
								.getPlaygroundCards().size(); i++) {
							int dV = receivedObject.getGame()
									.getPlaygroundCards().get(i).getDeckValue();
							gameTable.revalidatePlayField(dV);

						}
						gameTable.points.setText("Points: "
								+ receivedObject.getGame().getPoints());

						
					
						gameTable.action.setText("GAME FINISHED: Player "
								+ receivedObject.getGame()
								.getActive_user().getUserName()
						+ " WON");
						GameTableGUI.hand_Panel.revalidate();
						gameTable.repaint();
					} else if (receivedObject.getAction().equals("serverLeft")) {
						System.out.println("Server disconnected!");
						break;
					}else if (receivedObject.getAction().equals("notPlayable")) {
						gameTable.action.setText("Cards not playable!");
						gameTable.repaint();
					}
				}
			}
			System.out.println("Close client connection");
			outClient.close();
			inClient.close();
			socket.close();
			lGui.setVisible(true);

		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

		}

	}
	
	/**
	 * method reloadGameOverview
	 * 
	 * Should the player decide to exit the GameTableGUI, this method is called to reopen the GameOverview.
	 * 
	 * @param receivedObject
	 */

	private void reloadGameOverview(GameInput receivedObject) {
		if (poGUI == null) {
			poGUI = new PlayerOverviewGUI("Overview for "
					+ gameInput.getPlayer().getUserName(), receivedObject, this);
		}
		
		poGUI.playerView.removeAll();
		for (Player p : receivedObject.getPlayers()) {
			System.out.println(p.getUserName());

			poGUI.addToPlayerOverview(p.getUserName());

		}
		poGUI.playerView.repaint();
		poGUI.playerView.revalidate();

		Game[] games = new Game[receivedObject.getGames().size()];
		int i = 0;
		for (Game g : receivedObject.getGames()) {
			if (!g.getGameCreator().getUserId()
					.equals(receivedObject.getPlayer().getUserId())) {
				System.out
						.println(g.getGameCreator().getUserName() + "'s Game");
				games[i] = g;
				
			}
			i++;
		}
		poGUI.gameView.removeAll();
		poGUI.gameView.setListData(games);
		poGUI.gameView.repaint();
		poGUI.gameView.revalidate();
		poGUI.revalidate();
	}

	/**
	 * 
	 * @author Oliver Ostermann
	 * 
	 * the connect method:
	 * 
	 * @param host
	 *     - the host-name of the server (IP)
	 *
	 * As soon as the connect method obtains the IP address, a new socket is then created
	 * and a connection request is made to the server.
	 * 
	 *
	 */

	void connect(String host) {
		try {
			try {
				socket = new Socket(host, 40000);
			} catch (ConnectException ce) {
				JOptionPane.showMessageDialog(null,
						"No available host found to requested IP");
			}
			setupStreams();

			System.out.println("Connection is up and running...");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The sendData method serializes any gameInput object, which is then sent to the server.
	 * 
	 * @param gameInput
	 *            
	 * @throws IOException
	 * throws IOExcpection if transmission of the byte streams failed. For example EOFException could be
	 * thrown if the end of the file was reached unexpectedly.
	 * 
	 * 
	 */
	public void sendData(GameInput gameInput) {
		try {
			outClient.writeObject(gameInput);
			outClient.flush();
			outClient.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author Oliver Ostermann
	 * setupStreams():
	 * 
	 * ObjectOutputStream outClient: The ObjectOutputStream serialises Objects,
	 * Arrays and other values into a stream (Serialise: Converts objects into
	 * bytestreams). Only objects that implement the Interface Serializable
	 * can be serialized! The class gameInput is our serializable object.
	 * 
	 * Method setUpStreams();
	 * Both objects outClient und inClient are instanciated in this method.
	 * Assuming the sockets has built a connection to a channel, the OutputStream
	 * delegates all its operations to this channel.
	 *  
	 * 
	 * TCP is our point-to-point channel where the client- and serverprogram
	 * are connected to each other.
	 *
	 * 
	 *
	 * 
	 * Should the Outputstream be unexpectedly or intentionally closed, it corresponding socket
	 * will be closed too.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */

	private void setupStreams() {

		try {
			outClient = new ObjectOutputStream(socket.getOutputStream());
			outClient.flush();
			inClient = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
