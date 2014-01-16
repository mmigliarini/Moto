<%@page import="com.moto.remote.MotoManagerRemote"%>
<%@page import="com.moto.entity.*"%>
<%@page import="com.moto.exception.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>


<%

MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");

// Retrive idMovie from jsp/servlet attribute
String idMovieStr = null;
int idMovie = 0;

if(request.getParameter("idMovie") == null ){
	try{
		idMovie = (Integer) request.getAttribute("idMovie");
	}catch(Exception e){}
}else{
	try{
		idMovieStr 	= request.getParameter("idMovie");
		idMovie 	= Integer.parseInt(idMovieStr);
	}catch(Exception e){}
}



// Get Movie and his MovieShows
try {
	Movie movie = mm.getMovie(idMovie);

%>

<div class=main_box_bg style='padding: 15px; text-align: left; width: 635px; float: right'>

	<div><h2>Spettacoli: <font color="#CC0000"><%=movie.getName() %></font></h2></div>
	<div class="content_box">

		<%
		
		List<MovieShow> movieShows = mm.getMovieShows(movie);
		String style = "";
		int i = 0;
		for (MovieShow ms: movieShows){
		
			if(++i != movieShows.size())	style = "border-bottom: dotted 1px #999999";
			else						style = "";
			
			%>		
								
			<div style="<%=style%>">
			

			
				<div style="margin: 5px; margin-right: 10px; width: 35px; height: 35px; float: left;  font-size: 22px; color: #999999; border: solid 1px #DDDDDD; 	border-radius: 2px; text-align: center;">
				<%=ms.getVisionRoom().getId() %><br><span style="font-size: 6.5pt; color: #AAAAAA;">SALA</span>
				</div>	
				
				<div style="width: 130px; float: left; height: 35px; margin-top: 5px; margin-bottom: 5px; padding-top: 2px; text-align: left; line-height: 16px; font-size: 11px;">
				
				<%
				
				out.println("<b>" + (new SimpleDateFormat("E", Locale.ITALIAN)).format(ms.getDatetime()));
				out.println((new SimpleDateFormat("dd/MM", Locale.ITALIAN)).format(ms.getDatetime())+"</b>, Ora: ");
				out.println((new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(ms.getDatetime()) + "<br>");
				out.println("Posti liberi: "+ mm.getNumAvailablePlaces(ms) +"/"+ ms.getVisionRoom().getNumPlaces());

				%>
				</div>

				<div style="margin-top: 4px; margin-bottom: 4px; width: 35px; float: right; height: 35px; font-size: 26px; color: #999999; text-align: center; line-height: 35px;">
					<a href="index.jsp?action=MovieShow&idMovieShow=<%=ms.getId() %>"><img src="pics/icon_add_place.png" style='margin: 10px;' border="0" title="Prenotazione o acquististo"></a>
				</div>
			
				<div style="clear: both;"></div>
				

			</div>
			<%

		}
		
			
} catch (MovieNotFoundException e) {
%> 
	<div class=error_box id="error_box" style='padding: 15px; text-align: left; width: 635px; float: right'>
		<center>Il film richiesto (id: <%=idMovie %>) non è stato trovato.</center>
	</div>
<%
}
%>

	</div>


</div>