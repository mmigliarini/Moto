package com.moto.remote;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;
import com.moto.entity.*;
import com.moto.exception.*;

@Remote
public interface MotoManagerRemote extends AccessManagerRemote, UserManagerRemote {

	public void init();
	public User getLoggedUser() throws UserNotLoggedException;
	
	public List<Movie> getMovies() throws MoviesNotFoundException;
	public Movie getMovie(int id) throws MovieNotFoundException;
	
	public List<MovieShow> getMovieShows(Movie movie) throws MovieShowsNotFoundException;
	public MovieShow getMovieShow(int id) throws MovieShowNotFoundException;

	public int getNumMovieShows(Movie movie);
	public int getNumReservedPlaces(MovieShow movieShow);
	public int getNumAvailablePlaces(MovieShow movieShow);
	
	public HashMap<String, Place> getPlaces(MovieShow ms);
	public List<Place> getReservationPlaces(Reservation reservation);
	
	public void setPlaceAvailableForMovieShowTimeOut(MovieShow movieShow);

	
	public Date getDateAndTimeFromNow(int d, int h, int m);
	public List<Price> getMovieShowPrices(MovieShow movieShow);
	
	public int getNumTempTicketTransactionPlace(MovieShow movieShow);
	public TicketTransaction getTicketTransaction(int id) throws TicketTransactionNotFoundException;
	
	
	// REMOTE METHODS FOR JUNIT TESTING
	public Movie addMovie(String title, String description, int minutes);
	public MovieShow addMovieShow(Date date, Movie movie, VisionRoom visionRoom);
	public VisionRoom addVisionRoom(int r, int c);
	public Date getDateAndTimeFromToday(int d, int h, int m);
	public TicketSeller addTicketSeller(String username, String password, String name, String surname);
}
