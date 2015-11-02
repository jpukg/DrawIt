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
                        <input id="message-button" type="button" class="button" value="Send" />
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

    function leaveRoom() {
        window.location.href = "/room/leave/" + ${roomId};
    }
</script>

</body>
</html>