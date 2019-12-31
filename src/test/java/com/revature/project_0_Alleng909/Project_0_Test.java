package com.revature.project_0_Alleng909;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.exception.DatabaseNotFoundException;
import com.revature.exception.InvalidAmountException;
import com.revature.repository.Repository;

public class Project_0_Test {
	private static Repository repo;
	private static String userName;
	private static String otherName;
	private static String userPass;
	private static String otherPass;
	
	@Before
	public void setup() throws DatabaseNotFoundException, InvalidAmountException {
		//SETUP
		userName = "Adam";
		otherName = "King";
		userPass = "password";
		otherPass = "PaSsWoRd";
		Repository.addUser(userName, userPass);
		Repository.addUser(otherName, otherPass);
		
		Repository.deposit(userName, 50.0);
	}
	
	@After
	public void teardown() {
		//TEARDOWN
		userName = null;
	}
	
	
	@Test(expected = InvalidAmountException.class)
	public void breakingStringThrowsException2() throws DatabaseNotFoundException, InvalidAmountException {
		Repository.deposit(userName, -1.0);
	}
	
	@Test(expected = InvalidAmountException.class) //Take too much from an account.
	public void overtake() throws DatabaseNotFoundException, InvalidAmountException {
		Repository.withdraw(userName, 999.99);
	}
}
