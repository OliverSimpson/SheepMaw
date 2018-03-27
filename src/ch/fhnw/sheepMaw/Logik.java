package ch.fhnw.sheepMaw;

import java.util.ArrayList;
import java.util.Arrays;

public class Logik {

	private static boolean playedcardPair;
	private static boolean playedcardStreet;
	private static boolean playedcardPairStreet;
	private static boolean playedcardStreetPair;
	private static boolean playgroundcardPair;
	private static boolean playgroundcardStreet;
	private static boolean playgroundcardPairStreet;
	private static boolean playgroundcardStreetPair;
	private static int playedcardBomb;
	private static int playgroundcardBomb;
	private static boolean higher;
	private static boolean played;
	private static int playgroundcardLength;

	public boolean checkPlayable(Game playingGame) {

		int[] playedcards = new int[playingGame.getPlayedCards().size()];
		int[] playedcardscolor = new int[playingGame.getPlayedCards().size()];
		int[] playgroundcards = new int[playingGame.getPlaygroundCards().size()];
		int[] playgroundcardscolor = new int[playingGame.getPlaygroundCards()
				.size()];
		for (int i = 0; i < playedcards.length; i++) {
			String color = playingGame.getPlayedCards().get(i).getSuitValue();
			if (color.equals("gelb")) {
				playedcardscolor[i] = 1;
			} else if (color.equals("gruen")) {
				playedcardscolor[i] = 2;
			} else if (color.equals("orange")) {
				playedcardscolor[i] = 3;
			} else if (color.equals("rot")) {
				playedcardscolor[i] = 4;
			} else if (color.equals("joker") || color.equals("queen")
					|| color.equals("king")) {
				playedcardscolor[i] = 5;
			}
			playedcards[i] = playingGame.getPlayedCards().get(i).getRankValue();

		}

		for (int i = 0; i < playgroundcards.length; i++) {
			String color = playingGame.getPlaygroundCards().get(i)
					.getSuitValue();
			if (color.equals("gelb")) {
				playgroundcardscolor[i] = 1;
			} else if (color.equals("gruen")) {
				playgroundcardscolor[i] = 2;
			} else if (color.equals("orange")) {
				playgroundcardscolor[i] = 3;
			} else if (color.equals("rot")) {
				playgroundcardscolor[i] = 4;
			} else if (color.equals("joker") || color.equals("queen")
					|| color.equals("king")) {
				playgroundcardscolor[i] = 5;
			}
			playgroundcards[i] = playingGame.getPlaygroundCards().get(i)
					.getRankValue();

		}
		//check bomb
		setPlayedcardBomb(checkBomb(playedcards, playedcardscolor));
		setPlaygroundcardBomb(checkBomb(playgroundcards, playgroundcardscolor));
		if(getPlayedcardBomb() > 0){
			if(getPlaygroundcardBomb() == 0){
				return true;
			}else if(getPlaygroundcardBomb() < getPlayedcardBomb()){
				return true;
			}else{
				return false;
			}
		}
		if ((playgroundcards.length == 0 && playedcards.length == 1)) {
			return true;
		} else if ((playgroundcards.length == 1 && playedcards.length == 1)
				&& playedcards[0] > playgroundcards[0]) {
			return true;
		} else if ((playgroundcards.length == 1 && playedcards.length == 1)
				&& playedcards[0] <= playgroundcards[0]) {
			return false;
		} else if ((playgroundcards.length > 1 && playedcards.length == 1)) {
			return false;
		} else if ((playgroundcards.length == 1 && playedcards.length != 1 && getPlayedcardBomb() == 0)) {
			return false;
		}
		checkPlaygroundcards(playgroundcards, playedcards);
		setPlayedcardPair(checkpair(playedcards));
		setPlayedcardStreet(checkstreet(playedcards, playedcardscolor));
		setPlayedcardPairStreet(checkPaarstreet(playedcards, playedcardscolor));
		setPlayedcardStreetPair(checkStreetPair(playedcards, playedcardscolor));

		setPlaygroundcardPair(checkpair(playgroundcards));
		setPlaygroundcardStreet(checkstreet(playgroundcards,
				playgroundcardscolor));
		setPlaygroundcardPairStreet(checkPaarstreet(playedcards,
				playedcardscolor));
		setPlaygroundcardStreetPair(checkStreetPair(playedcards,
				playedcardscolor));
	

		if (isplayed() == true) {
			checkhigher(playedcards, playgroundcards);
			System.out.println("ist Höher: " + ishigher());
		}

		if (isplayed() == true) {
			checkhigher(playedcards, playgroundcards);
			System.out.println("ist Höher: " + ishigher());
		}

		System.out.println("played: " + isplayed());
		System.out.println("Länge: " + getlength());
		System.out.println("played Paar: " + isPlayedcardPair());
		System.out.println("played street: " + isPlayedcardStreet());
		System.out.println("played Paarstreet: " + isPlayedcardPairStreet());
		System.out.println("played StreetPair: " + isPlayedcardStreetPair());
		System.out.println("Played Bomb: " + getPlayedcardBomb());
		System.out.println("playground Paar: " + isPlaygroundcardPair());
		System.out.println("playground street: " + isPlaygroundcardStreet());
		System.out.println("playground Paarstreet: "
				+ isPlaygroundcardPairStreet());
		System.out.println("playground StreetPair: "
				+ isPlaygroundcardStreetPair());
		System.out.println("Playground Bomb: " + getPlaygroundcardBomb());

		for (int i = 0; i < playedcards.length; i++) {
			// System.out.print(playedcards.get(i).getRankValue() + " ");

		}
		if (isPlayedcardPair() && playedcards.length != playgroundcards.length
				&& playgroundcards.length > 0) {
			return false;
		}
		if (ishigher()) {
			System.out.println("OK");
			return true;
		} else if (!isplayed()
				&& (isPlayedcardPair() || isPlayedcardStreet()
						|| isPlayedcardPairStreet() || isPlayedcardStreetPair())) {
			System.out.println("OK");
			return true;
		} else {
			System.out.println("NOT OK");
			return false;
		}

	}

	public static void checkPlaygroundcards(int[] playgroundcards,
			int[] playedcards) {
		Arrays.sort(playgroundcards);
		Arrays.sort(playedcards);
		if (playgroundcards.length > 0) {
			setplayed(true);
			if (playgroundcards.length == playedcards.length) {
				setlength(playgroundcards.length);
			}

		} else {
			setplayed(false);
		}
	}

	public static int checkBomb(int[] cards, int[] color) {
		ArrayList<int[]> cardsColor = sortColorCards(cards, color);
		cards = cardsColor.get(0);
		color = cardsColor.get(1);

		int[] originalCards = new int[cards.length];
		System.arraycopy(cards, 0, originalCards, 0, cards.length);
		int bomb = 0;

		if (cards.length == 2) {
			if (cards[0] == 11 && cards[1] == 12) {
				bomb = 2;
			}
			if (cards[0] == 11 && cards[1] == 13) {
				bomb = 3;
			}
			if (cards[0] == 12 && cards[1] == 13) {
				bomb = 4;
			}
		}
		if (cards.length == 3 && cards[0] == 11 && cards[1] == 12
				&& cards[2] == 13) {
			bomb = 5;
		}
		if (cards.length == 4) {
			if (cards[0] == 3 && cards[1] == 5 && cards[2] == 7
					&& cards[3] == 9) {
				if (color[0] == color[1] && color[1] == color[2]
						&& color[2] == color[3]) {
					bomb = 6;
				} else {
					bomb = 1;
				}
			}
		}
		if (bomb == 0) {
			for (int i = 0; i < cards.length; i++) {
				cards[i] = originalCards[i];
			}
		}

		return bomb;
	}

	public static boolean checkpair(int[] cards) {
		Arrays.sort(cards);

		int count = 0;

		boolean pair = false;
		int[] originalCards = new int[cards.length];
		System.arraycopy(cards, 0, originalCards, 0, cards.length);

		for (int i = 0; i < cards.length; i++) {
			if (cards[i] > 10) {
				cards[i] = cards[0];
			}
		}
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == (cards[0])) {
				count++;
			}
		}
		if (count == cards.length) {
			pair = true;
		} else {
			for (int i = 0; i < cards.length; i++) {
				cards[i] = originalCards[i];
			}
		}
		return pair;
	}

	public static boolean checkstreet(int[] cards, int[] color) {
		ArrayList<int[]> cardsColor = sortColorCards(cards, color);
		cards = cardsColor.get(0);
		color = cardsColor.get(1);
		int count = 0;
		int countcolor = 0;
		boolean street = false;

		int[] originalCards = new int[cards.length];
		System.arraycopy(cards, 0, originalCards, 0, cards.length);

		for (int i = 0; i < cards.length; i++) {
			if (color[1] == color[i] || cards[i] > 10) {
				countcolor++;
			}
		}
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == ((cards[0]) + i) || cards[i] > 10) {
				if (cards[i] > 10) {

					if (i > 0) {
						cards[i] = cards[i - 1] + 1;
					} else {
						cards[i] = cards[i + 1] - 1;
					}
				}
			} else {
				zwei: for (int j = 0; j < cards.length; j++) {
					if (cards[j] > 10) {
						for (int k = 0; k <= (j - i); k++) {
							int temp = 0;
							temp = cards[j];
							cards[j] = cards[i];
							if (i > 0) {
								cards[i] = cards[i - 1] + 1;
							} else {
								cards[i] = cards[i + 1] - 1;
							}
							Arrays.sort(cards);
							break zwei;

						}
					}
				}
			}
		}
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == ((cards[0]) + i) || cards[i] > 10) {
				count++;
			}
		}

		if (countcolor == color.length && count == cards.length && count > 2) {
			street = true;

		} else {

			for (int i = 0; i < cards.length; i++) {
				cards[i] = originalCards[i];
			}
		}
		return street;
	}

	public static ArrayList<int[]> sortColorCards(int[] cards, int[] color) {
		int temp;
		int temp2;
		for (int i = 1; i < cards.length; i++) {
			temp = cards[i];
			temp2 = color[i];
			int j = i;
			while (j > 0 && cards[j - 1] > temp) {
				cards[j] = cards[j - 1];
				color[j] = color[j - 1];
				j--;
			}
			cards[j] = temp;
			color[j] = temp2;
		}
		ArrayList<int[]> cardsColor = new ArrayList<int[]>();
		cardsColor.add(cards);
		cardsColor.add(color);
		return cardsColor;
	}

	public static boolean checkPaarstreet(int[] cards, int[] color) {
		ArrayList<int[]> cardsColor = sortColorCards(cards, color);
		cards = cardsColor.get(0);
		color = cardsColor.get(1);
		int countA = 0;
		int countB = 0;
		int countcolor = 0;
		int tempLeft;
		int tempRight;
		boolean paarStreet = false;
		int[] originalCards = new int[cards.length];
		System.arraycopy(cards, 0, originalCards, 0, cards.length);

		for (int i = 0; i < color.length / 2; i++) {
			if (color[i] == color[color.length / 2 + i]) {
				countcolor++;
			}
		}
		for (int i = color.length / 2; i < color.length; i++) {
			if (cards[i] > 10) {
				countcolor++;
			}
		}

		for (int i = 0; i < (cards.length / 2); i++) {
			if (cards[i] == (cards[0]) || cards[i] > 10) {
				countA++;
				if (cards[i] > 10) {
					tempLeft = cards[i];
					cards[i] = cards[0];
				}
			} else {
				eins: for (int j = (cards.length / 2); j < cards.length; j++) {
					if (cards[j] > 10) {
						countA++;
						tempLeft = cards[i];
						cards[i] = cards[0];
						cards[j] = tempLeft;
						break eins;
					}
				}
			}
		}
		for (int i = cards.length / 2; i < cards.length; i++) {
			if (cards[i] == cards[cards.length / 2] || cards[i] > 10) {
				countB++;
				if (cards[i] > 10) {
					tempRight = cards[i];
					cards[i] = cards[cards.length / 2];

				}
			}
		}

		if (countcolor * 2 == color.length && countA + countB == cards.length
				&& countA + countB > 2
				&& cards[0] == (cards[cards.length - 1] - 1)) {
			originalCards = cards;
			paarStreet = true;

		} else {
			for (int i = 0; i < cards.length; i++) {
				cards[i] = originalCards[i];
			}
		}

		return paarStreet;
	}

	public static boolean checkStreetPair(int[] cards, int[] color) {
		ArrayList<int[]> cardsColor = sortColorCards(cards, color);
		cards = cardsColor.get(0);
		color = cardsColor.get(1);
		int count = 0;
		boolean streetPair = false;
		int[] originalCards = new int[cards.length];
		System.arraycopy(cards, 0, originalCards, 0, cards.length);

		for (int i = 0; i < cards.length; i++) {
			if (i % 2 == 0) {
				if (i > 0) {
					if (cards[i] == (cards[i - 1] + 1) || cards[i] > 10
							&& cards[i] == (color[i - 1] + 1) || cards[i] > 10) {
						count++;
						if (cards[i] > 10) {
							cards[i] = (cards[i - 1] + 1);

						}
					} else {
						eins: for (int j = 0; j > cards.length; j++) {
							if (cards[j] > 10) {
								count++;
								cards[j] = cards[i];
								cards[i] = cards[i - 1] + 1;
								Arrays.sort(cards);
								break eins;
							}
						}
					}
				} else {
					count++;

				} // else
			} // if modulo
			else {
				if (cards[i] == cards[i - 1] || cards[i] > 10
						&& color[i] == cards[i - 1] || cards[i] > 10) {
					count++;
					if (cards[i] > 10) {
						cards[i] = (cards[i - 1]);
					}
				} else {
					zwei: for (int j = 0; j < cards.length; j++) {
						if (cards[j] > 10) {
							count++;
							cards[j] = cards[i];
							cards[i] = cards[i - 1];
							Arrays.sort(cards);
							break zwei;
						}
					}
				}
			}
		}

		if (count == cards.length) {
			streetPair = true;
			for (int i = 0; i < cards.length; i++) {
			}
		} else {

			for (int i = 0; i < cards.length; i++) {
				cards[i] = originalCards[i];
			}
		}
		return streetPair;
	}

	public static void checkhigher(int[] playedcards, int[] playgroundcard) {
		if (isPlaygroundcardPair() == true && isPlayedcardPair() == true
				&& playedcards[0] > playgroundcard[0]
				&& playedcards.length == playgroundcard.length
				|| getPlayedcardBomb() > getPlaygroundcardBomb()) {
			sethigher(true);
		}
		if (isPlaygroundcardStreet() == true && isPlayedcardStreet() == true
				&& playedcards[0] > playgroundcard[0]
				&& playedcards.length == playgroundcard.length
				|| getPlayedcardBomb() > getPlaygroundcardBomb()) {
			sethigher(true);
		}

		if (isPlaygroundcardPairStreet() == true
				&& isPlayedcardPairStreet() == true
				&& playedcards[0] > playgroundcard[0]
				&& playedcards.length == playgroundcard.length
				|| getPlayedcardBomb() > getPlaygroundcardBomb()) {
			sethigher(true);
		}

		if (isPlaygroundcardStreetPair() == true
				&& isPlayedcardStreetPair() == true
				&& playedcards[0] > playgroundcard[0]
				&& playedcards.length == playgroundcard.length
				|| getPlayedcardBomb() > getPlaygroundcardBomb()) {
			sethigher(true);
		}

	}

	public static boolean isPlayedcardPairStreet() {
		return playedcardPairStreet;
	}

	public static void setPlayedcardPairStreet(boolean playedcardPairStreet) {
		Logik.playedcardPairStreet = playedcardPairStreet;
	}

	public static boolean isPlaygroundcardPair() {
		return playgroundcardPair;
	}

	public static void setPlaygroundcardPair(boolean playgroundcardPair) {
		Logik.playgroundcardPair = playgroundcardPair;
	}

	public static boolean isPlaygroundcardStreet() {
		return playgroundcardStreet;
	}

	public static void setPlaygroundcardStreet(boolean playgroundcardStreet) {
		Logik.playgroundcardStreet = playgroundcardStreet;
	}

	public static int getlength() {
		return playgroundcardLength;
	}

	public static void setlength(int length) {
		Logik.playgroundcardLength = length;
	}

	public static boolean isplayed() {
		return played;
	}

	public static void setplayed(boolean played) {
		Logik.played = played;
	}

	public static boolean ishigher() {
		return higher;
	}

	public static void sethigher(boolean higher) {
		Logik.higher = higher;
	}

	public static boolean isPlayedcardPair() {
		return playedcardPair;
	}

	public static boolean isPlayedcardStreet() {
		return playedcardStreet;
	}

	public static void setPlayedcardStreet(boolean playedcardStreet) {
		Logik.playedcardStreet = playedcardStreet;
	}

	public static void setPlayedcardPair(boolean playedcardPair) {
		Logik.playedcardPair = playedcardPair;
	}

	public static boolean isPlayedcardStreetPair() {
		return playedcardStreetPair;
	}

	public static void setPlayedcardStreetPair(boolean playedcardStreetPair) {
		Logik.playedcardStreetPair = playedcardStreetPair;
	}

	public static boolean isPlaygroundcardPairStreet() {
		return playgroundcardPairStreet;
	}

	public static void setPlaygroundcardPairStreet(
			boolean playgroundcardPairStreet) {
		Logik.playgroundcardPairStreet = playgroundcardPairStreet;
	}

	public static boolean isPlaygroundcardStreetPair() {
		return playgroundcardStreetPair;
	}

	public static void setPlaygroundcardStreetPair(
			boolean playgroundcardStreetPair) {
		Logik.playgroundcardStreetPair = playgroundcardStreetPair;
	}

	public static int getPlaygroundcardBomb() {
		return playgroundcardBomb;
	}

	public static void setPlaygroundcardBomb(int playgroundcardBomb) {
		Logik.playgroundcardBomb = playgroundcardBomb;
	}

	public static int getPlayedcardBomb() {
		return playedcardBomb;
	}

	public static void setPlayedcardBomb(int playedcardBomb) {
		Logik.playedcardBomb = playedcardBomb;
	}

}
