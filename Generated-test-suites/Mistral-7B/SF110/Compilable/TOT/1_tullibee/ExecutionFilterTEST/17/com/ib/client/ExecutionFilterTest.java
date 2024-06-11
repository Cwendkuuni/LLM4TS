package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testEquals_sameInstance() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = filter1;

 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEquals_sameFields() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentClientId() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentAccountCode() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentTime() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentSymbol() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "GOOG", "STK", "SMART", "BUY");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentSecType() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentExchange() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentSide() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_null() {
 ExecutionFilter filter = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertFalse(filter.equals(null));
 }

 @Test
 public void testHashCode_sameInstance() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = filter1;

 assertEquals(filter1.hashCode(), filter2.hashCode());
 }

 @Test
 public void testHashCode_sameFields() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertEquals(filter1.hashCode(), filter2.hashCode());
 }

 @Test
 public void testHashCode_differentFields() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");

 assertNotEquals(filter1.hashCode(), filter2.hashCode());
 }
}