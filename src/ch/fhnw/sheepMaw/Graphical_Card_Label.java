package ch.fhnw.sheepMaw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Graphical_Card_Label extends JLabel {

	// Grafische Karten abbild

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	ArrayList<ImageIcon> cardImages = new ArrayList<>();
	ArrayList<ImageIcon> wildCardImages = new ArrayList<>();

	HashMap<Integer, Graphical_Card_Label> graf_Karten_hm = new HashMap<>();

	public String[] suitColor = new String[] { "gelb", "gruen", "orange", "rot" };
	public String[] nr = new String[] { "02", "03", "04", "05", "06", "07",
			"08", "09", "10" };

	public int key;

	public static boolean ableToPlay = false;

	int index;

	private boolean selected = false;

	int deckValue;

	public Graphical_Card_Label() {

	}

	public Graphical_Card_Label(int key, ImageIcon icon, int deckValue) {
		
		super(icon);
		this.key = key;
		this.icon = icon;
		this.deckValue = deckValue;
		this.revalidate();
		
		
		setPreferredSize(new Dimension(80, 120));
		
		
		this.addMouseListener(new MouseListener() {
			int dV;

			@Override
			public void mouseClicked(MouseEvent e) {

				dV = getKey();
				// this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

				if (isSelected()) {
					GameTableGUI.removeSelected(dV);
					int listSize = GameTableGUI.list_GCL.size();
					setSelected(false);
					for (int i = 0; i < listSize; i++) {

						if (GameTableGUI.list_GCL.get(i) == dV) {
							GameTableGUI.list_GCL.remove(i);
							break;

						}
					}
					if (GameTableGUI.list_GCL.size() == 0) {
						ableToPlay = false;
					}
					System.out.println("This Cards key is: " + dV);
					System.out.println("The List GCL is: "
							+ GameTableGUI.list_GCL);
				} else {
					ableToPlay = true;
					setSelected(true);
					GameTableGUI.markAsSelected(dV);
					GameTableGUI.list_GCL.add(dV);
					System.out.println("This Cards key is: " + dV);
					System.out.println("The List GCL is: "
							+ GameTableGUI.list_GCL);
				}

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void loadCardImages(String[] suitColor, String[] nr) {
		int i;
		int j;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 9; j++) {
				cardImages.add(new ImageIcon(getClass().getClassLoader()
						.getResource(
								"ch/fhnw/sheepMaw/images/" + suitColor[i] + "/"
										+ suitColor[i] + nr[j] + ".jpg")));
			}
		}
	}

	public void loadWildCardImages() {
		// wildCardImages

		ImageIcon bube1 = new ImageIcon(getClass().getClassLoader()
				.getResource("ch/fhnw/sheepMaw/images/Bube.jpg"));

		ImageIcon dame1 = new ImageIcon(getClass().getClassLoader()
				.getResource("ch/fhnw/sheepMaw/images/Dame.jpg"));

		ImageIcon koenig1 = new ImageIcon(getClass().getClassLoader()
				.getResource("ch/fhnw/sheepMaw/images/Koenig.jpg"));

		wildCardImages.add(bube1);
		wildCardImages.add(dame1);
		wildCardImages.add(koenig1);
	}

	public HashMap<Integer, Graphical_Card_Label> getHashMap() {
		return this.graf_Karten_hm;
	}

	public int getKey() {
		return this.key;
	}

	// by overriding the paintComponent method of JLabel you can draw the image
	// having width and height as that of JLabel
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
	}

	// Getter Methode, welche mir aus dem Hashmap die richtige Graf_Karten
	// Objekt zur?ckgibt. Braucht als Parameter den deckValue
	public Graphical_Card_Label get_Graf_Card_from_HashMap(int key) {
		return graf_Karten_hm.get(key);
	}

	public void assign_Graf_Karten_to_HashMap() {

		int iterator = 0;
		for (index = 0; index < 36; index++) {

			int deckValue = Integer.parseInt(nr[iterator]);
			graf_Karten_hm.put(index, new Graphical_Card_Label(index,
					cardImages.get(index), deckValue));
			iterator++;
			if (iterator == 9) {
				iterator = 0;
			}

		}
		for (index = 36; index <= 38; index++) {

			graf_Karten_hm.put(index, new Graphical_Card_Label(index,
					wildCardImages.get(index - 36), 11));
		}

		System.out.println("Hasmap contains: " + graf_Karten_hm);
	}

	public int getDeckValue() {
		return this.deckValue;
	}

	public static void main(String[] args) {

	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
