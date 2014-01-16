<%@page import="com.moto.remote.MotoManagerRemote"%>
<%@page import="com.moto.entity.*"%>
<%@page import="com.moto.exception.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>


<%

MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");

try {

	// may cause UserNotLoggedException
	User user = mm.getLoggedUser();	

%>

	<div class=main_box_bg style='padding: 15px; text-align: left; width: 635px; float: right'>
	
		<div><h2>Programmazione e primo spettacolo</h2></div>
		<div class="content_box">
	
		<%
		
		// PRINT FINDRESERVATION ERROR
		/****************************************************************************************************************/
		String doServlet = request.getParameter("do");
		if(doServlet!=null && doServlet.equals("findReservation")){
			

			if(request.getAttribute("error")!=null){
				
				String error = (String)request.getAttribute("error");
				
				// Generate error message
				String error_msg = "";
				if (error.equals("ReservationNotFoundException"))	
					error_msg = "<h2>Errore ricerca prenotazione</h2>La prenotazione <b>"+ request.getParameter("id") +"</b> non è stata trovata.";
					
				else if (error.equals("ReservationNotAuthorizedException"))
					error_msg = "<h2>Errore ricerca prenotazione</h2>La prenotazione <b>"+ request.getParameter("id") +"</b> non è stata autorizzata. Verifica il tuo codice di autorizzazione.";

				else if (error.equals("UserNotAllowedException"))
					error_msg = "<h2>Errore ricerca prenotazione</h2>L' utente corrente non è abilitato a ricercare prenotazioni.";	
				
				else if (error.equals("EmptyField"))
					error_msg = "<h2>Errore ricerca prenotazione</h2>E' necessario indicare i parametri richiesti.";
				else
					error_msg = "<h2>Errore ricerca prenotazione</h2>" + error;
					
			%>
				<div class="error_box" style="text-align: left;"><%=error_msg %></div>
			<%
			}
		} else {
			
			if(request.getAttribute("error")!=null){
				%><div class="error_box" style="text-align: left;"><h2>Errore generico</h2>Operazione non specificata.</div><%
			}
		}
		/****************************************************************************************************************/
		
		
		try {
			List<Movie> movies = mm.getMovies();
			String style = "";
			int i = 0;
			for (Movie m: movies){
			
				if(++i != movies.size())	style = "border-bottom: dotted 1px #999999";
				else						style = "";

				List<MovieShow> movieShows = mm.getMovieShows(m);
				MovieShow ms = movieShows.get(0);
				
				%>		
									
				<div style="<%=style%>">
				
					<div style="float: left; height: 45px; line-height: 45px; ">
						<img src="pics/icon_movie.png" align="absmiddle" style='margin-left: 3px; margin-right: 5px'> 
						<a href="index.jsp?action=Movie&idMovie=<%=m.getId() %>"><b><%=m.getName() %></b></a> (<%=mm.getNumMovieShows(m) %>)
					</div>


					<div style="margin-top: 4px; margin-bottom: 4px; width: 35px; float: right; height: 35px; font-size: 26px; color: #999999; text-align: center; line-height: 35px;">
						<a href="index.jsp?action=MovieShow&idMovieShow=<%=ms.getId() %>"><img src="pics/icon_add_place.png" style='margin: 10px;' border="0" title="Prenotazione o acquististo"></a>
					</div>				

					
					<div style="width: 130px; float: right; height: 35px; margin-top: 5px; margin-bottom: 5px; padding-top: 2px; text-align: left; line-height: 16px; font-size: 11px;">
					
					<%
					
					out.println("<b>" + (new SimpleDateFormat("E", Locale.ITALIAN)).format(ms.getDatetime()));
					out.println((new SimpleDateFormat("dd/MM", Locale.ITALIAN)).format(ms.getDatetime())+"</b>, Ora: ");
					out.println((new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(ms.getDatetime()) + "<br>");
					out.println("Posti liberi: "+ mm.getNumAvailablePlaces(ms) +"/"+ ms.getVisionRoom().getNumPlaces());
					

					%>
					</div>
					<div style="margin: 5px; margin-right: 10px; width: 35px; height: 35px; float: right;  font-size: 22px; color: #999999; border: solid 1px #DDDDDD; 	border-radius: 2px; text-align: center;">
					<%=ms.getVisionRoom().getId() %><br><span style="font-size: 6.5pt; color: #AAAAAA;">SALA</span>
					</div>						
					<div style="clear: both;"></div>
					

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
// EXCEPTION ERROR
} catch (Exception e){
	
	response.sendRedirect(request.getContextPath()+"/index.jsp");
	
}
%>