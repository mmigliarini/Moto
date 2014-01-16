package com.moto.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class VisionRoom implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int numRows;
	private int numCols;
	
	
	@OneToMany(mappedBy = "visionRoom", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	@OrderBy("datetime")
	private List<MovieShow> movieShows = new ArrayList<MovieShow>();
	
	
	
	/**
	 * Constructor #1
	 * 
	 * @param numRows
	 * @param numCols
	 */
	public VisionRoom(int numRows, int numCols) {
		super();
		this.setNumRows(numRows);
		this.setNumCols(numCols);
	}
	
	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public VisionRoom() {
		super();
	}
	

	//
	public int getNumPlaces() {
		return this.getNumRows()*this.getNumCols();
	}
	
	
	//
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	
	//
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	
	//
	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	
	public int getNumCols() {
		return numCols;
	}

	
	//
	public void setMovieShows(List<MovieShow> movieShows) {
		this.movieShows = movieShows;
	}

	public List<MovieShow> getMovieShows() {
		return movieShows;
	}
	

	

}
