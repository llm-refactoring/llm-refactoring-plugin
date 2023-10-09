<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>


<html>
<head>
<title>.: Customer :.</title>
<link href="<s:url value="/css/style.css"/>" rel="stylesheet"
	type="text/css" />
</head>

<body>

<h2>.: Reports :.</h2>

<jsp:include page="Header.jsp" />

<s:actionmessage />
<s:actionerror />
<br/>

<p>Select the desired report:</p>
<p>&nbsp;&nbsp;&nbsp;- <a href="Report_productReport" style="color: gray;">List of Products</a></p>
<p>&nbsp;&nbsp;&nbsp;- <a href="Report_customerReport" style="color: gray;">List of Customers</a></p>

</body>
</html>
