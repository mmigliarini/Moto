package com.moto.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class MovieShow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date datetime;

	
	@ManyToOne	//(cascade = CascadeType.PERSIST)
	@JoinColumn(name="idMovie")
	private Movie movie;
	
	@ManyToOne	//(cascade = CascadeType.PERSIST)
	@JoinColumn(name="idVisionRoom")
	private VisionRoom visionRoom;

	
	@OneToMany(mappedBy = "movieShow", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	private Set<Price> prices = new HashSet<Price>();

	@OneToMany(mappedBy = "movieShow", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)	//cascade = CascadeType.ALL, 
	private Set<Place> places = new HashSet<Place>();
	
	
	
	/**
	 * 
	 * @param datetime
	 */
	public MovieShow(Date datetime, Movie movie, VisionRoom visionRoom) {
		super();
		this.setDatetime(datetime);
		this.setMovie(movie);
		this.setVisionRoom(visionRoom);
	}
	
	/**
	 * Constructor #2: takes no arguments; supercedes the default constructor.
	 */
	public MovieShow() {
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
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public Movie getMovie() {
		return movie;
	}

	
	//
	public void setVisionRoom(VisionRoom visionRoom) {
		this.visionRoom = visionRoom;
	}
	
	public VisionRoom getVisionRoom() {
		return visionRoom;
	}


	//
	public void setPrices(Set<Price> prices) {
		this.prices = prices;
	}

	public Set<Price> getPrices() {
		return prices;
	}

	
	//
	public void setPlaces(Set<Place> places) {
		this.places = places;
	}

	public Set<Place> getPlaces() {
		return places;
	}

}
