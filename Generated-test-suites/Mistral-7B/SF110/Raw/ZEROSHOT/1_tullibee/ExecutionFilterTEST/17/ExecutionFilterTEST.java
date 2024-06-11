package com.ib.client;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Before
 public void setup() {
 executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 }

 @Test
 public void testConstructor() {
 ExecutionFilter executionFilter3 = new ExecutionFilter();
 ExecutionFilter executionFilter4 = new ExecutionFilter(2, "account2", "2022-01-02", "GOOG", "ETF", "NASDAQ", "SELL");

 assertNotSame(executionFilter1, executionFilter3);
 assertNotSame(executionFilter1, executionFilter4);
 assertSame(executionFilter1, executionFilter1);
 }

 @Test
 public void testEquals() {
 assertTrue(executionFilter1.equals(executionFilter2));
 assertFalse(executionFilter1.equals(null));
 assertFalse(executionFilter1.equals("wrongType"));

 executionFilter2.setClientId(2);
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setClientId(1);
 executionFilter2.setAcctCode("account2");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setAcctCode("account1");
 executionFilter2.setTime("2022-01-02");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setTime("2022-01-01");
 executionFilter2.setSymbol("AAPL");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setSymbol("AAPL");
 executionFilter2.setSecType("ETF");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setSecType("STK");
 executionFilter2.setExchange("NASDAQ");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setExchange("SMART");
 executionFilter2.setSide("SELL");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2.setSide("BUY");
 assertTrue(executionFilter1.equals(executionFilter2));
 }

}