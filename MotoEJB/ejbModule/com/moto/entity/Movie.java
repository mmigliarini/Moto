package com.moto.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Movie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	@Lob
	private String description; 
	private int minutes;
	

	@OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	@OrderBy("datetime")
	private List<MovieShow> movieShows = new ArrayList<MovieShow>();
	
	
	/**
	 * Constructor #1
	 * 
	 * @param name
	 * @param description
	 * @param minutes
	 */
	public Movie(String name, String description, int minutes) {
		super();
		this.setName(name);
		this.setDescription(description);
		this.setMinutes(minutes);
	}
	
	/**
	 * Constructor #2: takes no arguments; supercedes the default constructor.
	 */
	public Movie() {
		super();
	}
	

	/*
	 * setter and getter methods
	 */
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getMinutes() {
		return minutes;
	}

	public void setMovieShows(List<MovieShow> movieShows) {
		this.movieShows = movieShows;
	}

	public List<MovieShow> getMovieShows() {
		return movieShows;
	}
	

	
}
