package com.revature.repository;

import com.revature.exception.DatabaseNotFoundException;
import com.revature.exception.InvalidAmountException;

public class Repository {
	//Checks database if contains userName.
	public static boolean doesUserExist(String userName) throws DatabaseNotFoundException {
		return true; //IN PROGRESS
	}
	
	//Adds User to password with password as password to database.
	public static void addUser(String userName, String password) throws DatabaseNotFoundException{
		return; //IN PROGRESS
	}
	
	//Checks if password matches userName's password.
	public static boolean checkPassword(String userName, String password) throws DatabaseNotFoundException {
		return true; //In Progress
	}
	
	//Returns balance tied to userName.
	public static double getBalance(String userName) throws DatabaseNotFoundException {
		return 1.0;
	}
	
	//Withdraws money from userName if enough and not negative.
	public static void withdraw(String userName, double amount) throws DatabaseNotFoundException, InvalidAmountException {
		//If enough money and not negative, do thing, otherwise:
		throw new InvalidAmountException();
	}
	
	//Deposits money to userName if not negative.
	public static void deposit(String userName, double amount) throws DatabaseNotFoundException, InvalidAmountException {
		//Check if not negative, do thing, otherwise:
		throw new InvalidAmountException();
	}
	
	//Prints past transactions.
	public static void printTransactions(String userName) throws DatabaseNotFoundException {
		System.out.println("Transaction history:");
	}
	
	public static void transfer(String fromAccount, String toAccount, double amount) throws DatabaseNotFoundException, InvalidAmountException {
		//If fromAccount has enough, give to toAccount, otherwise:
		throw new InvalidAmountException();
	}
	
}
