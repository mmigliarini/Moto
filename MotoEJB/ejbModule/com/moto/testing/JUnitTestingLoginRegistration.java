package com.moto.testing;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.moto.remote.MotoManagerRemote;
import com.moto.exception.*;

import junit.framework.TestCase;


public class JUnitTestingLoginRegistration extends TestCase {
	
	private Object ref;
	private Properties env;	
	private Context jndiContext;
	
	protected MotoManagerRemote mm;


	
	@Test
	public void testUserRegistrationA()  {
		System.out.println("JUnit4: Registering TicketSeller username1 (1st)\n");
		try {
			mm.register("TicketSeller", "username1", "password", "name", "surname", "", "");
		} catch (Exception e )
		{
			fail("Exception: " + e.getClass()  + " : " + e.getMessage());
		}
	}

	
	@Test(expected = UserAlreadyExistsException.class)
	public void testUserRegistrationB() {
		System.out.println("JUnit4: Registering TicketSeller username1 (2nd)\n");
		try {
			mm.register("TicketSeller", "username1", "password", "name", "surname", "", "");
		} catch (Exception e) {	}
	}
		

	
	@Test(expected = UserTypeDoesNotExistException.class)
	public void testUserRegistrationC() {
		System.out.println("JUnit4: Registering UnexpectedMotoUserType username2\n");
		try {
			mm.register("UnexpectedMotoUserType", "username2", "password", "name", "surname", "", "");
		} catch (Exception e) {	}
	}
		
		
	@Test(expected = UserNotFoundException.class)
	public void testUserLoginA() {
		System.out.println("JUnit4: Login NotExistingUser username3\n");
		try {
			mm.login("username", "password");
		} catch (Exception e) {	}
	}
	
	
	@Test
	public void testUserLoginB(){
		System.out.println("Junit4: Login username1\n");
		try {
			mm.login("username1", "password");
		} catch (UserNotFoundException e) {
			fail("Exception: " + e.getClass()  + " : " + e.getMessage());
		}
		
		try {assertNotNull(mm.getLoggedUser());} catch (Exception e) {}
	}

	
	@Test
	public void testUserLogut() {
		System.out.println("Junit4: Logout\n");
		try {
			mm.login("username1", "password");
			assertNotNull(mm.getLoggedUser());
			mm.logout();
			
		} catch (Exception e )
		{
			fail("Received Exception: " + e.getClass()  + " : " + e.getMessage());
		}
	}
	
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
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}

