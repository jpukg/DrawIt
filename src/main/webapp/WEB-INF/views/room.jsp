<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<html>
<head>
    <title>Draw It</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>

    <script src="<c:url value='/js/jquery/jquery.js'/>"></script>
    <script src="<c:url value='/js/jquery/jquery.tmpl.min.js'/>"></script>
    <script src="<c:url value='/js/jquery/jquery.atmosphere.js'/>"></script>
    <script src="<c:url value='/js/knockout/knockout.js'/>"></script>

    <script src="<c:url value='/js/pages/room_model.js'/>"></script>
    <script src="<c:url value='/js/pages/drawing_model.js'/>"></script>

    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <link href="/css/header.css" rel="stylesheet">
    <link href="/css/room.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>


</head>
<body onunload="">
<div id="header">
    <label>Draw It &#9998;</label>
</div>


<div id="backDiv">
    <a href="javascript:leaveRoom();" class="button" style="padding: 7px">
        <i class="fa fa-reply"></i>
        Leave
    </a>
</div>


<div class="testbox">

<h3>Room "${roomTitle}"</h3>
    <hr>

<%--Marina added for debugging--%>
<h3>Your login: ${pageContext.request.userPrincipal.name}</h3>
<hr>
    <div id="results">
        <b>Results:</b><br/>
    </div>
<table style="margin-top: 20px">
    <tr style="height: 40px">
        <td style="width: 37%;">
            <%--<p text-color=" #3aaf57" style="text-align: center"><b>Players:</b></p>--%>
            <h3>Players:</h3>
        </td>
        <td style="width: 430px">
            <h4 id="word"></h4>
        </td>
        <td>
            <div style="width: 370px; text-align: center">
                <%--<p text-color=" #3aaf57"><b>Chat</b></p>--%>
                <h3>Chat</h3>
            </div>
        </td>
    </tr>
    <tr>
        <td style="width: 37%; height: 430px;">
            <div id="members">
                <div id="memberList" style="height: 40px;"
                     data-bind="foreach: members">
                    <div><a data-bind="text: login, attr:{href:'/profile/'+login}" target="_blank"></a></div>
                </div>
            </div>
        </td>
        <td>
            <div id="canvasDiv" align="middle">
                <canvas id="can" width="430" height="430"></canvas>
            </div>
        </td>
        <td style="width: 35%;">
            <div align="top">
                <%--<div id="header">--%>
                <div id="content" style="height: 430px;  width: 365px;">
                    <div id="chat-messages" style="height: 398px; overflow: auto; max-height: 430px;width:365px; "
                         data-bind="foreach: notifications">
                        <div data-bind="text: notification, style: { color: mode == 'system' ? '#3aaf57' : '#4c4c4c' }"></div>
                    </div>
                    <div style="position: relative; bottom: -7px;">
                        <input id="message-field" type="text" size="40" placeholder="Message text"/>
                        <input id="message-button" type="button" class="button" value="Send" onclick="sendChatMessage();"/>
                    </div>
                </div>
            </div>
        </td>
        <%--</div>--%>
    </tr>
    <tr>
        <td></td>
        <td><div id="clock" style="margin-top: 15px;text-align: center">00:00</div></td>
        <td></td>
    </tr>
</table>
</div>
<%--Marina added for debugging:--%>
<%--<div id="clock"></div>--%>

<%--<div>--%>
<%--<h5>Notifications</h5>--%>
<%--<ul data-bind="foreach: notifications">--%>
<%--<li data-bind="text: notification"></li>--%>
<%--</ul>--%>
<%--</div>--%>


<script type="text/javascript">
    var appModel;
    var drawingAppModel;

    $(document).keypress(function(e) {
        $('#message-field').focus();
        if(e.which == 13 && $('#message-button').attr('disabled') != 'disabled') {
            sendChatMessage();
        }
    });
    $(function () {
        var targetUrl = "${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/room/" + ${roomId};

        var canvas = document.getElementById("can");
        drawingAppModel = new DrawingAppModel();
        drawingAppModel.init(canvas, canvas);

        appModel = new RoomAppModel(drawingAppModel);
        appModel.myLogin = "${pageContext.request.userPrincipal.name}";
        // Timer constants.
        appModel.gameInterval = ${gameInterval};
        appModel.turnInterval = ${turnInterval};

        appModel.connect(targetUrl);
        ko.applyBindings(appModel);

        var timer = setTimeout(function tick() {

            if (drawingAppModel.points.length > 0
                    && !(drawingAppModel.points.length == 1 && drawingAppModel.points[0].x == -1)) {
                appModel.sendDrawMessage(new DrawMessage({"points": drawingAppModel.points}));
                drawingAppModel.points.length = 0;
            }
            timer = setTimeout(tick, 350);
        }, 350);

        var timer2 = setTimeout(function tick() {
            var delta = new Date()-appModel.lastTime;
            var delta2;
            if (appModel.isGameInProcess){
                delta2 = appModel.turnInterval - delta;
            } else{
                delta2 = appModel.gameInterval - delta;
            }
            if (delta2<0){
                delta2 = 0;
            }
            var newDate = new Date(delta2);
            var m=newDate.getMinutes();
            var s=newDate.getSeconds();
            m = checkTime(m);
            s = checkTime(s);
            document.getElementById('clock').innerHTML = m+":"+s;
            timer = setTimeout(tick, 1000);
        }, 1000);


    function checkTime(i) {
        if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
        return i;
    }

        // Clock.
//        (function startTime() {
//            var today=new Date();
//            var h=today.getHours();
//            var m=today.getMinutes();
//            var s=today.getSeconds();
//            m = checkTime(m);
//            s = checkTime(s);
//            document.getElementById('clock').innerHTML = h+":"+m+":"+s;
//            var t = setTimeout(function(){startTime()},500);
//        })();
//
//        function checkTime(i) {
//            if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
//            return i;
//        }
    });

    function sendChatMessage() {
        var message = new ChatMessage({"text": $('#message-field').val().trim()});
        if(message.text.length > 0) {
            appModel.sendChatMessage(message);
            $('#message-field').val("");
        }
    }

    function leaveRoom() {
        window.location.href = "/room/leave/" + ${roomId};
    }
</script>

</body>
</html>