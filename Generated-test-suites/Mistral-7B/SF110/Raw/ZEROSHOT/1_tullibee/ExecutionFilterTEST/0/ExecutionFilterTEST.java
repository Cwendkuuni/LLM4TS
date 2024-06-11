package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter executionFilter1;
 ExecutionFilter executionFilter2;

 @Test
 public void testConstructorWithAllArguments() {
 executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 assertTrue(executionFilter1.equals(expectedExecutionFilter));
 }

 @Test
 public void testConstructorWithDefaultArguments() {
 executionFilter1 = new ExecutionFilter();
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter();
 assertTrue(executionFilter1.equals(expectedExecutionFilter));
 }

 @Test
 public void testEquals() {
 executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 executionFilter2 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 assertTrue(executionFilter1.equals(executionFilter2));

 executionFilter2 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Sell");
 assertFalse(executionFilter1.equals(executionFilter2));

 executionFilter2 = new ExecutionFilter(2, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testHashCode() {
 executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 int hashCode1 = executionFilter1.hashCode();

 executionFilter1.setClientId(2);
 int hashCode2 = executionFilter1.hashCode();

 executionFilter2 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 int hashCode3 = executionFilter2.hashCode();

 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 int expectedHashCode = expectedExecutionFilter.hashCode();

 assertEquals(expectedHashCode, hashCode1);
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "SMART", "Buy");
 String expectedToString = "ExecutionFilter{m_clientId=1, m_acctCode='Acct1', m_time='2022-01-01', m_symbol='IBM', m_secType='STK', m_exchange='SMART', m_side='Buy'}";
 assertEquals(expectedToString, executionFilter1.toString());
 }
}