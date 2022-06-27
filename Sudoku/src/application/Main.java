package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {
	Button b[][];
	Button a[];
	static int start = 0;
	int currentNumber = 0;
	String current = "";
	int difficulty = 20;
	int size = 9;
	Board board;
	int[][] copy;
	GridPane matrix[];
	Text TopText;
	ComboBox<String> combo;
	Solver solve;
	Scene scene;
	double width = 550;
	double height = 550;

	@Override
	public void start(Stage primaryStage) {
		Text MainLabel = new Text("Welcome to Sudoku");
		MainLabel.setFont(new Font("Times New Roman", 40));
		MainLabel.setFill(Color.WHITE);
		MainLabel.setStrokeWidth(.5);
		MainLabel.setStroke(Color.BLACK);
		VBox MainMenu = new VBox(70);
		MainMenu.getChildren().add(MainLabel);
		MainMenu.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
		combo = new ComboBox<String>();
		combo.getItems().addAll("EASY", "MEDIUM", "HARD");
		combo.setPromptText("Choose Your Difficulty");
		combo.prefWidthProperty().bind(MainMenu.widthProperty().divide(2));
		combo.prefHeightProperty().bind(MainMenu.heightProperty().divide(2));
		combo.setMaxHeight(150);
		combo.setOnAction(e -> {
			difficult();
		});
		scene = new Scene(MainMenu, width, height);
		MainMenu.setAlignment(Pos.CENTER);
		Button[] menu = new Button[3];
		board = new Board(size, difficulty);
		GridPane center = new GridPane();
		matrix = new GridPane[9];
		BorderPane root = new BorderPane();
		if (start == 1) {
			scene.setRoot(root);
		}
		HBox bottom = new HBox(5);
		bottom.setAlignment(Pos.CENTER);
		HBox top = new HBox(5);
		TopText = new Text("Choose a number");
		TopText.setFont(new Font("Times New Roman", 18));
		TopText.xProperty().bind(top.widthProperty().divide(4));

		Button topButtons[] = new Button[3];
		for (int i = 0; i < topButtons.length; i++) {
			topButtons[i] = new Button();
			topButtons[i].prefWidthProperty().bind(top.widthProperty().divide(4));
			topButtons[i].prefHeightProperty().bind(top.heightProperty().divide(4));
			topButtons[i].setMinSize(70, 40);
		}
		topButtons[0].setText("Restart");
		topButtons[1].setText("Menu");
		topButtons[2].setText("Solve");
		topButtons[0].setOnMouseClicked(e -> {
			width = scene.getWidth();
			height = scene.getHeight();
			start(primaryStage);
		});
		topButtons[1].setOnAction(e -> {
			start = 0;
			difficulty = 20;

			start(primaryStage);
		});
		topButtons[2].setOnAction(e -> {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (copy[i][j] == 0) {
						b[i][j].setText(solve.solvedMatrix[i][j] + "");
						b[i][j].setTextFill(Color.BLUE);

					}
				}

			}
			TopText.setText("You Used Auto Fill");
			for (int i = 0; i < a.length; i++) {
				a[i].setDisable(true);
			}
		});
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(topButtons[0], topButtons[2], TopText, topButtons[1]);
		root.setTop(top);
		root.setCenter(center);
		root.setBottom(bottom);
		root.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
		center.setPadding(new Insets(5));
		center.setHgap(5);
		center.setVgap(5);
		center.setGridLinesVisible(true);
		for (int i = 0; i < menu.length; i++) {
			menu[i] = new Button();
			menu[i].prefWidthProperty().bind(MainMenu.widthProperty().divide(4));
			menu[i].prefHeightProperty().bind(MainMenu.heightProperty().divide(4));
			menu[i].setMaxHeight(150);
		}
		menu[0].setText("START");
		menu[1].setText("Quit");
		menu[2].setText("How To Play");
		menu[0].setOnMouseClicked(e -> {
			scene.setRoot(root);
			Solved();
			start = 1;
		});

		menu[1].setOnAction(e -> {
			System.exit(0);
		});
		menu[2].setOnAction(e -> {
			MainLabel.setFont(new Font("Times New ``Roman", 16));
			MainLabel.setText(
					"Choose a number from below and place it in an empty space using left Click \n Right click on a number to undo");
			Timeline tmp = new Timeline(new KeyFrame(Duration.millis(5000), a -> {
				MainLabel.setFont(new Font("Times New Roman", 40));
				MainLabel.setText("Welcome To Sudoku");
			}));
			tmp.play();
		});
		MainMenu.getChildren().addAll(menu[0], combo, menu[2], menu[1]);
		b = new Button[size][size];
		for (int i = 0; i < 9; i++) {
			matrix[i] = new GridPane();
			matrix[i].setGridLinesVisible(true);
			center.add(matrix[i], i % 3, i / 3);
		}

		a = new Button[9];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				b[i][j] = new Button();
				b[i][j].prefWidthProperty().bind(center.widthProperty().divide(2));
				b[i][j].prefHeightProperty().bind(center.heightProperty().divide(2));
				b[i][j].setBackground(null);
				b[i][j].setFont(new Font("Comic Sans", 24));
				int x = i;
				int y = j;
				b[i][j].setOnMouseClicked(e -> {
					switch (e.getButton()) {
					case PRIMARY:
						play(x, y);
						break;
					case SECONDARY:
						clear(x, y);
						break;
					default:
						break;
					}
				});

			}
		}
		for (int i = 0; i < 9; i++) {
			a[i] = new Button();
			a[i].prefWidthProperty().bind(bottom.widthProperty().divide(2));
			a[i].prefHeightProperty().bind(bottom.heightProperty().divide(2));
			a[i].setText((i + 1) + "");
			int j = i;
			a[i].setOnMouseClicked(e -> {
				currentNumber = Integer.parseInt(a[j].getText());
				TopText.setText("You Have Choosen : " + currentNumber);
			});
			bottom.getChildren().add(a[i]);
		}
		addParts(0, 0, 0);
		addParts(1, 0, 3);
		addParts(2, 0, 6);
		addParts(3, 3, 0);
		addParts(4, 3, 3);
		addParts(5, 3, 6);
		addParts(6, 6, 0);
		addParts(7, 6, 3);
		addParts(8, 6, 6);
		board.fillValues();
		board.SudokuBoard(b);
		copy = board.copy();
		empty();
		Solved();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Sudoku");
		primaryStage.getIcons().add(new Image("103253.png"));
		BackgroundImage myBI = new BackgroundImage(
				new Image("BackGround.jpg", root.getWidth(), root.getHeight(), false, true), BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		MainMenu.setBackground(new Background(myBI));
		primaryStage.show();
	}

	public void play(int x, int y) {

		if (Integer.parseInt(b[x][y].getText()) == 0 && currentNumber != 0
				&& currentNumber == solve.solvedMatrix[x][y]) {
			b[x][y].setText(currentNumber + "");
			board.UpdateBoard(b);
			b[x][y].setTextFill(Color.BLUE);
			if (!board.checkFull()) {
				TopText.setText("You Win");
				for (int i = 0; i < a.length; i++) {
					a[i].setDisable(true);
				}
			}
		} else if (Integer.parseInt(b[x][y].getText()) == 0 && currentNumber != 0
				&& currentNumber != solve.solvedMatrix[x][y]) {
			b[x][y].setText(currentNumber + "");
			b[x][y].setTextFill(Color.RED);
			TopText.setText("Wrong");

		}
	}

	public void clear(int x, int y) {

		if (Integer.parseInt(b[x][y].getText()) != copy[x][y]) {
			b[x][y].setText(0 + "");
			board.UpdateBoard(b);
			b[x][y].setTextFill(Color.BEIGE);

		}
	}

	public void addParts(int index, int row, int column) {
		for (int i = row; i < row + 3; i++) {
			for (int j = column; j < column + 3; j++) {
				matrix[index].add(b[i][j], j, i);
			}
		}
	}

	public void newSBoard() {
		board = new Board(size, difficulty);
		board.fillValues();
		board.SudokuBoard(b);
		copy = board.copy();
		empty();
		Solved();
	}

	public void difficult() {
		if (combo.getValue().equals("EASY")) {
			difficulty = 20;
			newSBoard();

		}
		if (combo.getValue().equals("MEDIUM")) {
			difficulty = 36;
			newSBoard();

		}
		if (combo.getValue().equals("HARD")) {
			difficulty = 46;
			newSBoard();

		}

	}

	public void empty() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (Integer.parseInt(b[i][j].getText()) == 0) {
					b[i][j].setTextFill(Color.BEIGE);
				}
			}
		}
	}

	public void Solved() {
		solve = new Solver(size, board.mat);
		if (solve.solve()) {

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
