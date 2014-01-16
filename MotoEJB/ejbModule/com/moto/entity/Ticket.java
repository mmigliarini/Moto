package com.moto.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Ticket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;


	@OneToOne(cascade = CascadeType.PERSIST, optional=false)
	@JoinColumn(name="idPlace")
	private Place place;
	

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="idTicketTransaction")
	private TicketTransaction ticketTransaction;
	
	@ManyToOne
	@JoinColumn(name="idPrice")
	private Price price;
	
	
	/**
	 * Constructor #1
	 * 
	 * @param place
	 * @param ticketTransaction
	 * @param price
	 */
	public Ticket(Place place, TicketTransaction ticketTransaction, Price price) {
		place.setStatus(Place.Status.SOLD);
		this.setPlace(place);
		this.setPrice(price);
		this.setTicketTransaction(ticketTransaction);
	}



	/**
	 * Constructor #2
	 * 
	 * @param place
	 * @param ticketTransaction
	 */
	public Ticket(Place place, TicketTransaction ticketTransaction) {
		this.setPlace(place);
		this.setTicketTransaction(ticketTransaction);
	}
	
	
	/**
	 * Constructor #3: takes no arguments; supersedes the default constructor.
	 */
	public Ticket() {
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
	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}
	
	
	//
	public void setTicketTransaction(TicketTransaction ticketTransaction) {
		this.ticketTransaction = ticketTransaction;
	}

	public TicketTransaction getTicketTransaction() {
		return ticketTransaction;
	}
	
	
	//
	public void setPrice(Price price) {
		this.price = price;
	}

	public Price getPrice() {
		return price;
	}








}
