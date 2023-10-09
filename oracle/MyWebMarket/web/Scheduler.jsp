<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>




<html>
<head>
<title>.: Scheduler :.</title>
<link href="<s:url value="/css/style.css"/>" rel="stylesheet"
	type="text/css" />
</head>

<body>

<h2>.: Scheduler :.</h2>

<jsp:include page="Header.jsp" />

<s:actionmessage />
<s:actionerror />
<br/>
<s:form action="Scheduler">
	<s:textfield key="timeInterval" required="true"/>
	
	<s:submit action="Scheduler_schedule" value="Adjust" />
</s:form>


</body>
</html>

