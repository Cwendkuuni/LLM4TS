package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructor() {
     executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
     assertEquals(1, executionFilter.m_clientId);
     assertEquals("ACCT1", executionFilter.m_acctCode);
     assertEquals("2022-01-01 10:30:00", executionFilter.m_time);
     assertEquals("AAPL", executionFilter.m_symbol);
     assertEquals("STK", executionFilter.m_secType);
     assertEquals("SMART_Routing_ID", executionFilter.m_exchange);
     assertEquals("BUY", executionFilter.m_side);
 }

 @Test
 public void testExecutionFilterDefaultConstructor() {
     executionFilter = new ExecutionFilter();
     assertEquals(0, executionFilter.m_clientId);
     assertNull(executionFilter.m_acctCode);
     assertNull(executionFilter.m_time);
     assertNull(executionFilter.m_symbol);
     assertNull(executionFilter.m_secType);
     assertNull(executionFilter.m_exchange);
     assertNull(executionFilter.m_side);
 }

 @Test
 public void testExecutionFilterEquals() {
     ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
     ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
     ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

     assertTrue(filter1.equals(filter1));
     assertTrue(filter1.equals(filter2));
     assertFalse(filter1.equals(filter3));
 }

 @Test
 public void testExecutionFilterEqualsNull() {
     ExecutionFilter filter = new ExecutionFilter();

     assertFalse(filter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsSameInstance() {
     ExecutionFilter filter = new ExecutionFilter();

     assertTrue(filter.equals(filter));
 }

}