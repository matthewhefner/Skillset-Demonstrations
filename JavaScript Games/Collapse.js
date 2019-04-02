var board;
var millisecond;
var sec = 0;
var blockSize;
var rows = 16;
var cols = 22;
var state;
var lose;
var score;
var searchKeeper;
var searchBoard;
var level;
var completed;
var colors = 3;

function setup() {
  w = 800;
  createCanvas(w, w * (rows / cols));
  blockSize = width / cols;
  board = [];
  searchBoard = [];
  state = 0;
  lose = 0;
  score = 0;
  level = 5;
  searchKeeper = 0;
  init();
  drawBoard()
}

function draw() {
  secon = floor(level * millis() / 1000);
  if (secon - sec > 0) {
    sec = secon;
    advanceState();
  }
  if (lose != 0) {
    noLoop();
    print("GAME OVER");
    textSize(48);
    stroke(0);
    strokeWeight(4);
    fill(255);
    textAlign(CENTER);
    text("GAME OVER", width / 2, 150);
  } else if (completed >= rows * ((level + 5) / 5)) {
    print("PAGEEEE");
    init();
    level += 5;
    if (level / 5 % 5 == 0 && colors < 6) {
      colors++;
    }
  }
}

function mousePressed() {
  if (lose == 0) {
  if (mouseY < (rows - 1) * blockSize) {
    search(floor(mouseY / blockSize), floor(mouseX / blockSize));
    if (searchKeeper >= 3) {
      del(floor(mouseY / blockSize), floor(mouseX / blockSize));
      score += (level / 5) * (searchKeeper - 2) * (searchKeeper - 1) / 2;
      collapse();
      drawBoard();
    }
  }
  }
}

function init() {
  for (var i = 0; i < rows; i++) {
    board[i] = [];
    searchBoard[i] = [];
    for (var j = 0; j < cols; j++) {
      board[i][j] = 0;
      searchBoard[i][j] = 0;
    }
  }
  completed = 0;
  for (var i = rows - 2; i > rows - 5; i--) {
    for (var j = 0; j < cols; j++) {
      board[i][j] = floor(1 + random(1) * colors);
    }
  }
}

function advanceState() {
  if (state == cols) {
    state = 0;
    advanceRow();
    drawBoard();
    completed++;
    print(completed);
  } else {
    board[rows - 1][state] = floor(1 + random(1) * colors);
    //print(state + " " + board[rows - 1][state]);
    strokeWeight(2);
    stroke(255);
    drawRect(rows - 1, state, board[rows - 1][state]);
    state++;
  }
}

function advanceRow() {
    for (var j = 0; j < cols; j++) {
		lose += board[0][j];
	}
  	for (var k = 0; k < rows - 1; k++) {
		board[k] = board[k + 1].slice();
	}
    for (var j = 0; j < cols; j++) {
      board[rows - 1][j] = 0;
    }
}

function drawBoard() {
  //TODO: Add number of rows until next level
  background(0);
  stroke(255);
  strokeWeight(2);
  // Feeder Row Divider 
  line(0, (rows - 1) * blockSize, width, (rows - 1) * blockSize);
  strokeWeight(2);
  for (var i = 0; i < rows; i++) {
    for (var j = 0; j < cols; j++) {
      if (board[i][j] != 0){
       drawRect(i, j, board[i][j]); 
      }
    }
  }
  textSize(32);
  stroke(0);
  fill(255);
  textAlign(LEFT);
  text("Score: " + score, 10, 30);
  textAlign(RIGHT);
  text("Level: " + (level / 5), width - 10, 30);
}

function drawRect(j, i, k) {
  switch (k) {
    case 1:
      fill(255, 0, 0);
      break;
    case 2:
      fill(0, 255, 0);
      break;
    case 3:
      fill(0, 0, 255);
      break;
    case 4:
      fill(255, 255, 0);
      break;
    case 5:
      fill(255, 0, 255);
      break;
    default:
      fill(200);
  }
  rect(i * blockSize, j * blockSize, blockSize, blockSize);
}

function search(i, j) {
    searchKeeper = 0;
  	for (var k = 0; k < rows; k++) {
		searchBoard[k] = board[k].slice();
	}
    searchDel(i, j)
}

function searchDel(i, j) {
  if (searchBoard[i][j] != 0) {
    searchKeeper++;
    old = searchBoard[i][j];
    searchBoard[i][j] = 0;
  if (i > 0 && searchBoard[i - 1][j] == old) {
    searchDel(i - 1, j);
  } 
  if (i < rows - 2 && searchBoard[i + 1][j] == old) {
    searchDel(i + 1, j)
  }
  if (j > 0 && searchBoard[i][j - 1] == old) {
    searchDel(i, j - 1);
  } 
  if (j < cols - 1 && searchBoard[i][j + 1] == old) {
    searchDel(i, j + 1)
  }
  }
}

function del(i, j) {
  if (board[i][j] != 0) {
    old = board[i][j];
    board[i][j] = 0;
  if (i > 0 && board[i - 1][j] == old) {
    del(i - 1, j);
  } 
  if (i < rows - 2 && board[i + 1][j] == old) {
    del(i + 1, j)
  }
  if (j > 0 && board[i][j - 1] == old) {
    del(i, j - 1);
  } 
  if (j < cols - 1 && board[i][j + 1] == old) {
    del(i, j + 1)
  }
  }
}

function collapse() {
  	for (var i = rows - 3; i >= 0; i--) {
		for (var j = 0; j < cols; j++) {
          var k = i;
         while (board[k + 1][j] == 0 
                && board[k][j] != 0
                && k < rows - 2) {
           board[k + 1][j] = board[k][j];
           board[k][j] = 0;
           k++;
         }
        }
	}
}