import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import java.util.*; //<>//

public class Labirynt extends PApplet {

	// Run this project as Java application and this
	// method will launch the sketch
	public static void main(String[] args) {
		String[] a = { "MAIN" };
		PApplet.runSketch(a, new Labirynt());
	}

	// The rest is what you would expect in the sketch

	int x, y;

	public void settings() {
		size(1000, 1000);

	}

	Maze maze;
	Map k;
	int map[][][];
	int scale = 6;
	java.awt.Point start = new java.awt.Point();
	java.awt.Point end = new java.awt.Point();
	int posX = 0;
	int posY = 0;
	java.util.Random generator = new java.util.Random();
	int m;

	public void setup() {
		translate(25, 25);
		background(255, 255, 255);
		frameRate(100);
		strokeWeight(1);
		fill(255, 100);
		stroke(0, 100);
		maze = new Maze("mazeData.txt");
		k = new Map("mazeData.txt");
		map = k.getMapMatrix();
		end.x = k.getMaxX();
		end.y = k.getMaxY();
		maze.drawMaze();
		fill(255, 0, 0);
		ArrayList<String> places = new ArrayList<String>();
	}

	void drawObj(int x, int y, int scale) {
		stroke(0);
		rect(x * scale, y * scale, scale, scale);
	}

	boolean isDeadEnd() {

		int pom = 0;
		if (map[posX][posY][MapHelper.R] == 1) {
			pom++;
		}
		if (map[posX][posY][MapHelper.D] == 1) {
			pom++;
		}

		if (map[posX][posY][MapHelper.L] == 1) {
			pom++;
		}

		if (map[posX][posY][MapHelper.U] == 1) {
			pom++;
		}
		if (pom >= 3) {
			return true;
		} else
			return false;
	}

	boolean canGoForwad(int posX, int posY, int lastDirection) {
		if (map[posX][posY][lastDirection] == 0) {
			return true;
		}
		return false;
	}

	void moveForward(int direction) {
		boolean temp = isDeadEnd();
		switch (direction) {
		case 3:
			posX++;
			if(temp==true) {
				map[posX][posY][MapHelper.L]=1;
			}
			break;
		case 2:
			posY++;
			if(temp==true) {
				map[posX][posY][MapHelper.U]=1;
			}
			break;
		case 1:
			posY--;
			if(temp==true) {
				map[posX][posY][MapHelper.D]=1;
			}
			break;
		case 0:
			posX--;
			if(temp==true) {
				map[posX][posY][MapHelper.R]=1;
			}
			break;
		}
	}

	int lastDir;
	// -1 - vertical, 1 horizontal
	int orientation = 1;

	void firsAlgorithm() {
		boolean temp = isDeadEnd();
		println("Aktualna pozycja X =  " + posX + " Y = " + posY + " ");
		if (isDeadEnd()) {
			fill(0, 0, 0);
			drawObj(posX, posY, scale);
			fill(255, 0, 0);
			println("POZx " + posX + " POZy " + posY + " is dedEnd");
		}

		if (orientation == 0) {

			if (map[posX][posY][MapHelper.R] == 0) {
				// posX++;

				lastDir = MapHelper.R;
				orientation = 1;
				// return 0;
			} else if (canGoForwad(posX, posY, lastDir)) {

			} else if (map[posX][posY][MapHelper.L] == 0) {
				// posX++;
				lastDir = MapHelper.L;
				orientation = 1;
				// return 0;
			} else {
				if (lastDir == MapHelper.R) {
					lastDir = MapHelper.L;
				} else if (lastDir == MapHelper.L) {
					lastDir = MapHelper.R;
				} else if (lastDir == MapHelper.U) {
					lastDir = MapHelper.D;
				} else {
					lastDir = MapHelper.U;
				}
			}
		} else {
			if (map[posX][posY][MapHelper.D] == 0) {
				// posX++;

				lastDir = MapHelper.D;
				orientation = 0;
				// return 0;
			} else if (canGoForwad(posX, posY, lastDir)) {

			} else if (map[posX][posY][MapHelper.U] == 0) {
				// posX++;
				lastDir = MapHelper.U;
				orientation = 0;
				// return 0;
			} else {
				if (lastDir == MapHelper.R) {
					lastDir = MapHelper.L;
				} else if (lastDir == MapHelper.L) {
					lastDir = MapHelper.R;
				} else if (lastDir == MapHelper.U) {
					lastDir = MapHelper.D;
				} else {
					lastDir = MapHelper.U;
				}
			}
		}
		moveForward(lastDir);

		// return 0;
	}

	public void draw() {
		translate(25, 25);
		drawObj(posX, posY, scale);
		firsAlgorithm();

		if ((posX == end.x) && (posY == end.y)) {
			redraw();
			fill(0, 255, 0);
			drawObj(posX, posY, scale);
			noLoop();
		}
		// noLoop();
	}

	public class Maze {
		private String lines[];
		private String matrix[][];
		private int maxX = 0;
		private int maxY = 0;
		private final int MAX_ELEMENTS_IN_LINE = 3;

		Maze(String path) {
			loadTrimedLines(path);
			this.matrix = loadMatrix(this.lines);
		}

		private void loadTrimedLines(String path) {
			String temp[] = loadStrings(path);
			for (int i = 0; i < temp.length; i++) {
				temp[i] = trimLine(temp[i]);
			}
			this.lines = temp;
		}

		public String[] getLines() {
			return this.lines;
		}

		private String trimLine(String line) {
			return trim(line);
		}

		private String[] splitLine(String line) {
			return split(line, ' ');
		}

		private String[][] loadMatrix(String lines[]) {
			String temp[][] = new String[lines.length][MAX_ELEMENTS_IN_LINE];
			for (int i = 0; i < lines.length; i++) {
				temp[i] = splitLine(lines[i]);
			}
			return temp;
		}

		public void drawMaze() {
			for (int i = 0; i < matrix.length; i++) {
				if (Integer.parseInt(matrix[i][Helper.X]) > maxX) {
					maxX = Integer.parseInt(matrix[i][Helper.X]);
				}
				if (Integer.parseInt(matrix[i][Helper.Y]) > maxY) {
					maxY = Integer.parseInt(matrix[i][Helper.Y]);
				}
				if (matrix[i][Helper.DIR].equals("2")) {
					line(Float.parseFloat(matrix[i][Helper.X]) * scale, Float.parseFloat(matrix[i][Helper.Y]) * scale,
							Float.parseFloat(matrix[i][Helper.X]) * scale + scale,
							Float.parseFloat(matrix[i][Helper.Y]) * scale);
					// println(matrix[i][Helper.X] + matrix[i][Helper.Y] +
					// matrix[i][Helper.DIR]);
				}
				if (matrix[i][Helper.DIR].equals("0")) {
					line(Float.parseFloat(matrix[i][Helper.X]) * scale, Float.parseFloat(matrix[i][Helper.Y]) * scale,
							Float.parseFloat(matrix[i][Helper.X]) * scale + scale,
							Float.parseFloat(matrix[i][Helper.Y]) * scale);
					line(Float.parseFloat(matrix[i][Helper.X]) * scale, Float.parseFloat(matrix[i][Helper.Y]) * scale,
							Float.parseFloat(matrix[i][Helper.X]) * scale,
							Float.parseFloat(matrix[i][Helper.Y]) * scale + scale);
					// println(matrix[i][Helper.X] + matrix[i][Helper.Y] +
					// matrix[i][Helper.DIR]);
				}
				if (matrix[i][Helper.DIR].equals("1")) {
					line(Float.parseFloat(matrix[i][Helper.X]) * scale, Float.parseFloat(matrix[i][Helper.Y]) * scale,
							Float.parseFloat(matrix[i][Helper.X]) * scale,
							Float.parseFloat(matrix[i][Helper.Y]) * scale + scale);
					// println(matrix[i][Helper.X] + matrix[i][Helper.Y] +
					// matrix[i][Helper.DIR]);
				}
			}
			rect(0, 0, maxX * scale + scale, maxY * scale + scale);
		}
	}

	public class Map {
		private int MazeMatrix[][][];
		private String lines[];
		private String matrix[][];
		private final int MAX_ELEMENTS_IN_LINE = 3;

		Map(String path) {
			loadTrimedLines(path);
			this.matrix = loadMatrix(this.lines);
		}

		private void loadTrimedLines(String path) {
			String temp[] = loadStrings(path);
			for (int i = 0; i < temp.length; i++) {
				temp[i] = trimLine(temp[i]);
			}
			this.lines = temp;
		}

		public String[] getLines() {
			return this.lines;
		}

		private String trimLine(String line) {
			return trim(line);
		}

		private String[] splitLine(String line) {
			return split(line, ' ');
		}

		private String[][] loadMatrix(String lines[]) {
			String temp[][] = new String[lines.length][MAX_ELEMENTS_IN_LINE];
			for (int i = 0; i < lines.length; i++) {
				temp[i] = splitLine(lines[i]);
			}
			return temp;
		}

		public String[][] getMatrix() {
			return this.matrix;
		}

		public int getMaxX() {
			float temp = 0;
			for (int i = 0; i < matrix.length; i++) {
				if (Float.parseFloat(matrix[i][Helper.X]) > temp) {
					temp = Float.parseFloat(matrix[i][Helper.X]);
				}
			}
			return (int) temp;
		}

		public int getMaxY() {
			float temp = 0;
			for (int i = 0; i < matrix.length; i++) {
				if (Float.parseFloat(matrix[i][Helper.Y]) > temp) {
					temp = Float.parseFloat(matrix[i][Helper.Y]);
				}
			}
			return (int) temp;
		}

		// [x][y][Left,Up,Down,Right]
		private int[][][] generateMapMatrix() {
			MazeMatrix = new int[getMaxX() + 1][getMaxY() + 1][Helper.DIR_NUMB];
			for (int x = 0; x < MazeMatrix.length; x++) {
				for (int y = 0; y < MazeMatrix[x].length; y++) {

					if (x == 0) {
						MazeMatrix[x][y][MapHelper.L] = 1;
					}
					if (x == getMaxX()) {
						MazeMatrix[x][y][MapHelper.R] = 1;
					}
					if (y == 0) {
						MazeMatrix[x][y][MapHelper.U] = 1;
					}
					if (y == getMaxY()) {
						MazeMatrix[x][y][MapHelper.D] = 1;
					}

					for (int x1 = 0; x1 < matrix.length; x1++) {
						// if ((x>0)||(y>0)) {
						if ((Integer.parseInt(matrix[x1][Helper.X]) == x)
								&& (Integer.parseInt(matrix[x1][Helper.Y]) == y)) {
							// println("Matrix:"+matrix[x1][Helper.X]+" "+
							// matrix[x1][Helper.Y], "For: "+ x+" "+y);

							// println(matrix[x1][Helper.DIR]);
							switch (matrix[x1][Helper.DIR]) {
							case "0":
								// println("Matrix:"+matrix[x1][Helper.X]+" "+
								// matrix[x1][Helper.Y], "For: "+ x+" "+y,
								// "dec"+matrix[x1][Helper.DIR]);
								if ((y - 1) >= 0) {
									// println("Matrix:"+matrix[x1][Helper.X]+"
									// "+ matrix[x1][Helper.Y], "For: "+ x+"
									// "+y, "dec"+matrix[x1][Helper.DIR]);
									MazeMatrix[x][y - 1][MapHelper.D] = 1;
									MazeMatrix[x][y][MapHelper.U] = 1;
								}
								if ((x - 1) >= 0) {
									// println("Matrix:"+matrix[x1][Helper.X]+"
									// "+ matrix[x1][Helper.Y], "For: "+ x+"
									// "+y, "dec"+matrix[x1][Helper.DIR]);
									MazeMatrix[x - 1][y][MapHelper.R] = 1;
									MazeMatrix[x][y][MapHelper.L] = 1;
								}

								break;
							case "2":
								if ((y - 1) >= 0) {
									// println("Matrix:"+matrix[x1][Helper.X]+"
									// "+ matrix[x1][Helper.Y], "For: "+ x+"
									// "+y, "dec"+matrix[x1][Helper.DIR]);
									MazeMatrix[x][y - 1][MapHelper.D] = 1;
									MazeMatrix[x][y][MapHelper.U] = 1;
								}
								break;
							case "1":
								if ((x - 1) >= 0) {
									MazeMatrix[x - 1][y][MapHelper.R] = 1;
									MazeMatrix[x][y][MapHelper.L] = 1;
								}
								break;
							}
						}
						// }
					}
				}
			}
			return MazeMatrix;
		}

		public int[][][] getMapMatrix() {
			return generateMapMatrix();
		}
	}

	public static class Helper {
		public static int X = 0;
		public static int Y = 1;
		public static int DIR = 2;
		public static int DIR_NUMB = 4;
	}

	public static class MapHelper {
		// left
		public static int L = 0;
		// up
		public static int U = 1;
		// down
		public static int D = 2;
		// right
		public static int R = 3;
	}
}