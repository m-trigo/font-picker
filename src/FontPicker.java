/**
 * Program Name: FontPicker.java
 * Purpose: Displays a list of fonts available in the current system with Java.Swing.
 * Author: Trigo, Murilo
 * Date: July 09, 2017
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FontPicker extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String VERSION = "v2.1";

	public static void main(String[] args) { new FontPicker(); }
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 620;
	private static final int FONT_SIZE = 40;
	private static final int SMALL_FONT_SIZE = 24;
	private static final int SCROLL_SPEED_FACTOR = FONT_SIZE;
	private static final String WINDOW_TITLE = " Font Picker " + VERSION;

	private final static Color SELECT_HIGHLIGHT_COLOR = new Color(240, 248, 244);
	private final static Color DEFAULT_FONTPANEL_COLOR = Color.WHITE;
	
	private static final String EXAMPLE_PHRASE = "The quick brown fox jumps over the lazy dog"; 
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String SYMBOLS = "@ + - / * < > \\ \" ' ! @ # $ % ^ & ( ) [ ] { }";
	private static final String DIGITS = "0123456789";
	private static final String SHOWCASE_STRING = SYMBOLS + "\n" + DIGITS 
			+ "\n" + ALPHABET.toLowerCase() + "\n" + ALPHABET + "\n" + EXAMPLE_PHRASE;
	
	private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK, 2, true);
	private static final int BORDER_WIDTH = 20;
	private static final Border MARGIN = 
			BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
	
	private JTextArea exampleTextArea;
	private JTextField selectedFontTextField;

	private final static String DEFAULT_IMAGE_PATH = "./data/font-picker-icon.png";
	
	public FontPicker()
	{
		super(WINDOW_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		ImageIcon img = new ImageIcon(DEFAULT_IMAGE_PATH);

		if (img != null)
		{
			this.setIconImage(img.getImage());
		}
		
		// Dummy JTextField to avoid a null pointer reference the first time a font is selected
		// without having the selection highlight algorithm check for the special case
		selectedFontTextField = new JTextField(); 
		
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(2, 1));
		
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		
		// Mouse-click Logic
		MouseListener mouseListener = new MouseListener()
		{
			public void mouseClicked(MouseEvent event) 
			{
				JTextField textFieldSource = (JTextField)event.getSource();
				
				if (textFieldSource == selectedFontTextField)
				{
					return; // Nothing needs to happen when clicking an already selected font
				}
				
				selectedFontTextField.setBackground(DEFAULT_FONTPANEL_COLOR);
				selectedFontTextField = textFieldSource;
				textFieldSource.setBackground(SELECT_HIGHLIGHT_COLOR);
				renderExampleWithFont(exampleTextArea, textFieldSource.getFont().getName());
			}
			
			public void mousePressed(MouseEvent event) {}	
			public void mouseReleased(MouseEvent arg0) {}		
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
		};
		
		
		JPanel fontListPanel = new JPanel(new GridLayout(simbolicsFontsCount(fonts), 1));

		// This loop is so inefficient it hurts
		// It's the reason the JWindow takes a few seconds to pop up after the program's execution
		// Do note that an alternative method using getListCellRendererComponent is possible.
		for(int i = 0; i < fonts.length; i++) 
		{
			String fontName = fonts[i];
			
			if (isSymbolicFont(fontName)) 
			{
				continue; // Don't display symbolic fonts
			} 
			
			JTextField textField = new JTextField(fontName);
			textField.setFont(new Font(fontName, Font.PLAIN, FONT_SIZE));
			textField.setEditable(false);
			textField.setBackground(Color.WHITE);
			textField.setHorizontalAlignment(JTextField.CENTER);
			fontListPanel.add(textField);
			textField.addMouseListener(mouseListener);
		}
		
		JScrollPane fontListScrollPane = new JScrollPane(fontListPanel);
		fontListScrollPane.setBorder(BORDER);
		fontListScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED_FACTOR);
		displayPanel.add(fontListScrollPane);
			
		
		exampleTextArea = new JTextArea();
		exampleTextArea.setEditable(false);
		exampleTextArea.setBorder(MARGIN);
		JScrollPane exampleScrollPane = new JScrollPane(exampleTextArea);
		exampleScrollPane.setBorder(BORDER);
		displayPanel.add(exampleScrollPane);

		
		this.add(displayPanel);		
		
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}// FontPicker constructor

	private boolean isSymbolicFont(String fontName)
	{
		Font font = new Font(fontName, Font.PLAIN, FONT_SIZE);
		return !font.canDisplay('A');
	}
	
	private int simbolicsFontsCount(String[] fontNames)
	{
		int total = 0;
		for(int i = 0; i < fontNames.length; i++) 
		{
			if (!isSymbolicFont(fontNames[i]))
			{
				total++;
			}
		}
		
		return total;
	}
	
	private void renderExampleWithFont(JTextArea textArea, String fontName)
	{
		textArea.setFont(new Font(fontName, Font.PLAIN, SMALL_FONT_SIZE));
		textArea.setText("[ " + fontName + " - size " + SMALL_FONT_SIZE + " ]\n\n" + SHOWCASE_STRING);
	}
	
}// FontPicker
