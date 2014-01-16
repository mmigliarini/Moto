package com.moto;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.moto.remote.MotoManagerRemote;


public class MotoServletContextListener implements ServletContextListener{ 
	

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		/*******************************************************/
		System.out.println("MotoServletContextListener started");
		/*******************************************************/
		
		Properties env = new Properties();
		env.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		env.setProperty("java.naming.provider.url", "localhost:1099");
		env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");

		try {
			Context jndiContext = new InitialContext();
			Object ref = jndiContext.lookup("MotoManagerRemote");
			
			//MotoManagerRemote mm = (MotoManagerRemote) PortableRemoteObject.narrow(ref, MotoManagerRemote.class);
			MotoManagerRemote mm = (MotoManagerRemote) ref;
			mm.init();
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
			
	}
	
	
	

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		/*********************************************************/
		System.out.println("MotoServletContextListener destroyed");
		/*********************************************************/
	}   
	
	
	
}
