package com.revature.controller;

import java.util.Scanner;

public class Controller {
	public Controller() {}

	private static Scanner sc = new Scanner(System.in);
	
	public void start() {
		boolean quit = false;
		while (!quit) {
			boolean registered = yn("Are you a registered user? (y/n): ");
			if (registered) {
				quit = login();
			} else {
				quit = register();
			}
		}
		
		//Close out of program and release resources.
		sc.close();
	}
	
	private boolean yn(String question) { //Gets a yes or no out of user with prompt question.
		String userInput = null;
		while (userInput == null) {
			System.out.println(question);
			userInput = sc.nextLine();
			if (userInput.equalsIgnoreCase("y")) {
				return true;
			} else if (userInput.equalsIgnoreCase("n")) {
				return false;
			} else {
				userInput = null;
				System.out.println("That was an invalid input.");
			}
		}
		return true;
	}

	private boolean login() {
		String userInput = null;
		while (userInput == null) {
			System.out.println("Please enter your username: ");
			userInput = sc.nextLine();
			boolean doesUserExistInDatabase = true;
			if (doesUserExistInDatabase) {//TEST IF USERNAME EXISTS IN DATABASE***********
				//Ask for password and if correct, go to account.
				String userPassword = null;
				while (userPassword == null) {
					System.out.println("Please enter your password: ");
					userPassword = sc.nextLine();
					if (false) {//TEST IF PASSWORD IS CORRECT*****************
						//Go to account stuffs.
					} else {
						System.out.println("That was incorrect.");
						userPassword = null;
					}
				}
				
			} else {
				userInput = null;
				System.out.println("That username does not exist.");
				boolean wantToRegister = yn("Would you like go back and register instead? (y/n)");
				if (wantToRegister) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean register() {
		String userInput = null;
		while (userInput == null) {
			System.out.println("Please enter your desired username: ");
			userInput = sc.nextLine();
			boolean doesUserExistInDatabase = true;
			if (doesUserExistInDatabase) {//TEST IF USERNAME EXISTS IN DATABASE***********
				//Account already exists. CHoose another one.
				
			} else {
				//Ask for password.
			}
		}
		System.out.println("Please enter your desired user name: ");
		//If successful, ask if the user wants to login by going back to start with a return false; Otherwise quit.
	return true;
	}

}
