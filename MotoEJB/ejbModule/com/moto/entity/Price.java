package com.moto.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Price implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String rateName;
	private float rateAmmount;
	private boolean isDefault = false;
	
	
	@ManyToOne
	@JoinColumn(name="idMovieShow")
	private MovieShow movieShow;

	
	/**
	 * Constructor #1
	 * 
	 * @param rateName
	 * @param rateAmmount
	 * @param isDefault
	 * @param movieShow
	 */
	public Price(String rateName, float rateAmmount, boolean isDefault, MovieShow movieShow) {
		super();
		this.setRateName(rateName);
		this.setRateAmmount(rateAmmount);
		this.setIsDefault(isDefault);
		this.setMovieShow(movieShow);
	}
	
	
	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public Price() {
		super();
	}
	
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateAmmount(float rateAmmount) {
		this.rateAmmount = rateAmmount;
	}

	public float getRateAmmount() {
		return rateAmmount;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean getIsDefault() {
		return isDefault;
	}


	public void setMovieShow(MovieShow movieShow) {
		this.movieShow = movieShow;
	}


	public MovieShow getMovieShow() {
		return movieShow;
	}

	
}
