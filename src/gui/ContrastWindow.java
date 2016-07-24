package gui;

import javax.swing.JFrame;

public class ContrastWindow extends JFrame{
	public static ContrastWindow window;
	
	public ContrastWindow(String title)
	{
		setTitle(title);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		requestFocus();
		window = this;
	}
	public static ContrastWindow getInstance()
	{
		return window;
	}
}
