package com.moto.entity;

import javax.persistence.*;

@Entity
@SecondaryTable(name="Webcustomer",pkJoinColumns=@PrimaryKeyJoinColumn(name="idUser"))
public class WebCustomer extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Column(name="email", table="Webcustomer")
	private String email;
	@Column(name="cf", table="Webcustomer")
	private String cf; 
	
	/**
	 * Constructor #1
	 * 
	 * @param username
	 * @param password
	 * @param name
	 * @param surname
	 * @param email
	 * @param cf
	 */
	public WebCustomer(String username, String password, String name, String surname, String email, String cf){
		super(username, password, name, surname);
		this.setEmail(email);
		this.setCf(cf);
	}

	/**
	 * Constructor #2: takes no arguments; supersedes the default constructor.
	 */
	public WebCustomer() {
		super();
	}  
	
	
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
	
	@Override
	public void setCf(String cf) {
		this.cf = cf;
	}

	@Override
	public String getCf() {
		return cf;
	}

	
	@Override
	public Type getType(){
		return Type.WEBCUSTOMER;
	}
	
}
