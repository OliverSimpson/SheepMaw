package ch.fhnw.sheepMaw;

import java.io.Serializable;

public class Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Klasse Card
	 * 
	 * Array suitNames speichert und repr???sentiert alle 5 Farben des
	 * Kartenobjekts Array rankNames speichert und repr???sentiert jeden Rang
	 * den die Karte einnimmt
	 */

	// Card values
	// private static int deckValue; //total 54
	private int rankValue; // 2 - 10
	private String suitValue; // 1 - 5
	private int deckValue;

	// Konstruktor
	public Card(String suitValue, int rankValue, int deckValue) {

		this.setRankValue(rankValue);
		this.setSuitValue(suitValue);
		this.deckValue = deckValue;
	}

	public Card(String royalty, int deckValue) {
		this.deckValue = deckValue;
		setSuitValue(royalty);
	}

	public int getDeckValue() {
		return this.deckValue;
	}

	public String getSuitValue() {
		return suitValue;
	}

	public void setSuitValue(String suitValue) {
		this.suitValue = suitValue;
	}

	public int getRankValue() {
		return rankValue;
	}

	public void setRankValue(int rankValue) {
		this.rankValue = rankValue;
	}
}
