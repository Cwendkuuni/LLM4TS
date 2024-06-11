package com.ib.client;
import org.junit.Assert;import org.junit.Test;import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testEquals_sameObject() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = filter1;

 Assert.assertTrue("Two identical objects should be equal", filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentObjects_sameValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue("Two objects with same values should be equal", filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentObjects_differentValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account2", "2022-01-02", "GOOG", "opt", "NASDAQ", "SELL");

 Assert.assertFalse("Two objects with different values should not be equal", filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentObjects_null() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse("Object should not be equal to null", filter1.equals(null));
 }

 @Test
 public void testEquals_differentTypes() {
 ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Object notFilter = new Object();

 Assert.assertFalse("Objects of different types should not be equal", filter.equals(notFilter));
 }
}