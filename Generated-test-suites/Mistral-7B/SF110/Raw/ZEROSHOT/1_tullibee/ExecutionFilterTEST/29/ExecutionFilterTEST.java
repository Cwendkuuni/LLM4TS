package com.ib.client;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Before
 public void setUp() {
 executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol1", "Stock", "IBKR", "Buy");
 executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol1", "Stock", "IBKR", "Buy");
 }

 @Test
 public void testEquals() {
 ExecutionFilter executionFilter3 = new ExecutionFilter(1, "ACCT2", "2022-01-01", "Symbol1", "Stock", "IBKR", "Buy");
 ExecutionFilter executionFilter4 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol2", "Stock", "IBKR", "Buy");
 ExecutionFilter executionFilter5 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol1", "Stock", "IBKR", "Sell");

 assertTrue(executionFilter1.equals(executionFilter2));
 assertFalse(executionFilter1.equals(executionFilter3));
 assertFalse(executionFilter1.equals(executionFilter4));
 assertFalse(executionFilter1.equals(executionFilter5));
 }

 @Test
 public void testEmptyConstructor() {
 ExecutionFilter filter = new ExecutionFilter();
 assertEquals(0, filter.m_clientId);
 assertNull(filter.m_acctCode);
 assertNull(filter.m_time);
 assertNull(filter.m_symbol);
 assertNull(filter.m_secType);
 assertNull(filter.m_exchange);
 assertNull(filter.m_side);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullClientId() {
 ExecutionFilter filter = new ExecutionFilter(null, "ACCT1", "2022-01-01", "Symbol1", "Stock", "IBKR", "Buy");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullAccountCode() {
 ExecutionFilter filter = new ExecutionFilter(1, null, "2022-01-01", "Symbol1", "Stock", "IBKR", "Buy");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullTime() {
 ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", null, "Symbol1", "Stock", "IBKR", "Buy");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSymbol() {
 ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01", null, "Stock", "IBKR", "Buy");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSecType() {
 ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol1", null, "IBKR", "Buy");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullExchange() {
 ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol1", "Stock", null, "Buy");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSide() {
 ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01", "Symbol1", "Stock", "IBKR", null);
 fail("IllegalArgumentException expected but not thrown");
 }
}