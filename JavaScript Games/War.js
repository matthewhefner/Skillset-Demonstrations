var active //2d boolean array of iteration
var newActive; //2d boolean array of next iteration
var alive; //2d boolean array of iteration
var newAlive; //2d boolean array of next iteration
var rows; //int # of rows
var cols; //int # of cols
var paused; //boolean toggles resume/pause
var neighbors; //2d int array of neighbors
var rulers; //2d int array of rulers
var scaler; //int size of each cell when drawn to screen
var blankArray; //for cleaning the arrays
var water; //int array holding RGBA value of water

function preload() {
  img = loadImage('map.png');
  pausedMenuImg = loadImage('pausedMenu.png');
  playingMenuImg = loadImage('playingMenu.png');
  // TODO:
  //new colors
  //world wrap
  //map options
  //touch menu 
  //resources
}

function setup()
{
    var canvas = createCanvas(960,540);
    //setAttributes('antialias', true);
	paused = true;
	scaler = 1;
	rows = 480 * 2;
	cols = 270 * 2;
	populateArrays();
	background(0);
	background(img, [0]);
	img.loadPixels();
	water = [img.pixels[0], img.pixels[1], img.pixels[2], img.pixels[3]];
	frameRate(500);
}

function draw() {
	if (!paused) {
		generation();
		background(img);
		drawMenu();
	} else {
		drawMenu();
	}
}

function drawMenu() {
	if (paused) {
		image(pausedMenuImg,0,0);
	} else {
		image(playingMenuImg,0,0);
	}
}

function keyPressed() {
	if (key == ' ') {
		pauseProcess();
	} else if (key == 's') {
		startMoves();
	}
	if (keyCode == DELETE) {
		if (confirm("Are you sure you want to clear the map?")) {
			populateArrays();
		}
	}
	return false; // prevent any default behavior
}

function pauseProcess() {
	paused = !paused;
	if (paused) {
		for (var k = 0; k < rows; k++) {
			newActive[k] = blankArray[k].slice();
			newAlive[k] = blankArray[k].slice();
			newActive[k] = active[k].slice();
			newAlive[k] = alive[k].slice();
		}
	} else {
		for (var k = 0; k < rows; k++) {
			active[k] = newActive[k].slice();
			alive[k] = newAlive[k].slice();
			newActive[k] = blankArray[k].slice();
			newAlive[k] = blankArray[k].slice();
		}
		fullCheck();
	}
}

function keyReleased() {
  return false; // prevent any default behavior
}

function mouseDragged() {
	if (paused)
	{
		var row = floor(mouseX / scaler);
		var col = floor(mouseY / scaler);
		switch(key) {
			case '1':
				fill(77,175,74);
				mouseHelper(row, col, 1);
				mouseHelper(row + 1, col, 1);
				mouseHelper(row, col + 1, 1);
				mouseHelper(row + 1, col + 1, 1);
				break;
			case '2':
				fill(55,126,255);
				mouseHelper(row, col, 2);
				mouseHelper(row + 1, col, 2);
				mouseHelper(row, col + 1, 2);
				mouseHelper(row + 1, col + 1, 2);
				break;
			case '3':
				fill(255,26,28);
				mouseHelper(row, col, 3);
				mouseHelper(row + 1, col, 3);
				mouseHelper(row, col + 1, 3);
				mouseHelper(row + 1, col + 1, 3);
				break;
			case '4':
				fill(255,255,51);
				mouseHelper(row, col, 4);
				mouseHelper(row + 1, col, 4);
				mouseHelper(row, col + 1, 4);
				mouseHelper(row + 1, col + 1, 4);
				break;
		}
	}
}

function startMoves() {
	fill(55,126,255);
	for (let u = 0; u < rows; u++) {
		for (let v = 0; v < cols; v++) {
			if ( (u) % 500 == 0 && u != 0) {
				mouseHelper(u, v, 2);
			}
		}
	}
}

function mouseHelper(row, col, rul) {
	rulers[row][col] = rul;
	newActive[row][col] = true;
	newAlive[row][col] = true;
	newLife(row, col);
	noStroke();
	rect(row * scaler, col * scaler, scaler, scaler);
}

function mousePressed() {
	let z = (rows * floor(mouseX / scaler) + floor(mouseY / scaler)) * 2 * 4;
	print(img.width, img.height);
	print(img.pixels[z]);
	if ((mouseX < 224 || mouseY < 50)) {
		if (mouseX > 8 && mouseX < 39 && mouseY > 8 && mouseY < 39) {
			key = '1';
		} else if (mouseX > 44 && mouseX < 74 && mouseY > 8 && mouseY < 39) {
			key = '2';
		} else if (mouseX > 79 && mouseX < 109 && mouseY > 8 && mouseY < 39) {
			key = '3';
		} else if (mouseX > 114 && mouseX < 144 && mouseY > 8 && mouseY < 39) {
			key = '4';
		} else if (mouseX > 149 && mouseX < 180 && mouseY > 8 && mouseY < 39) {
			pauseProcess();
		}
	} else if (paused) {
		var row = floor(mouseX / scaler);
		var col = floor(mouseY / scaler);
		switch(key) {
			case '1':
				fill(77,175,74);
				mouseHelper(row, col, 1);
				break;
			case '2':
				fill(55,126,255);
				mouseHelper(row, col, 2);
				break;
			case '3':
				fill(255,26,28);
				mouseHelper(row, col, 3);
				break;
			case '4':
				fill(255,255,51);
				mouseHelper(row, col, 4);
				break;
		}
	}
}

function generation() {
	for (var i = 0; i < rows; i++) {
		for (var j = 0; j < cols; j++) {
			if (active[i][j]) {
				if ((neighbors[i][j] == 2 || neighbors[i][j] == 3) & alive[i][j]) {
					newActive[i][j] = true;
					newAlive[i][j] = true;
					drawRect(i, j);
				} else if ((neighbors[i][j] == 3) & !alive[i][j]) {
					//New life.
					newLife(i,j);
					newActive[i][j] = true;
					newAlive[i][j] = true;
					drawRect(i, j);
				} else if ((neighbors[i][j] > 6) & alive[i][j]) {
					//Allows resource management.
					//(rows * floor(mouseY / scaler) + floor(mouseX / scaler)) * 2 * 4;
					let z = floor((rows * j * 2 + i) * 4 * 2);
					if (img.pixels[z] == 0) {
						newActive[i][j] = true;
						newAlive[i][j] = true;
						drawRect(i, j);	
					}
				} else if (neighbors[i][j] == 0){
					newActive[i][j] = false;
					newAlive[i][j] = false;
				} else {
					newActive[i][j] = true;
					newAlive[i][j] = false;
				}
			}
		}
	}
	for (var k = 0; k < rows; k++) {
		active[k] = newActive[k].slice();
		alive[k] = newAlive[k].slice();
		newActive[k] = blankArray[k].slice();
		newAlive[k] = blankArray[k].slice();
	}
	
	fullCheck();
}

function drawRect(i, j) {
	switch(rulers[i][j]) {
		case 1:
			fill(77,255,74);
			break;
		case 2:
			fill(55,126,255);
			break;
		case 3:
			fill(255,26,28);
			break;
		case 4:
			fill(255,255,51);
			break;
		default:
			break;
	} 
	rect(i * scaler, j * scaler, scaler, scaler);
}

function newLife(i, j) {
	//Top Left
	if (i > 0 && j > 0) {
		newActive[i - 1][j - 1] = true;
	} else if (j > 0){
		newActive[rows - 1][j - 1] = true;
	}
	//Left
	if (i > 0) {
		newActive[i - 1][j] = true;
	} else {
		newActive[rows - 1][j] = true;
	}
	//Bottom Left
	if (i > 0 && j < cols - 1) {
		newActive[i - 1][j + 1] = true;
	} else if (j < cols - 1){
		newActive[rows - 1][j + 1] = true;
	}
	//Top
	if (j > 0) {
		newActive[i][j - 1] = true;
	}
	//Bottom
	if (j < cols - 1) {
		newActive[i][j + 1] = true;
	}
	//Top Right
	if (i < rows - 1 && j > 0) {
		newActive[i + 1][j - 1] = true;
	} else if (j > 0) {
		newActive[0][j - 1] = true;
	}
	if (i < rows - 1) {
		newActive[i + 1][j] = true;
	} else {
		newActive[0][j] = true;
	}
	if (i < rows - 1 && j < cols - 1) {
		newActive[i + 1][j + 1] = true;
	} else if (j < cols - 1) {
		newActive[0][j + 1] = true;
	}
}

function fullCheck() {
	for (var i = 0; i < rows; i++) {
		for (var j = 0; j < cols; j++) {
			if (active[i][j]) {
				neighborCheck(i, j);
			}
		}
	}
}

function neighborCheck(i, j) {
	neighbors[i][j] = 0;
	var ruler = [0, 0, 0, 0, 0];
	//top left
	if (i > 0 && j > 0 && alive[i - 1][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i - 1][j - 1]] += 1;
	} else if (i == 0 && j > 0 && alive[rows - 1][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[rows - 1][j - 1]] += 1;
	}
	//left
	if (i > 0 && alive[i - 1][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[i - 1][j]] += 1;
	} else if (i == 0 && alive[rows - 1][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[rows - 1][j]] += 1;
	}
	//bottom left
	if (i > 0 && j < cols - 1 && alive[i - 1][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i - 1][j + 1]] += 1;
	} else if (i == 0 && j < cols - 1 && alive[rows - 1][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[rows - 1][j + 1]] += 1;
	}
	
	//top
	if (j > 0 && alive[i][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i][j - 1]] += 1;
	}
	//bottom
	if (j < cols - 1 && alive[i][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i][j + 1]] += 1;
	}
	
	//top right
	if (i < rows - 1 && j > 0 && alive[i + 1][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i + 1][j - 1]] += 1;
	} else if (i == rows - 1 && j > 0 && alive[0][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[0][j - 1]] += 1;
	}
	//right
	if (i < rows - 1 && alive[i + 1][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[i + 1][j]] += 1;
	} else if (i == rows - 1 && alive[0][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[0][j]] += 1;
	}
	//bottom right
	if (i < rows - 1 && j < cols - 1 && alive[i + 1][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i + 1][j + 1]] += 1;
	} else if (i == rows - 1 && j < cols - 1 && alive[0][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[0][j + 1]] += 1;
	}
	var max = 0;
	var maxIndex = 0;
	var maxCount = 2;
	for (var k = 1; k < 5; k++){
		if (ruler[k] > max) {
			max = ruler[k];
			maxIndex = k;
			maxCount = 1;
		} else if (ruler[k] == max) {
			maxCount++;
		}
	}
	if (maxCount == 1) {
		rulers[i][j] = maxIndex;
	} else {
		rulers[i][j] = 0;
	}
	return false;
}

function populateArrays() {
	active = [];
	newActive = [];
	alive = [];
	newAlive = [];
	neighbors = [];
	rulers = [];
	blankArray = [];
	for (var i = 0; i < rows; i++) {
		active.push([]);
		newActive.push([]);
		alive.push([]);
		newAlive.push([]);
		neighbors[i] = [];
		rulers[i] = [];
		blankArray.push([]);
		for (var j = 0; j < cols; j++) {
			active[i].push(false);
			newActive[i].push(false);
			alive[i].push(false);
			newAlive[i].push(false);
			blankArray[i].push(false);
			neighbors[i][j] = 0;
			rulers[i][j] = 0;
		}
	}
	return false;
}