package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterDefaultConstructor() {
 ExecutionFilter executionFilter = new ExecutionFilter();
 assertNotNull(executionFilter);
 assertTrue(executionFilter.m_clientId == 0);
 assertNull(executionFilter.m_acctCode);
 assertNull(executionFilter.m_time);
 assertNull(executionFilter.m_symbol);
 assertNull(executionFilter.m_secType);
 assertNull(executionFilter.m_exchange);
 assertNull(executionFilter.m_side);
 }

 @Test
 public void testExecutionFilterConstructedWithValidArguments() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
 assertNotNull(executionFilter);
 assertFalse(executionFilter.m_clientId == 0);
 assertEquals("Acct123", executionFilter.m_acctCode);
 assertEquals("2022-01-01 10:30:00", executionFilter.m_time);
 assertEquals("IBM", executionFilter.m_symbol);
 assertEquals("STK", executionFilter.m_secType);
 assertEquals("IDEALPRO", executionFilter.m_exchange);
 assertEquals("BUY", executionFilter.m_side);
 }

 @Test
 public void testExecutionFilterEquals() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
 ExecutionFilter executionFilter3 = new ExecutionFilter(1, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "SELL");
 ExecutionFilter executionFilter4 = new ExecutionFilter(2, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");

 assertTrue(executionFilter1.equals(executionFilter2));
 assertFalse(executionFilter1.equals(executionFilter3));
 assertFalse(executionFilter1.equals(executionFilter4));
 }

 @Test
 public void testExecutionFilterNull() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
 assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testExecutionFilterDifferentClass() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "Acct123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
 Object otherObj = new Object();
 assertFalse(executionFilter.equals(otherObj));
 }
}