function DrawingAppModel() {
    var self = this;

    self.strokeStyle = "blue";
    self.strokeWidth = 2;

    self.myTurn = false;

    var canvas,
        ctx,
        boundingRect,
        flag = false,
        prevX = 0,
        currX = 0,
        prevY = 0,
        currY = 0,
        dot_flag = false;

    self.points = new Array();

    self.init = function (canvas11, canvas22) {
        canvas = canvas11;
        ctx = canvas.getContext("2d");

        canvas.addEventListener("mousemove", function (e) {
            self.findxy('move', e)
        }, false);
        canvas.addEventListener("mousedown", function (e) {
            self.findxy('down', e)
        }, false);
        canvas.addEventListener("mouseup", function (e) {
            self.findxy('up', e)
        }, false);
        canvas.addEventListener("mouseout", function (e) {
            self.findxy('out', e)
        }, false);

        canvas2 = canvas22;
        ctx2 = canvas2.getContext("2d");
    };

    self.draw = function () {
        ctx.beginPath();
        ctx.moveTo(prevX, prevY);
        ctx.lineTo(currX, currY);
        ctx.strokeStyle = self.strokeStyle;
        ctx.lineWidth = self.strokeWidth;
        ctx.stroke();
        ctx.closePath();

        var point = new Object();
        point.x = currX;
        point.y = currY;
        self.points.push(point);
    };

    self.findxy = function (res, e) {
        if (self.myTurn == true) {
            if (res == 'down') {
                prevX = currX;
                prevY = currY;

                boundingRect = canvas.getBoundingClientRect();
                currX = e.pageX - (boundingRect.left + pageXOffset);
                currY = e.pageY - (boundingRect.top + pageYOffset);

                flag = true;
                dot_flag = true;
                if (dot_flag) {
                    ctx.beginPath();
                    ctx.fillStyle = self.strokeStyle;
                    ctx.fillRect(currX, currY, self.strokeWidth, self.strokeWidth);
                    ctx.closePath();
                    dot_flag = false;
                }
            }
            if (res == 'up' || res == "out") {
                flag = false;

                var point = new Object();
                point.x = -1;
                point.y = -1;
                self.points.push(point);
            }
            if (res == 'move') {
                if (flag) {
                    prevX = currX;
                    prevY = currY;

                    boundingRect = canvas.getBoundingClientRect();
                    currX = e.pageX - (boundingRect.left + pageXOffset);
                    currY = e.pageY - (boundingRect.top + pageYOffset);

                    self.draw();
                }
            }
        }
    };

    var canvas2,
        ctx2;
    var prevX2 = -1,
        prevY2 = -1;

    self.draw2 = function (currX2, currY2) {
        if (currX2 != -1 && currY2 != -1) {
            if (prevX2 == -1 || prevY2 == -1) {
                ctx2.beginPath();
                ctx2.fillStyle = self.strokeStyle;
                ctx2.fillRect(currX2, currY2, self.strokeWidth, self.strokeWidth);
                ctx2.closePath();
            }
            else {
                ctx2.beginPath();
                ctx2.moveTo(prevX2, prevY2);
                ctx2.lineTo(currX2, currY2);
                ctx2.strokeStyle = self.strokeStyle;
                ctx2.lineWidth = self.strokeWidth;
                ctx2.stroke();
                ctx2.closePath();
            }
        }

        prevX2 = currX2;
        prevY2 = currY2;
    };

    self.receiveDrawMessage = function (drawMessage) {
        if (self.myTurn == false) {
            for (var i = 0; i < drawMessage.points.length; i++) {
                self.draw2(drawMessage.points[i].x, drawMessage.points[i].y);
            }
        }
    };

    self.clearCanvas = function () {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx2.clearRect(0, 0, canvas2.width, canvas2.height);
    }

    self.enableMyTurn = function () {
        self.myTurn = true;
    };

    self.disableMyTurn = function () {
        self.myTurn = false;
    }
}