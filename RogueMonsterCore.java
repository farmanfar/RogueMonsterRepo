import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.util.Random;

@SuppressWarnings("serial")
public class RogueMonsterCore extends JFrame {

	private int[][] boardLogic = new int[NUM_OF_ROWS][NUM_OF_COLS];
	private JButton[][] boardView = new JButton[NUM_OF_ROWS][NUM_OF_COLS];
	private JButton[] buttonDeck = new JButton[10];

	private JPanel boardPanel;
	private JPanel buttonPanel;

	private int stoneCount = 0;
	private int wallCount = 0;
	private boolean wasLastStone = false;

	private int playerHP = 15;
	private int gameLevel = 1;
	private JLabel playerHPLabel;
	private JLabel gameLevelLabel;

	private Random randomGen = new Random();

	private int playerRow;
	private int playerCol;
	private int oldTileType = 0;

	private boolean collected;

	private ImageIcon grid = new ImageIcon("grid.jpg");
	private ImageIcon grid1 = new ImageIcon("grid1.jpg");
	private ImageIcon player = new ImageIcon("player.jpg");
	private ImageIcon wall = new ImageIcon("wall.jpg");
	private ImageIcon enemy = new ImageIcon("enemy.jpg");
	private ImageIcon sword = new ImageIcon("swordAttack.jpg");
	private ImageIcon powerUp = new ImageIcon("powerUp.jpg");

	private static final int NUM_OF_ROWS = 10;
	private static final int NUM_OF_COLS = 12;

	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 900;

	public RogueMonsterCore() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new BorderLayout());

		boardPanelMake();
		buttonPanelMake();
		playerInfoMake();

		collected = false;

		class GameButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JButton theButton = (JButton) e.getSource();
				int row = (Integer) theButton.getClientProperty("row");
				int col = (Integer) theButton.getClientProperty("col");
				setGameLogic(row, col);
			}
		}
		ActionListener gameButtonListener = new GameButtonListener();

		for (int row = 0; row < NUM_OF_ROWS; row++) {
			for (int col = 0; col < NUM_OF_COLS; col++) {
				ImageIcon ground = randomGroundIcon();
				boardView[row][col] = new JButton("", ground);

				if (ground == grid)
					boardLogic[row][col] = 0;

				if (ground == grid1)
					boardLogic[row][col] = 3;

				boardView[row][col].putClientProperty("row", new Integer(row));
				boardView[row][col].putClientProperty("col", new Integer(col));
				boardView[row][col].addActionListener(gameButtonListener);
				boardPanel.add(boardView[row][col]);
			}
		}
		initialGameLogic();
	}

	public void boardPanelMake() {
		boardPanel = new JPanel(new GridLayout(NUM_OF_ROWS, NUM_OF_COLS));
		add(boardPanel, BorderLayout.CENTER);
	}

	public void buttonPanelMake() {
		buttonPanel = new JPanel(new GridLayout(5, 1));
		for (int i = 0; i < 5; i++) {
			buttonDeck[i] = buttonDeckDealer();
			buttonPanel.add(buttonDeck[i]);
		}
		this.add(buttonPanel, BorderLayout.WEST);
	}

	public JButton buttonDeckDealer() {
		int randomButton = randomGen.nextInt(5);

		JButton emptyButton = new JButton();

		if (collected == true)
			return buttonDeck[randomButton];

		else
			return emptyButton;
	}

	public void playerInfoMake() {
		JPanel playerInfoPanel = new JPanel(new GridLayout(1, 3));
		playerHPLabel = new JLabel("HP||" + playerHP);
		gameLevelLabel = new JLabel("Level||" + gameLevel);
		JButton newGameButton = new JButton("New Game");

		playerInfoPanel.add(playerHPLabel);
		playerInfoPanel.add(gameLevelLabel);
		playerInfoPanel.add(newGameButton);
		add(playerInfoPanel, BorderLayout.SOUTH);
	}

	public void initialGameLogic() {
		int spawnRow = randomGen.nextInt(5);
		int spawnCol = randomGen.nextInt(9);

		if (spawnRow == 0 || spawnCol == 0) {
			spawnRow = 2;
			spawnCol = 2;
		}
		setEnemySpawn(spawnRow, spawnCol);
		setWalls(spawnRow, spawnCol);
		setPlayerSpawn(spawnRow, spawnCol);
		setPowerUps(spawnRow, spawnCol);
	}

	public void setGameLogic(int row, int col) {
		if (boardLogic[row][col] != 2 && boardLogic[row][col] != 1) {
			if (row - 1 == playerRow && col == playerCol) {
				System.out.println("" + playerRow + " " + playerCol + " " + row
						+ " " + col);
				if (boardLogic[row][col] == 4) {
					boardView[row][col].setIcon(sword);
					playerHP--;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP - 1");
					boardView[playerRow][playerCol].setIcon(player);
					boardLogic[row][col] = 0;
					boardView[row][col].setIcon(grid);
				} else if (boardLogic[row][col] == 6) {
					boardLogic[row][col] = 3;
					boardView[row][col].setIcon(grid1);
					playerHP++;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP + 1");
				} else if (boardLogic[row][col] == 3) {
					oldTileType = 3;
					boardLogic[playerRow][playerCol] = 3;
					boardView[playerRow][playerCol].setIcon(grid1);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;
				} else {
					oldTileType = 0;
					boardLogic[playerRow][playerCol] = 0;
					boardView[playerRow][playerCol].setIcon(grid);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;

				}
				enemyMovement(playerRow, playerCol);
			}
			if (row + 1 == playerRow && col == playerCol) {
				System.out.println("" + playerRow + " " + playerCol + " " + row
						+ " " + col);

				if (boardLogic[row][col] == 4) {
					boardView[row][col].setIcon(sword);
					playerHP--;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP - 1");
					boardView[playerRow][playerCol].setIcon(player);
					boardLogic[row][col] = 0;
					boardView[row][col].setIcon(grid);
				} else if (boardLogic[row][col] == 6) {
					boardLogic[row][col] = 3;
					boardView[row][col].setIcon(grid1);
					playerHP++;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP + 1");
				} else if (boardLogic[row][col] == 3) {
					oldTileType = 3;
					boardLogic[playerRow][playerCol] = 3;
					boardView[playerRow][playerCol].setIcon(grid1);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;
				} else {
					oldTileType = 0;
					boardLogic[playerRow][playerCol] = 0;
					boardView[playerRow][playerCol].setIcon(grid);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;
				}
				enemyMovement(playerRow, playerCol);
			}
			if (row == playerRow && col + 1 == playerCol) {
				System.out.println("" + playerRow + " " + playerCol + " " + row
						+ " " + col);
				if (boardLogic[row][col] == 4) {
					boardView[row][col].setIcon(sword);
					playerHP--;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP - 1");
					boardView[playerRow][playerCol].setIcon(player);
					boardLogic[row][col] = 0;
					boardView[row][col].setIcon(grid);
				} else if (boardLogic[row][col] == 6) {
					boardLogic[row][col] = 3;
					boardView[row][col].setIcon(grid1);
					playerHP++;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP + 1");
				} else if (boardLogic[row][col] == 3) {
					oldTileType = 3;
					boardLogic[playerRow][playerCol] = 3;
					boardView[playerRow][playerCol].setIcon(grid1);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;

				} else {
					oldTileType = 0;
					boardLogic[playerRow][playerCol] = 0;
					boardView[playerRow][playerCol].setIcon(grid);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;
				}
				enemyMovement(playerRow, playerCol);
			}
			if (row == playerRow && col - 1 == playerCol) {
				System.out.println("" + playerRow + " " + playerCol + " " + row
						+ " " + col);
				if (boardLogic[row][col] == 4) {
					boardView[row][col].setIcon(sword);
					playerHP--;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP - 1");
					boardView[playerRow][playerCol].setIcon(player);
					boardLogic[row][col] = 0;
					boardView[row][col].setIcon(grid);
				} else if (boardLogic[row][col] == 6) {
					boardLogic[row][col] = 3;
					boardView[row][col].setIcon(grid1);
					playerHP++;
					playerHPLabel.setText("HP||" + playerHP);
					JOptionPane.showMessageDialog(null, "HP + 1");
				} else if (boardLogic[row][col] == 3) {
					oldTileType = 3;
					boardLogic[playerRow][playerCol] = 3;
					boardView[playerRow][playerCol].setIcon(grid1);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;

				} else {
					oldTileType = 0;
					boardLogic[playerRow][playerCol] = 0;
					boardView[playerRow][playerCol].setIcon(grid);
					boardView[row][col].setIcon(player);
					playerRow = row;
					playerCol = col;
				}
				enemyMovement(playerRow, playerCol);
			}

		}
	}

	public ImageIcon randomGroundIcon() {
		int randomNum = randomGen.nextInt(3);

		if (randomNum == 1) {
			wasLastStone = false;
			return grid;
		}
		if (randomNum == 2 && stoneCount < 20 && wasLastStone != true) {
			wasLastStone = true;
			stoneCount++;
			return grid1;
		} else {
			return grid;
		}
	}

	public void setPlayerSpawn(int row, int col) {
		playerRow = row;
		playerCol = col;
		boardLogic[row][col] = 1;
		boardView[row][col].setIcon(player);

	}

	public void setWalls(int pRow, int pCol) {
		for (int i = 0; i < 12; i++) {
			int wallRow = randomGen.nextInt(10);
			int wallCol = randomGen.nextInt(12);

			for (int cRow = 0; cRow < NUM_OF_ROWS; cRow++) {
				boardLogic[cRow][0] = 2;
				boardView[cRow][0].setIcon(wall);
				boardLogic[cRow][NUM_OF_COLS - 1] = 2;
				boardView[cRow][NUM_OF_COLS - 1].setIcon(wall);
			}
			for (int cCol = 0; cCol < NUM_OF_COLS; cCol++) {
				boardLogic[0][cCol] = 2;
				boardView[0][cCol].setIcon(wall);
				boardLogic[NUM_OF_ROWS - 1][cCol] = 2;
				boardView[NUM_OF_ROWS - 1][cCol].setIcon(wall);
			}
			if (wallRow != pRow && wallCol != pCol && wallCount < 12) {
				boardLogic[wallRow][wallCol] = 2;
				boardView[wallRow][wallCol].setIcon(wall);
				wallCount++;
			}
		}
	}

	public void setPowerUps(int pRow, int pCol) {
		for (int i = 0; i < 3; i++) {
			int rowCord = randomGen.nextInt(9);
			int colCord = randomGen.nextInt(11);
			if (boardLogic[rowCord][colCord] != boardLogic[pRow][pCol]
					&& boardLogic[rowCord][colCord] != 2) {
				boardLogic[rowCord][colCord] = 6;
				boardView[rowCord][colCord].setIcon(powerUp);
				collected = true;
			}

		}

	}
	public void enemyMovement(int pRow, int pCol) {
		for(int row = 0; row < NUM_OF_ROWS; row++) { 
			for(int col = 0; col < NUM_OF_COLS; col++){
				if(boardLogic[row][col] == 4){
					if(boardLogic[row][col] == 4 && boardLogic[row][col - 1] == 1 || boardLogic[row - 1][col] == 1
						|| boardLogic[row][col + 1] == 1 || boardLogic[row + 1][col] == 1) 
						{
							playerHP--;
							playerHPLabel.setText("HP||" + playerHP);
							JOptionPane.showMessageDialog(null, "HP - 1");
						}
					
					if(boardLogic[row][col + 1] != 1 && boardLogic[row][col - 1] != 1
							&& boardLogic[row][col + 1] != 2 && boardLogic[row][col - 1] != 2
							&& boardLogic[row][col + 1] != 4 && boardLogic[row][col - 1] != 4)
					{
						if(col - pCol > 0){
							boardLogic[row][col] = 0;
							boardView[row][col].setIcon(grid);
							boardLogic[row][col - 1] = 4;
							boardView[row][col - 1].setIcon(enemy);
						}
						else if(col - pCol < 0) {
							boardLogic[row][col] = 0;
							boardView[row][col].setIcon(grid);
							boardLogic[row][col + 1] = 4;
							boardView[row][col + 1].setIcon(enemy);
						}
					}
					else if( boardLogic[row + 1][col] != 1 && boardLogic[row - 1][col] != 1
							&& boardLogic[row + 1][col] != 2 && boardLogic[row - 1][col] != 2
							&& boardLogic[row + 1][col] != 4 && boardLogic[row - 1][col] != 4) 
					{
						if(row - pRow > 0) {
							boardLogic[row][col] = 0;
							boardView[row][col].setIcon(grid);
							boardLogic[row - 1][col] = 4;
							boardView[row - 1][col].setIcon(enemy);
						}
						else if(row - pRow < 0){
							boardLogic[row][col] = 0;
							boardView[row][col].setIcon(grid);
							boardLogic[row + 1][col] = 4;
							boardView[row + 1][col].setIcon(enemy);
						}
					}
					else 
					{
						int randomMove = randomGen.nextInt(4);
					
						if(randomMove == 1) 
						{
							if(boardLogic[row + 1][col] != 2 && boardLogic[row + 1][col] != 4 
							&& boardLogic[row + 1][col] != 1)
							{
								boardLogic[row][col] = 0;
								boardView[row][col].setIcon(grid);
								boardLogic[row + 1][col] = 4;
								boardView[row + 1][col].setIcon(enemy);
							}
							else if(boardLogic[row - 1][col] != 2 && boardLogic[row - 1][col] != 4 
							&& boardLogic[row - 1][col] != 1)
							{
								boardLogic[row][col] = 0;
								boardView[row][col].setIcon(grid);
								boardLogic[row - 1][col] = 4;
								boardView[row - 1][col].setIcon(enemy);
							}
						}
						else if(randomMove == 2) 
						{
							if(boardLogic[row - 1][col] != 2 && boardLogic[row - 1][col] != 4 
							&& boardLogic[row - 1][col] != 1)
							{
								boardLogic[row][col] = 0;
								boardView[row][col].setIcon(grid);
								boardLogic[row - 1][col] = 4;
								boardView[row - 1][col].setIcon(enemy);
							}
							else if(boardLogic[row + 1][col] != 2 && boardLogic[row + 1][col] != 4 
							&& boardLogic[row + 1][col] != 1)
							{
								boardLogic[row][col] = 0;
								boardView[row][col].setIcon(grid);
								boardLogic[row + 1][col] = 4;
								boardView[row + 1][col].setIcon(enemy);
							}
						}
					}
				}
			}
		}
	}
	public void setEnemySpawn(int pRow, int pCol) {
		for (int i = 0; i < 8; i++) {
			int spawnRow = randomGen.nextInt(10);
			int spawnCol = randomGen.nextInt(12);
			if (boardLogic[spawnRow][spawnCol] != 1
					&& boardLogic[spawnRow][spawnCol] != 2) {
				boardLogic[spawnRow][spawnCol] = 4;
				boardView[spawnRow][spawnCol].setIcon(enemy);

				System.out.println("enemy spawned at " + spawnRow + spawnCol);
			}
		}
	}
}
