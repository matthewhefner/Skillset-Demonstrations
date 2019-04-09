var active; //2d boolean array of iteration
var newActive; //2d boolean array of next iteration
var alive; //2d boolean array of iteration
var newAlive; //2d boolean array of next iteration
var rows; //int # of rows
var cols; //int # of cols
var paused; //boolean toggles resume/pause
var neighbors; //2d int array of neighbors
var rulers; //2d int array of rulers
var scalar; //int size of each cell when drawn to screen
var blankArray; //for cleaning the arrays
var water; //int array holding RGBA value of water

function preload() {
  img = loadImage('map.png');
  pausedMenuImg = loadImage('pausedMenu.png');
  playingMenuImg = loadImage('playingMenu.png');
  // TODO:
  //new colors
  //map options
  //resources
}

function setup() {
    var canvas = createCanvas(960,540);
    //setAttributes('antialias', true);
	paused = true;
	scalar = 1;
	cols = width;
	rows = height;
	populateArrays();
	background(0);
	image(img, 0, 0);
	img.loadPixels();
	//TODO: Resources
	//water = [img.pixels[0], img.pixels[1], img.pixels[2], img.pixels[3]];
	//print(water);
	frameRate(500);
}

function draw() {
	if (!paused) {
		generation();
		if (frameCount % 4 == 1) {
			background('rgba(0%,0%,0%,0.01)');
		}
		image(img, 0, 0);
	}
	drawMenu();
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
		for (var k = 0; k < cols; k++) {
			newActive[k] = blankArray[k].slice();
			newAlive[k] = blankArray[k].slice();
			newActive[k] = active[k].slice();
			newAlive[k] = alive[k].slice();
		}
	} else {
		for (var k = 0; k < cols; k++) {
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
		var col = floor(mouseX / scalar);
		var row = floor(mouseY / scalar);
		switch(key) {
			case '1':
				rulers[col][row] = 1;
				fill(77,175,74);
				mouseHelper(col, row);
				break;
			case '2':
				rulers[col][row] = 2;
				fill(55,126,255);
				mouseHelper(col, row);
				break;
			case '3':
				rulers[col][row] = 3;
				fill(255,26,28);
				mouseHelper(col, row);
				break;
			case '4':
				rulers[col][row] = 4;
				fill(255,255,51);
				mouseHelper(col, row);
				break;
		}
	}
}

function mouseHelper(col, row) {
	newActive[col][row] = true;
	newAlive[col][row] = true;
	newLife(col, row);
	noStroke();
	rect(col * scalar, row * scalar, scalar, scalar);
}

function mousePressed() {
	let z = (rows * floor(mouseX / scalar) + floor(mouseY / scalar)) * 2 * 4;
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
		var col = floor(mouseX / scalar);
		var row = floor(mouseY / scalar);
		switch(key) {
			case '1':
				rulers[col][row] = 1;
				fill(77,175,74);
				mouseHelper(col, row);
				break;
			case '2':
				rulers[col][row] = 2;
				fill(55,126,255);
				mouseHelper(col, row);
				break;
			case '3':
				rulers[col][row] = 3;
				fill(255,26,28);
				mouseHelper(col, row);
				break;
			case '4':
				rulers[col][row] = 4;
				fill(255,255,51);
				mouseHelper(col, row);
				break;
		}
	}
}

function generation() {
	for (var i = 0; i < cols; i++) {
		for (var j = 0; j < rows; j++) {
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
					if (onLand(i, j)) {
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
	for (var k = 0; k < cols; k++) {
		active[k] = newActive[k].slice();
		alive[k] = newAlive[k].slice();
		newActive[k] = blankArray[k].slice();
		newAlive[k] = blankArray[k].slice();
	}
	fullCheck();
}

function onLand(i, j) {
	//There are 4 numbers in the array per pixel
	return img.pixels[((width * j + i) * scalar * 4)] != 103;
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
	rect(i * scalar, j * scalar, scalar, scalar);
}

function newLife(i, j) {
	//Top Left
	if (i > 0 && j > 0) {
		newActive[i - 1][j - 1] = true;
	} else if (j > 0){
		newActive[cols - 1][j - 1] = true;
	}
	//Left
	if (i > 0) {
		newActive[i - 1][j] = true;
	} else {
		newActive[cols - 1][j] = true;
	}
	//Bottom Left
	if (i > 0 && j < rows - 1) {
		newActive[i - 1][j + 1] = true;
	} else if (j < rows - 1){
		newActive[cols - 1][j + 1] = true;
	}
	//Top
	if (j > 0) {
		newActive[i][j - 1] = true;
	}
	//Bottom
	if (j < rows - 1) {
		newActive[i][j + 1] = true;
	}
	//Top Right
	if (i < cols - 1 && j > 0) {
		newActive[i + 1][j - 1] = true;
	} else if (j > 0) {
		newActive[0][j - 1] = true;
	}
	if (i < cols - 1) {
		newActive[i + 1][j] = true;
	} else {
		newActive[0][j] = true;
	}
	if (i < cols - 1 && j < rows - 1) {
		newActive[i + 1][j + 1] = true;
	} else if (j < rows - 1) {
		newActive[0][j + 1] = true;
	}
}

function fullCheck() {
	for (var i = 0; i < cols; i++) {
		for (var j = 0; j < rows; j++) {
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
	} else if (i == 0 && j > 0 && alive[cols - 1][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[cols - 1][j - 1]] += 1;
	}
	//left
	if (i > 0 && alive[i - 1][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[i - 1][j]] += 1;
	} else if (i == 0 && alive[cols - 1][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[cols - 1][j]] += 1;
	}
	//bottom left
	if (i > 0 && j < rows - 1 && alive[i - 1][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i - 1][j + 1]] += 1;
	} else if (i == 0 && j < rows - 1 && alive[cols - 1][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[cols - 1][j + 1]] += 1;
	}
	
	//top
	if (j > 0 && alive[i][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i][j - 1]] += 1;
	}
	//bottom
	if (j < rows - 1 && alive[i][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i][j + 1]] += 1;
	}
	
	//top right
	if (i < cols - 1 && j > 0 && alive[i + 1][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i + 1][j - 1]] += 1;
	} else if (i == cols - 1 && j > 0 && alive[0][j - 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[0][j - 1]] += 1;
	}
	//right
	if (i < cols - 1 && alive[i + 1][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[i + 1][j]] += 1;
	} else if (i == cols - 1 && alive[0][j]) {
		neighbors[i][j] += 1;
		ruler[rulers[0][j]] += 1;
	}
	//bottom right
	if (i < cols - 1 && j < rows - 1 && alive[i + 1][j + 1]) {
		neighbors[i][j] += 1;
		ruler[rulers[i + 1][j + 1]] += 1;
	} else if (i == cols - 1 && j < rows - 1 && alive[0][j + 1]) {
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
	for (var i = 0; i < cols; i++) {
		active.push([]);
		newActive.push([]);
		alive.push([]);
		newAlive.push([]);
		neighbors[i] = [];
		rulers[i] = [];
		blankArray.push([]);
		for (var j = 0; j < rows; j++) {
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