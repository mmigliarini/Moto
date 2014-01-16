package com.moto.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private String username;
	private String password; 
	private String name;
	private String surname;
	public enum Type{ USER, TICKETSELLER, WEBCUSTOMER }

	
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	private Set<Reservation> reservations = new HashSet<Reservation>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	private Set<TicketTransaction> ticketTransactions = new HashSet<TicketTransaction>();
	
	
	/**
	 * Constructor #1
	 * 
	 * @param username
	 * @param password
	 * @param name
	 * @param surname
	 */
	public User(String username, String password, String name, String surname) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
		this.setSurname(surname);
	}
	
	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public User() {
		super();
	}  
	
	
	
	/*
	 * setter and getter methods
	 */
	/*
	// id
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	*/
	
	
	// username
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	
 	// password
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	// name
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	
	// surname
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getSurname() {
		return surname;
	}

	
	// Type
	public Type getType(){
		return Type.USER;
	}

	
	// reservations
	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	
	// ticketTransactions
	public void setTicketTransactions(Set<TicketTransaction> ticketTransactions) {
		this.ticketTransactions = ticketTransactions;
	}

	public Set<TicketTransaction> getTicketTransactions() {
		return ticketTransactions;
	}
	
	
	public void setCf(String cf) {
	}

	
	public String getCf() {
		return null;
	}
	
	
}
