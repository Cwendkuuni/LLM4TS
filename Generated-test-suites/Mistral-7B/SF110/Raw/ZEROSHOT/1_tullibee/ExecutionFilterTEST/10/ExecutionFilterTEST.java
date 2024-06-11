package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter filter1, filter2;

 @Test
 public void testExecutionFilterConstructor() {
 filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 assertEquals(1, filter1.getClientId());
 assertEquals("account1", filter1.getAcctCode());
 assertEquals("2022-01-01 10:30:00", filter1.getTime());
 assertEquals("AAPL", filter1.getSymbol());
 assertEquals("STK", filter1.getSecType());
 assertEquals("SMART_Routing", filter1.getExchange());
 assertEquals("BUY", filter1.getSide());
 }

 @Test
 public void testExecutionFilterEquals() {
 filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 assertTrue(filter1.equals(filter2));

 filter2 = new ExecutionFilter(2, "account2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 assertFalse(filter1.equals(filter2));

 filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "SELL");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterHashCode() {
 filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 assertEquals(filter1.hashCode(), filter2.hashCode());

 filter2 = new ExecutionFilter(2, "account2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 assertNotEquals(filter1.hashCode(), filter2.hashCode());

 filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "SELL");
 assertNotEquals(filter1.hashCode(), filter2.hashCode());
 }

 @Test
 public void testExecutionFilterToString() {
 filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing", "BUY");
 assertEquals("ExecutionFilter [clientId=1, acctCode=account1, time=2022-01-01 10:30:00, symbol=AAPL, secType=STK, exchange=SMART_Routing, side=BUY]", filter1.toString());
 }
}