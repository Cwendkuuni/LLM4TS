package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter filter1;
 ExecutionFilter filter2;

 @Test
 public void testConstructorWithNullArguments() {
 filter1 = new ExecutionFilter();
 assertNotNull(filter1);
 }

 @Test
 public void testConstructorWithValidArguments() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertNotNull(filter1);
 }

 @Test
 public void testEqualsWithNullObject() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = null;
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithSelf() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assert True(filter1.equals(filter1));
 }

 @Test
 public void testEqualsWithDifferentClientId() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(2, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentAccountCode() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentTime() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentSymbol() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "GOOG", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentSecType() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentExchange() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentSide() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithSameValues() {
 filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertTrue(filter1.equals(filter2));
 }
}