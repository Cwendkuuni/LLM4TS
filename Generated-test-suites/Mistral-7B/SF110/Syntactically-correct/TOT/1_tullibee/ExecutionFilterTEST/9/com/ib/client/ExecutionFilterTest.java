package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterEquals() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter filter3 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
 ExecutionFilter filter4 = new ExecutionFilter(2, "JohnDoe", "2022-01-01", "IBM", "STK", "SMART", "BUY");

 // Test equals with same instance
 Assert.assertTrue(filter1.equals(filter1));

 // Test equals with same values
 Assert.assertTrue(filter1.equals(filter2));

 // Test equals with null
 Assert.assertFalse(filter1.equals(null));

 // Test equals with different instance and same values
 Assert.assertTrue(filter1.equals(filter2));

 // Test equals with different values
 Assert.assertFalse(filter1.equals(filter3));

 // Test equals with different clientId
 Assert.assertFalse(filter1.equals(filter4));
 }

 @Test
 public void testExecutionFilterNullValues() {
 ExecutionFilter filter1 = new ExecutionFilter();
 ExecutionFilter filter2 = new ExecutionFilter(null, null, null, null, null, null, null);

 // Test equals with null filter
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEmptyStrings() {
 ExecutionFilter filter1 = new ExecutionFilter("", "", "", "", "", "", "");
 ExecutionFilter filter2 = new ExecutionFilter(1, "", "", "", "", "", "");

 // Test equals with empty filter
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterMixedValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-02", "IBM", "STK", "SMART", "SELL");

 // Test not equals
 Assert.assertFalse(filter1.equals(filter2));
 }
}