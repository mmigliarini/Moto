package com.moto.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class TicketTransaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date datetime;
	
	
	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;

	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "ticketTransaction", fetch=FetchType.EAGER)
	@OrderBy("id ASC") 
	private List<Ticket> tickets = new ArrayList<Ticket>();
	


	/**
	 * Constructor #1:
	 * 
	 * @param datetime
	 * @param user
	 */
	public TicketTransaction(Date datetime, User user) {
		super();
		this.setDatetime(datetime);
		this.setUser(user);
	}

	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public TicketTransaction() {
		super();
	} 
	
	
	//
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	
	//
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Date getDatetime() {
		return datetime;
	}

	
	//
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	
	//
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	
}
