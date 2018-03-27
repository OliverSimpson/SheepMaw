package ch.fhnw.sheepMaw;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * this class holds all data that is sent between client and server
 * e.g. list of players on the server list of games on the server ...
 * @author Nicolas Sax
 *
 */
public class GameInput implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player player = null;
	private String action = new String(); // actios (joinServer, serverJoined,
											// playCards, playSucces,
											// playFailed, yourTurn.....)
	private Game game = null;
	private Player active_user = null; // user who has to play cards
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Game> games = new ArrayList<Game>();

	/**
	 * @return the user_id
	 */
	public GameInput() {

	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the active_user
	 */
	public Player getActive_user() {
		return active_user;
	}

	/**
	 * @param active_user
	 *            the active_user to set
	 */
	public void setActive_user(Player active_user) {
		this.active_user = active_user;
	}

	/**
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	/**
	 * @return the games
	 */
	public ArrayList<Game> getGames() {
		return games;
	}

	/**
	 * @param games
	 *            the games to set
	 */
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}