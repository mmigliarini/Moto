package com.moto;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moto.entity.MovieShow;
import com.moto.entity.Place;
import com.moto.entity.Reservation;
import com.moto.entity.TicketTransaction;
import com.moto.entity.User;
import com.moto.exception.NoPlaceSelectedException;
import com.moto.exception.NumPlaceQuotaExcededException;
import com.moto.exception.PlaceNotAvailableException;
import com.moto.exception.ReservationNotAuthorizedException;
import com.moto.exception.ReservationNotFoundException;
import com.moto.exception.TicketTransactionNotFoundException;
import com.moto.exception.TicketTransactionTimeOutException;
import com.moto.exception.UserNotAllowedException;
import com.moto.remote.MotoManagerRemote;

/**
 * Servlet implementation class MovieShow
 */
public class MovieShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieShowServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		MotoManagerRemote mm = (MotoManagerRemote) request.getSession().getAttribute("mm");
		RequestDispatcher dispatcher;
		Reservation reservation = null;

		if(request.getParameter("do") != null){
		
			/* ADD RESERVATION
			/**********************************************************************************************/
			if (request.getParameter("do").equals("addReservation")) {
				
				String[] place 		  = request.getParameterValues("place");	
				String idMovieShowStr = request.getParameter("idMovieShow");
				int idMovieShow = 0;
					
						
				if(place != null) {
					
					if(idMovieShowStr != null) idMovieShow = Integer.parseInt(idMovieShowStr.trim());
					
					try {
						
						// new reservation
						reservation = mm.addReservation(place, (int)idMovieShow);
						request.setAttribute("reservation", reservation);
					
					// Errors
					}catch (NumPlaceQuotaExcededException e) {
						request.setAttribute("error", "NumPlaceQuotaExcededException");
					}  catch (NoPlaceSelectedException e) {
						request.setAttribute("error", "NoPlaceSelectedException");
					} catch (PlaceNotAvailableException e) {
						request.setAttribute("error", "PlaceNotAvailableException");
					} catch (Exception e) {
						request.setAttribute("error", e.getMessage());
					}			
						
				} else {
					request.setAttribute("error", "NoPlaceSelectedException");					
				}
		
				dispatcher = request.getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
	
				
			/* FIND RESERVATION
			/**********************************************************************************************/
			} else if(request.getParameter("do").equals("findReservation")){
				
				
				// Retrive id and Code from jsp Parameter
				String code  = request.getParameter("code");
				String idStr = request.getParameter("id");
				int id 		 = 0;
	
		
				if(idStr != null && code != null && !((String)idStr).isEmpty() && !((String)code).isEmpty() ){
	
										
					try{
						id	= Integer.parseInt(idStr);
					}catch(Exception e){}
	
					
					try {
						reservation = mm.findReservation(id, code);
						request.setAttribute("reservation", reservation);
						// Retrive MovieShow of Reservation
						List<Place> p = mm.getReservationPlaces(reservation);
						MovieShow ms = p.get(0).getMovieShow();
						request.setAttribute("idMovieShow", (int)ms.getId());
						request.setAttribute("action", "MovieShow");
					
					// Errors
					} catch (ReservationNotFoundException e) {
						request.setAttribute("error", "ReservationNotFoundException");
					} catch (ReservationNotAuthorizedException e) {
						request.setAttribute("error", "ReservationNotAuthorizedException");
					} catch (UserNotAllowedException e) {
						request.setAttribute("error", "UserNotAllowedException");
					} catch (NumberFormatException e) {
						request.setAttribute("error", "EmptyField");	
					} catch (Exception e) {
						request.setAttribute("error", e.getMessage());
					}	
										
				} else {
					request.setAttribute("error", "EmptyField");
				}
				
					
				dispatcher = request.getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
	
			
			/* INIT TICKETTRANSACTION
			/**********************************************************************************************/
			} else if(request.getParameter("do").equals("initTicketTransaction")){
	
				
				String[] place 			= request.getParameterValues("place");
				String idMovieShowStr   = request.getParameter("idMovieShow");
				String idReservationSrt = request.getParameter("idReservation");
				String code 			= request.getParameter("code");
				int idMovieShow   = 0;
				int idReservation = 0;
				
				TicketTransaction ticketTransaction = null;
	
				
				// idMovieShow
				if(idMovieShowStr != null) 	idMovieShow = Integer.parseInt(((String)idMovieShowStr).trim());
					
				// idReservation
				if(idReservationSrt != null) idReservation = Integer.parseInt(((String)idReservationSrt).trim());
					
				// AuthCode and idReservation, set by findReservation
				if(code == null ) 
					code = "";
				else 				 
					code = (String) request.getParameter("code").trim();
				
				
				
				// initTicketTransaction
				if(place != null) {
					try {
						
						// new reservation
						ticketTransaction = mm.initTicketTransaction(place, (int)idMovieShow, code, idReservation);
						request.setAttribute("ticketTransaction", ticketTransaction);
						request.setAttribute("action", "TicketTransaction");
	
					} catch (PlaceNotAvailableException e){
						request.setAttribute("error", "PlaceNotAvailableException");	
					} catch (NoPlaceSelectedException e){
						request.setAttribute("error", "NoPlaceSelectedException");					
					} catch (Exception e) {
						request.setAttribute("error", e.getMessage());
					}			
						
				} else {
					request.setAttribute("error", "NoPlaceSelectedException");					
				}
		
				dispatcher = request.getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				
				
			/* ADD TICKETTRANSACTION
			/**********************************************************************************************/
			} else if(request.getParameter("do").equals("addTicketTransaction")){
				
				
				String[] ticket 		      = request.getParameterValues("ticket");	
				String[] price 			      = request.getParameterValues("price");
				String idTicketTransactionStr = request.getParameter("idTicketTransaction");
				String cc					  = request.getParameter("cc");
				int idTicketTransaction = 0;
					
	
				
				if(ticket != null && price != null && idTicketTransactionStr != null){

					
					idTicketTransaction = Integer.parseInt(idTicketTransactionStr.trim());
					
					TicketTransaction ticketTransaction;
					try {
						
						// complete ticket transaction
						if(mm.getLoggedUser().getType().equals(User.Type.TICKETSELLER) || (mm.getLoggedUser().getType().equals(User.Type.WEBCUSTOMER) && (!(cc.isEmpty()) && cc.length()==16))){
							ticketTransaction = mm.addTicketTransaction((int)idTicketTransaction, ticket, price);
							request.setAttribute("ticketTransaction", ticketTransaction);
						
						// data missing: do initTicketTransaction again
						} else {
							
							ticketTransaction = mm.getTicketTransaction((int)idTicketTransaction);							
							request.setAttribute("error", "PaymentNotAuthorized");
							request.setAttribute("do", "initTicketTransaction");
							request.setAttribute("action", "TicketTransaction");
							request.setAttribute("ticketTransaction", ticketTransaction);
							
						}
							
						
					} catch (TicketTransactionNotFoundException e) {
						request.setAttribute("error", "TicketTransactionNotFoundException");
					} catch (TicketTransactionTimeOutException e) {
						request.setAttribute("error", "TicketTransactionTimeOutException");
					} catch (PlaceNotAvailableException e) {
						request.setAttribute("error", "PlaceNotAvailableException");	
					} catch (Exception e) {
						request.setAttribute("error", e.getMessage());
					}
					
						
				} else {
					request.setAttribute("error", "EmptyField");					
				}
		
				dispatcher = request.getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
				
			}
			
		} else {
			request.setAttribute("error", "Nessuna operazione specificata");
			dispatcher = request.getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);
		}
		
		
		
		
	}
			
}