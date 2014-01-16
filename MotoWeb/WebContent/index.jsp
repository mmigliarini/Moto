<%@page import="com.moto.remote.MotoManagerRemote"%>
<%@page import="com.moto.entity.*"%>
<%@page import="com.moto.exception.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="Content-Language" content="it-it" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" >
	<link rel=stylesheet type="text/css" href="${pageContext.request.contextPath}/css/style.css" media=all>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.barcode.min.js"></script>
	<title>Moto: Movie Theater Office</title>
</head>

<body>
<center>

<%

	MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");
	
	User user = null;
	if( mm != null)	user = mm.getLoggedUser(); 
	
	if(user==null){
	%>
		<jsp:include page="include/login.inc.jsp" flush='true' />
	<%
	}else{
	%>
		<jsp:include page="include/ui.inc.jsp" flush='true' />
	<%
	}
	%>
	
	
			
			
			

</center>
</body>
</html>