package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter objExecutionFilter1, objExecutionFilter2;

 @Test
 public void testExecutionFilterConstructor() {
 ExecutionFilter objExecutionFilter = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 assertNotNull(objExecutionFilter);
 }

 @Test
 public void testExecutionFilterEquals() {
 objExecutionFilter1 = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 objExecutionFilter2 = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 assertTrue(objExecutionFilter1.equals(objExecutionFilter2));

 objExecutionFilter2 = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "SELL");
 assertFalse(objExecutionFilter1.equals(objExecutionFilter2));

 objExecutionFilter2 = new ExecutionFilter(2, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 assertFalse(objExecutionFilter1.equals(objExecutionFilter2));
 }

 @Test
 public void testExecutionFilterHashCode() {
 objExecutionFilter1 = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 objExecutionFilter2 = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 assertEquals(objExecutionFilter1.hashCode(), objExecutionFilter2.hashCode());

 objExecutionFilter2 = new ExecutionFilter(1, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "SELL");
 assertNotSame(objExecutionFilter1.hashCode(), objExecutionFilter2.hashCode());

 objExecutionFilter2 = new ExecutionFilter(2, "AC123", "2022-03-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
 assertNotSame(objExecutionFilter1.hashCode(), objExecutionFilter2.hashCode());
 }

 @Test
 public void testExecutionFilterDefaultConstructor() {
 ExecutionFilter objExecutionFilter = new ExecutionFilter();
 assertNotNull(objExecutionFilter);
 }
}