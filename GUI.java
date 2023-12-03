import com.formdev.flatlaf.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.*;

class GUI {

	private JFrame frame;
	private JTabbedPane tab;
	private JPanel main;
	private JPanel song_panel;
	private JPanel album_panel;
	private JLabel song_label;
	private JLabel artist_label;
	private JLabel folder;
	private JTextField song_text;
	private JTextField artist_text;
	private JButton download;
	private JProgressBar download_bar;
	private Insets i;
	private int state;
	// private Def
	GUI() {

		try {
			UIManager.setLookAndFeel( new FlatLightLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}

		frame = new JFrame();
		main = new JPanel();
		tab = new JTabbedPane();
		song_panel = new JPanel();
		album_panel = new JPanel();
		song_label = new JLabel("Song:");
		artist_label = new JLabel("Artist:");
		song_text = new JTextField("", 12);
		artist_text = new JTextField("", 12);
		download = new JButton("Download");
		download_bar = new JProgressBar();
		download_bar.setMinimum(0);
		download_bar.setMaximum(100);

		download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent d) {
				String artist = artist_text.getText();
				String song = song_text.getText();
					new Thread(new Runnable(){
						@Override
						public void run(){
							try{
								YouTubeSearch yts = new YouTubeSearch();
								String url = yts.Search(song, artist);
								ProcessBuilder pb = new ProcessBuilder("python", "download.py", url, song);
								Process p = pb.start();
								BufferedReader buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
								StringBuilder output = new StringBuilder();
								String line = null;
								state = 0;

								while ((line = buffer.readLine()) != null) {
									if (!line.equals("")) {
										state = Integer.parseInt(line);
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												setProg();
											}
										});
									}
								}

								if (state == 100) {
									download_bar.setValue(0);
									JOptionPane.showMessageDialog(frame, "Download Was Successful!\nEnjoy Listening To Your Song!",
											"SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
								}
							} catch (Exception e) {
								System.out.print(e);
							}
						}
					}).start();

				}
			});

			song_panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 60, 30));

			i = new Insets(15, 30, 15, 30);
			GridBagLayout layout = new GridBagLayout();
			song_panel.setLayout(layout);
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.insets = i;
			gbc.weightx = 0.75;
			gbc.weighty = 0.75;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.gridx = 0;
			gbc.gridy = 0;
			song_panel.add(song_label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			song_panel.add(song_text, gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			song_panel.add(artist_label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 2;
			song_panel.add(artist_text, gbc);

			gbc.gridx = 0;
			gbc.gridy = 6;
			gbc.gridwidth = 2;
			song_panel.add(download, gbc);

			gbc.gridx = 0;
			gbc.gridy = 8;
			gbc.gridwidth = 2;
			song_panel.add(download_bar, gbc);

			// Setting Up Frame
			tab.add("Song", song_panel);
			tab.add("Album", album_panel);
			frame.add(tab);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

		}

		public void setProg() {
			download_bar.setValue(state);
		}

		public static void main(String[] args) {
			new GUI();
		}
	}
