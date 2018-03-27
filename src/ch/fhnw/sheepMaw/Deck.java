package ch.fhnw.sheepMaw;

import java.util.HashMap;

public class Deck {

	/**
	 * Klasse Deck
	 * 
	 * F?r die Collection wurde HashMap ausgew?hlt, Sie kann Card Objekte
	 * aufnehmen mit dem Schl?sselverweis (Integer). Der Schl?ssel repr?sentiert
	 * den deckValue von 1 - 45 (die 9 Joker, Queen und K?nigs Karten
	 * ausgenommen). Damit gemeint: HashMap<Referenzchl?ssel, Karte>::: wenn ich
	 * deck.get(Referenzschl?ssel); ausf?hre, erhalte ich das entsprechende
	 * KartenObjekt
	 * 
	 * 
	 */

	static HashMap<Integer, Card> deck_HM = new HashMap<>();

	// Array Rank name constants
	private static int[] rankNames = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	// Array Suit name constants
	private static String[] suitNames = new String[] { "gelb", "gruen",
			"orange", "rot", "grau" };

	public static String[] getSuitNames() {
		return suitNames;
	}

	public Deck(int noOfPlayers) {
		
		int noOfSuits = 0;
		if(noOfPlayers == 3){
			noOfSuits = 5;
		}else{
			noOfSuits = 4;
		}
		fillDeck(noOfSuits);
	}

	private void fillDeck(int noOfSuits) {
		// deck ID ist der eindeutige Schl?ssel um auf die Card Objekte im
		// HashMap zugreifen zu k?nnen.
		int deckID = 0;

		// Alle 45 Karten werden die entsprechenden Farben und Rankings
		// zugeordnet

		for (int i = 0; i < noOfSuits; i++) { // Zuerst werden alle gelben Karten, dann
										// die Grauen, Gr?nen etc...
			for (int j = 0; j < 9; j++) {
				deck_HM.put(deckID,
						new Card(suitNames[i], rankNames[j], deckID));
				deckID++;
				// Hier geschehen 2 Sachen gleichzeitig: Die Collection deck
				// wird mit den Karten abgef?llt, zur gleichen Zeit werden die
				// Card Objekte
				// mit Farben und Rankings instanziert werden.

				// deckID++ wird nach jedem Loop um 1 erh?ht von 0 - 44
			}
		}
		Card joker = new Card("joker", 11, 36);
		Card queen = new Card("queen", 12, 37);
		Card king = new Card("king", 13, 38);

		deck_HM.put(36, joker);
		deck_HM.put(37, queen);
		deck_HM.put(38, king);

	}
}
