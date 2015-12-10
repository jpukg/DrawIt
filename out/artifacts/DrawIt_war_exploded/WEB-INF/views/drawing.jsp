<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Draw It</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>

    <script src="<c:url value='/js/jquery/jquery.js'/>"></script>
    <script src="<c:url value='/js/jquery/jquery.tmpl.min.js'/>"></script>
    <script src="<c:url value='/js/jquery/jquery.atmosphere.js'/>"></script>

    <script src="<c:url value='/js/pages/drawing.js'/>"></script>

</head>
<body>

<div id="header">
    <h1>Drawing</h1>
</div>

<div id="canvasDiv">
    <canvas id="can" width="200" height="200" style="border:2px solid;"></canvas>
</div>

<div id="canvasDiv2">
    <canvas id="can2" width="200" height="200" style="border:2px solid;"></canvas>
</div>

<br>

<div>
    <c:url value="/main" var="mainUrl"/>
    <a href="${mainUrl}">Back to main</a>
</div>

<script type="text/javascript">

    $(function () {

        function onMessage(response) {

            var message = response.responseBody;
            var result;

            try {
                result = $.parseJSON(message);
            } catch (e) {
                console.log("An error ocurred while parsing the JSON Data: " + message.data + "; Error: " + e);
                return;
            }

            if (result == null || !result.hasOwnProperty("@class")) {
                console.log("No object returned or no class specified.");
                return;
            }

            var resultType = result['@class'];
            console.log('Object type returned: ' + resultType);

            if (resultType == "draw_it.data.messages.DrawMessage") {
                handleDrawMessage(result);
            } else {
                throw "resultType " + resultType + " is not handled.";
            }
        }

        function handleDrawMessage(data) {
            console.log("Handling Draw Message...");
            for (var i = 0; i < data.points.length; i++) {
                draw2(data.points[i].x, data.points[i].y);
            }
        }

        var socket = $.atmosphere;
        var subSocket;
        var transport = 'long-polling';
        var drawUrl = "${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/draw/";

        var request = {
            url: drawUrl,
            contentType: "application/json",
            logLevel: 'debug',
            //shared : 'true',
            transport: transport,
            fallbackTransport: 'long-polling',
            maxRequest: 10000000,
            //reconnectInterval: 10000,
            //callback: callback,
            onMessage: onMessage,
            onOpen: function (response) {
                console.log('Atmosphere onOpen: Atmosphere connected using ' + response.transport);
                transport = response.transport;
            },
            onReconnect: function (request, response) {
                console.log("Atmosphere onReconnect: Reconnecting");
            },
            onClose: function (response) {
                console.log('Atmosphere onClose executed');
            },

            onError: function (response) {
                console.log('Atmosphere onError: Sorry, but there is some problem with your '
                + 'socket or the server is down');
            }
        };

        subSocket = socket.subscribe(request);

//        Drawing init

        canvas = document.getElementById('can');
        ctx = canvas.getContext("2d");
        canvas2 = document.getElementById('can2');
        ctx2 = canvas2.getContext("2d");
        var w = canvas.width;
        var h = canvas.height;

        canvas.addEventListener("mousemove", function (e) {
            findxy('move', e)
        }, false);
        canvas.addEventListener("mousedown", function (e) {
            findxy('down', e)
        }, false);
        canvas.addEventListener("mouseup", function (e) {
            findxy('up', e)
        }, false);
        canvas.addEventListener("mouseout", function (e) {
            findxy('out', e)
        }, false);

        var timer = setTimeout(function tick() {

            if (points.length > 0) {
                sendDraw(points);
                points.length = 0;
            }
            timer = setTimeout(tick, 350);
        }, 350);

        function sendDraw(points) {
            var drawingAdded = new Object();
            drawingAdded["@class"] = "draw_it.data.messages.DrawMessage";
            drawingAdded.points = points;
            var value = JSON.stringify(drawingAdded);
            subSocket.push(value);
        }
    });
</script>
</body>
</html>
