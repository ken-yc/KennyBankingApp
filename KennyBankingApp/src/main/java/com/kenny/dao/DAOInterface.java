package com.kenny.dao;

import java.util.List;

public interface DAOInterface<E> {
	
// CREATE METHODS
	// insert data into DB. Generic E should be overridden with User or Account objects 
	public int insert(E obj);
	
//READ METHODS
	// run a query to find all rows of E (User or Account) and store in a List
	public List<E> findAll();
	
	// run a query to find a DB entry that matches a certain id. Returns object of that type.
	public E findById(int id);
	
//UPDATE METHODS
	public int update(E obj);
	
//DELETE METHODS
	public int delete(int id);
}
