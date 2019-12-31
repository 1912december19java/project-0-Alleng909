package com.revature.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.print.attribute.standard.MediaSize.Other;
import org.apache.log4j.Logger;
import com.revature.exception.DatabaseNotFoundException;
import com.revature.exception.InvalidAmountException;

public class Repository {
  
  private static Logger log = Logger.getLogger(Repository.class); 

  private static Connection conn;
  
  static {
    try {
      conn = DriverManager.getConnection(System.getenv("comstring"), System.getenv("username"), System.getenv("password"));
      log.info("Connected to Database");
    } catch (SQLException e) {
      log.error("Failed to connect to database", e);
    }
  }
  
  public static void setStaticsForTest(Logger l, Connection c) {
    log = l;
    conn = c;
  }
  
  // Checks database if contains userName.
  public static boolean doesUserExist(String userName) throws DatabaseNotFoundException {
    try {
      PreparedStatement ps = conn.prepareStatement("SELECT COUNT(username) FROM bank WHERE username = ?");
      ps.setString(1, userName);
      ps.execute();
      ResultSet rs = ps.getResultSet();
      rs.next();
      //GET COUNT OUT OF RS, if not 0, then user exists.
      int usersWithUserName = rs.getInt(1); //TEST IF WORKS
      if (usersWithUserName > 0) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      log.error("SQLException at doesUserExist with userName " + userName);
      throw new DatabaseNotFoundException();
    }
  }

  // Adds User to password with password as password to database.
  public static void addUser(String userName, String password) throws DatabaseNotFoundException {
    //INSERT username and password with 0 balance.
    PreparedStatement ps;
    try {
      ps = conn.prepareStatement("INSERT INTO bank VALUES (?, ?, 0)");
      ps.setString(1, userName);
      ps.setString(2, password);
      ps.execute();
    } catch (SQLException e) {
      log.error("SQLException at addUser with userName" + userName);
      throw new DatabaseNotFoundException();
    }
    return;
  }

  // Checks if password matches userName's password.
  public static boolean checkPassword(String userName, String password) throws DatabaseNotFoundException {
    try {
      PreparedStatement ps = conn.prepareStatement("SELECT COUNT(username) FROM bank WHERE username = ? AND pass = ?");
      ps.setString(1, userName);
      ps.setString(2, password);
      ps.execute();
      ResultSet rs = ps.getResultSet();
      rs.next();
      //GET COUNT OUT OF RS, if not 0, then user exists.
      int usersWithUserName = rs.getInt(1); //TEST IF WORKS
      if (usersWithUserName > 0) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      log.error("SQLException at checkPassword with userName " + userName);
      throw new DatabaseNotFoundException();
    }
  }

  // Returns balance tied to userName.
  public static double getBalance(String userName) throws DatabaseNotFoundException {
    try {
      PreparedStatement ps = conn.prepareStatement("SELECT balance FROM bank WHERE username = ?");
      ps.setString(1, userName);
      ps.execute();
      ResultSet rs = ps.getResultSet();
      rs.next();
      return rs.getInt(1);
    } catch (SQLException e) {
      log.error("SQLException at getBalance with userName: " + userName);
      throw new DatabaseNotFoundException();
    }
  }

  // Withdraws money from userName if enough and not negative.
  public static void withdraw(String userName, double amount)
      throws DatabaseNotFoundException, InvalidAmountException {
    double bal = getBalance(userName);
    if (amount < 0 || amount > bal) { //Negative or withdraw more than the account has.
      log.warn("User tried to withdraw " + amount);
      throw new InvalidAmountException(); 
    }
    setBalance(userName, bal - amount);
    addTransaction(userName, -amount);
  }

  // Deposits money to userName if not negative.
  public static void deposit(String userName, double amount)
      throws DatabaseNotFoundException, InvalidAmountException {
    double bal = getBalance(userName);
    if (amount < 0) { //Negative
      log.warn("User tried to deposit " + amount);
      throw new InvalidAmountException(); 
    }
    setBalance(userName, bal + amount);
    addTransaction(userName, amount);
  }
  
  //Helper function to add transactions
  private static void addTransaction(String userName, double amount) throws DatabaseNotFoundException {
    try {
      PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions VALUES (?, ?, DEFAULT)");
      ps.setString(1, userName);
      ps.setDouble(2, amount);
      ps.execute();
    } catch (SQLException e) {
      log.error("Failed to add transaction userName: " + userName + " with amount: " + amount);
      throw new DatabaseNotFoundException();
    }
  }
  
  // Helper function to set new balance of user (Doesn't check values)
  private static void setBalance(String userName, double newBalance) throws DatabaseNotFoundException {
    try {
      PreparedStatement ps = conn.prepareStatement("UPDATE bank SET balance = ? WHERE username = ?");
      ps.setDouble(1, newBalance);
      ps.setString(2, userName);
      ps.execute();
    } catch (SQLException e) {
      log.error("Failed to update balance with userName " + userName + " and amount: " + newBalance);
      throw new DatabaseNotFoundException();
    }
  }
  
  // Prints past transactions.
  public static void printTransactions(String userName) throws DatabaseNotFoundException {
    try {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions WHERE transaction_username = ?");
      //TEST
      System.out.println("Setting transaction");
      ps.setString(1, userName);
      if (ps.execute()) {
        //TEST
        System.out.println("Executed.");
        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
          System.out.println(rs.getDate("transaction_time") + "   " + rs.getDouble("transaction_amount"));
        }
      }
    } catch (SQLException e) {
      log.error("SQLException at printTransactions with userName: " + userName);
      throw new DatabaseNotFoundException();
    }
  }

  public static void transfer(String fromAccount, String toAccount, double amount)
      throws DatabaseNotFoundException, InvalidAmountException {
    double bal = getBalance(fromAccount);
    if (amount < 0 || amount > bal) { //Negative or withdraw more than the account has.
      log.warn("User tried to transfer " + amount);
      throw new InvalidAmountException(); 
    }
    log.info("Beginning transfer of $" + amount + " from " + fromAccount + " to " + toAccount + ".");
    withdraw(fromAccount, amount);
    deposit(toAccount, amount);
    log.info("Finished transfer of $" + amount + " from " + fromAccount + " to " + toAccount + ".");
  }

}
