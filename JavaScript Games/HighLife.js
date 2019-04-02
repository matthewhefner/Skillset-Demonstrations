var board;
var newBoard;
var rows;
var cols;
var paused;
var neighbors;
var grid;

function preload() {
  //img = loadImage('background.png');
}

function setup()
{
    var canvas = createCanvas(610,610);
    //setAttributes('antialias', true);
	paused = true;
	grid = false;
	rows = 100;
	cols = 100;
	board = [];
	newBoard = [];
	neighbors = [];
	populateBoard();
	print("Done.");
	drawBoard();
}

function mouseWheel(event)
{
	//change wave type?
}

function draw() {
	if (!paused) {
		generation();
		drawBoard();
	}
}

function keyPressed() {
	if (key == ' ') {
		paused = !paused;
		fullCheck();
	}
	if (key == 'g') {
		grid = !grid;
		drawBoard();
	}
	return false; // prevent any default behavior
}

function keyReleased() {

  return false; // prevent any default behavior
}

function mouseDragged() {
	if (paused)
	{
		if (grid) {
			stroke(255);
		} else {
			noStroke();
		}
		var row = floor(mouseX / 6);
		var col = floor(mouseY / 6);
		print(row);
		print(col);
		if (board[row][col] == 0) {
			board[row][col] = 1;
			fill(255);
			rect(row * 6, col * 6, 6, 6);
		}
	}
}

function mousePressed() {
	if (paused)
	{
		if (grid) {
			stroke(255);
		} else {
			noStroke();
		}
		var row = floor(mouseX / 6);
		var col = floor(mouseY / 6);
		print(row);
		print(col);
		if (board[row][col] == 0) {
			if ((row > 0) & (row < rows - 1) & (col > 0) & (col < cols - 1)) {
				board[row][col] = 1;
				fill(255);
				rect(row * 6, col * 6, 6, 6);
			}
		}
	}
}

function generation() {
	for (var i = 1; i < rows - 1; i++) {
		for (var j = 1; j < cols - 1; j++) {
			if (((neighbors[i][j] == 2 || neighbors[i][j] == 3) & board[i][j] == 1) ||
				(neighbors[i][j] == 3 || neighbors[i][j] == 6) & board[i][j] == 0) {
				newBoard[i][j] = 1;
			} else {
				newBoard[i][j] = 0;
			}
		}
	}
	board = newBoard;
	fullCheck();
}

function fullCheck() {
	for (var i = 1; i < rows - 1; i++) {
		for (var j = 1; j < cols - 1; j++) {
			neighborCheck(i, j);
		}
	}
}

function neighborCheck(i, j) {
	neighbors[i][j] = 0;
	neighbors[i][j] += board[i - 1][j - 1];
	neighbors[i][j] += board[i - 1][j];
	neighbors[i][j] += board[i - 1][j + 1];
	neighbors[i][j] += board[i][j - 1];
	neighbors[i][j] += board[i][j + 1];
	neighbors[i][j] += board[i + 1][j - 1];
	neighbors[i][j] += board[i + 1][j];
	neighbors[i][j] += board[i + 1][j + 1];
	return false;
}

function drawBoard() {
	if (grid) {
		stroke(255);
	} else {
		noStroke();
	}
	for (var i = 0; i < rows; i++) {
		for (var j = 0; j < cols; j++) {
			if (board[i][j] == 1) {
				fill(255);
				if (neighbors[i][j] == 2 || neighbors[i][j] == 3) {
					fill(0,255,0);
				} else {
					fill(255,0,0);
				}
			} else {
				fill(0);
				if (neighbors[i][j] == 3 || neighbors[i][j] == 6) {
					fill(0,0,255);
				}
			}
			rect(i * 6, j * 6, 6, 6);
		}
	}
	return false;
}

function populateBoard() {
	for (var i = 0; i < rows; i++) {
		board[i] = [];
		newBoard[i] = [];
		neighbors[i] = [];
		for (var j = 0; j < cols; j++) {
			board[i][j] = 0;
			newBoard[i][j] = 0;
			neighbors[i][j] = 0;
		}
	}
	return false;
}