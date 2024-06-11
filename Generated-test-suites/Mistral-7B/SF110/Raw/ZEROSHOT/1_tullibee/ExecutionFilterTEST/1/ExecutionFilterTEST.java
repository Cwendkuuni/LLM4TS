package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter filter1;
 ExecutionFilter filter2;

 @Test
 public void testExecutionFilterDefaultConstructor() {
 filter1 = new ExecutionFilter();
 assertEquals(0, filter1.getClientId());
 assertNull(filter1.getAcctCode());
 assertNull(filter1.getTime());
 assertNull(filter1.getSymbol());
 assertNull(filter1.getSecType());
 assertNull(filter1.getExchange());
 assertNull(filter1.getSide());
 }

 @Test
 public void testExecutionFilterCustomConstructor() {
 filter1 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", "AAPL", "STK", "SMART_Routing", "BUY");
 filter2 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", "AAPL", "STK", "SMART_Routing", "BUY");
 assertTrue(filter1.equals(filter2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterCustomConstructorWithNullAcctCode() {
 filter1 = new ExecutionFilter(1, null, "2022-12-12 13:30:01", "AAPL", "STK", "SMART_Routing", "BUY");
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterCustomConstructorWithNullTime() {
 filter1 = new ExecutionFilter(1, "account1", null, "AAPL", "STK", "SMART_Routing", "BUY");
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterCustomConstructorWithNullSymbol() {
 filter1 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", null, "STK", "SMART_Routing", "BUY");
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterCustomConstructorWithNullSecType() {
 filter1 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", "AAPL", null, "SMART_Routing", "BUY");
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterCustomConstructorWithNullExchange() {
 filter1 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", "AAPL", "STK", null, "BUY");
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterCustomConstructorWithNullSide() {
 filter1 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", "AAPL", "STK", "SMART_Routing", null);
 fail("Exception expected: IllegalArgumentException");
 }

 @Test
 public void testExecutionFilterCustomConstructorWithDifferentValues() {
 filter1 = new ExecutionFilter(1, "account1", "2022-12-12 13:30:01", "AAPL", "STK", "SMART_Routing", "BUY");
 filter2 = new ExecutionFilter(1, "account2", "2022-12-12 13:30:02", "IBM", "OPT", "NASDAQ", "SELL");
 assertFalse(filter1.equals(filter2));
 }

}