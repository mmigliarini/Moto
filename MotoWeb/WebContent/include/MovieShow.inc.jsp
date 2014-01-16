<%@page import="com.moto.remote.MotoManagerRemote"%>
<%@page import="com.moto.entity.*"%>
<%@page import="com.moto.exception.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>


<%

MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");
// may cause UserNotLoggedException
User user = mm.getLoggedUser();

// Retrive idMovieShow from jsp/servlet attribute
String idMovieShowStr = null;
int idMovieShow = 0;

if(request.getParameter("idMovieShow") == null ){
	try{
		idMovieShow = (Integer) request.getAttribute("idMovieShow");
	}catch(Exception e){}
}else{
	try{
		idMovieShowStr 	= request.getParameter("idMovieShow").trim();
		idMovieShow	 	= Integer.parseInt(idMovieShowStr);
	}catch(Exception e){}
}


	

// Get MovieShows
try {
	
	MovieShow ms = mm.getMovieShow(idMovieShow);
	Reservation r = null;
	
%>

<div class=main_box_bg style='padding: 15px; text-align: left; width: 635px; float: right'>

	<div><h2>Prenotazione e acquisto</h2></div>
	<div class="content_box">

	
		<!-- MovieShow Data -->
		<div style="margin-left: 0px; margin-right: 10px; width: 80px; height: 80px; float: left;  font-size: 40pt; color: #999999; border: solid 1px #CCCCCC; border-radius: 2px; text-align: center; font-weight: 200;">
			<%=ms.getVisionRoom().getId() %><br><span style="font-size: 9pt; color: #AAAAAA;">SALA</span>
		</div>	
	
		<div style="margin: 3px; line-height: 16px;">
			<span style="font-size: 12pt; font-weight: 600; color: #CC0000"><%=ms.getMovie().getName() %></span><br><br>
			
		
			<b>Data:</b> <% 
							out.println((new SimpleDateFormat("EEEE", Locale.ITALIAN)).format(ms.getDatetime())+", "); 
							out.println((new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN)).format(ms.getDatetime()));
						 %><br>
			<b>Ora:</b>  <% out.println((new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(ms.getDatetime())); %><br>
			<b>Posti liberi:</b> <% out.println(mm.getNumAvailablePlaces(ms) +"/"+ ms.getVisionRoom().getNumPlaces()); %>
			
		</div>

		<div style="clear: both;"></div>
		
		<br>

		<!-- Information && errors -->
		<%
		
		// Verify if current time+20mins is after the show datetime and check if still exists reserved place: procede to set them AVAILABLE
		if(mm.getDateAndTimeFromNow(0, 0, 20).after(ms.getDatetime()) && (mm.getNumReservedPlaces(ms)-mm.getNumTempTicketTransactionPlace(ms)) > 0){
			
			// Set Place as Available and print feedback for TICKETSELLER
			mm.setPlaceAvailableForMovieShowTimeOut(ms);
			if(user.getType().equals(User.Type.TICKETSELLER)){
				out.println("<div class=\"info_box\" style=\"text-align: left\">Le prenotazioni non convertite in acquisto sono state rimosse.</div>");
			}
			
		}
		
		// Check and delte Temporary Ticket
		if(mm.getNumTempTicketTransactionPlace(ms)>0) {}
		
		
		// 1. PRINT ADDRESERVATION FEEDBACK
		/****************************************************************************************************************/
		String doServlet = request.getParameter("do");
		if(doServlet!=null && doServlet.equals("addReservation")){
			
			String error = (String)request.getAttribute("error");
			if(error!=null){
				// Generate error message
				String error_msg = "";
				if (error.equals("NumPlaceQuotaExcededException"))	
					error_msg = "<h2>Prenotazione fallita</h2>E' possibile prenotare un massimo di 5 posti.";
					
				else if (error.equals("NoPlaceSelectedException"))
					error_msg = "<h2>Prenotazione fallita</h2>E' necessario selezionare almeno 1 posto.";
				
				else if (error.equals("PlaceNotAvailableException"))
					error_msg = "<h2>Prenotazione fallita</h2>Alcuni posti selezionati risultano già prenotati.";
				else
					error_msg = "<h2>Prenotazione fallita</h2>" + error;
					
			%>
				<div class="error_box" style="text-align: left;"><%=error_msg %></div>
			<%
			} else {
				// Reservation data
				r = (Reservation)request.getAttribute("reservation");
				if(r!=null)	{
					out.println("<div class=\"info_box\" style=\"text-align: left\"><h2>Prenotazione effettuata con successo</h2><br>");
					
					out.println("<div class=testo9>");
					/** reservation number */
					out.println("Numero prenotazione: <font color=#CC0000>" + r.getId()+"</font><br>");	
					/** reservation code */
					out.println("Codice autorizzazione: <font color=#CC0000>" + r.getCode()+"</font><br>");
					/** reservation places */
					out.println("Posti prenotati: <font color=#CC0000>");
					
					int i = 0;
					for (Place p : mm.getReservationPlaces(r)){
						out.print((char)(p.getIdRow()+64)+""+p.getIdCol());
						if(++i < mm.getReservationPlaces(r).size()) out.print(", ");
						else										out.print(". ");
					}
					
					/** please, print this page **/
					out.println("</font><br><br><span class=testo8><i>");
					
						if(mm.getLoggedUser().getType().equals(User.Type.TICKETSELLER)){
							out.println("<a href='javascript:window.print()'>Stampa questa pagina e consegnala al cliente.</a> I codici riportati saranno necessari al momento dell'acquisto.");
						}else{
							out.println("<a href='javascript:window.print()'>Stampa</a> questa pagina e portala con te in biglietteria.</a> I codici riportati saranno necessari per procedere all'acquisto.<br>");
							out.println("Puoi acquistare i tuoi biglietti entro 20 minuti prima dell'inizio dello spettacolo. Diversamente, la tua prenotazione decade.");								
						}
						
					out.println("</i></span></div></div>");
					
				}
			}

			
			
		// 2. FIND RESERVATION FEEDBACK
		/****************************************************************************************************************/
		} else if(doServlet!=null && doServlet.equals("findReservation")){

			// Reservation data
			r = (Reservation)request.getAttribute("reservation");
			if(r!=null)	{
				out.println("<div class=\"info_box\" style=\"text-align: left\"><h2>Prenotazione "+ r.getId() +"</h2><br>");
				
				out.println("<div class=testo9>");
				/** reservation number */
				out.println("Data prenotazione: <font color=#CC0000>" + (new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN)).format(r.getDatetime())+", "+ (new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(r.getDatetime()) +"</font><br>");	
				/** reserved place */
				out.println("Posti prenotati: <font color=#CC0000>");
				
				int i = 0;
				
				List<Place> placeList = mm.getReservationPlaces(r);
				List<Place> placeSoldList = new ArrayList<Place>();
				
				for (Place p : placeList){
					out.print((char)(p.getIdRow()+64)+""+p.getIdCol());
					if(++i < placeList.size()) out.print(", ");
					else										out.print(". ");
					
					if(p.getStatus().equals(Place.Status.SOLD))
						placeSoldList.add(p);
					
				}
				out.println("</font><br>");
				
				/** purchased reserved place */
				out.println("Posti già acquistati: <font color=#CC0000>");
				
				i = 0;
				for (Place p : placeSoldList){
					out.print((char)(p.getIdRow()+64)+""+p.getIdCol());
					if(++i < placeSoldList.size()) out.print(", ");
					else										out.print(". ");
													
				}							
				
				if(i==0) out.println("nessuno.");
				
				out.println("</font><br><br><span class=testo8><i>");
				/** warning on purchase procedure **/
				
				out.println("E' possibile acquistare posti diversi, anche in quantità, rispetto a quelli prenotati.<br>");
				out.println("I posti non acquistati resteranno riservati fino a 20 minuti prima dell'inizio dello spettacolo. <br>");
				out.println("E' possibile acquistare i posti prenotati in diverse transazioni.");								
				out.println("</i></span></div></div>");
				

			}


		// 3. INIT TICKETTRANSACTION ERROR: PLACE NOT SELECTED
		/****************************************************************************************************************/
		} else if(doServlet!=null && doServlet.equals("initTicketTransaction")){
			
			String error = (String)request.getAttribute("error");
			if(error!=null){
				// Generate error message
				String error_msg = "";
				if (error.equals("NoPlaceSelectedException"))
					error_msg = "<h2>Acquisto fallito</h2>E' necessario selezionare almeno 1 posto.";
				
				else if (error.equals("PlaceNotAvailableException"))
					error_msg = "<h2>Acquisto fallito</h2>Alcuni posti selezionati risultano già prenotati.";
				else
					error_msg = "<h2>Acquisto fallito</h2>" + error;
			%>
				<div class="error_box" style="text-align: left;"><%=error_msg %></div>	
		<%	
			}
		}
		%>


		<!-- VisionRoom Map -->	
		<form action="MovieShow" id="movieShow" method="post">
		<input type="hidden" name="action" value="MovieShow">
		<input type="hidden" name="idMovieShow" value="<%=idMovieShow%>">
			
		<%
		// Print reservation data
		if(doServlet!=null && doServlet.equals("findReservation") && r!=null){
			out.println("<input type=\"hidden\" name=\"idReservation\" value=\""+ r.getId() +"\">");
			out.println("<input type=\"hidden\" name=\"code\" value=\""+ r.getCode() +"\">");
		}
		%>
								
		<div class="content_box" style="text-align: center"><center>
		<div style='display: table; position: relative'>

		<%
		
			// Disable checkbox if MovieShow datetime is after than now
			Date now = mm.getDateAndTimeFromNow(0, 0, 0);
			String disabled = "";
			if(now.after(ms.getDatetime())) disabled = " disabled";
		
			HashMap<String, Place> hmp = mm.getPlaces(ms);

		
			// PRINT COLS NUMBER
			out.println("<div style=\"clear: both;\">");
			out.println("<div class=\"place\"></div>");
			
			for (int c=1; c<=ms.getVisionRoom().getNumCols(); c++){
				out.println("<div class=\"place\" style=\"width: 18px; height: 18px; border: solid 1px #CCCCCC; border-radius: 2px;\"><b>"+c+"</b></div>");					
			}
			
			out.println("</div>");
				
		
			// PRINT PLACES, WITH ROW LETTER (ASCII Conversion: 1 -> A (64 on ASCII TAB)
			for (int row=1; row<=ms.getVisionRoom().getNumRows(); row++){
				
				out.println("<div style=\"clear: both;\">");
				out.println("<div class=\"place\" style=\"width: 18px; height: 18px; border: solid 1px #CCCCCC; border-radius: 2px;\"><b>"+ (char)(row+64) + "</b></div>");
				
					for (int col=1; col<=ms.getVisionRoom().getNumCols(); col++){
				
						
						// find place on coordinate (R1C1)
						Place p = hmp.get("R"+row+"C"+col);
						
						// print div, with background color in case of findReservation
						if ((p!=null && doServlet!=null && doServlet.equals("findReservation") && p.getReservation() != null) && (p.getReservation().getId() == r.getId() && p.getStatus().equals(Place.Status.RESERVED)))
							out.println("<div class=\"place\" style=\"background: #C3E6F5\">");
						else
							out.println("<div class=\"place\">");
						

						// check place status
						if(p==null)	{
							out.println("<input type=checkbox name=\"place\" value=\""+row+"-"+col+"\""+ disabled +">");
						} else {
							
							// place reserved
							if(p.getStatus().equals(Place.Status.RESERVED)){
								
								// place reserved and place of current reservation
								if ((p!=null && doServlet!=null && doServlet.equals("findReservation") && p.getReservation() != null) && (p.getReservation().getId() == r.getId()))
									out.println("<input type=checkbox name=\"place\" value=\""+row+"-"+col+"\""+ disabled +" checked>");
								else
									out.println("<img src=\"pics/icon_place_reserved.png\" title=\"Riservato\">");
							}
							
							//place sold
							else if(p.getStatus().equals(Place.Status.SOLD))	out.println("<img src=\"pics/icon_place_sold.png\" title=\"Venduto\">");
							else												out.println("<input type=checkbox name=\"place\" value=\""+row+"-"+col+"\""+ disabled +">");
						}

						
						out.println("</div>");
					}
				

					
				out.println("</div>");
	
				
			}				
		
		%>
			<center><div style='width: 100%'>
				<div style="margin: 2px; line-height: 25px; clear: both; height: 25px; border: solid 1px #CCCCCC; border-radius: 2px;"><b>SCHERMO</b></div>
			</div></center>
		</div>
		</center>
		</div>
		
		<div style="clear: both;"></div>
		
		<!-- Submit button & Legenda -->
		<div style="margin-top: 10px;">
			<div style="float: left" class="testo8">
				<img src="pics/icon_place_sold.png" align="absmiddle" style='margin-left: 3px; margin-right: 5px'> <b>Posto venduto</b>
				<img src="pics/icon_place_reserved.png" align="absmiddle" style='margin-left: 15px; margin-right: 5px'> <b>Posto prenotato</b><br>					
			</div>
			
			<div style="float: right; text-align: right">		
				<%
				// Disable reservation if current time+20mins is after the show datetime
				if(mm.getDateAndTimeFromNow(0, 0, 20).before(ms.getDatetime()) && !(doServlet!=null && doServlet.equals("findReservation")))
					out.println("<button type=\"submit\" name=\"do\" value=\"addReservation\">Prenota</button>");
				else
					out.println("<button type=\"submit\" name=\"do\" value=\"addReservation\" disabled>Prenota</button>");
				%>
				
				<button type="submit" name="do" value="initTicketTransaction"<%=disabled %>>Acquista</button>

			</div>
			
			<div style="clear: both;"></div>
		</div>

		</form>

	<!-- END MAIN CONTENT -->
	</div>	

</div>


<%
} catch (MovieShowNotFoundException e) {
%> 
	<div class=error_box id="error_box" style='padding: 15px; text-align: left; width: 635px; float: right'>
		<center>Lo spettacolo richiesto (id: <%=idMovieShow %>) non è stato trovato.</center>
	</div>
<%
}
%>