package com.moto.testing;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.moto.remote.MotoManagerRemote;
import com.moto.entity.Movie;
import com.moto.entity.MovieShow;
import com.moto.entity.TicketSeller;
import com.moto.entity.VisionRoom;
import com.moto.exception.*;

import junit.framework.TestCase;


public class JUnitTestingMovieShow extends TestCase {
	
	/*
	 * VAR
	 */
	private Object ref;
	private Properties env;	
	private Context jndiContext;
	
	protected MotoManagerRemote mm;
	private Movie movie;		
	private VisionRoom visionRoom;
	private MovieShow movieShow;
	private TicketSeller ticketSeller;

	
	@Before
	public void setUp() throws Exception {
		super.setUp();
			env = new Properties();
			env.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
			env.setProperty("java.naming.provider.url", "localhost:1099");
			env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
			
			jndiContext = new InitialContext(env);
			ref = jndiContext.lookup("MotoManagerRemote");
			mm = (MotoManagerRemote) PortableRemoteObject.narrow(ref, MotoManagerRemote.class);
			
			try {
				
				movie = mm.addMovie("Fast & Furious 5", "", 130);		
				visionRoom = mm.addVisionRoom(8, 18);
				movieShow = mm.addMovieShow(mm.getDateAndTimeFromToday(0, 20, 30), movie, visionRoom);

				
			} catch (Exception e){}
			
	}
	
	
	
	@Test
	public void testInit() {
		System.out.println("JUnit4: Init Moto");
		try {
			
			mm.init();
			ticketSeller = mm.addTicketSeller("a","b","c", "d");
			mm.login("a", "a");
			ticketSeller = (TicketSeller) mm.getLoggedUser();
			
		} catch (Exception e) {	}
	}
	
	
		
	@Test(expected = MovieNotFoundException.class)
	public void testGetMovieA() {
		System.out.println("JUnit4: Retrive Movie (100)\n");
		try {
			mm.getMovie(10000);
		} catch (Exception e) {	}
	}


	@Test
	public void testGetMovieB()  {
		System.out.println("JUnit4: Retrive Movie (movie.getId())");
		try {
			mm.getMovie(movie.getId());
		} catch (Exception e )
		{
			fail("Exception: " + e.getClass()  + " : " + e.getMessage());
		}
	}
	
	

	
	@Test(expected = MovieShowNotFoundException.class)
	public void testGetMovieShowA() {
		System.out.println("JUnit4: Retrive MovieShow (-1)");
		try {
			mm.getMovieShow(-1);
		} catch (Exception e) {	}
	}

	

	@Test
	public void testGetMovieShowB()  {
		System.out.println("JUnit4: Retrive MovieShow (movieShow.getId())");
		try {
			mm.getMovieShow(movieShow.getId());
		} catch (Exception e )
		{
			fail("Exception: " + e.getClass()  + " : " + e.getMessage());
		}
	}
	
	
	

	
	@Test(expected = NoPlaceSelectedException.class)
	public void testAddReservationA() {
		System.out.println("JUnit4: Add Reservation, place null");
		try {		
			mm.addReservation(null, 1);	
		} catch (Exception e) {	}
	}
	

	
	
	@Test(expected = UserNotLoggedException.class)
	public void testAddReservationB() {
		System.out.println("JUnit4: Add Reservation, place.lenght > 0");
		try {	
			String[] place = {"1-1", "1-2", "1-3"};
			mm.addReservation(place, 1);	
		} catch (Exception e) {	}
	}
	
	

	@Test(expected = NumPlaceQuotaExcededException.class)
	public void testAddReservationC() {
		System.out.println("JUnit4: Add Reservation, place.lenght > 5");
		try {	
			mm.login(ticketSeller.getUsername(), ticketSeller.getPassword());
			String[] place = {"1-1", "1-2", "1-3", "1-4", "1-5", "1-5"};
			mm.addReservation(place, movieShow.getId());
			mm.logout();
		} catch (Exception e) {	}
	}

	

	@Test(expected = PlaceNotAvailableException.class)
	public void testAddReservationD() {
		System.out.println("JUnit4: Add Reservation, place '1-1' RESERVED");
		try {	
			mm.login(ticketSeller.getUsername(), ticketSeller.getPassword());
			String[] place = {"1-1", "1-2", "1-3"};
			mm.addReservation(place, movieShow.getId());
		} catch (Exception e) {	}
	}	
	
	
	
	@Test
	public void testAddReservationE()  {
		System.out.println("JUnit4: Add Reservation, place '7-7' AVAILABE");
		try {
			mm.login(ticketSeller.getUsername(), ticketSeller.getPassword());
			String[] place = {"7-7"};
			mm.addReservation(place, movieShow.getId());
		}  catch (Exception e )
		{
			fail("Exception: " + e.getClass()  + " : " + e.getMessage());
		}
	}	
	

	
	


	@After
	public void tearDown() throws Exception {
		super.tearDown();		
	}

}