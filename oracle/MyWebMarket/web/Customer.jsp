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

<h2>.: Customer :.</h2>

<jsp:include page="Header.jsp" />

<s:actionmessage />
<s:actionerror />
<br/>
<s:form action="Customer">
	<s:hidden name="task" />
	<s:textfield key="customer.id" required="true"/>

	<s:textfield key="customer.name" required="true"/>

	<s:textfield key="customer.address" required="true"/>

	<s:textfield key="customer.phone" required="true"/>

	<s:textfield key="customer.email" required="true"/>

	<c:if test="${task eq 'cr'}">
		<s:submit action="Customer_save" value="Save" />
		<s:submit action="Customer_find" value="Find" />	
	</c:if>
	
	<c:if test="${task eq 'ud'}">
		<s:submit action="Customer_update" value="Update" />
		<s:submit action="Customer_delete" value="Delete" />
	</c:if>
	
	<s:submit action="Customer_input" value="Clean" />
</s:form>



<c:if test="${not empty requestScope.list}">
	<br />
	<br />
	<table>
		<tr>
			<th>Id</th>
			<th>Name</th>
			<th>Address</th>
			<th>Phone</th>
			<th>Email</th>
			<th>&nbsp;</th>
		</tr>
		<c:forEach items="${requestScope.list}" var="c">
			<tr>
				<td><c:out value="${c.id}" /></td>
				<td><c:out value="${c.name}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><c:out value="${c.address}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><c:out value="${c.phone}" /></td>
				<td><c:out value="${c.email}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>				
				<td><a href="Customer_edit?id=${c.id}">Edit</a></td>
			</tr>
		</c:forEach>

	</table>
</c:if>

<c:if test="${empty requestScope.list and requestScope.list ne null}">
	<br />
	<br />
	No results.
</c:if>

</body>
</html>

