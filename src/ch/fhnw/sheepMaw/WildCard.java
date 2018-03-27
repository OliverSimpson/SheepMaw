package ch.fhnw.sheepMaw;

public class WildCard extends Card {

	/**
	 * Klasse WildCard
	 * 
	 * NOCH NICHT AUSGEREIFT, BRAUCHT ?BERARBEITUNG
	 */

	private static int wildCardValue;

	public static int deckValue = 11;
	public static String wildCardName;

	public static String[] wildCardNames = new String[] { "Jack", "Queen",
			"King" };

	public WildCard(String wildCardName, int deckValue) {

		super(wildCardName, deckValue);
		WildCard.wildCardName = wildCardName;
		WildCard.deckValue = deckValue;
	}

	public static String getWildCardNames(int i) {
		return wildCardNames[i];

	}

}
