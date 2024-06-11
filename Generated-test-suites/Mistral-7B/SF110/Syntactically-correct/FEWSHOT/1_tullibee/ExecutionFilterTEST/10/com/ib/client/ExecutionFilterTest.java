package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

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
    public void testExecutionFilterCustomConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertNotNull(filter);
        assertEquals(1, filter.m_clientId);
        assertEquals("AC123", filter.m_acctCode);
        assertEquals("2022-10-01 12:34:56", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("SMART", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectsSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectsDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "GOOG", "OPT", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectsSameValuesDifferentCases() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-10-01 12:34:56", "aApL", "StK", "sMARt", "bUy");
        assertTrue(filter1.equals(filter2));
    }
}