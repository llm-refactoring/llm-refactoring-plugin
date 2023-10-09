<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld"%>




<html>
<head>
<title>.: Purchase Order :.</title>
<link href="<s:url value="/css/style.css"/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
    src="dwr/interface/PurchaseOrderAjax.js"> </script>
<script type="text/javascript"
    src="dwr/engine.js"> </script>		

<script>

function updatePrice(obj){
	var str = obj.id;
	str = str.substring(0,str.length-10);
	str = str + 'price';
	var targetObj = document.getElementById(str);
	
	if (obj.value == '') {
		targetObj.value = '';
	}else{
		PurchaseOrderAjax.getProductPrice(obj.value, {
			  callback:function(str) { 
			    targetObj.value = str;
			  }
		});
	}	
}
</script>

</head>

<body>

<h2>.: Purchase Order :.</h2>

<jsp:include page="Header.jsp" />


<s:actionmessage />
<s:actionerror />
<br />
<s:form action="PurchaseOrder">
	<s:hidden name="task" />

	<s:date name="purchaseOrder.orderDate" id="createdDateId"
		format="MM/dd/yyyy" />
	<s:textfield key="purchaseOrder.orderDate" value="%{createdDateId}"
		readonly="true" />

	<s:textfield key="purchaseOrder.id" readonly="true"/>

	<s:select key="purchaseOrder.customer.id"
		headerValue="-- Please Select --" 
		list="#session.listCustomer"
		listValue="name"
		listKey="id" emptyOption="true" required="true"/>

	<s:textfield key="purchaseOrder.discount" required="true"/>

	<tr><td colspan="2">&nbsp;</td></tr>

	<tr>
		<td colspan="2">
			<table class="wwFormTable">
				<tr>
					<th>&nbsp;</th>
					<th>Product</th>
					<th>Price</th>
					<th>Quantity</th>
				</tr>
				
				<s:iterator begin="0" end="9" status="s">
					<s:set var="i">${s.index}</s:set>
					<s:hidden name="purchaseOrder.purchaseOrderItems[%{i}].purchaseOrder.id" value="%{purchaseOrder.id}"/>
					<s:hidden name="purchaseOrder.purchaseOrderItems[%{i}].id" />
					<tr>	
						<td>
							${i+1}
							<c:if test="${i == 0}">
								<span class="required">*</span>
							</c:if>
						</td>
						<td width="170"> 
							<s:select name="purchaseOrder.purchaseOrderItems[%{i}].product.id"
									headerValue="-- Please Select --" 
									list="#session.listProduct"
									listValue="name"
									listKey="id" emptyOption="true" required="true" theme="simple"
									onchange="updatePrice(this);"/>
							<s:fielderror>
								<s:param name="fieldName">item${i}_product</s:param>
							</s:fielderror>						
						</td>
						
						<td width="170">
							<s:textfield name="purchaseOrder.purchaseOrderItems[%{i}].price" theme="simple" readonly="true" />
							<s:fielderror>
								<s:param name="fieldName">item${i}_price</s:param>
							</s:fielderror>
						</td>
						<td width="170">
							<s:textfield name="purchaseOrder.purchaseOrderItems[%{i}].quantity" theme="simple" />
							<s:fielderror>
								<s:param name="fieldName">item${i}_quantity</s:param>
							</s:fielderror>	
						</td>
					</tr>
				</s:iterator>

			</table>
		</td>
	</tr>

	<c:if test="${task eq 'cr'}">
		<s:submit action="PurchaseOrder_save" value="Save" />
		<s:submit action="PurchaseOrder_find" value="Find" />
	</c:if>

	<c:if test="${task eq 'ud'}">
		<s:submit action="PurchaseOrder_update" value="Update" />
		<s:submit action="PurchaseOrder_delete" value="Delete" />
	</c:if>

	<s:submit action="PurchaseOrder_input" value="Clean" />
</s:form>



<c:if test="${not empty requestScope.list}">
	<br />
	<br />
	<table>
		<tr>
			<th>Id</th>
			<th>Customer</th>
			<th>Date</th>
			<th>Discount</th>
			<th>&nbsp;</th>
		</tr>
		<c:forEach items="${requestScope.list}" var="po">
			<tr>
				<td><c:out value="${po.id}" /></td>
				<td><c:out value="${po.customer.name}" /> (<c:out
					value="${po.customer.id}" />)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><fmt:formatDate value="${po.orderDate}" dateStyle="short" /></td>
				<td><c:out value="${po.discount}" />%</td>
				<td><a href="PurchaseOrder_edit?id=${po.id}">Edit</a></td>
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

