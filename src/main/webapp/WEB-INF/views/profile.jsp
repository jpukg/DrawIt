<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<%@page session="true" %>
<html>
<head>
    <title>Profile</title>
    <link href="/css/profile.css" rel="stylesheet">

    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600' rel='stylesheet'
          type='text/css'>
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <link href="/css/header.css" rel="stylesheet">
</head>
<body onunload="">
<div id="header">
    <label>Draw It &#9998;</label>
</div>

<c:url value="/main" var="mainUrl"/>
<!---<a href="${mainUrl}">Home</a>-->
<a class="button" style="float: right; margin: 20px;width: 70px" href="${mainUrl}">
    <i class="fa fa-home" style="margin-right: 7px;"></i>Home
</a>

<div class="testbox">
    <h2>Profile</h2>
    <hr>
    <table>
        <tr>
            <td colspan="3" align="center" width="450 px"><img src="/picture/avatar?id=${user.id}" width="100"
                                                               height="100"/></td>
        </tr>
    </table>
    <hr>
    <table cellspacing="8 px">
        <tr>
            <td width="50 px"></td>
            <td>
                <div class="border"><label id="icon" for="name"><i class="icon-user"></i></label>
                    &nbsp&nbsp&nbsp${login}</div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="border"><label id="icon" for="name"><i class="icon-user"></i></label>
                    &nbsp&nbsp&nbsp${user.name}</div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="border"><label id="icon" for="name"><i class="icon-user"></i></label>
                    &nbsp&nbsp&nbsp${user.surname}</div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="border"><label id="icon" for="name"><i class="fa fa-envelope-o fa-fw"></i></label>
                    &nbsp&nbsp&nbsp${user.email}</div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="border"><label id="icon" for="name"><i class="fa fa-gamepad"></i></label>
                    &nbsp&nbsp&nbsp${user.gameAmount}</div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="border"><label id="icon" for="name"><i class="fa fa-trophy"></i></label>
                    &nbsp&nbsp&nbsp${user.pointAmount}</div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="border"><label id="icon"><i class="fa fa-users"></i></label>
                    &nbsp&nbsp&nbsp${user.country}</div>
            </td>
        </tr>
    </table>

    <sec:authorize access="hasRole('ROLE_AUTH')">
        <c:if test="${currentUserID==user.id}">
            <table>
                <tr>
                    <td width="50 px"></td>
                    <td align="center" width="450 px"><a class="button" style=" width: 70px"
                                                         href="/profile/edit?id=${user.id}">Edit
                    </a></td>
                </tr>
            </table>
        </c:if>
    </sec:authorize>

    <br/>
</div>
</body>
</html>


<!---<table cellspacing="7 px">
<tr>
<td width="50 px"></td>
<td width="80 px"><label id="icon" for="name"><i class="icon-user"></i></label></td>
<td width="140 px">${login}</div></td></tr>
<tr>
<td></td>
<td><label id="icon" for="name"><i class="icon-user"></i></label></td>
<td>${user.name}</td></tr>
<tr>
<td></td>
<td><label id="icon" for="name"><i class="icon-user"></i></label></td>
<td>${user.surname}</td></tr>
<tr>
<td></td>
<td><label id="icon" for="name"><i class="fa fa-envelope-o fa-fw"></i></label></td>
<td>${user.email}</td></tr>
<tr>
<td></td>
<td><label id="icon" for="name"><i class="fa fa-gamepad"></i></label></td>
<td>${user.gameAmount}</td></tr>
<tr>
<td></td>
<td><label id="icon"><i class="fa fa-users"></i></label></td>
<td>${user.country}</td></tr>
</table>--->