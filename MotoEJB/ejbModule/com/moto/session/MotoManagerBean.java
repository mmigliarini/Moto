package com.moto.session;

import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import com.moto.entity.*;
import com.moto.exception.*;

import org.jboss.ejb3.annotation.RemoteBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.moto.remote.MotoManagerRemote;


@Stateful
@Remote({ MotoManagerRemote.class })
@RemoteBinding(jndiBinding = "MotoManagerRemote")
public class MotoManagerBean implements MotoManagerRemote {

	@PersistenceContext(unitName = "moto", type = PersistenceContextType.EXTENDED)
	public EntityManager em;
	public User user = null;


	
	/**
	 * Inizialize MOTO with default values
	 * This method is called by MotoServletContextListenerServlet on deploy
	 */
	@SuppressWarnings("unused")
	@Override
	public void init() {
		
		/**********************************************/
		System.out.println("MotoManagerBean :: init()");
		/**********************************************/
		
		try {
			
			// Verify is system is already initialized
			int nUser  		  = (int)(long)(Long) getSingleResult("SELECT COUNT(u) FROM User u");
			int nMovie 		  = (int)(long)(Long) getSingleResult("SELECT COUNT(m) FROM Movie m");
			int nVisionRoom   = (int)(long)(Long) getSingleResult("SELECT COUNT(v) FROM VisionRoom v");
			int nReservation  = (int)(long)(Long) getSingleResult("SELECT COUNT(r) FROM Reservation r");
			int nTTransaction = (int)(long)(Long) getSingleResult("SELECT COUNT(t) FROM TicketTransaction t");
			
			
			// Initialized with default datas
			if (nUser==0 && nMovie == 0 && nVisionRoom == 0 && nReservation == 0 && nTTransaction ==0){
				
				// TicketSeller and WebCustomer
				System.out.println("1. Creating TicketSellers (1) and WebCustomer (1)");	
				TicketSeller ticketSeller_1 = addTicketSeller("mrossi", "mrossi", "Mario", "Rossi");
				WebCustomer webCustomer_1 	= addWebCustomer("mverdi", "mverdi", "Mario", "Verdi", "mverdi@google.com", "ABCDEF00G00H000G");
				
				// Movies
				System.out.println("2. Creating Movies (4)");
				Movie movie_1 = addMovie("Fast & Furious 5", "Regia:\nCast:\nTrama:", 130);
				Movie movie_2 = addMovie("Pirati dei Caraibi: oltre i confini del mare", "Regia:\nCast:\nTrama:", 140);
				Movie movie_3 = addMovie("Una notte da leoni 2", "Regia:\nCast:\nTrama:", 105);
				Movie movie_4 = addMovie("Red", "Regia:\nCast:\nTrama:", 110);
				
				// VisionRooms
				System.out.println("3. Creating VisionRooms (2)");	
				VisionRoom visionRoom_1 = addVisionRoom(8, 18);
				VisionRoom visionRoom_2 = addVisionRoom(10, 21);
			
				
				// MovieShows
				System.out.println("4. Creating MovieShows (8)");
				MovieShow movieShow_1 = addMovieShow(getDateAndTimeFromToday(0, 20, 30), movie_1, visionRoom_1);
				MovieShow movieShow_2 = addMovieShow(getDateAndTimeFromToday(0, 20, 30), movie_2, visionRoom_2);
				MovieShow movieShow_3 = addMovieShow(getDateAndTimeFromToday(1, 20, 30), movie_3, visionRoom_1);
				MovieShow movieShow_4 = addMovieShow(getDateAndTimeFromToday(2, 20, 30), movie_4, visionRoom_2);
				MovieShow movieShow_5 = addMovieShow(getDateAndTimeFromToday(3, 20, 30), movie_2, visionRoom_1);
				MovieShow movieShow_6 = addMovieShow(getDateAndTimeFromToday(3, 20, 30), movie_3, visionRoom_2);
				MovieShow movieShow_7 = addMovieShow(getDateAndTimeFromToday(4, 20, 30), movie_1, visionRoom_1);
				MovieShow movieShow_8 = addMovieShow(getDateAndTimeFromToday(4, 20, 30), movie_4, visionRoom_2);

					
				// Prices
				System.out.println("5. Creating MovieShow Prices (8)");
				Price price_01 = addPrice("Prezzo intero",  (float)7.5, true,  movieShow_1);
				Price price_02 = addPrice("Prezzo ridotto", (float)5.5, false, movieShow_1);
				Price price_03 = addPrice("Prezzo intero",  (float)7.5, true,  movieShow_2);
				Price price_04 = addPrice("Prezzo ridotto", (float)5.5, false, movieShow_2);
				Price price_05 = addPrice("Prezzo intero",  (float)7.5, true,  movieShow_3);
				Price price_06 = addPrice("Prezzo ridotto", (float)5.5, false, movieShow_3);
				Price price_07 = addPrice("Prezzo intero",  (float)7.5, true,  movieShow_4);
				Price price_08 = addPrice("Prezzo ridotto", (float)5.5, false, movieShow_4);
				Price price_09 = addPrice("Prezzo intero",  (float)7.5, true,  movieShow_5);
				Price price_10 = addPrice("Prezzo ridotto", (float)5.5, false, movieShow_5);
				Price price_11 = addPrice("Prezzo ridotto", (float)5.5, true,  movieShow_6);
				Price price_12 = addPrice("Prezzo intero",  (float)7.5, false, movieShow_6);
				Price price_13 = addPrice("Prezzo ridotto", (float)5.5, true,  movieShow_7);
				Price price_14 = addPrice("Prezzo intero",  (float)7.5, false, movieShow_7);
				Price price_15 = addPrice("Prezzo ridotto", (float)5.5, true,  movieShow_8);
				Price price_16 = addPrice("Prezzo intero",  (float)7.5, false, movieShow_8);
					

				// New Reservation
				System.out.println("6. Creating Reservation (1)");
				Reservation reservation = addReservation(new Date(), "c", ticketSeller_1);

				
				// New Places
				Place place_1 = null, place_2, place_3, place_4, place_5;
				try{
					place_1 = addPlace(1, 1, reservation, movieShow_1);
					place_2 = addPlace(5, 5, reservation, movieShow_1);		
					place_3 = addPlace(5, 6, reservation, movieShow_1);
					place_4 = addPlace(5, 7, reservation, movieShow_1);
					place_5 = addPlace(5, 8, reservation, movieShow_1);
				} catch (Exception e){}
				
				
				// New TicketTransaction
				System.out.println("7. Creating TicketTransaction (1)");
				TicketTransaction ticketTransaction = addTicketTransaction(new Date(), ticketSeller_1);
			
				
				// New Ticket
				System.out.println("8. Creating Ticket (1)");
				Ticket ticket = new Ticket(place_1, ticketTransaction, price_01);
				persist(ticket);
				
				//merge
				ticketTransaction.getTickets().add(ticket);
				merge(ticketTransaction);
				
			} else {
				System.out.println("Sistema già inizializzato.");	
			}
			
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		

	
	}
	

	
	/**
	 * 0. ENTITY MANAGER UTILS
	 *********************************************************/	
	public void persist(Object object) {
		em.persist(object);
		em.flush();
	}
	
	public void merge(Object object){
		em.merge(object);
		em.flush();
	}
	
	public void remove(Object object){
		em.remove(object);
		em.flush();
	}
	
	

	
	/**
	 * 1. DEFAULT ADD ENTITY METHOS, WITH PERSIST AND MERGE
	 *********************************************************/

	
	/**
	 * 
	 * @param r
	 * @param c
	 * @return visionRoom
	 */
	public VisionRoom addVisionRoom(int r, int c) {
		
		VisionRoom visionRoom = null;
		
		try {
			visionRoom = new VisionRoom(r, c);
			persist(visionRoom);
		} catch (Exception e){}
		
		return visionRoom;
		
	}

	
	/**
	 * 
	 * @param date
	 * @param movie
	 * @param visionRoom
	 * @return movieShow
	 */
	public MovieShow addMovieShow(Date date, Movie movie, VisionRoom visionRoom){
		
		MovieShow movieShow = null;
		
		try {
			movieShow = new MovieShow(date, movie, visionRoom);
			persist(movieShow);
			visionRoom.getMovieShows().add(movieShow);
			movie.getMovieShows().add(movieShow);
			merge(visionRoom);
			merge(movie);
		} catch (Exception e){}
		
		return movieShow;
	}


	/**
	 * 
	 * @param title
	 * @param description
	 * @param minutes
	 * @return movie
	 */
	public Movie addMovie(String title, String description, int minutes){
		
		Movie movie = null;
		
		try {
			movie = new Movie(title, description, minutes);
			persist(movie);
		} catch (Exception e){}
		
		return movie;
	}
	
	/**
	 * 
	 * @param rateName
	 * @param rateAmmount
	 * @param isDefault
	 * @param movieShow
	 * @return price
	 */
	public Price addPrice(String rateName, float rateAmmount, boolean isDefault, MovieShow movieShow){
		
		Price price = null;
		
		try {
			price = new Price(rateName, rateAmmount, isDefault, movieShow);
			persist(price);
			movieShow.getPrices().add(price);
			merge(movieShow);
		} catch (Exception e){}
		
		return price;
	}

		
	/**
	 * 
	 * @param username
	 * @param password
	 * @param name
	 * @param surname
	 * @return ticketSeller
	 */
	public TicketSeller addTicketSeller(String username, String password, String name, String surname){
		
		TicketSeller ticketSeller = null;
		
		try {
			ticketSeller = new TicketSeller(username, password, name, surname);
			persist(ticketSeller);
		} catch (Exception e){}
		
		return ticketSeller;
	}
	

	/**
	 * 
	 * @param username
	 * @param password
	 * @param name
	 * @param surname
	 * @param email
	 * @param cf
	 * @return webCustomer
	 */
	public WebCustomer addWebCustomer(String username, String password, String name, String surname, String email, String cf){
		
		WebCustomer webCustomer = null;
		
		try {
			webCustomer = new WebCustomer(username, password, name, surname, email, cf);
			persist(webCustomer);
		} catch (Exception e){}
		
		return webCustomer;
	}
	
	
	/**
	 * Add a new place, Reserved
	 * 
	 * @param idRow
	 * @param idCol
	 * @param reservation
	 * @param movieShow
	 * @return reservation
	 * @throws PlaceNotAvailableException
	 * @throws PlaceExcededVisionRoomException 
	 */
	public Place addPlace(int idRow, int idCol, Reservation reservation, MovieShow movieShow) throws PlaceNotAvailableException, PlaceExcededVisionRoomException {
		
		/************************************************/
		System.out.print("MotoManagerBean :: addPlace()");
		/************************************************/
		
		// idRow o idCol greater than VisionRoom dimension
		if(idRow>movieShow.getVisionRoom().getNumRows() || idCol>movieShow.getVisionRoom().getNumCols()){
			System.out.print("Posto R"+ idRow +"C"+ idCol +" supera le dimensioni di VisionRoom.");
			throw new PlaceExcededVisionRoomException();	
		}
		
		Place place = null;
		try {
			place = (Place)getSingleResult("SELECT p FROM Place p WHERE p.idRow=?1 AND p.idCol=?2 AND p.movieShow=?3 AND (p.status=?4 OR p.status=?5)", idRow, idCol, movieShow, Place.Status.RESERVED, Place.Status.SOLD);
			
			System.out.print("Posto R"+ idRow +"C"+ idCol +" non disponibile.");
			if(place!=null)	throw new PlaceNotAvailableException();			
		
		// Create new Place, id Place not found
		} catch (NoResultException e) {
			System.out.print("Posto R"+ idRow +"C"+ idCol +" disponibile, procedo alla creazione.");
			place = new Place(idRow, idCol, Place.Status.RESERVED, reservation, movieShow);
			persist(place);
			
			if(reservation!=null){
				reservation.getPlaces().add(place);
				merge(reservation);
			}
			
			movieShow.getPlaces().add(place);
			merge(movieShow);	
		}

		return place;
	}


	
	/**
	 * 
	 * @param date
	 * @param code
	 * @param user
	 * @return reservation
	 */
	public Reservation addReservation(Date date, String code, User user){
		
		Reservation reservation = null;
		
		try {
			reservation = new Reservation(date, code, user);
			persist(reservation);
			user.getReservations().add(reservation);
			merge(user);
		} catch (Exception e){}
		
		return reservation;
	}
	

	/**
	 * 
	 * @param date
	 * @param user
	 * @return ticketTransaction
	 */
	public TicketTransaction addTicketTransaction(Date date, User user){
		
		TicketTransaction ticketTransaction = null;
		
		try {
			ticketTransaction = new TicketTransaction(date, user);
			persist(ticketTransaction);
			user.getTicketTransactions().add(ticketTransaction);
			merge(user);
			
		} catch (Exception e){}
		
		return ticketTransaction;
	}
	
	
	
	
	
	
	
	
	/**
	 * 2. DATE UTILES
	 *********************************************************/
	
	/**
	 * Get a modified date, from now.
	 * Add/remove days, hours, minutes
	 * 
	 * @param d Day
	 * @param h Hour
	 * @param m Minutes
	 * @return
	 */
	public Date getDateAndTimeFromNow(int d, int h, int m){
		
		Date date = new Date();  
		  
		// Get Calendar object set to the date and time of the given Date object   
		Calendar cal = Calendar.getInstance();   
		cal.setTime(date);   
		  
		// Adds a number of days to a date	
		cal.add(Calendar.DATE, d); 
		cal.add(Calendar.HOUR, h);
		cal.add(Calendar.MINUTE,m);
	
		// Put it back in the Date object   
		date = cal.getTime();
	    
		return date;
	}

	
	/**
	 * Get a modified date, from Today.
	 * Start from midnight (0hr, 0min).
	 * Add/remove days, hours, minutes
	 * 
	 * @param d Day
	 * @param h Hour
	 * @param m Minutes
	 * @return
	 */
	public Date getDateAndTimeFromToday(int d, int h, int m){
		
		Date date = new Date();  
		  
		// Get Calendar object set to the date and time of the given Date object   
		Calendar cal = Calendar.getInstance();   
		cal.setTime(date);   
		  
		// Set time fields to zero   
		cal.set(Calendar.HOUR_OF_DAY, h);   
		cal.set(Calendar.MINUTE, m);   
		cal.set(Calendar.SECOND, 0);   
		cal.set(Calendar.MILLISECOND, 0);  
		
		// Adds a number of days to a date
		cal.add(Calendar.DATE, d); 
	
		// Put it back in the Date object   
		date = cal.getTime();
	    
		return date;
	}
	

	
	/**
	 * 3. ENTITY MANAGER UTILS
	 *********************************************************/
	
	/**
	 * Query getSingleResult
	 * 
	 * @param query
	 * @param parameter
	 * @return
	 */
	public Object getSingleResult(String query, Object... parameter) {
		Query q = em.createQuery(query);
		for (int i = 0; i < parameter.length; i++) {
			q.setParameter(i + 1, parameter[i]);
		}
		return q.getSingleResult();
	}
	
	
	/**
	 * Query getResultList
	 * 
	 * @param query
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getResultList(String query, Object... parameter) {
		Query q = em.createQuery(query);
		for (int i = 0; i < parameter.length; i++) {
			q.setParameter(i + 1, parameter[i]);
		}
		return q.getResultList();
	}

	
	
	
	/**
	 * 4. MOTO METHODS
	 *********************************************************/
	

	/**
	 * Login
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws UserNotFoundException
	 */
	public User login(String username, String password) throws UserNotFoundException {
		
		/*********************************************/
		System.out.print("MotoManagerBean :: login()");
		/*********************************************/
		
		if(user==null){
			
			try {
				user = (User)getSingleResult("SELECT u FROM User u WHERE u.username=?1 AND u.password=?2", username, password);
			} catch (NoResultException e) {
				System.out.print("User " + username + " not found.");
				throw new UserNotFoundException();
			}
			
			
		}
		return user;
	}


	
	//	The @Remove annotation is used with a method to inform the container that the client 
	//	no longer needs a session and that the session should be destroyed. This annotation is 
	//	important, as it is the only way to request the container that the session should end. 
	//	If the remove method is not used, the session will have to be timed out before it can 
	//	be destroyed by itself. If stateful session bean instances are not removed when they 
	//	are no longer needed, the number of inactive instances will keep on incrementing and 
	//	will waste memory and CPU cycles
	@Remove
	public void logout() {
		
		/**********************************************/
		System.out.print("MotoManagerBean :: logout()");
		/**********************************************/
		user = null;
	}	


	
	/**
	 * Register Moto's users
	 * 
	 * @param type
	 * @param username
	 * @param password
	 * @param name
	 * @param surname
	 * @param email
	 * @param cf
	 * @throws UserAlreadyExistsException
	 * @throws UserTypeDoesNotExistException
	 */
	public void register(String type, String username, String password, String name, String surname, String email, String cf) throws UserAlreadyExistsException, UserTypeDoesNotExistException {

		/************************************************/
		System.out.print("MotoManagerBean :: register()");
		/************************************************/
		
		if(em.find(User.class, username) != null){
			throw new UserAlreadyExistsException();
		} else {
			
			// create new TicketSeller
			if (type.equals("TicketSeller")) {
				user = addTicketSeller(username, password, name, surname);
				
			// create new WebCustomer
			} else if(type.equals("WebCustomer")) {
				user = addWebCustomer(username, password, name, surname, email, cf);	
				
			// usertype does not exist	
			} else {
				throw new UserTypeDoesNotExistException();
			}
			
		}
		
	}
	
	
	/**
	 * Get the CurrentLoggedUser, owner of bean
	 * 
	 * @throws UserNotLoggedException 
	 * 
	 */
	@Override
	public User getLoggedUser() throws UserNotLoggedException {
		
		/*****************************************************/
		System.out.print("MotoManagerBean :: getLoggedUser()");
		/*****************************************************/
		if(user == null) throw new UserNotLoggedException();
		return user;
	}


	/**
	 * List all movies, with at least one MovieShow
	 */
	@SuppressWarnings("unchecked")
	public List<Movie> getMovies() throws MoviesNotFoundException{	
		
		/*************************************************/
		System.out.print("MotoManagerBean :: getMovies()");
		/*************************************************/
		
		List<Movie> movies;
		movies = getResultList("SELECT DISTINCT m FROM Movie m JOIN m.movieShows ms WHERE ms.datetime >= ?1 ORDER BY m.name", new Date());
		for (Movie m: movies){
			System.out.println("Film: " + m.getName() + ".");
		}

		if(movies.isEmpty()){
			System.out.println("Movies not found.");
			throw new MoviesNotFoundException();
		}
			
		return movies;
	}


	
	/**
	 * Get single Movie, starting from it's id
	 * 
	 * @param id
	 * @return
	 * @throws MovieNotFoundException
	 */
	public Movie getMovie(int id) throws MovieNotFoundException {

		/********************************************************/
		System.out.print("MotoManagerBean :: getMovie(Movie.id)");
		/********************************************************/
		Movie movie = null;
		
		try { 
			movie = em.find(Movie.class, id); 
			if(movie==null) throw new MovieNotFoundException();
		} catch (Exception ex) { 
			throw new MovieNotFoundException();
		}

		return movie;	
	}
	
	
	
	/**
	 * Get all future MovieShow, strarting from projected Movie
	 * 
	 * @param movie
	 * @return
	 * @throws MovieShowsNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<MovieShow> getMovieShows(Movie movie) throws MovieShowsNotFoundException{	
		
		/**********************************************************/
		System.out.print("MotoManagerBean :: getMovieShows(Movie)");
		/**********************************************************/
		
		List<MovieShow> movieShows;
		movieShows = getResultList("SELECT ms FROM MovieShow ms WHERE ms.movie=?1 AND ms.datetime >= ?2 ORDER BY ms.datetime", movie, new Date());

		for (MovieShow ms: movieShows){
			System.out.println("Film: " + ms.getMovie().getName() + ", Data: " + (new SimpleDateFormat("dd/MM/yyyy")).format(ms.getDatetime()) + ".");
			System.out.println("Ora: " + (new SimpleDateFormat("HH:mm")).format(ms.getDatetime()) + ", Sala: " + ms.getVisionRoom().getId() + ".");
		}

		if(movieShows.isEmpty()){
			System.out.println("MovieShows not found.");
			throw new MovieShowsNotFoundException();
		}
		
		return movieShows;
		
	}


	
	/**
	 * Get single MovieShow, starting from it's id
	 * 
	 * @param id
	 * @return
	 * @throws MovieShowNotFoundException
	 */
	public MovieShow getMovieShow(int id) throws MovieShowNotFoundException {

		/****************************************************************/
		System.out.print("MotoManagerBean :: getMovieShow(MovieShow.id)");
		/****************************************************************/
		MovieShow ms = null;
		
		try { 
			ms = em.find(MovieShow.class, id); 
			if(ms==null) throw new MovieShowNotFoundException();
		} catch (Exception ex) { 
			throw new MovieShowNotFoundException();
		}
		
		return ms;	
	}
	
	

	/**
	 * Get number of MovieShow, starting from it's projected movie
	 * 
	 * @param movie
	 * @return
	 */
	public int getNumMovieShows(Movie movie){
		
		/*************************************************************/
		System.out.print("MotoManagerBean :: getNumMovieShows(Movie)");
		/*************************************************************/
		
		int n = (int)(long)(Long)getSingleResult("SELECT count(m) FROM MovieShow m WHERE m.movie=?1 AND m.datetime>=?2", movie, new Date());
		System.out.println("Film: " + movie.getName() + ". Numero spettacoli: " + n);
		return (int)n;		
	}
	

	
	/**
	 * Get TicketTransaction, starting from it's id
	 * 
	 * @param id
	 * @return
	 * @throws TicketTransactionNotFoundException
	 */
	public TicketTransaction getTicketTransaction(int id) throws TicketTransactionNotFoundException {

		/********************************************************************************/
		System.out.print("MotoManagerBean :: getTicketTransaction(TicketTransaction.id)");
		/********************************************************************************/
		TicketTransaction ticketTransaction = null;
		
		try { 
			ticketTransaction = em.find(TicketTransaction.class, id); 
			if(ticketTransaction==null) throw new TicketTransactionNotFoundException();
		} catch (Exception ex) { 
			throw new TicketTransactionNotFoundException();
		}

		return ticketTransaction;	
	}
	
	
	
	
	/**
	 * Get number of reserved place, for each MovieShow
	 * 
	 * @param movieShow
	 * @return
	 */
	public int getNumReservedPlaces(MovieShow movieShow){
		
		/*********************************************************************/
		System.out.print("MotoManagerBean :: getNumReservedPlaces(MovieShow)");
		/*********************************************************************/
		
		int n = (int)(long)(Long)getSingleResult("SELECT count(p) FROM Place p WHERE p.movieShow=?1 AND p.status=?2", movieShow, Place.Status.RESERVED);
		return n;
		
	}
	
	
	/**
	 * Get number of available place, for each MovieShow
	 * 
	 * @param movieShow
	 * @return
	 */
	public int getNumAvailablePlaces(MovieShow movieShow){

		/**********************************************************************/
		System.out.print("MotoManagerBean :: getNumAvailablePlaces(MovieShow)");
		/**********************************************************************/
		
		int n = (int)(long)(Long)getSingleResult("SELECT count(p) FROM Place p WHERE p.movieShow=?1 AND (p.status=?2 OR p.status=?3)", movieShow, Place.Status.RESERVED, Place.Status.SOLD);

		int numPlaces = movieShow.getVisionRoom().getNumPlaces();
		
		System.out.println("Numero posti liberi: " + (numPlaces-(int)n));
		return (numPlaces-n);
		
	}
	

	
	/**
	 * Get an ordered list of Place
	 * 
	 * @param ms
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Place> getPlaces(MovieShow movieShow){
		
		/**********************************************************/
		System.out.print("MotoManagerBean :: getPlaces(MovieShow)");
		/**********************************************************/
		
		HashMap<String, Place> hmp = new HashMap<String,Place>();
		List<Place> places;
		places = getResultList("SELECT p FROM Place p WHERE p.movieShow=?1 AND (p.status=?2 OR p.status=?3)", movieShow, Place.Status.RESERVED, Place.Status.SOLD);

		for (Place p: places){
			hmp.put("R"+p.getIdRow()+"C"+p.getIdCol(), p);
		}
		return hmp;
		
	}


	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Place> getReservationPlaces(Reservation reservation){
		
		/***********************************************************************/
		System.out.print("MotoManagerBean :: getReservationPlaces(Reservation)");
		/***********************************************************************/
		
		List<Place> placeList = getResultList("SELECT p FROM Place p WHERE p.reservation=?1", reservation);
		return placeList;
		
	}
	

	/**
	 * List Place of each TicketTransaction
	 * 
	 * @param ticketTransaction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Place> getTicketTransactionPlaces(TicketTransaction ticketTransaction){
		
		/***********************************************************************************/
		System.out.print("MotoManagerBean :: getTicketTransactionPlaces(TicketTransaction)");
		/***********************************************************************************/
		
		List<Place> placeList = getResultList("SELECT p FROM Ticket t JOIN Place p WHERE t.ticketTransaction=?1", ticketTransaction);
		return placeList;
		
	}
	
	
	
	/**
	 * Set all reserved place to AVAILABLE stas, if (now+20minuti) is after MovieShow datetime
	 * 
	 * @param movieShow
	 */
	@SuppressWarnings("rawtypes")
	public void setPlaceAvailableForMovieShowTimeOut(MovieShow movieShow) {
		
		/*************************************************************************************/
		System.out.print("MotoManagerBean :: setPlaceAvailableForMovieShowTimeOut(MovieShow)");
		/*************************************************************************************/
		
		if(getDateAndTimeFromNow(0, 0, 20).after(movieShow.getDatetime()) && getNumReservedPlaces(movieShow) > 0){
			HashMap<String, Place> hmp = getPlaces(movieShow);

			// Iterate retrived HashMap throught Collection
			Collection cp = hmp.values();
			Iterator i	  = cp.iterator();
			
			// Set Status AVAILABLE where is RESERVED
			while(i.hasNext()){
				Place place = (Place)i.next();
				
				// prevent deleting "temporary" reserved for purchase
				try {
					@SuppressWarnings("unused")
					Ticket t = (Ticket)getSingleResult("SELECT t FROM Ticket t WHERE t.place=?1 AND t.place.status=?2", place, Place.Status.RESERVED);
				} catch (NoResultException e) {
					if(place.getStatus().equals(Place.Status.RESERVED)){
						place.setStatus(Place.Status.AVAILABLE);
						merge(place);
					}
				}				
			}			
		}
	}
	

	
	
	/**
	 * Set Place Available for TicketTransaction TimeOut (5 minutes from initTicketTransaction)
	 * 
	 * @param movieShow
	 */
	public void setPlaceAvailableForTicketTransactionTimeOut(MovieShow movieShow) {
		
		/*********************************************************************************************/
		System.out.print("MotoManagerBean :: setPlaceAvailableForTicketTransactionTimeOut(MovieShow)");
		/*********************************************************************************************/
		

		@SuppressWarnings("unchecked")
		List<Ticket> tList = getResultList("SELECT t FROM Ticket t JOIN t.place p WHERE p.movieShow=?1 AND p.status=?2", movieShow, Place.Status.RESERVED);
		TicketTransaction tt = null;
		Place place = null;
		
		for (Ticket t : tList){
							
			place 	  = em.find(Place.class, t.getPlace().getId());
			movieShow = em.find(MovieShow.class, movieShow.getId());
			tt = em.find(TicketTransaction.class, t.getTicketTransaction().getId());
			
			Date date = tt.getDatetime();
			Calendar cal = Calendar.getInstance();   
			cal.setTime(date);
			cal.add(Calendar.MINUTE, 3);	// ADD 3 MINUTES TO TICKETTRANSACTION DATE
			cal.add(Calendar.SECOND, 10); 	// LET ME ADD 10 FOR HTTP RESPONSE LATENCY MARGIN
			date = cal.getTime();
			
			//System.out.println("new date is: "+ date);
			//System.out.println("the place is: "+ place.getId());
			
			
			// remove places without a living reservation		
			if(date.before(new Date())){

				// removing all temporary ticket				
				tt.getTickets().remove(t);
				remove(t);
							
				// removing places
				movieShow.getPlaces().remove(place);
				remove(place);
				
				// merge ticketTransaction
				merge(tt);
				
				// delete ticketTransaction
				if(tt.getTickets().isEmpty()){
					tt.getUser().getTicketTransactions().remove(tt);
					remove(tt);					
				}

			}	
			
		}
		
		
		
		
	}

	
	
	/**
	 * Get number of temporary Ticket (during initTicketTransaction, for 3 minutes)
	 * 
	 * @param movieShow
	 * @return
	 */
	public int getNumTempTicketTransactionPlace(MovieShow movieShow) {
		
		/************************************************************************/
		System.out.print("MotoManagerBean :: getNumTempTicketTransactionPlace()");
		/************************************************************************/

		int n = (int)(long)(Long)getSingleResult("SELECT COUNT(t) FROM Ticket t JOIN t.place p WHERE p.movieShow=?1 AND p.status=?2", movieShow, Place.Status.RESERVED);
				
		if (n>0){ setPlaceAvailableForTicketTransactionTimeOut(movieShow); }
		
		return n;
		
	}
	
	
	
	/**
	 * 
	 * Add a new Reservation, with a Set (Array) of places
	 * 
	 * @param place
	 * @param idMovieShow
	 * @return
	 * @throws NumPlaceQuotaExcededException
	 * @throws NoPlaceSelectedException
	 * @throws UserNotLoggedException
	 * @throws PlaceNotAvailableException
	 * @throws PlaceExcededVisionRoomException
	 * @throws MovieShowNotFoundException
	 */
	public Reservation addReservation(String[] place, int idMovieShow) throws NumPlaceQuotaExcededException, NoPlaceSelectedException, UserNotLoggedException, PlaceNotAvailableException, PlaceExcededVisionRoomException, MovieShowNotFoundException {

		/******************************************************/
		System.out.print("MotoManagerBean :: addReservation()");
		/******************************************************/
		
		Reservation reservation = null;
		@SuppressWarnings("unused")
		Place p	= null;
		
		MovieShow ms = getMovieShow(idMovieShow);
		
		if(ms!=null){
		
			// set Place Available for MovieShow Timeout
			setPlaceAvailableForMovieShowTimeOut(ms);
			
			// Num place selected
			if (place != null && place.length>0)    { 
				
	
				// User is logged
				if(user!= null){
				
					// Create a new Reservation
					reservation = addReservation (new Date(), Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 6), user);
					
					if(reservation != null){
						
						if(place.length<=5){
						
							try{
								
								// Create a new Reserved Place
								for (int i = 0; i < place.length; i++) {
									System.out.println(ms.getMovie().getName());
									String[] c = place[i].split("-"); 	//	c[0] = idRow;  c[1] = idCol;						
									p = addPlace((int)Integer.parseInt(c[0]), (int)Integer.parseInt(c[1]), reservation, ms);
								}
												
							} catch (PlaceNotAvailableException e) {
								
								@SuppressWarnings("unchecked")
								List<Place> pList = getResultList("SELECT p FROM Place p WHERE p.reservation=?1", reservation);
								
								for (Place placeToDelete : pList){
													
									ms   		= em.find(MovieShow.class, ms.getId());
									reservation = em.find(Reservation.class, placeToDelete.getReservation().getId());
													
									// removing places
									ms.getPlaces().remove(placeToDelete);
									reservation.getPlaces().remove(placeToDelete);
									remove(placeToDelete);
										
									// merge reservation
									merge(reservation);
										
									// delete reservation
									if(reservation.getPlaces().isEmpty()){
										reservation.getUser().getReservations().remove(reservation);
										remove(reservation);					
									}
									
								}
																
								throw new PlaceNotAvailableException();
								
							} catch (Exception ex) {}
						
						// Num place > 5 
						} else 	throw new NumPlaceQuotaExcededException();	
						
					}
				// No user logged
				} else throw new UserNotLoggedException();
				
			// No place selected	
			} else throw new NoPlaceSelectedException();

		}

		return reservation;
		
	}


	/**
	 * Find reservation based on it's id and auth code
	 * 
	 * @param id
	 * @param code
	 * @return
	 * @throws ReservationNotFoundException
	 * @throws ReservationNotAuthorizedException
	 * @throws UserNotLoggedException 
	 * @throws UserNotAllowedException 
	 */
	@Override
	public Reservation findReservation(int id, String code) throws ReservationNotFoundException, ReservationNotAuthorizedException, UserNotLoggedException, UserNotAllowedException {
		
		/******************************************************/
		System.out.print("MotoManagerBean :: findReservation()");
		/******************************************************/
		
		Reservation reservation = null;
			
		if(getLoggedUser().getType().equals(User.Type.TICKETSELLER)){
				
				reservation = em.find(Reservation.class, id);
				
				if ( reservation == null ){
					throw new ReservationNotFoundException();	
				}
				else if ( !(reservation.getCode().equals(code)) ){	
					reservation = null;
					throw new ReservationNotAuthorizedException();	
				} 
				
			
		} else {
			
				throw new UserNotAllowedException();
		}
			
		return reservation;
		
		
	}
	
	

	/**
	 * Get the list of MovieShow prices, for purchase
	 * 
	 * @param movieShow
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Price> getMovieShowPrices(MovieShow movieShow) {	
		
		/**********************************************************/
		System.out.print("MotoManagerBean :: getShowMoviePrices()");
		/**********************************************************/
		
		List<Price> prices;
		prices = getResultList("SELECT DISTINCT p FROM Price p WHERE p.movieShow = ?1 ORDER BY p.rateAmmount DESC", movieShow);
		return prices;
		
	}
	

	
	

	/**
	 * Inizialize a ticketTransaction. Temporary reserve places (3mins)
	 * 
	 * @param place
	 * @param idMovieShow
	 * @return
	 * @throws MovieShowNotFoundException
	 * @throws UserNotLoggedException
	 * @throws PlaceNotAvailableException
	 * @throws NoPlaceSelectedException
	 */
	public TicketTransaction initTicketTransaction(String[] place, int idMovieShow, String code, int idReservation) throws NoPlaceSelectedException, UserNotLoggedException, PlaceNotAvailableException, PlaceExcededVisionRoomException, MovieShowNotFoundException {

		/*************************************************************/
		System.out.print("MotoManagerBean :: initTicketTransaction()");
		/*************************************************************/
		
		TicketTransaction ticketTransaction = null;
		MovieShow ms = getMovieShow(idMovieShow);
		Place p  = null;
		Ticket t = null;

		
		if(code!= null) System.out.println(code);
						System.out.println(idReservation);
		
		
		// set Place Available for MovieShow Timeout
		setPlaceAvailableForMovieShowTimeOut(ms);
		
		// Num place selected
		if (place.length>0)    { 
			

			// User is logged
			if(user!= null){
			
				// Create a new TicketTransaction
				ticketTransaction = addTicketTransaction (new Date(), user);
					
				try{
					
					// Create a new Reserved Place
					for (int i = 0; i < place.length; i++) {
						System.out.println(ms.getMovie().getName());
						String[] c = place[i].split("-"); 	//	c[0] = idRow;  c[1] = idCol;						
						
							try {
							p = (Place) getSingleResult("SELECT p FROM Place p " +
														"WHERE p.idRow=?1 and p.idCol=?2 and p.status=?3 and p.reservation.id=?4 and p.reservation.code=?5",
														(int)Integer.parseInt(c[0]), (int)Integer.parseInt(c[1]), Place.Status.RESERVED, idReservation, code);
							
							} catch (NoResultException e) {
								p = addPlace((int)Integer.parseInt(c[0]), (int)Integer.parseInt(c[1]), null, ms);
							}
							
							t = new Ticket(p, ticketTransaction);
							persist(t);
							ticketTransaction.getTickets().add(t);
					}
					
					merge(ticketTransaction);
					merge(ms);
				
				} catch (PlaceNotAvailableException e) {
					
					@SuppressWarnings("unchecked")
					List<Ticket> tList = getResultList("SELECT t FROM Ticket t WHERE t.ticketTransaction=?1", ticketTransaction);
					TicketTransaction tt = null;
					
					for (Ticket ticketToDelete : tList){
										
						p  = em.find(Place.class, ticketToDelete.getPlace().getId());
						ms = em.find(MovieShow.class, ms.getId());
						tt = em.find(TicketTransaction.class, ticketToDelete.getTicketTransaction().getId());
						
						// removing all temporary ticket				
						tt.getTickets().remove(ticketToDelete);
						remove(ticketToDelete);
										
						// removing places
						ms.getPlaces().remove(p);
						remove(p);
							
						// merge ticketTransaction
						merge(tt);
							
						// delete ticketTransaction
						if(tt.getTickets().isEmpty()){
							tt.getUser().getTicketTransactions().remove(tt);
							remove(tt);					
						}

					}	
										
					
					throw new PlaceNotAvailableException();
					
				} catch (Exception e) {}					
					

			// No user logged
			} else throw new UserNotLoggedException();
			
		// No place selected	
		} else throw new NoPlaceSelectedException();

		

		return ticketTransaction;
		
	}
	



	/**
	 * Consolidate a TicketTransaction
	 * 
	 * @param idTicketTransaction
	 * @param ticket
	 * @param price
	 * @return
	 * @throws TicketTransactionNotFoundException
	 * @throws TicketTransactionTimeOutException
	 * @throws PlaceNotAvailableException 
	 */
	public TicketTransaction addTicketTransaction(int idTicketTransaction, String[] ticket, String[] price) throws TicketTransactionNotFoundException, TicketTransactionTimeOutException, PlaceNotAvailableException{

		/************************************************************/
		System.out.print("MotoManagerBean :: addTicketTransaction()");
		/************************************************************/
		
		TicketTransaction ticketTransaction = em.find(TicketTransaction.class, idTicketTransaction);

		if(ticketTransaction != null){
			
			Date date = ticketTransaction.getDatetime();
			Calendar cal = Calendar.getInstance();   
			cal.setTime(date);
			cal.add(Calendar.MINUTE, 3);	// ADD 3 MINUTES TO TICKETTRANSACTION DATE
			cal.add(Calendar.SECOND, 10);
			date = cal.getTime();
			
			
			if(date.before(new Date())){				
				throw new TicketTransactionTimeOutException();
			} else {
			
				int error = 0;
				
				// check if ticket is already sold
				for (int i = 0; i < ticket.length; i++) {
					Ticket t = em.find(Ticket.class, (int)Integer.parseInt(ticket[i]));
					if(t.getPlace().getStatus().equals(Place.Status.SOLD))	error=1;
				}
				
				if(error == 0){
					for (int i = 0; i < ticket.length; i++) {
						
						Ticket t = em.find(Ticket.class, (int)Integer.parseInt(ticket[i]));
						if(t.getTicketTransaction().equals(ticketTransaction)){
							t.getPlace().setStatus(Place.Status.SOLD);
							merge(t.getPlace());						
							
							//System.out.println(price[i]);
							Price p = em.find(Price.class, (int)Integer.parseInt(price[i]));
							//System.out.println(p.getId());
							t.setPrice(p);
							merge(t);	
						}
						
					}
				} else {	
				
					throw new PlaceNotAvailableException();
				}
					
					
			}
		} else throw new TicketTransactionNotFoundException();
		
		return ticketTransaction;
		
	}

	@Override
	public boolean payTicketTransaction(TicketTransaction t) {
		return true;
	}

	
}