package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Test
 public void testExecutionFilterConstruction() {
     executionFilter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
     assertNotNull(executionFilter1);
 }

 @Test
 public void testExecutionFilterEqualsNull() {
     assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testExecutionFilterSelfEquals() {
     assertTrue(executionFilter1.equals(executionFilter1));
 }

 @Test
 public void testExecutionFilterEqualsSameValues() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
     assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentValues() {
     executionFilter2 = new ExecutionFilter(2, "ACCT456", "2022-01-01 11:30:00", "MSFT", "OPT", "NASDAQ", "SELL");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentClientId() {
     executionFilter2 = new ExecutionFilter(2, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentAccountCode() {
     executionFilter2 = new ExecutionFilter(1, "ACCT456", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentTime() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 11:30:00", "AAPL", "STK", "SMART", "BUY");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentSymbol() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentSecurityType() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentExchange() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "NASDAQ", "BUY");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterDifferentSide() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
     assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterIgnoreCaseEqualsAccountCode() {
     executionFilter2 = new ExecutionFilter(1, "acct123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
     assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterIgnoreCaseEqualsExchange() {
     executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "aAPL", "STK", "SMART", "BUY");
     assertTrue(executionFilter1.equals(executionFilter2));
 }
}