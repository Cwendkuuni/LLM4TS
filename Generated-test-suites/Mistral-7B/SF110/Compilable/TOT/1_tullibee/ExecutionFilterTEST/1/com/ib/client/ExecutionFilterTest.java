package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testConstructorAndEquals() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter3 = new ExecutionFilter(2, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter4 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "GOOG", "OPT", "NASDAQ", "SELL");

 // Test equals with same object
 assertTrue(filter1.equals(filter1));

 // Test equals with null object
 assertFalse(filter1.equals(null));

 // Test equals with this object
 assertTrue(filter1.equals(filter1));

 // Test equals with same values
 assertTrue(filter1.equals(filter2));

 // Test equals with different clientId
 assertFalse(filter1.equals(filter3));

 // Test equals with different accountCode
 assertFalse(filter1.equals(new ExecutionFilter(1, "johndoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY")));

 // Test equals with different time
 assertFalse(filter1.equals(new ExecutionFilter(1, "JohnDoe", "2022-01-02 10:30:00", "AAPL", "STK", "SMART", "BUY")));

 // Test equals with different symbol
 assertFalse(filter1.equals(new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY")));

 // Test equals with different secType
 assertFalse(filter1.equals(new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY")));

 // Test equals with different exchange
 assertFalse(filter1.equals(new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "NASDAQ", "BUY")));

 // Test equals with different side
 assertFalse(filter1.equals(new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL")));

 // Test equals with different objects having same values
 ExecutionFilter filter5 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertTrue(filter1.equals(filter5));

 // Test equals with different objects having different values
 ExecutionFilter filter6 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "GOOG", "OPT", "NASDAQ", "SELL");
 assertFalse(filter1.equals(filter6));
 }
}