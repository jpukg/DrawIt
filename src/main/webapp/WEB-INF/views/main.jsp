<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Draw It</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>

    <script src="<c:url value='/js/jquery/jquery.js'/>"></script>
    <script src="<c:url value='/js/jquery/jquery.tmpl.min.js'/>"></script>
    <script src="<c:url value='/js/jquery/jquery.atmosphere.js'/>"></script>
    <script src="<c:url value='/js/knockout/knockout.js'/>"></script>

    <script src="<c:url value='/js/pages/main_model.js'/>"></script>

    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600' rel='stylesheet' type='text/css'>
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <link href="/css/header.css" rel="stylesheet">

    <link href="/css/main.css" rel="stylesheet">

</head>
<body onunload="">
<div id="header">
    <label>Draw It &#9998;</label>
</div>

<div id="backDiv">
    <sec:authorize access="hasRole('ROLE_AUTH')">
        <c:url value="/j_spring_security_logout" var="logoutUrl"/>
        <form action="${logoutUrl}" method="post" id="logoutForm"/>

        <a href="javascript:logout();" class="button" style="height: 25px;">
            <i class="fa fa-sign-out"></i>
            Sign out
        </a>
    </sec:authorize>

    <sec:authorize access="hasRole('ROLE_FREE')">
        <c:url value="/login_as_auth" var="loginAsAuthUrl"/>
        <form action="${loginAsAuthUrl}" method="post" id="loginAsAuthForm"/>
        <a href="javascript:loginAsAuth();" style="height: 25px;" class="button">
            <i class="fa fa-sign-in"></i>
            Sign in
        </a>
    </sec:authorize>
</div>

<div class="testbox">
    <h2>
        Welcome,
        <i>
            <sec:authorize access="hasRole('ROLE_AUTH')">
                ${pageContext.request.userPrincipal.name}!
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_FREE')">
                anonymous!
            </sec:authorize>
        </i>
    </h2>
    <hr>

    <h5>Rooms:</h5>
    <hr>
    <div id="rooms">
        <div id="roomList">
            <table>
                <thead>
                <tr>
                    <th>&#8470;</th>
                    <th>Title</th>
                    <th></th>
                </tr>
                </thead>
                <tbody data-bind="foreach: rooms">
                <tr>
                    <td data-bind="text: id"></td>
                    <td data-bind="text: title"></td>
                    <td><input type="button" data-bind="name: id" class="button" value="Join" onclick="joinRoom(event);"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <sec:authorize access="hasRole('ROLE_AUTH')">
            <div id="rooms-actions">
                <input type="button" class="button" id="addRoom" onclick="addNewRoom();" value="Create room"/>
            </div>
        </sec:authorize>
    </div>
</div>
<script>
    var appModel;

    $(function () {
        var targetUrl = "${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/roomlist";
        appModel = new MainAppModel();
        appModel.myLogin = "${pageContext.request.userPrincipal.name}";

        appModel.connect(targetUrl);
        ko.applyBindings(appModel);
    });

    function logout() {
        document.getElementById("logoutForm").submit();
    }

    function loginAsAuth() {
        document.getElementById("loginAsAuthForm").submit();
    }

    function addNewRoom() {
        window.location.href = "/room/add";
    }

    function joinRoom(event) {
        var tr = event.currentTarget.parentNode.parentNode;
        var td = tr.getElementsByTagName('td')[0];
        var roomId = td.childNodes[0].data;
        window.location.href = "/room/join/" + roomId;
    }
</script>

</body>
</html>
