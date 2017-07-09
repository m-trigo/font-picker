/**
 * Program Name: FontPicker.java
 * Purpose: Creates a list of fonts available on the system.
 * 			Right-clicking while bring up a menu will example displays
 * 			while the right mouse button is being held.
 * Author: Trigo, Murilo
 * Date: July 09, 2017
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FontPicker extends JFrame
{
	public static void main(String[] args) { new FontPicker(); }
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 420;
	private static final int FONT_SIZE = 40;
	private static final int SMALL_FONT_SIZE = 24;
	private static final int SCROLL_SPEED_FACTOR = FONT_SIZE;
	private static final String WINDOW_TITLE = " -- Fonts Available in this System -- ";
	
	private static final String EXAMPLE_PHRASE = "The quick brown fox jumps over the lazy dog"; 
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String SYMBOLS = "@ + - / * < > \\ \" ' ! @ # $ % ^ & ( ) [ ] { }";
	private static final String DIGITS = "0123456789";
	private static final String SHOWCASE_STRING = SYMBOLS + "\n" + DIGITS 
			+ "\n" + ALPHABET.toLowerCase() + "\n" + ALPHABET + "\n" + EXAMPLE_PHRASE;
	
	private ExampleWindow exampleWindow;
	private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK, 2, true);
	private static final int BORDER_WIDTH = 20;
	private static final Border MARGIN = 
			BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
	
	private static final double POPUP_MIN_DISTANCE_FROM_EDGE_IN_SCREEN_PERCENTAGE = 0.05;
	
	public FontPicker()
	{
		super(WINDOW_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout()); // Will force sub-panels to be take the full length of the container
		
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		JPanel fontListPanel = new JPanel(new GridLayout(simbolicsFontsCount(fonts), 1));

		MouseListener mouseListener = new MouseListener()
		{
			public void mousePressed(MouseEvent event) 
			{
				if (SwingUtilities.isLeftMouseButton(event))
				{
					JTextField textFieldSource = (JTextField)event.getSource();					
					
					// Necessary to avoid multiple exampleWindows
					// Essentially doing double-duty as an isExampleWindowActive boolean
					if (exampleWindow == null) 
					{
						exampleWindow = renderExampleWindowForFont(textFieldSource.getFont().getName());
					}
				}
			}
			
			public void mouseReleased(MouseEvent event) 
			{
				if (exampleWindow != null)
				{
					exampleWindow.dispose();
					exampleWindow = null; // Necessary to signal a window isn't currently open
				}
			}
			
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
		};
		
		// This loop is so inefficient it hurts
		// It's the reason the JWindow takes a few seconds to pop up after the program's execution
		// An alternative method using getListCellRendererComponent is possible, however.
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
		
		JScrollPane scrollPane = new JScrollPane(fontListPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED_FACTOR);
		displayPanel.add(scrollPane);
		
		this.add(displayPanel);

		this.setLocationRelativeTo(null);
		this.setVisible(true);	
	}
		
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
	
	private ExampleWindow renderExampleWindowForFont(String fontName)
	{
		return new ExampleWindow(fontName);
	}
	
	private class ExampleWindow extends JFrame
	{
		public ExampleWindow(String fontName)
		{
			super(fontName);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setUndecorated(true);

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(BORDER);
			
			JTextArea textArea = new JTextArea();
			textArea.setText("[ " + fontName + " ]" + "\n\n" + SHOWCASE_STRING);
			textArea.setEditable(false);
			textArea.setBorder(MARGIN);
			textArea.setFont(new Font(fontName, Font.PLAIN, SMALL_FONT_SIZE));
			
			panel.add(textArea);
			this.add(panel);
			this.pack();
			
			Point mousePosition = MouseInfo.getPointerInfo().getLocation();	
			
			// Pop-up reposition
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double minHorizontalDistance = screenSize.getWidth() * POPUP_MIN_DISTANCE_FROM_EDGE_IN_SCREEN_PERCENTAGE;
			double minVerticalDistance = screenSize.getHeight() * POPUP_MIN_DISTANCE_FROM_EDGE_IN_SCREEN_PERCENTAGE;
			
			// Horizontal adjustment
			if (mousePosition.x - this.getWidth() >=  minHorizontalDistance)
			{
				mousePosition = new Point(mousePosition.x - this.getWidth(), mousePosition.y);
			}
			
			// Vertical adjustment
			if (mousePosition.y - this.getHeight() >= minVerticalDistance)
			{
				mousePosition = new Point(mousePosition.x, mousePosition.y - this.getHeight());
			}
			
			this.setLocation(mousePosition);
			this.setVisible(true);
		}
	}
}
