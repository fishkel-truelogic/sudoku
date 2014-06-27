package ar.com.finit.sudoku;

import java.util.Random;

/**
 * @author leo
 */
public class Sudoku {

	private static final int VERY_EASY = 1;
	private static final int EASY = 2;
	private static final int MEDIUM = 3;
	private static final int HARD = 4;
	private static final int VERY_HARD = 5;

	private static Sudoku sudoku;

	private int[][] matrix;
	private int[][] matrix2;
	private int[] numbers;
	private String result;
	
	
	public static Sudoku getInstance() {
		if (sudoku == null) sudoku = new Sudoku(); 
		return sudoku;
	}
	
	public void init() {
		clearAll();
		
		boolean selected = false;
		int count = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				selected = false;
				count = 0;
				do {
					int random = this.getRandom(0, 9);
					if (!existConflict(random, i, j)) {
						numbers[random] = numbers[random] - 1;
						matrix[i][j] = random + 1;
						selected = true;
					}
					count++;
				} while (!selected && count <= 600);

				if (count > 600) {
					clearAll();
					i = 0;
					j = -1;
				}
			}
		}
	}

	private void clearAll() {
		this.setMatrix(null);
		this.setNumbers(null);
		this.setMatrix2(null);
		this.matrix = this.getMatrix();
		this.matrix2 = this.getMatrix2();
		this.numbers = this.getNumbers();
		
	}

	private boolean existConflict(int random, int i, int j) {
		if (!existsNumber(random)) return true;
		if (existsInRow(random, i)) return true;
		if (existsInColumn(random, j)) return true;
		if (existsInSquare(random, i, j)) return true;
		return false;
	}

	private boolean existsNumber(int random) {
		return numbers[random] != 0;
	}

	private boolean existsInSquare(int random, int i, int j) {
		int square = getSquare(i, j);
		switch(square) {
			case 1: return existInSquare(0, 2, 0, 2, random); 
			case 2: return existInSquare(0, 2, 3, 5, random);
			case 3: return existInSquare(0, 2, 6, 8, random);
			case 4: return existInSquare(3, 5, 0, 2, random);
			case 5: return existInSquare(3, 5, 3, 5, random);
			case 6: return existInSquare(3, 5, 6, 8, random);
			case 7: return existInSquare(6, 8, 0, 2, random);
			case 8: return existInSquare(6, 8, 3, 5, random);
			case 9: return existInSquare(6, 8, 6, 8, random);
			default: return true;
		}
	}

	private boolean existInSquare(int min_i, int max_i, int min_j, int max_j, int random) {
		for (int i = min_i; i <= max_i; i++) {
			for (int j = min_j; j <= max_j; j++) {
				if (matrix[i][j] == random + 1) {
					return true;
				}
			}
		}
		return false;
	}

	public int getSquare(int i, int j) {
		if(i <= 2) {
			if (j <= 2) return 1;
			else if (j <= 5) return 2;
			else return 3;
		} else if(i <= 5) {
			if (j <= 2) return 4;
			else if (j <= 5) return 5;
			else return 6;
		} else {
			if (j <= 2) return 7;
			else if (j <= 5) return 8;
			else return 9;
		}
	}

	private boolean existsInColumn(int random, int j) {
		for (int i = 0; i < 9; i++) {
			if (matrix[i][j] == random + 1)
				return true;
		}
		return false;
	}

	private boolean existsInRow(int random, int i) {
		for (int j = 0; j < 9; j++) {
			if (matrix[i][j] == random + 1)
				return true;
		}
		return false;
	}

	public int[][] getMatrix() {
		if (matrix == null) matrix = new int[9][9];
		return matrix;
	}

	public int[][] getMatrix2() {
		if (matrix2 == null) matrix2 = new int[9][9];
		return matrix2;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public int[] getNumbers() {
		if (numbers == null) {
			numbers = new int[9];
			for (int i = 0; i < 9; i++) {
				numbers[i] = 9;
			}
		}
		return numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}
	
	
	public int getRandom(int from, int to) {
	    if (from < to)
	        return from + new Random().nextInt(Math.abs(to - from));
	    return from - new Random().nextInt(Math.abs(to - from));
	}

	@Override
	public String toString() {
		String out = "";
		for (int i = 0; i < 9; i++) {
			out += " ";
		    for (int j = 0; j < 9; j++) {
		    	if (matrix2[i][j] != 0)
		    		out += matrix2[i][j] + " ";
		    	else 
		    		out += "  ";
		        if (j == 2 || j == 5) out += "| ";
		    }
		    
		    out += "\n";
		    if (i == 2 || i == 5) out += "------------------------ \n";
		}
		
		out += "\n\n";
		
		for (int i = 0; i < 9; i++) {
			out += " ";
			for (int j = 0; j < 9; j++) {
				out += matrix[i][j] + " ";
				if (j == 2 || j == 5) out += "| ";
			}
			
			out += "\n";
			if (i == 2 || i == 5) out += "------------------------ \n";
		}
		return out;
	}

	public void clear(int level) {
		int count = 0;
		int maxClear = 0;
		int maxNumber = 0;
		int maxInSquare = 0;
		switch (level) {
		case Sudoku.VERY_EASY: maxClear = 60; maxNumber = 9; maxInSquare = 9; break; 
		case Sudoku.EASY: maxClear = 50; maxNumber = 7; maxInSquare = 7; break; 
		case Sudoku.MEDIUM: maxClear = 40; maxNumber = 6; maxInSquare = 5; break; 
		case Sudoku.HARD: maxClear = 36; maxNumber = 5; maxInSquare = 4; break; 
		case Sudoku.VERY_HARD: maxClear = 32; maxNumber = 4; maxInSquare = 4; break; 
		}
		do {
			int i = getRandom(0, 9);
			int j = getRandom(0, 9);
			int square = getSquare(i, j);
			boolean clear = false;
			switch(square) {
				case 1: clear = clearInSquare(0, 2, 0, 2, maxInSquare); break; 
				case 2: clear = clearInSquare(0, 2, 3, 5, maxInSquare); break;
				case 3: clear = clearInSquare(0, 2, 6, 8, maxInSquare); break;
				case 4: clear = clearInSquare(3, 5, 0, 2, maxInSquare); break;
				case 5: clear = clearInSquare(3, 5, 3, 5, maxInSquare); break;
				case 6: clear = clearInSquare(3, 5, 6, 8, maxInSquare); break;
				case 7: clear = clearInSquare(6, 8, 0, 2, maxInSquare); break;
				case 8: clear = clearInSquare(6, 8, 3, 5, maxInSquare); break;
				case 9: clear = clearInSquare(6, 8, 6, 8, maxInSquare); break;
			}
			if (numbers[matrix[i][j] - 1] < maxNumber) {
				if (clear && matrix2[i][j] == 0) {
					matrix2[i][j] = matrix[i][j];
					count ++;
					numbers[matrix[i][j] - 1] ++;
				}
				
			}
			
		} while (count < maxClear);
		
	}


	private boolean clearInSquare(int min_i, int max_i, int min_j, int max_j, int maxInSquare) {
		int count = 0;
		for (int y = min_i; y <= max_i; y++) {
			for (int z = min_j; z <= max_j; z++) {
				if (matrix2[y][z] != 0) {
					count ++;
				}
			}
		}
		return count < maxInSquare;
	}

	public boolean isEndOfGame() {
		this.setResult("ganaste");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (matrix2[i][j] == 0) {
					return false;
				} else if (matrix2[i][j] != matrix[i][j]) {
					this.setResult("perdiste");
				}
			}
		}
		return true;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setMatrix2(int[][] matrix2) {
		this.matrix2 = matrix2;
	}
	
	
	
	

}
