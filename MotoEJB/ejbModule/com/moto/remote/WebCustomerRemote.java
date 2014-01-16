package com.moto.remote;

import javax.ejb.Remote;

import com.moto.entity.TicketTransaction;



@Remote
public interface WebCustomerRemote {
	public boolean payTicketTransaction(TicketTransaction t);
}
