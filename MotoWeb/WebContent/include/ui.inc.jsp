<%@page import="com.moto.remote.MotoManagerRemote"%>
<%@page import="com.moto.entity.*"%>
<%@page import="com.moto.exception.*"%>
<%@page import="java.util.*"%>
<%@page import="java.net.URL"%>
<%!public URL fileURL;%>

<%

MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");

try {
	
	// may cause UserNotLoggedException
	User user = mm.getLoggedUser();	

	String username = user.getUsername();
	String name     = user.getName();
	String surname  = user.getSurname();
	String type 	= "";
	
	if (user.getType().equals(User.Type.TICKETSELLER)) 		type = "Ticket Seller";
	else if (user.getType().equals(User.Type.WEBCUSTOMER)) 	type = "Web Customer";	
		
%>
	 
		

<div style='width: 960px'>

	<!-- Header: user data -->
	<div class=main_box style='width: 940px; display: table-cell'>
		<div style='width: 280px; float: left; text-align: left;'>
			<img src="${pageContext.request.contextPath}/pics/logo.png" alt="Moto: Movie Theater Office." title="Moto: Movie Theater Office.">
		</div>
		
		<div style='width: 550px; float: right; text-align: right; vertical-align: center;'>
		
					
			<div style="float: right; margin: 5px; margin-top: 8px">
				<h1 style="margin-top: 0px; margin-bottom: 6px;"><%=name %> <%=surname %></h1>
				<span class="testo8"><font color=#888888><%=type %></font></span><br>
				<span class="testo9"><font color=#0066CC><b><%=username %></b></font></span>
			</div>
			
		</div>
	</div>
	
	<br>
	
	<!-- Menu -->
	<div style='width: 960px; display: table'>
		<div class="tab tabOn"><a href='index.jsp'>Moto Home</a></div>
		<div class="tab tabOut"><a style='color: #CC0000;' href='Login?action=logout'>Esci</a></div>
		<div class=tabLine style='width: 656px'></div>
	</div>
	
	<br>
	
	
	<!-- Left menu -->
	<div style="float: left; margin-right: 10px">
	
		<!-- Film in programmazione -->
		<div class=main_box_bg style='width: 250px; padding: 15px; text-align: left;'>
			<div><h2>Film in programmazione</h2></div>
			<div class="content_box">
		
				<%
				
				try {
					List<Movie> movies = mm.getMovies();
					
					for (Movie m: movies){
						%>
						
						<div style='line-height: 25px'>
							<img src=pics/icon_movie.png align=absmiddle style='margin-left: 3px; margin-right: 5px'> 
							<a href="index.jsp?action=Movie&idMovie=<%=m.getId() %>"><%=m.getName() %></a>
						</div>
						<%

					}
					
					
				} catch (MoviesNotFoundException e) {
					%> 
					<center>Nessun film in programmazione.</center>
					<%
				}
				
				%>
		
			</div>
		</div>	
	
		<%
		if (user.getType().equals(User.Type.TICKETSELLER)){		
		%>
	
		<br>
	
		<!-- Ricerca programmazione -->
		<div class=main_box_bg style='width: 250px; padding: 15px; text-align: left;'>
			<div><h2>Ricerca prenotazione</h2></div>
			<div class="content_box">
		
				<form action="FindReservation" id="findReservation" method="post" class="access">
				<input type="hidden" name="do" value="findReservation">
				
					<!-- Username -->
					<div class="access">
						<div>Numero prenotazione</div>
						<div><input type="text" id="id" value="" name="id" title="Numero prenotazione" style="width: 240px; font-size: 13px;"/></div>  
					</div>
					
					<!-- Password -->
					<div class="access" style="margin-top: 10px">
						<div>Codice autorizzazione</div>
						<div><input type="text" id="code" value="" name="code" title="Codice autorizzazione" style="width: 240px; font-size: 13px;"/></div>
					</div>
				
					<!-- Submit Button -->
					<div style="float: right; margin-top: 10px">
						<button type="submit">Ricerca</button>
					</div>
					<div style="clear: both;"></div>
					
				</form>
				
			</div>
		</div>	
	
		<%
		}
		%>
	
	</div>	
	
	
	<!-- Main content -->
	
	<% 


	String action 	= null;
	String fileName = null;
	String actionJsp	 = (String) request.getParameter("action");
	String actionServlet = (String) request.getAttribute("action");
	
	if(actionServlet == null )	action = actionJsp;
	else 						action = actionServlet;

	//out.println(action);
	
	// Generate file path to be included
	if(action == null || action.equals("login") ||  action.isEmpty())	fileName = "home.inc.jsp";
	else																fileName = action + ".inc.jsp";
	
	// File do be included does exists?
	String filePath = "/include/"+fileName;
	fileURL = pageContext.getServletContext().getResource(filePath);

	%>
	
	<% if(fileURL==null) {%>

		<div class=error_box id="error_box" style='padding: 15px; text-align: left; width: 635px; float: right'>
			<center>La pagina richiesta non esiste.</center>
		</div>

	<% } else {%>
	    <jsp:include page="<%=fileName%>" flush="true"/>
	<% } %>
	
		
	
	
	<!-- Footer line -->
	<div style="clear: both;"></div>
	<br><div style='width: 960px; border-top: solid 1px #87CEEB; clear: both;'></div>
	

</div>



<%
// EXCEPTION ERROR
} catch (Exception e){
%>
<jsp:forward page="/index.jsp">
 <jsp:param name="error" value="error"/>
</jsp:forward>
 
 <%
	//response.sendRedirect(request.getContextPath()+"/index.jsp");
}
%>
