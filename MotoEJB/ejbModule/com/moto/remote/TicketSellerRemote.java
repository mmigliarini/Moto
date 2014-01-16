package com.moto.remote;

import javax.ejb.Remote;

import com.moto.entity.Reservation;
import com.moto.exception.ReservationNotAuthorizedException;
import com.moto.exception.ReservationNotFoundException;
import com.moto.exception.UserNotAllowedException;
import com.moto.exception.UserNotLoggedException;



@Remote
public interface TicketSellerRemote {
	public Reservation findReservation(int id, String code) throws ReservationNotFoundException, ReservationNotAuthorizedException, UserNotLoggedException, UserNotAllowedException;
}
