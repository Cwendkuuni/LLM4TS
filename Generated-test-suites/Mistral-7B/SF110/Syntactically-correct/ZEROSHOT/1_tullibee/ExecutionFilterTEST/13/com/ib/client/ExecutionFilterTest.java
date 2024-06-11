package com.ib.client;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Before
 public void setUp() {
 executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 }

 @Test
 public void testEqualsObjectsNull() {
 ExecutionFilter nullFilter = null;
 assertFalse(executionFilter1.equals(nullFilter));
 }

 @Test
 public void testEqualsObjectsSelf() {
 assertTrue(executionFilter1.equals(executionFilter1));
 }

 @Test
 public void testEqualsObjectsDifferentValues() {
 ExecutionFilter differentFilter = new ExecutionFilter(2, "account2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");
 assertFalse(executionFilter1.equals(differentFilter));
 }

 @Test
 public void testEqualsObjectsDifferentClientId() {
 ExecutionFilter differentClientIdFilter = new ExecutionFilter(3, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertFalse(executionFilter1.equals(differentClientIdFilter));
 }

 @Test
 public void testEqualsObjectsDifferentAccountCode() {
 ExecutionFilter differentAccountCodeFilter = new ExecutionFilter(1, "account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertFalse(executionFilter1.equals(differentAccountCodeFilter));
 }

 @Test
 public void testEqualsObjectsDifferentTime() {
 ExecutionFilter differentTimeFilter = new ExecutionFilter(1, "account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
 assertFalse(executionFilter1.equals(differentTimeFilter));
 }

 @Test
 public void testEqualsObjectsDifferentSymbol() {
 ExecutionFilter differentSymbolFilter = new ExecutionFilter(1, "account1", "2022-01-01", "MSFT", "STK", "SMART", "BUY");
 assertFalse(executionFilter1.equals(differentSymbolFilter));
 }

 @Test
 public void testEqualsObjectsDifferentSecType() {
 ExecutionFilter differentSecTypeFilter = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");
 assertFalse(executionFilter1.equals(differentSecTypeFilter));
 }

 @Test
 public void testEqualsObjectsDifferentExchange() {
 ExecutionFilter differentExchangeFilter = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
 assertFalse(executionFilter1.equals(differentExchangeFilter));
 }

 @Test
 public void testEqualsObjectsDifferentSide() {
 ExecutionFilter differentSideFilter = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
 assertFalse(executionFilter1.equals(differentSideFilter));
 }

 @Test
 public void testEqualsObjectsSameValues() {
 ExecutionFilter sameValuesFilter = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertTrue(executionFilter1.equals(sameValuesFilter));
 }

}