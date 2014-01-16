package com.moto.remote;

import javax.ejb.Remote;

import com.moto.entity.User;
import com.moto.exception.UserAlreadyExistsException;
import com.moto.exception.UserNotFoundException;
import com.moto.exception.UserTypeDoesNotExistException;

@Remote
public interface AccessManagerRemote {
	
	public User login(String username, String password) throws UserNotFoundException;
	public void logout();
	public void register(String type, String username, String password, String name, String surname, String email, String cf) throws UserAlreadyExistsException, UserTypeDoesNotExistException;

	
}
