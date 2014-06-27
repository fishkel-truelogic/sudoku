package ar.com.finit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

import ar.com.finit.sudoku.SaveHelper;
import ar.com.finit.sudoku.SavedGame;
import ar.com.finit.sudoku.Sudoku;

import com.google.gson.Gson;

/**
 * 
 * @author Leo
 * 
 */
public class App extends JFrame {

	private static final long serialVersionUID = -6447813198557109913L;

	private static JFrame jframe;
	private static JPanel sudokuGrid;
	private static JPanel layoutGrid;
	private static JPanel timePanel;
	
	private static JLabel timeLabel;

    private static JPanel buttonPanel;
    private static JButton startButton;
    private static JButton resetButton;
    private static JButton stopButton;

    private static byte centiseconds = 0;
    private static byte seconds = 0;
    private static short minutes = 0;

    private static DecimalFormat timeFormatter;

    private static Timer timer;

	public static void main(String[] args) {
		initFrame();
		renderMainMenu();
	}

	private static void renderMainMenu() {
		buttonPanel = new JPanel();
		buttonPanel.setSize(650, 100);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		addMainButton("Muy Facil", "1");
		addMainButton("Facil", "2");
		addMainButton("Intermedio", "3");
		addMainButton("Dificil", "4");
		addMainButton("Muy Dificil", "5");
		if (SaveHelper.isSaved()) {
			JButton savedGame = new JButton("Continuar Partida");
			buttonPanel.add(savedGame);
			savedGame.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Sudoku sudoku = Sudoku.getInstance();
					try {
						SavedGame savedGame = SaveHelper.getSavedGame();
						centiseconds = savedGame.getCentiseconds();
						seconds = savedGame.getSeconds();
						minutes = savedGame.getMinutes();
						sudoku.setMatrix(savedGame.getMatrix());
						sudoku.setMatrix2(savedGame.getMatrix2());
						initFrame();
						startGame(0);
					} catch (Exception ex) {
						JDialog dialog = new JDialog(jframe, "", Dialog.ModalityType.DOCUMENT_MODAL);
						dialog.add(new JLabel(ex.getMessage()));
						dialog.setSize(300, 150);
						dialog.setVisible(true);
					}
					
				}

				
			});
		}
		jframe.add(buttonPanel);
	}
	
	private static void addMainButton(String label, String name) {
		JButton button = new JButton(label);
		button.setName(name);
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JButton btn = (JButton) e.getSource();
            	initFrame();
				startGame(Integer.parseInt(btn.getName()));
            }
        });
		buttonPanel.add(button);
	}

	private static void initFrame() {
		if (timer != null) resetTimer();
		sudokuGrid = null;
		layoutGrid = null;
		timePanel = null;
		if (jframe != null) jframe.dispose();
		jframe = new JFrame("SUDOKU LEO FINI");
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
		jframe.setSize(600, 100);

	}

	private static void resetTimer() {
		timer.stop();
		centiseconds = 0;
		seconds = 0;
		minutes = 0;		
	}

	private static void addMenu() {
		MenuBar mbar;
		Menu menu, submenu;
		mbar = new MenuBar();
		menu = new Menu("Menu");
		submenu = new Menu("Nuevo Juego");
		MenuItem save = new MenuItem("Guardar Juego");
		
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Sudoku sudoku = Sudoku.getInstance();
				SavedGame savedGame = new SavedGame();
				Gson gson = new Gson();
				savedGame.setMatrix(sudoku.getMatrix());
				savedGame.setMatrix2(sudoku.getMatrix2());
				savedGame.setCentiseconds(centiseconds);
				savedGame.setSeconds(seconds);
				savedGame.setMinutes(minutes);
				String json = gson.toJson(savedGame);
				timer.stop();
				sudokuGrid.setVisible(false);
				SaveHelper.saveGame(json);
				
			}
		});
		
		addMenuLevel(submenu, "Muy Facil", "1");
		addMenuLevel(submenu, "Facil", "2");
		addMenuLevel(submenu, "Intermedio", "3");
		addMenuLevel(submenu, "Dificil", "4");
		addMenuLevel(submenu, "Muy Dificil", "5");
		menu.add(submenu);
		menu.add(save);
		mbar.add(menu);
		jframe.setMenuBar(mbar);
	}


	private static void addMenuLevel(Menu submenu, String label, String name) {
		MenuItem mi = new MenuItem(label);
		mi.setName(name);
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MenuItem item = (MenuItem) e.getSource();
				initFrame();
				startGame(Integer.parseInt(item.getName()));
			}
		});
		submenu.add(mi);
	}

	private static void startGame(int level) {
		addMenu();
		if (level != 0) {
			Sudoku sudoku = Sudoku.getInstance();
			sudoku.init();
			sudoku.clear(level);
		}

		if (layoutGrid != null) jframe.remove(layoutGrid);
		layoutGrid = new JPanel();
		layoutGrid.setLayout(new GridLayout(1, 2));
		jframe.add(layoutGrid);
		renderTimerPanel();
		jframe.setSize(1200, 600);

	}

	private static void renderSudokuGrid() {
		sudokuGrid = new JPanel();
		sudokuGrid.setLayout(new GridLayout(9, 9));
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Component cell = renderCell(i, j);
				sudokuGrid.add(cell);
			}
		}
		sudokuGrid.setSize(600, 600);
		sudokuGrid.setVisible(true);
		layoutGrid.add(sudokuGrid);
		
	}

	private static void renderTimerPanel() {
		timePanel = new JPanel();
		timePanel.setLayout(new GridLayout(2, 1));
		timePanel.setSize(600, 600);
		
		timeLabel = new JLabel();
        timeLabel.setFont(new Font("Consolas", Font.BOLD, 32));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timePanel.add(timeLabel);

        buttonPanel = new JPanel();
        buttonPanel.setSize(600, 100);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        startButton = new JButton("Iniciar/Continuar");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (sudokuGrid == null) {
            		renderSudokuGrid();
            	} else {
            		sudokuGrid.setVisible(true);
            	}
            	timer.start();
            }
        });
        buttonPanel.add(startButton);

        resetButton = new JButton("Reiniciar");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                centiseconds = 0;
                seconds = 0;
                minutes = 0;
                timeLabel.setText(timeFormatter.format(minutes) + ":"
                        + timeFormatter.format(seconds) + "."
                        + timeFormatter.format(centiseconds));
            }
        });

        buttonPanel.add(resetButton);

        stopButton = new JButton("Pausa");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (sudokuGrid != null) {
            		sudokuGrid.setVisible(false);
            	}
            	timer.stop();
            }
        });

        buttonPanel.add(stopButton);

        timePanel.add(buttonPanel, BorderLayout.SOUTH);

        timeFormatter = new DecimalFormat("00");

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (centiseconds < 99) {
                    centiseconds ++;
                } else {
                	centiseconds = 0;
                    if (seconds < 59) {
                    	seconds ++;
                    } else {
                        minutes ++;
                        seconds = 0;
                    }
                }
                timeLabel.setText(timeFormatter.format(minutes) + ":"
                        + timeFormatter.format(seconds) + "."
                        + timeFormatter.format(centiseconds));
            }
        });

        timeLabel.setText(timeFormatter.format(minutes) + ":"
                + timeFormatter.format(seconds) + "."
                + timeFormatter.format(centiseconds));

		layoutGrid.add(timePanel);
	}

	@SuppressWarnings("deprecation")
	private static Component renderCell(int i, int j) {
		JTextField textField = new JTextField(1);
		textField.setBackground(null);
		textField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Sudoku sudoku = Sudoku.getInstance();
		Border border = createBorder(i, j);
		Font font = new Font("asd", Font.BOLD, 32);

		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTextField tf = (JTextField) e.getSource();
				String[] index = tf.getName().split("-");
				int x = Integer.parseInt(index[0]);
				int y = Integer.parseInt(index[1]);
				int square = Sudoku.getInstance().getSquare(x, y);
				backgroundYellow(x, y, square, tf.getText());
				tf.setBackground(Color.RED);

			}

			private void backgroundYellow(int x, int y, int square, String value) {
				for (Component component : sudokuGrid.getComponents()) {
					JTextField tf = (JTextField) component;
					tf.setBackground(null);
					String[] index = tf.getName().split("-");
					int i = Integer.parseInt(index[0]);
					int j = Integer.parseInt(index[1]);
					if (i == x || j == y) {
						component.setBackground(Color.YELLOW);
					}
					if (!tf.getText().trim().isEmpty() && tf.getText().trim().equals(value.trim())) {
						component.setBackground(Color.RED);
					}
				}

				switch (square) {
				case 1:
					backgroundYelloySquare(0, 2, 0, 2, value);
					break;
				case 2:
					backgroundYelloySquare(0, 2, 3, 5, value);
					break;
				case 3:
					backgroundYelloySquare(0, 2, 6, 8, value);
					break;
				case 4:
					backgroundYelloySquare(3, 5, 0, 2, value);
					break;
				case 5:
					backgroundYelloySquare(3, 5, 3, 5, value);
					break;
				case 6:
					backgroundYelloySquare(3, 5, 6, 8, value);
					break;
				case 7:
					backgroundYelloySquare(6, 8, 0, 2, value);
					break;
				case 8:
					backgroundYelloySquare(6, 8, 3, 5, value);
					break;
				case 9:
					backgroundYelloySquare(6, 8, 6, 8, value);
					break;
				}

			}

			private void backgroundYelloySquare(int min_i, int max_i, int min_j, int max_j, String value) {
				for (int i = min_i; i <= max_i; i++) {
					for (int j = min_j; j <= max_j; j++) {
						for (Component component : sudokuGrid.getComponents()) {
							if (component.getName().equals(i + "-" + j)) {
								component.setBackground(Color.YELLOW);
								break;
							}
						}
					}
				}
			}
		});

		if (sudoku.getMatrix2()[i][j] != 0) {
			textField.setText("  " + Integer.toString(sudoku.getMatrix2()[i][j]));
			textField.setFont(font);
			textField.setBorder(border);
			textField.setName(i + "-" + j);
			textField.disable();
			textField.setDisabledTextColor(Color.BLACK);
			return textField;
		} else {
			textField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyTyped(KeyEvent e) {
					String key = Character.toString(e.getKeyChar());
					JTextField jtf = (JTextField) e.getSource();
					String[] index = jtf.getName().split("-");
					if (key.trim().matches("([1-9]){1}")) {
						jtf.setText("  " + jtf.getText().trim());
						jtf.setEditable(true);
						Sudoku.getInstance().getMatrix2()[Integer.parseInt(index[0])][Integer.parseInt(index[1])] = Integer.parseInt(key);
						for (Component component : sudokuGrid.getComponents()) {
							JTextField tf = (JTextField) component;
							if (!key.trim().isEmpty() && key.trim().equals(tf.getText().trim())) {
								component.setBackground(Color.RED);
							}
						}
						if (Sudoku.getInstance().isEndOfGame()) {
							JDialog dialog = new JDialog(jframe, "", Dialog.ModalityType.DOCUMENT_MODAL);
							dialog.add(new JLabel(Sudoku.getInstance().getResult()));
							dialog.add(new JLabel("Tiempo: " + timeFormatter.format(minutes) + ":"
					                + timeFormatter.format(seconds) + "."
					                + timeFormatter.format(centiseconds)));
							timer.stop();
							dialog.setSize(300, 150);
							dialog.setVisible(true);
						}
					} else {
						jtf.setText("  ");
						Sudoku.getInstance().getMatrix2()[Integer.parseInt(index[0])][Integer.parseInt(index[1])] = 0;
					}
				}
			});

			textField.setBorder(border);
			textField.setFont(font);
			textField.setText("  ");
			textField.setEditable(true);
			textField.setName(i + "-" + j);
			return textField;
		}

	}

	private static Border createBorder(int i, int j) {
		int bottom = 2, right = 2, left = 2, top = 2;
		if (j == 2 || j == 5) {
			right = 7;
		}
		if (i == 2 || i == 5) {
			bottom = 7;
		}
		if (i == 0) {
			top = 7;
		}
		if (i == 8) {
			bottom = 7;
		}
		if (j == 0) {
			left = 7;
		}
		if (j == 8) {
			right = 7;
		}
		return BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
	}

}
