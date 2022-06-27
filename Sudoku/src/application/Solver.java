package application;

public class Solver {
	int[][] solvedMatrix;

	Solver(int n, int mat[][]) {
		solvedMatrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				solvedMatrix[i][j] = mat[i][j];
			}

		}
	}

	public boolean inRow(int number, int row) {
		for (int i = 0; i < solvedMatrix.length; i++) {
			if (solvedMatrix[row][i] == number) {
				return true;
			}
		}
		return false;
	}

	public boolean inColumn(int number, int Column) {
		for (int i = 0; i < solvedMatrix.length; i++) {
			if (solvedMatrix[i][Column] == number) {
				return true;
			}
		}
		return false;
	}

	public boolean inBox(int number, int row, int column) {
		int rowInBox = row - row % 3;
		int columnInBox = column - column % 3;
		for (int i = rowInBox; i < rowInBox + 3; i++) {
			for (int j = columnInBox; j < columnInBox + 3; j++) {
				if (solvedMatrix[i][j] == number)
					return true;
			}
		}
		return false;
	}

	public boolean freeNumber(int number, int row, int column) {
		return !inBox(number, row, column) && !inColumn(number, column) && !inRow(number, row);
	}

	public boolean solve() {
		for (int i = 0; i < solvedMatrix.length; i++) {
			for (int j = 0; j < solvedMatrix.length; j++) {
				if (solvedMatrix[i][j] == 0) {
					for (int num = 1; num <= 9; num++) {
						if (freeNumber(num, i, j)) {
							solvedMatrix[i][j] = num;
							if (solve()) {
								return true;
							} else {
								solvedMatrix[i][j] = 0;
							}

						}
					}
					return false;
				}
			}
		}
		return true;
	}
}
