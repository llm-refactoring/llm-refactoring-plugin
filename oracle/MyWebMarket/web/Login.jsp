<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>.: Login :.</title>
	<link href="<s:url value="/css/style.css"/>" 
  						rel="stylesheet" type="text/css"/>
</head>

<body>

<h2>.: Login :.</h2>

<br />

<s:actionerror/>

<br />
<s:form action="Login" focusElement="Login_user_username">
    <s:textfield key="user.username" required="true" />
    <s:password key="user.password" showPassword="true" required="true"/>
    <s:submit value="Enter"/>
</s:form>
</body>

<script type="text/javascript"> 
        var element = document.getElementById("Login_user_username");
        if(element) {
            element.focus();
        }
</script>

</html>


