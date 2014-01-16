package com.moto.entity;

import javax.persistence.*;

@Entity
public class TicketSeller extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor #1
	 * 
	 * @param username
	 * @param password
	 * @param name
	 * @param surname
	 */
	public TicketSeller(String username, String password, String name, String surname){
		super(username, password, name, surname);
	}
	

	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public TicketSeller() {
		super();
	}  
	
	@Override
	public Type getType(){
		return Type.TICKETSELLER;
	}
	
}
