package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructorWithAllArguments() {
        ExecutionFilter filter = new ExecutionFilter(1, "Acct123", "2022-01-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
        assertNotNull(filter);
        assertEquals(1, filter.m_clientId);
        assertEquals("Acct123", filter.m_acctCode);
        assertEquals("2022-01-01 12:30:45", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("SMART", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertNotNull(filter);
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct123", "2022-01-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct123", "2022-01-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "Acct123", "2022-01-01 12:30:45", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "Acct123", "2022-01-01 12:30:45", "AAPL", "STK", "SMART", "SELL");
        ExecutionFilter filter5 = new ExecutionFilter();

        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
        assertFalse(filter1.equals(filter5));
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }
}