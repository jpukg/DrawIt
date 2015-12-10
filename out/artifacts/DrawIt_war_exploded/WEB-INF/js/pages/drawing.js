var canvas,
    ctx,
    flag = false,
    prevX = 0,
    currX = 0,
    prevY = 0,
    currY = 0,
    dot_flag = false;

var points = new Array();

var x = "blue",
    y = 2;

function draw() {
    ctx.beginPath();
    ctx.moveTo(prevX, prevY);
    ctx.lineTo(currX, currY);
    ctx.strokeStyle = x;
    ctx.lineWidth = y;
    ctx.stroke();
    ctx.closePath();

    var point = new Object();
    point.x = currX;
    point.y = currY;
    points.push(point);
}

function findxy(res, e) {
    if (res == 'down') {
        prevX = currX;
        prevY = currY;
        currX = e.clientX - canvas.offsetLeft;
        currY = e.clientY - canvas.offsetTop;

        flag = true;
        dot_flag = true;
        if (dot_flag) {
            ctx.beginPath();
            ctx.fillStyle = x;
            ctx.fillRect(currX, currY, y, y);
            ctx.closePath();
            dot_flag = false;
        }
    }
    if (res == 'up' || res == "out") {
        flag = false;

        var point = new Object();
        point.x = -1;
        point.y = -1;
        points.push(point);
    }
    if (res == 'move') {
        if (flag) {
            prevX = currX;
            prevY = currY;
            currX = e.clientX - canvas.offsetLeft;
            currY = e.clientY - canvas.offsetTop;
            draw();
        }
    }
}

var canvas2,
    ctx2;
var prevX2 = -1,
    prevY2 = -1;
var x2 = "blue",
    y2 = 2;

function draw2(currX2, currY2) {
    if (currX2 != -1 && currY2 != -1) {
        if (prevX2 == -1 || prevY2 == -1) {
            ctx2.beginPath();
            ctx2.fillStyle = x2;
            ctx2.fillRect(currX2, currY2, y2, y2);
            ctx2.closePath();
        }
        else {
            ctx2.beginPath();
            ctx2.moveTo(prevX2, prevY2);
            ctx2.lineTo(currX2, currY2);
            ctx2.strokeStyle = x2;
            ctx2.lineWidth = y2;
            ctx2.stroke();
            ctx2.closePath();
        }
    }

    prevX2 = currX2;
    prevY2 = currY2;
}