package com.moto.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Reservation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date datetime;
	private String code;
	
	
	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;

	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "reservation", fetch=FetchType.EAGER)
	private Set<Place> places = new HashSet<Place>();	
	
	
	
	/**
	 * Constructor #1
	 * 
	 * @param datetime
	 * @param code
	 * @param user
	 */
	public Reservation(Date datetime, String code, User user) {
		super();
		this.setDatetime(datetime);
		this.setCode(code);
		this.setUser(user);
	}

	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public Reservation() {
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
	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	
	//
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	
	//
	public void setPlaces(Set<Place> places) {
		this.places = places;
	}

	public Set<Place> getPlaces() {
		return places;
	}

}
