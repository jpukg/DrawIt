<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Access denied.</title>
        <link href="/css/403.css" rel="stylesheet">

        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
        <link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600' rel='stylesheet' type='text/css'>
        <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
        <link href="/css/header.css" rel="stylesheet">

    </head>
    <body onunload="">
        <div id="header">
            <label>Draw It &#9998;</label>
        </div>

        <c:url value="/main" var="mainUrl"/>
        <a class="button" style="float: left; margin: 20px;width: 70px" href="${mainUrl}">
            <i class="fa fa-home" style="margin-right: 7px;"></i>Home
        </a>

        <div class="testbox">
            <h2>You have no access here, sorry.</h2>
        </div>
    </body>
</html>