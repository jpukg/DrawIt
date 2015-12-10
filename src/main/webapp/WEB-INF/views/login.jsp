<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<html>
<head>
    <title>Login Page</title>
    <link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600' rel='stylesheet' type='text/css'>
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <link href="/css/login.css" rel="stylesheet">
    <link href="/css/header.css" rel="stylesheet">

    <script type="text/javascript">
        function enterAnonymously() {
            document.getElementById("enterAnonymouslyForm").submit();
        }
    </script>
</head>

<body onload="document.loginForm.username.focus();" onunload="">

<%--If user is already authenticated, he can't log in again.--%>
<sec:authorize access="hasRole('ROLE_AUTH')">
    <c:redirect url="/main"/>
</sec:authorize>

<div id="header">
    <label>Draw It &#9998;</label>
</div>

<div id="login-box">
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <c:if test="${not empty msg}">
        <div class="msg">${msg}</div>
    </c:if>

    <div class="testbox">
        <h1>Welcome!</h1>
        <h5 align="center"> ${gamesAmount} games by ${usersAmount} users were played! </h5>
        <c:url value='/j_spring_security_check' var="loginUrl"/>
           <form name='loginForm'
              action="${loginUrl}" method='POST'>
            <hr>
            <label id="icon" for="name"><i class="icon-user"></i></label>
            <input type="text" name="username" id="name" placeholder="Name" required/>

            <br/>
            <label id="icon" for="name"><i class="icon-shield"></i></label>
            <input type="password" name="password" id="name2" placeholder="Password" required/>
            <input name="submit" class="button" type="submit" value="Sign in"/>
            </form>
            
            <c:url value="/enter_anonymously" var="enterAnonymouslyUrl"/>
            <form id='enterAnonymouslyForm'
                  action="${enterAnonymouslyUrl}" method='POST'/>
            <a href="javascript:enterAnonymously();" id="anon" class="button">Enter anonymously</a>

            <c:url var="registration" value="/registration"/>
            <a href="${registration}" class="button">Sign up</a>
            </form>
    </div>
</div>
</body>
</html>