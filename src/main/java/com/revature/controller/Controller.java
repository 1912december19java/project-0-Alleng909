package com.revature.controller;

import java.util.Scanner;
import com.revature.exception.DatabaseNotFoundException;
import com.revature.exception.InvalidAmountException;
import com.revature.repository.Repository;

public class Controller {
  public Controller() {}

  private static Scanner sc = new Scanner(System.in);

  public void start() {
    try {
      boolean quit = false;
      while (!quit) {
        boolean registered = yn("Are you a registered user? (y/n): ");
        if (registered) {
          quit = login();
        } else {
          quit = register();
        }
      }
    } catch (DatabaseNotFoundException e) {
      System.out.println("Sorry, database is currently inaccessible.");
    }
    // Close out of program and release resources.
    sc.close();
  }

  private boolean yn(String question) { // Gets a yes or no out of user with prompt question.
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

  private boolean login() throws DatabaseNotFoundException {
    String userInput = null;
    while (userInput == null) {
      System.out.println("Please enter your username: ");
      userInput = sc.nextLine();

      if (Repository.doesUserExist(userInput)) {
        String userPassword = null;
        while (userPassword == null) {
          System.out.println("Please enter your password: ");
          userPassword = sc.nextLine();
          if (Repository.checkPassword(userInput, userPassword)) {// TEST IF PASSWORD IS CORRECT
            launch(userInput);
            return true;
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

  private boolean register() throws DatabaseNotFoundException {
    String userInput = null;
    String userPass = null;
    while (userInput == null) {
      System.out.println("Please enter your desired username: ");
      userInput = sc.nextLine();
      if (Repository.doesUserExist(userInput)) {// TEST IF USERNAME EXISTS IN DATABASE***********
        // Account already exists. Choose another one.
        userInput = null;
        System.out.println("That user name already exists.");
      } else {
        System.out.println("Please enter your desired password: ");
        userPass = sc.nextLine();
        Repository.addUser(userInput, userPass);
        System.out.println("Your account has successfully been registered. Returning to login.");
        return false;
      }
    }
    // If successful, ask if the user wants to login by going back to start with a return false;
    // Otherwise quit.
    return true;
  }

  private void launch(String userName) throws DatabaseNotFoundException {
    System.out.println("Hello " + userName + "!");
    String userInput = "";
    while (!userInput.equalsIgnoreCase("quit")) {
      System.out.println(
          "What would you like to do? (balance/withdraw/deposit/transactions/transfer/quit):");

      userInput = sc.nextLine();
      if (userInput.equalsIgnoreCase("balance")) {
        launchBalance(userName);
      } else if (userInput.equalsIgnoreCase("withdraw")) {
        launchWithdraw(userName);
      } else if (userInput.equalsIgnoreCase("deposit")) {
        launchDeposit(userName);
      } else if (userInput.equalsIgnoreCase("transactions")) {
        launchTransactions(userName);
      } else if (userInput.equalsIgnoreCase("transfer")) {
        launchTransfer(userName);
      } else if (!userInput.equalsIgnoreCase("quit")) {
        System.out.println("That was an invalid input.");
      }
    }
  }

  private void launchBalance(String userName) throws DatabaseNotFoundException {
    System.out.println("Your balance is: " + Repository.getBalance(userName));
  }

  private void launchWithdraw(String userName) throws DatabaseNotFoundException {
    System.out.println("Enter the amount you would like to withdraw: ");
    double amount = sc.nextDouble();
    String trash = sc.nextLine(); //Clear newline char.
    try {
      Repository.withdraw(userName, amount);
      System.out.println("Sucessfully withdrew $" + amount);
    } catch (InvalidAmountException e) {
      System.out.println("That was an invalid amount.");
    }
  }

  private void launchDeposit(String userName) throws DatabaseNotFoundException {
    System.out.println("Enter the amount you would like to deposit: ");
    double amount = sc.nextDouble();
    String trash = sc.nextLine(); //Clear newline char.
    try {
      Repository.deposit(userName, amount);
      System.out.println("Successfully deposited $" + amount);
    } catch (InvalidAmountException e) {
      System.out.println("That was an invalid amount of money.");
    }
  }

  private void launchTransactions(String userName) throws DatabaseNotFoundException {
    Repository.printTransactions(userName);
  }

  private void launchTransfer(String userName) throws DatabaseNotFoundException {
    System.out.println("Please enter the name of the user you would like to tranfer to: ");
    String otherAccount = sc.nextLine();
    System.out.println("Please enter amount to transfer: ");
    double amount = sc.nextDouble();
    String trash = sc.nextLine(); //Clear newline char.
    if (Repository.doesUserExist(otherAccount)) {
      try {
        Repository.transfer(userName, otherAccount, amount);
        System.out.println("Successfully transferred $" + amount + " to " + otherAccount + ".");
      } catch (InvalidAmountException e) {
        System.out.println("That was an invalid amount of money.");
      }
    } else {
      System.out.println(otherAccount + " does not exist.");
    }
  }


}
