package com.moto.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Place implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int idRow;
	private int idCol;
	public enum Status{ AVAILABLE, RESERVED, SOLD }
	private Status status;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="idMovieShow")
	private MovieShow movieShow;

	@ManyToOne(cascade = CascadeType.PERSIST, optional=true)
	@JoinColumn(name="idReservation", nullable=true)
	private Reservation reservation;
	

	@OneToOne(optional=true, mappedBy="place")	//cascade = CascadeType.ALL, 
	private Ticket ticket;
	
	
	/**
	 * Constructor #1
	 * 
	 * @param idRow
	 * @param idCol
	 * @param status
	 * @param reservation
	 * @param movieShow
	 */
	public Place(int idRow, int idCol, Status status, Reservation reservation, MovieShow movieShow) {
		super();
		this.setIdRow(idRow);
		this.setIdCol(idCol);
		this.setStatus(status);
		this.setReservation(reservation);
		this.setMovieShow(movieShow);
	}

	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public Place() {
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
	public void setIdRow(int idRow) {
		this.idRow = idRow;
	}
	
	public int getIdRow() {
		return idRow;
	}

	
	//
	public void setIdCol(int idCol) {
		this.idCol = idCol;
	}
	
	public int getIdCol() {
		return idCol;
	}

	
	//
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}

	
	//
	public void setMovieShow(MovieShow movieShow) {
		this.movieShow = movieShow;
	}
	
	public MovieShow getMovieShow() {
		return movieShow;
	}

	//
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
	public Reservation getReservation() {
		return reservation;
	}

	
	//
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Ticket getTicket() {
		return ticket;
	}

}
