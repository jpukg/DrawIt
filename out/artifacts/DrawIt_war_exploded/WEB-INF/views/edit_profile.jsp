<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<html>
<head>
    <link href="/css/registration.css" rel="stylesheet">

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

<c:if test="${not empty errors}">
    <div class="msg">${errors}</div>
</c:if>
<c:if test="${not empty success}">
    <div class="success">${success}</div>
</c:if>

<c:url var="back" value="/my_profile"/>
<a class="button" style="float: left; margin: 20px;width: 60px" href="${back}">
    <i class="fa fa-hand-o-left" style="margin-right: 7px;"></i>Back
</a>

<div class="testbox">
    <div id="registration-box">
        <h2>Edit Profile</h2>

        <c:url var="confirm" value="/profile/edit?id=${userProfile.id}"/>

        <hr>
        <div class="accounttype">
            <form:form modelAttribute="userProfile" method="post" action="${confirm}" enctype="multipart/form-data">
                <table>
                    <label id="icon" for="name"><i class="icon-user"></i></label>
                    <input type="text" value="${login}" placeholder="Login" name="login"/>


                    <label id="icon" for="name"><i class="icon-shield"></i></label>
                    <input id="password" name="password" type="password" placeholder="Password" value=""/>

                    <label id="icon" for="name"><i class="icon-shield"></i></label>
                    <input id="passwordRepeat" name="passwordRepeat" type="password" placeholder="Repeat password" value=""/>

                    <label id="icon" for="name"><i class="fa fa-envelope-o fa-fw"></i></label>
                    <form:input path="email" placeholder="E-mail" />

                    <label id="icon" for="name"><i class="icon-user"></i></label>
                    <form:input path="name" id="name" placeholder="First name" />

                    <label id="icon" for="name"><i class="icon-user"></i></label>
                    <form:input path="surname" placeholder="Last name" />

                    <label id="icon"><i class="fa fa-users"></i></label>
                        <select name="country">
                            <c:forEach items="${countries}" var="country">
                                <c:choose>
                                    <c:when test="${country==countryCurrent}">
                                        <option selected="true"
                                                value="${country}">${country}
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${country}">${country}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>

                    <input type="file" name="avatar" />

                    <input type="submit" class="button" value="Confirm">
                </table>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>
