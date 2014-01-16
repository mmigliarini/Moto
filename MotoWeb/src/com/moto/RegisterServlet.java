package com.moto;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.moto.remote.MotoManagerRemote;
import com.moto.exception.UserAlreadyExistsException;
import com.moto.exception.UserTypeDoesNotExistException;


/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
		
			
		RequestDispatcher dispatcher;
		request.getSession().setAttribute("register_error", null);
		
		Properties env = new Properties();
		env.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		env.setProperty("java.naming.provider.url", "localhost:1099");
		env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		
		try {
			Context jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("MotoManagerRemote");
			MotoManagerRemote mm = (MotoManagerRemote) ref;

			// retrive user data
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String name	 	= request.getParameter("name");
			String surname  = request.getParameter("surname");
			String type	 	= request.getParameter("type");
			String email	= request.getParameter("email");
			String cf		= request.getParameter("cf");			
			
			int error = 0;
			
			if(type!=null){
			
				// WEB CUSTOMER
				if (type=="WebCustomer"){
					
					// check for NullPointer
					if(username != null && password != null && name != null && surname != null && email != null && cf != null) {
						
						// check for Null value
						if(username.isEmpty() || username.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || cf.isEmpty()) error = 1;
							
					} else {
						error = 1;
					}
				
				// TICKETSELLER	
				} else {
					
					// check for NullPointer
					if(username != null && password != null && name != null && surname != null) {
						
						// check for Null value
						if(username.isEmpty() || username.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()) error = 1;
							
					} else {
						error = 1;
					}
					
				}

			} else {
				error = 1;
			}
	
			// check for existing errors
			if(error==1){
				request.getSession().setAttribute("register_error", "EmptyField");
				dispatcher = request.getRequestDispatcher("/registrazione.jsp");			
			} else {
;					// registering
				mm.register(type, username, password, name, surname, email, cf);
				request.getSession().setAttribute("mm", mm);
				dispatcher = request.getRequestDispatcher("/index.jsp");
			}

			
		} catch (NamingException e) {
			request.getSession().setAttribute("register_error", e.getMessage());
			dispatcher = request.getRequestDispatcher("/registrazione.jsp");
			
		} catch (UserAlreadyExistsException e) {
			request.getSession().setAttribute("register_error", "UserAlreadyExists");
			dispatcher = request.getRequestDispatcher("/registrazione.jsp");
			
		} catch (UserTypeDoesNotExistException e) {
			request.getSession().setAttribute("register_error", "UserTypeDoesNotExistException");
			dispatcher = request.getRequestDispatcher("/registrazione.jsp");
		}
		
		
		dispatcher.forward(request, response);
	}

}
