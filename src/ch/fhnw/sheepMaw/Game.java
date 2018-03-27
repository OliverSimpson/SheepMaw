package ch.fhnw.sheepMaw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * This class holds all data relevant for a game
 * @author Nicolas Sax
 *
 */
public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameId;
	private int points = 0;
	private int calcPoints;
	private int passCount = 0;
	private Player gameCreator;
	private Player active_user;
	private Player last_played;
	private ArrayList<Player> players = new ArrayList<Player>();
	private HashMap<Integer, Card> playerCards = new HashMap<Integer, Card>();
	private ArrayList<Card> playedCards = new ArrayList<Card>();
	private ArrayList<Card> playgroundCards = new ArrayList<Card>();

	public Game(String gameId, Player gameCreator) {
		this.setGameId(gameId);
		this.setGameCreator(gameCreator);
	}

	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId
	 *            the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
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
	 * @return the playerCards
	 */
	public HashMap<Integer, Card> getPlayerCards() {
		return playerCards;
	}

	/**
	 * @param playerCards
	 *            the playerCards to set
	 */
	public void setPlayerCards(HashMap<Integer, Card> playerCards) {
		this.playerCards = playerCards;
	}

	/**
	 * @return the playedCards
	 */
	public ArrayList<Card> getPlayedCards() {
		return playedCards;
	}

	/**
	 * @param playedCards
	 *            the playedCards to set
	 */
	public void setPlayedCards(ArrayList<Card> playedCards) {
		this.playedCards = playedCards;
	}

	/**
	 * @return the playgroundCards
	 */
	public ArrayList<Card> getPlaygroundCards() {
		return playgroundCards;
	}

	/**
	 * @param playgroundCards
	 *            the playgroundCards to set
	 */
	public void setPlaygroundCards(ArrayList<Card> playgroundCards) {
		this.playgroundCards = playgroundCards;
	}

	public Player getGameCreator() {
		return gameCreator;
	}

	public void setGameCreator(Player gameCreator) {
		this.gameCreator = gameCreator;
	}

	@Override
	public String toString() {
		return this.gameCreator.getUserName() + "'s game";
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Player getLast_played() {
		return last_played;
	}

	public void setLast_played(Player last_played) {
		this.last_played = last_played;
	}

	public int getCalcPoints() {
		return calcPoints;
	}

	public void setCalcPoints(int calcPoints) {
		this.calcPoints = calcPoints;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

}
