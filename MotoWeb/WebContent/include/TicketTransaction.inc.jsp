<%@page import="com.moto.remote.MotoManagerRemote"%>
<%@page import="com.moto.entity.*"%>
<%@page import="com.moto.exception.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>


<%

MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");
//may cause UserNotLoggedException
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

	<div><h2>Acquisto</h2></div>
	<div class="content_box">

	
		<!-- MovieShow Data -->
		<div style="margin-left: 0px; margin-right: 10px; width: 80px; height: 80px; float: left;  font-size: 40pt; color: #999999; border: solid 1px #CCCCCC; border-radius: 2px; text-align: center; font-weight: 200;">
			<%=ms.getVisionRoom().getId() %><br><span style="font-size: 9pt; color: #AAAAAA;">SALA</span>
		</div>	
	
		<div style="margin: 3px; line-height: 16px;">
		
			<span style="font-size: 12pt; font-weight: 600; color: #CC0000"><%=ms.getMovie().getName() %></span><br><br>			
		
			<b>Data:</b> <% 
							out.print((new SimpleDateFormat("EEEE", Locale.ITALIAN)).format(ms.getDatetime())+", "); 
							out.print((new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN)).format(ms.getDatetime()));
						 %><br>
			<b>Ora:</b>  <% out.print((new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(ms.getDatetime())); %><br>
			<b>Posti liberi:</b> <% out.print(mm.getNumAvailablePlaces(ms) +"/"+ ms.getVisionRoom().getNumPlaces()); %>
			
		</div>

		<div style="clear: both;"></div>
		
		<br>

		<!-- Information && errors -->
		<%
		

		// 0. RETRIVE do VALUE: if set by Serverl, then replace
		/****************************************************************************************************************/	
		String doString	 = null;
		String doJsp     = (String) request.getParameter("do");
		String doServlet = (String) request.getAttribute("do");
		
		if(doServlet == null )	doString = doJsp;
		else 					doString = doServlet;
		
		// 1. PRINT initTicketTransaction FEEDBACK
		/****************************************************************************************************************/
		if(doString!=null && doString.equals("initTicketTransaction")){
			

			
			
			String[] place 	= request.getParameterValues("place");
			TicketTransaction ticketTransaction = (TicketTransaction)request.getAttribute("ticketTransaction");
			List<Price> priceList = mm.getMovieShowPrices(ms);
			
			
			if (ticketTransaction != null)    { 
			
				out.println("<div class=\"info_box\" style=\"text-align: left\">");
				out.println("Hai 3:00 minuti di tempo dall'inizio della transazione (ore "+(new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(ticketTransaction.getDatetime())+"), per completare l'acquisto.</div>");
				
				String error = (String)request.getAttribute("error");
				
				if(error!=null){
					// Generate error message
					String error_msg = "";
					if (error.equals("PaymentNotAuthorized"))	
						error_msg = "<h2>Acquisto fallito</h2>Pagamento non autorizzato: numero di carta di credito non valido (16 cifre).";
									
					else
						error_msg = "<h2>Acquisto fallito</h2>" + error;
		
				%>
				<div class="error_box" style="text-align: left;"><%=error_msg %></div><br>
				<%			
				}
				%>
			
			
				<form action="MovieShow" id="movieShow" method="post">
				<input type="hidden" name="action" value="TicketTransaction">
				<input type="hidden" name="idMovieShow" value="<%=idMovieShow%>">
				<input type="hidden" name="idTicketTransaction" value="<%=ticketTransaction.getId()%>">
				
				<script>
				$(document).ready(function(){
									
					$('[name=price]').change(function() {
						updateTotalPrice();
					});
				
					updateTotalPrice();
					
				});
				
				function updateTotalPrice(){
					
					var sum = 0;
					var price = new Array();
					
					<%for(Price pl : priceList){%>
							price[<%=pl.getId() %>] = <%=pl.getRateAmmount() %>;
					<% 
					}
					%>
					
			 		$('[name=price]').each(function(){ 
						  i = $(this).val();
						  sum = sum + price[i];
					});
			 		
			 		$('#total').html(sum.toFixed(2));			 		
				}				
				</script>
				

			<%
							
				// Ticker and associated Price!
				for (Ticket t : ticketTransaction.getTickets()) {
					
			
					%>					
					<div style="width: 100px; float: left; height: 30px; line-height: 30px">Posto: <b>
					<% 
						out.print((char)(t.getPlace().getIdRow()+64));
						out.print(t.getPlace().getIdCol());
					%></b>
					</div>
					<div style="float: left; height: 30px">
						<input type="hidden" name="ticket" value="<%=t.getId()%>">
						<select name="price" style="width: 240px; font-size: 13px;" class="access">			
					<% 
					
						// Decimal forma for 2 decimal digit
						DecimalFormat df = new DecimalFormat();
						df.setMinimumFractionDigits(2);
						df.setMaximumFractionDigits(2);

					
						for(Price pl : priceList){
							
							if((mm.getLoggedUser().getType().equals(User.Type.TICKETSELLER) || (mm.getLoggedUser().getType().equals(User.Type.WEBCUSTOMER) && pl.getIsDefault()))){
							
								out.print("<option value=\""+pl.getId()+"\"");
								if(pl.getIsDefault())	out.print(" selected");
								out.print(">"+pl.getRateName()+": &#8364;"+df.format(pl.getRateAmmount())+"</selected>");
							
							}
						}
					%>
						</select>
						
					</div>
					<div style="clear: both;"></div>
											
				<%			
				}
				%>
				
				<%				
					if(mm.getLoggedUser().getType().equals(User.Type.WEBCUSTOMER)){
				%>
				
						<br><br>
						<h2>Dettagli per il pagamento</h2>
						<br>
						<div style="width: 100px; float: left; height: 30px; line-height: 30px">Nome: </div>
						<div style="float: left; height: 30px" class="access"><input type="text" name="name" readonly="readonly" value="<%=mm.getLoggedUser().getName() %>" style="width: 240px; font-size: 13px;"></div><br>
						<div style="clear: both;"></div>
						
						<div style="width: 100px; float: left; height: 30px; line-height: 30px">Cognome: </div>
						<div style="float: left; height: 30px" class="access"><input type="text" name="surname" readonly="readonly" value="<%=mm.getLoggedUser().getSurname() %>" style="width: 240px; font-size: 13px;"></div><br>	
						<div style="clear: both;"></div>
						
						<div style="width: 100px; float: left; height: 30px; line-height: 30px">Codice Fiscale: </div>
						<div style="float: left; height: 30px" class="access"><input type="text" name="cf" readonly="readonly" value="<%=mm.getLoggedUser().getCf() %>" style="width: 240px; font-size: 13px;"></div><br>	
						<div style="clear: both;"></div>
						
						<div style="width: 100px; float: left; height: 30px; line-height: 30px">Carta di Credito: </div>
						<div style="float: left; height: 30px" class="access"><input type="text" name="cc" maxlength="16" style="width: 240px; font-size: 13px; "></div><br>				
						<div style="clear: both;"></div>
											
				<%	
					}
				%>
				
				<!-- Total -->
				<br>
				<div style="margin-top: 10px;" class="content_box">
 					Importo totale: <font color="#CC0000"><b>&#8364; <span id="total">00,00</span></b></font>
				</div>
				
				<!-- Submit button  -->
				<div style="margin-top: 10px;">
					
					<div style="float: right; text-align: right">		
						<button type="submit" name="do" value="addTicketTransaction">Procedi</button>
					</div>
					
					<div style="clear: both;"></div>
				</div>
				
				</form>
				
				<%				
				
			}
			
			
			
		// 2. FIND RESERVATION FEEDBACK
		/****************************************************************************************************************/
		} else if(doString!=null && doString.equals("addTicketTransaction")){

			String[] place 	= request.getParameterValues("place");
			TicketTransaction ticketTransaction = (TicketTransaction)request.getAttribute("ticketTransaction");
			
			String error = (String)request.getAttribute("error");
			
			if(error!=null){
				// Generate error message
				String error_msg = "";
				if (error.equals("TicketTransactionTimeOutException"))	
					error_msg = "<h2>Acquisto fallito</h2>E' scaduto il limite di tempo massimo per la procedura di acquisto.";
					
				else if (error.equals("TicketTransactionNotFoundException"))
					error_msg = "<h2>Acquisto fallito</h2>TicketTransaction non trovata.";
					
				else if (error.equals("PlaceNotAvailableException"))
					error_msg = "<h2>Acquisto fallito</h2>Alcuni biglietti selezionati sono gi&agrave; stati acquistati.";	
				
				else
					error_msg = "<h2>Acquisto fallito</h2>" + error;
					
			%>
				<div class="error_box" style="text-align: left;"><%=error_msg %></div>
			<%
			} else {
				

				// Reservation data
				if(ticketTransaction!=null)	{
					out.println("<div class=\"info_box\" style=\"text-align: left\"><h2>Acquisto effettuato con successo</h2><br><a href='javascript:window.print()'>Stampa</a> i biglietti qui sotto.");
					out.println("Una volta stampati i tagliandi di ingresso, torna alla <a href=index.jsp?action=MovieShow&idMovieShow="+idMovieShow+">pagina dello spettacolo</a>.</div>");
				%>
				
					
					
					<!-- Tickets -->
					<center>
					<div style="border: solid 3px #000000">
				<%
				
					// Decimal forma for 2 decimal digit
					DecimalFormat df = new DecimalFormat();
					df.setMinimumFractionDigits(2);
					df.setMaximumFractionDigits(2);
				
					int i = 0;
					String style = "";
					for(Ticket t : ticketTransaction.getTickets()){
						
						if(++i != ticketTransaction.getTickets().size())	style = "border-bottom: dotted 1px #999999";
						else												style = "";
				%>
				
						<div style="position: relative; padding: 10px;<%=style%>">
							<div style="margin-left: 0px; margin-right: 10px; width: 100px; height: 100px; float: left;  font-size: 50pt; color: #999999; border: solid 1px #999999; border-radius: 2px; text-align: center; font-weight: 300;">
								<%=t.getPlace().getMovieShow().getVisionRoom().getId() %><br><span style="font-size: 11pt; color: #999999;">SALA</span>
							</div>	
						
							<div style="margin: 3px; line-height: 16px; text-align: left">
								<span style="font-size: 12pt; font-weight: 600; color: #CC0000"><%=t.getPlace().getMovieShow().getMovie().getName() %></span><br><br>
								
							
								<b>Data:</b> <% 
												out.print((new SimpleDateFormat("EEEE", Locale.ITALIAN)).format(t.getPlace().getMovieShow().getDatetime())+", "); 
												out.print((new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN)).format(t.getPlace().getMovieShow().getDatetime()));
											 %><br>
								<b>Ora:</b>  <% out.print((new SimpleDateFormat("HH:mm", Locale.ITALIAN)).format(t.getPlace().getMovieShow().getDatetime())); %><br>
								<b>Posto:</b> <% 
												out.print((char)(t.getPlace().getIdRow()+64));
												out.print(t.getPlace().getIdCol());
											   %><br>
											   
								<b>Prezzo:</b> &#8364; <%=df.format(t.getPrice().getRateAmmount()) %>

							</div>
							<%
							NumberFormat formatter = new DecimalFormat("0000000000");
							String s = formatter.format(t.getId());
							%>
							<script>
							$(document).ready(function(){
								$("#bcTicket-<%=t.getId() %>").barcode("<%=s%>", "code128",{barWidth:1, barHeight:30});
							});
							</script>
							<div style="position: absolute; right: 10px; top: 10px; margin-right: 10px margin-top: 10px" id="bcTicket-<%=t.getId() %>"></div>
							
							<div style="clear: both;"></div>
						
						</div>
								
				<%
				
					}
				%>
					 </div>
					 </center>
				<%
				
				} 
				
			}
			
		}
		
		%>


		
		<div style="clear: both;"></div>
		


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