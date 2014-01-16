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
import com.moto.exception.UserNotFoundException;


/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		
		String username;
		String password;
		
		RequestDispatcher dispatcher;
		request.getSession().setAttribute("login_error", null);
		
		Properties env = new Properties();
		env.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		env.setProperty("java.naming.provider.url", "localhost:1099");
		env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		
		try {
			Context jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("MotoManagerRemote");
			MotoManagerRemote mm = (MotoManagerRemote) ref;

			
			// LOGIN
			if (request.getParameter("action").equals("login")) {
				
				username = request.getParameter("username").trim();
				password = request.getParameter("password").trim();
				
				if ( username.isEmpty() || password.isEmpty() ) {
					request.getSession().setAttribute("login_error", "EmptyField");
				} else {				
					mm.login(username, password);
					request.getSession().setAttribute("mm", mm);
				}
			
				
			// LOGOUT
			} else if (request.getParameter("action").equals("logout")) {
				
				mm.logout();
				request.getSession().setAttribute("mm", null);
			}

			
		} catch (NamingException e) {
			request.getSession().setAttribute("login_error", e.getMessage());
		} catch (UserNotFoundException e) {
			request.getSession().setAttribute("login_error", "UserNotFound");
		}
		
		dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

}
