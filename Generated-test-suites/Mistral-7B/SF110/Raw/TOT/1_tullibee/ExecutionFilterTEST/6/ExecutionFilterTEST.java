package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterEquals() {
 // Test case for equal objects
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertTrue(filter1.equals(filter2));

 // Test case for null object
 ExecutionFilter filter3 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter3.equals(null));

 // Test case for different object types
 ExecutionFilter filter4 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 String str = "Test String";
 Assert.assertFalse(filter4.equals(str));

 // Test case for different clientId
 ExecutionFilter filter5 = new ExecutionFilter(2, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter5));

 // Test case for different accountCode
 ExecutionFilter filter6 = new ExecutionFilter(1, "Account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter6));

 // Test case for different time
 ExecutionFilter filter7 = new ExecutionFilter(1, "Account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter7));

 // Test case for different symbol
 ExecutionFilter filter8 = new ExecutionFilter(1, "Account1", "2022-01-01", "GOOG", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter8));

 // Test case for different secType
 ExecutionFilter filter9 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter9));

 // Test case for different exchange
 ExecutionFilter filter10 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
 Assert.assertFalse(filter1.equals(filter10));

 // Test case for different side
 ExecutionFilter filter11 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
 Assert.assertFalse(filter1.equals(filter11));
 }
}