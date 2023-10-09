<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>




<html>
<head>
<title>.: Product :.</title>
<link href="<s:url value="/css/style.css"/>" rel="stylesheet"
	type="text/css" />
</head>

<body>

<h2>.: Product :.</h2>

<jsp:include page="Header.jsp" />


<s:actionmessage />
<s:actionerror />
<br/>
<s:form action="Product">
	<s:hidden name="task" />
	<s:textfield key="product.id" required="true"/>

	<s:textfield key="product.name" required="true"/>

	<s:textfield key="product.price" required="true"/>

	<s:textfield key="product.supply" required="true"/>

	<c:if test="${task eq 'cr'}">
		<s:submit action="Product_save" value="Save" />
		<s:submit action="Product_find" value="Find" />	
	</c:if>
	
	<c:if test="${task eq 'ud'}">
		<s:submit action="Product_update" value="Update" />
		<s:submit action="Product_delete" value="Delete" />
	</c:if>
	
	<s:submit action="Product_input" value="Clean" />
</s:form>



<c:if test="${not empty requestScope.list}">
	<br />
	<br />
	<table>
		<tr>
			<th>Id</th>
			<th>Name</th>
			<th>Price</th>
			<th>Supply</th>
			<th>&nbsp;</th>			
		</tr>
		<c:forEach items="${requestScope.list}" var="p">
			<tr>
				<td><c:out value="${p.id}" /></td>
				<td><c:out value="${p.name}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><c:out value="${p.price}" /></td>
				<td><c:out value="${p.supply}" /></td>
				<td><a href="Product_edit?id=${p.id}">Edit</a></td>
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

