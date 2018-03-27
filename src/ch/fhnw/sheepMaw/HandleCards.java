package ch.fhnw.sheepMaw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

public class HandleCards implements Serializable {

	ArrayList<HashMap<Integer, Card>> playersCards = new ArrayList<HashMap<Integer, Card>>();
	Card cardShell;


	// Card Instances

	Graphical_Card_Label card;

	int[] Array_of_Shuffeled_Indices;
	String currentPlayer;

	public int i = 0;

	public HandleCards(int noOfPlayers) {
		int StackOfCards = 14 * noOfPlayers;
		// 1. Erzeuge Random Array
		Array_of_Shuffeled_Indices = new int[StackOfCards];
		shuffle_Array(Array_of_Shuffeled_Indices);

		Deck deck = new Deck(noOfPlayers);

		// deck1.shuffle();
		int selectCard = 0;
		for (int j = 0; j < noOfPlayers; j++) {
			HashMap<Integer, Card> playerCards = new HashMap<Integer, Card>();
			for (int i = 0; i < 14; i++) {
				playerCards.put(i, Deck.deck_HM
						.get(Array_of_Shuffeled_Indices[selectCard]));
				Deck.deck_HM.remove(Array_of_Shuffeled_Indices[selectCard]);
				selectCard++;
			}
			playerCards.put(14, new Card("joker", 11, 36));
			playerCards.put(15, new Card("queen", 12, 37));
			playerCards.put(16, new Card("king", 13, 38));
			playersCards.add(playerCards);

		}

	}

	public void revalidate(JFrame frame) {
		frame.revalidate();
	}

	public String whichPlayer() {

		i++;
		String s = "player";

		currentPlayer = s + i;

		return currentPlayer;

	}

	// --------------------------Spielkarten
	// Objekte-------------------------------------------

	// Hand Karten ==> ?bergabeparameter: Array_of_Shuffeled_Indices[i] und
	// welchen Spieler

	// ?bergabeParameter Array_of_Shuffeled_Indices[i]

	/*
	 * public void card(int [] randomArray){ //Aus der HashMap hand_forPlayer1
	 * wird die aktuelle Karte in //der tempor?ren globalen Variable cardShell
	 * abgespeichert for(int i = 0; i < 14; i++){ cardShell = (Card)
	 * hand.getHand_forPlayer1().get(i); } }
	 */

	// -------------------------------- Hilfsmethoden
	// -------------------------------------

	public int[] shuffle_Array(int[] random_int) {

		for (int i = 0; i < random_int.length; i++) {

			int r1 = random_Nr_Generator();

			random_int[i] = r1;

			// searches for duplicates. If so: repeat!
			for (int j = 0; j < i; j++) {
				if (random_int[j] == r1) {
					i--;
				}
			}
		}

		return random_int;

	}

	// Hilfsmethode zu shuffle_Array(), R?ckgabewert eine Zufallszahl zwischen 1
	// - 45
	public int random_Nr_Generator() {
		int r = (int) (Math.random() * 35 + 0);
		return r;
	}

	public int[] getArray() {
		return this.Array_of_Shuffeled_Indices;
	}

}
