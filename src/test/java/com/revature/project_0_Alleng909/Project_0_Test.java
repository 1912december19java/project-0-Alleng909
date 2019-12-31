package com.revature.project_0_Alleng909;

import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.revature.exception.DatabaseNotFoundException;
import com.revature.exception.InvalidAmountException;
import com.revature.repository.Repository;

public class Project_0_Test {
  private static Repository repo;
  private static String userName = "Tester"; // Already exists in database with $500 (Try not to
                                             // change it)
  private static String userPass = "Tester";
  
  private static Logger testLog = Logger.getLogger(Repository.class); 
  private static Connection testConn;
  
  static {
    try {
      testConn = DriverManager.getConnection(System.getenv("comstring"), System.getenv("username"), System.getenv("password"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  @Before
	public void setup() throws DatabaseNotFoundException, InvalidAmountException {
	  Repository.setStaticsForTest(testLog, testConn);
		//SETUP
	}

  @After
  public void teardown() {
    // TEARDOWN
  }

  // Testing negative and other inappropriate values.

  @Test(expected = InvalidAmountException.class)
  public void depositANegativeException() throws DatabaseNotFoundException, InvalidAmountException {
    Repository.deposit(userName, -1.0);
  }

  @Test(expected = InvalidAmountException.class)
  public void withdrawANegativeException()
      throws DatabaseNotFoundException, InvalidAmountException {
    Repository.withdraw(userName, -1.0);
  }

  @Test(expected = InvalidAmountException.class) // Take too much from an account.
  public void withdrawTooMuchException() throws DatabaseNotFoundException, InvalidAmountException {
    Repository.withdraw(userName, 999.99);
  }

  // Testing if logging in works.

  @Test
  public void checkUserExists() throws DatabaseNotFoundException {
    assertTrue(Repository.doesUserExist(userName));
  }

  @Test
  public void checkUserLogin() throws DatabaseNotFoundException {
    assertTrue(Repository.checkPassword(userName, userPass));
  }
}
