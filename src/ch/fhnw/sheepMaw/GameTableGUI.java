package ch.fhnw.sheepMaw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameTableGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public int deckValue;

	private ArrayList<Integer> sortedTempArray = new ArrayList<>();


	ArrayList<Card> played_Cards;

	
	private boolean sortedByVal = false;
	private boolean sortedByCol = false;
	// Panels

	JPanel cardDump = new JPanel();
	JPanel mainControls_Panel = new JPanel();
	static JPanel hand_Panel = new JPanel();

	static Player player;

	public JButton play;
	public JButton pass;
	public JLabel points;
	public JLabel action;
	JLabel greenTableLabel;
	Sound sound;
	static Graphical_Card_Label gcl = new Graphical_Card_Label();

	JPanel mainPanel = new JPanel();

	static ArrayList<Integer> list_GCL;

	HashMap<Integer, Graphical_Card_Label> graf_Karten_hm;

	int[] randomArray;

	private JPanel wildCardsPanel = new JPanel();

	Deck deck = null;

	GameInput gameInput;
	Client client;

	String whichPlayer;

	private int totalWidth = 800;

	private int totalHeight = 950;

	private JPanel head_Panel = new JPanel();
	
	/**
	 * @author Oliver Ostermann
	 */

	// Konstruktor
	public GameTableGUI(String whichPlayer, GameInput gameInput,
			final Client client) {

		super(whichPlayer + "'s Gametable");
		deck = new Deck(gameInput.getGame().getPlayers().size());
		this.client = client;
		this.gameInput = gameInput;
		this.whichPlayer = whichPlayer;

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GameInput leaveGame = new GameInput();
				leaveGame.setAction("leaveGame");
				client.sendData(leaveGame);
				client.poGUI.setVisible(true);
			}
		});

		gcl.loadCardImages(gcl.suitColor, gcl.nr);
		gcl.loadWildCardImages();
		gcl.assign_Graf_Karten_to_HashMap();

		graf_Karten_hm = gcl.getHashMap();

		list_GCL = new ArrayList<Integer>();

		played_Cards = new ArrayList<>();

		// Main Panel mit Bild

		mainPanel = new JPanel() {

			
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				try {
					BufferedImage image = ImageIO.read(getClass()
							.getClassLoader().getResource(
									"ch/fhnw/sheepMaw/images/green.jpg"));
					Image image1 = image.getScaledInstance(getWidth(),
							getHeight(), Image.SCALE_SMOOTH);
					g.drawImage(image1, 0, 0, null);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		};

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setPreferredSize(new Dimension(totalWidth, totalHeight));

		getContentPane().add(mainPanel);

		// Panel Methods
		// 1*

		headPanel();

		// 2*

		mainPanel.add(hand_Panel);

		hand_Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		hand_Panel.setPreferredSize(new Dimension(totalWidth, 260));

		hand_Panel.setOpaque(false);
		handPanel(gameInput.getGame());

		// 3*
		wildCards(gameInput.getGame());
		
		JPanel messages = new JPanel();
		
		
		messages.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
		messages.setPreferredSize(new Dimension(totalWidth, 35));
		messages.setOpaque(false);
		
		
		action = new JLabel();
		
		messages.add(action);
		action.setText("Welcome!");
		fontSettings(action);
		mainPanel.add(messages);
		
		
		// 4*
		buttonControls();

		if (!gameInput.getGame().getActive_user().getUserId()
				.equals(gameInput.getPlayer().getUserId())) {
			play.setEnabled(false);
			pass.setEnabled(false);
		}
		JPanel infoPanel = new JPanel();
		points = new JLabel();
		points.setText("Points: 0");
		infoPanel.add(points);
		infoPanel.setPreferredSize(new Dimension(totalWidth, 20));
		infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		infoPanel.setOpaque(false);
		fontSettings(points);
		mainPanel.add(infoPanel);
		setVisible(true);
		pack();
	}

	// ----------------------------------------- Panel Methods
	// -------------------------------------------------------
	// 1* Head
	private void headPanel() {

		mainPanel.add(cardDump);
		cardDump.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
		cardDump.setPreferredSize(new Dimension(totalWidth, 200));
		cardDump.setOpaque(false);
		cardDump.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	}

	// 2* Hand
	public void handPanel(Game gamePlaying) {

		gcl.removeAll();
		hand_Panel.removeAll();
		wildCardsPanel.removeAll();
		sortedTempArray.clear();

		System.out.println("hand_Panel properties loaded");

		for (int j = 0; j < gamePlaying.getPlayerCards().size(); j++) {

			if (gamePlaying.getPlayerCards().get(j).getRankValue() != 11
					&& gamePlaying.getPlayerCards().get(j).getRankValue() != 12
					&& gamePlaying.getPlayerCards().get(j).getRankValue() != 13) {
				Graphical_Card_Label gcl = graf_Karten_hm.get(gamePlaying
						.getPlayerCards().get(j).getDeckValue());
				sortedTempArray.add(gamePlaying.getPlayerCards().get(j)
						.getDeckValue());
				try {
					hand_Panel.add(gcl);
				} catch (NullPointerException npe1) {
					j = 0;

				}
			} else {
				Graphical_Card_Label gcl = graf_Karten_hm.get(gamePlaying
						.getPlayerCards().get(j).getDeckValue());

				try {
					wildCardsPanel.add(gcl);
				} catch (NullPointerException npe1) {
					j = 0;

				}
			}
			if (isSortedByCol()) {
				sortBySuit();
			} else if (isSortedByVal()) {
				sortByValue();
			}
		}

	}

	// 3* Wild Card Panel

	private void wildCards(Game gamePlaying) {

		mainPanel.add(wildCardsPanel);
		wildCardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		wildCardsPanel.setOpaque(false);
		wildCardsPanel.setPreferredSize(new Dimension(totalWidth, 200));

	}

	// 4* Button Controls
	private void buttonControls() {

		System.out.println("loading buttons...");

		mainPanel.add(mainControls_Panel);
		mainControls_Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
		mainControls_Panel.setOpaque(false);
		mainControls_Panel.setPreferredSize(new Dimension(totalWidth, 80));

		JPanel smallBtns = new JPanel();
		mainPanel.add(smallBtns);
		smallBtns.setLayout(new FlowLayout());
		smallBtns.setOpaque(false);
		smallBtns.setPreferredSize(new Dimension(totalWidth, 100));

		// ------------------------------------------PLAY
		// BUTTON--------------------------------------------------------
		int btnWidth = 130;
		int btnHeight = 60;

		play = new JButton("Play");
		play.setBackground(Color.white);
		play.setPreferredSize(new Dimension(btnWidth, btnHeight));
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				// /do
				// shit!!!--------------------------<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>-----------------------------------

				if (Graphical_Card_Label.ableToPlay == true) {
					ArrayList<Card> playedCards = new ArrayList<>();
					for (int i = 0; i < list_GCL.size(); i++) {

						// TempDeckValue = card key

						int tempDeckValue = list_GCL.get(i);

						Card card = Deck.deck_HM.get(tempDeckValue);
						playedCards.add(card);
						removeSelected(tempDeckValue);
						// addCardtoPanel(tempDeckValue);

						System.out.println();
						int index = getIndex(tempDeckValue);
						System.out
								.println("TempDeckValue "
										+ tempDeckValue
										+ " is found in sortedTempArray at its index of: "
										+ index);
						if (tempDeckValue < 36) {
							sortedTempArray.remove(index);
							System.out
									.println("Size of sortedTempArray is now: "
											+ sortedTempArray.size());
						}
						// Nehme die angewh???lte Karte aus dem hand_Panel
						// heraus:

						// revalidatePlayField(tempDeckValue);

						revalidate();
					}

					gameInput.getGame().setPlayedCards(playedCards);
					gameInput.getGame().setActive_user(gameInput.getPlayer());
					gameInput.setAction("playCards");
					client.sendData(gameInput);
					list_GCL.clear();

					playedCards.clear();
					System.out.println("List gcl has following helements: "
							+ list_GCL);
				}
			}
		});
		// _________________________________END
		// PLAY______________________________________________________________

		// ---------------------------------PASS
		// BUTTON------------------------------------------------------------
		pass = new JButton("Pass");
		pass.setBackground(Color.white);
		pass.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				// ///////////Test!!!!!!!!!!!!!!!!
				gameInput.getGame().setActive_user(gameInput.getPlayer());
				gameInput.setAction("passCards");
				client.sendData(gameInput);

			}
		});
		final JButton stopMusic = new JButton("Music Off");
		final JButton playMusic = new JButton("Music On");
		playMusic.setBackground(Color.white);
		playMusic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						try {
							sound = new Sound();

							sound.play_stop(true);
							playMusic.setEnabled(false);
							stopMusic.setEnabled(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});

		stopMusic.setBackground(Color.white);
		stopMusic.setEnabled(false);
		// stopMusic.setPreferredSize(new Dimension(200, 100));
		stopMusic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sound.play_stop(false);
				playMusic.setEnabled(true);
			}

		});

		JButton Sort_bySuit_Btn = new JButton("Sort by Suit");
		Sort_bySuit_Btn.setBackground(Color.white);
		Sort_bySuit_Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				sortBySuit();
			}
		});

		JButton Sort_byValue_Btn = new JButton("Sort by Value");
		Sort_byValue_Btn.setBackground(Color.white);
		Sort_byValue_Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				sortByValue();
			}
		});

		smallBtns.add(playMusic);
		smallBtns.add(stopMusic);
		smallBtns.add(Sort_bySuit_Btn);
		smallBtns.add(Sort_byValue_Btn);

		mainControls_Panel.add(play);
		mainControls_Panel.add(pass);

		// mainControls_Panel.add(opponentInfoPanel);

		// _________________________________END
		// PASS____________________________________________________________________
	}

	public void fontSettings(JLabel label) {

		label.setFont(new Font("Verdana", Font.BOLD, 20));
		label.setForeground(Color.white);
	}

	public void sortByValue() {
		setSortedByCol(false);
		setSortedByVal(true);
		for (int i = 0; i < sortedTempArray.size(); i++) {
			hand_Panel.remove(gcl.getHashMap().get(sortedTempArray.get(i)));
			revalidate();
		}

		System.out.println("Method sortBy Value => sortedTempArray old is: ");
		for (int i = 0; i < sortedTempArray.size(); i++) {
			System.out.print(sortedTempArray.get(i) + " ");
		}
		System.out.println();
		System.out.println("The cards corresponding deckValue: ");
		for (int i = 0; i < sortedTempArray.size(); i++) {
			System.out.print(gcl.get_Graf_Card_from_HashMap(
					sortedTempArray.get(i)).getDeckValue()
					+ " ");
		}
		System.out.println();

		// Inserstion Sort Algorithmus
		for (int i = 0; i < sortedTempArray.size() - 1; i++) {

			for (int j = sortedTempArray.size() - 1; j > 0; j--) {
				Graphical_Card_Label gclTemp1 = graf_Karten_hm
						.get(sortedTempArray.get(j - 1));

				Graphical_Card_Label gclTemp2 = graf_Karten_hm
						.get(sortedTempArray.get(j));

				if (gclTemp1.getDeckValue() > gclTemp2.getDeckValue()) {
					int temp = sortedTempArray.get(j);
					sortedTempArray.set(j, sortedTempArray.get(j - 1));
					sortedTempArray.set(j - 1, temp);
				}
			}
		}

		System.out.println("Method sortBy Value => sortedTempArray new is: ");
		for (int i = 1; i < sortedTempArray.size(); i++) {
			System.out.print(sortedTempArray.get(i) + " ");
		}

		for (int i = 0; i < sortedTempArray.size(); i++) {
			hand_Panel.add(graf_Karten_hm.get(sortedTempArray.get(i)));
			revalidate();
		}

	}

	public void sortBySuit() {
		setSortedByCol(true);
		setSortedByVal(false);
		for (int i = 0; i < sortedTempArray.size(); i++) {
			hand_Panel.remove(graf_Karten_hm.get(sortedTempArray.get(i)));

			revalidate();
		}
		Collections.sort(sortedTempArray);
		for (int i = 0; i < sortedTempArray.size(); i++) {
			hand_Panel.add(graf_Karten_hm.get(sortedTempArray.get(i)));
			revalidate();
		}
	}


	public void revalidatePlayField(int tempDeckValue) {

		Graphical_Card_Label temp = loadGraf_Karten();

		cardDump.add(temp.getHashMap().get(tempDeckValue));

		cardDump.add(temp.getHashMap().get(tempDeckValue));

		revalidate();

	}

	public static void markAsSelected(int tempDeckValue) {
		gcl.getHashMap().get(tempDeckValue)
				.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

		hand_Panel.revalidate();
	}

	public static void removeSelected(int tempDeckValue) {
		gcl.getHashMap().get(tempDeckValue).setBorder(null);

		hand_Panel.revalidate();
	}

	public Graphical_Card_Label loadGraf_Karten() {
		Graphical_Card_Label temp = new Graphical_Card_Label();
		temp.loadCardImages(temp.suitColor, temp.nr);
		temp.loadWildCardImages();
		temp.assign_Graf_Karten_to_HashMap();
		return temp;
	}

	public int getIndex(int tempNr) {
		int j = 0;
		for (int i = 0; i < sortedTempArray.size(); i++) {
			if (tempNr == sortedTempArray.get(i)) {
				return j = i;
			}

		}
		return j;

	}

	public boolean isSortedByVal() {
		return sortedByVal;
	}

	public void setSortedByVal(boolean sortedByVal) {
		this.sortedByVal = sortedByVal;
	}

	public boolean isSortedByCol() {
		return sortedByCol;
	}

	public void setSortedByCol(boolean sortedByCol) {
		this.sortedByCol = sortedByCol;
	}

}
