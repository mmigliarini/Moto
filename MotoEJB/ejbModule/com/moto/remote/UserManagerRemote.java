package com.moto.remote;

import javax.ejb.Remote;

import com.moto.entity.Reservation;
import com.moto.entity.TicketTransaction;

import com.moto.exception.MovieShowNotFoundException;
import com.moto.exception.NoPlaceSelectedException;
import com.moto.exception.NumPlaceQuotaExcededException;
import com.moto.exception.PlaceExcededVisionRoomException;
import com.moto.exception.PlaceNotAvailableException;
import com.moto.exception.TicketTransactionNotFoundException;
import com.moto.exception.TicketTransactionTimeOutException;
import com.moto.exception.UserNotLoggedException;


@Remote
public interface UserManagerRemote extends TicketSellerRemote, WebCustomerRemote {

	public Reservation addReservation(String[] place, int idShow) throws NumPlaceQuotaExcededException, NoPlaceSelectedException, UserNotLoggedException, PlaceNotAvailableException, PlaceExcededVisionRoomException, MovieShowNotFoundException;
	public TicketTransaction initTicketTransaction(String[] place, int idMovieShow, String code, int idReservation) throws MovieShowNotFoundException, UserNotLoggedException, PlaceNotAvailableException, NoPlaceSelectedException, PlaceExcededVisionRoomException;
	public TicketTransaction addTicketTransaction(int idTicketTransaction, String[] place, String[] price) throws TicketTransactionNotFoundException, TicketTransactionTimeOutException, PlaceNotAvailableException;
	
}
