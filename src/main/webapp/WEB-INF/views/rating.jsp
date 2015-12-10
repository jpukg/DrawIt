<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<%@page isELIgnored="false" %>

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
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>


</head>
<body onunload="">
<div id="header">
    <label>Draw It &#9998;</label>
</div>


<c:url value="/main" var="mainUrl"/>
<a class="button" style="float: right; margin: 20px;width: 70px" href="${mainUrl}">
    <i class="fa fa-home" style="margin-right: 7px;"></i>Home
</a>

<br>
<h2>Best Gamers</h2>
<br>

<div class="container">
    <div class="row">
        <div class="col-xs-2">
        </div>
        <div class="col-xs-8">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>Place</th>
                    <th>Name</th>
                    <th>Game Amount</th>
                    <th>Point Amount</th>
                </tr>
                </thead>

                <c:set var="count" value="1" />
                <c:forEach items="${userProfiles}" var="userProfile">
                    <tr>
                        <td>${count }</td>
                        <td> <a href="http://localhost:8080/profile/${userProfile.name }" >${userProfile.name }&nbsp;${userProfile.surname }</a></td>
                        <td>${userProfile.gameAmount }</td>
                        <td>${userProfile.pointAmount }</td>
                    </tr>
                    <c:set var="count" value="${count + 1}" />
                </c:forEach>
            </table>
        </div>
        <div class="col-xs-2"></div>
    </div>
</div>

</body>
</html>