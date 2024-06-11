package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertNotNull(filter);
        assertEquals(1, filter.m_clientId);
        assertEquals("AC123", filter.m_acctCode);
        assertEquals("2022-01-01 10:30:00", filter.m_time);
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
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = filter1;
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "AC456", "2022-01-02 11:30:00", "GOOG", "OPT", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentButEqualValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsIgnoringCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ac123", "2022-01-01 10:30:00", "AAPL", "stk", "SMART", "buy");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentButSameExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "NSX", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentButSameSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        int hashCode1 = filter1.hashCode();
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        int hashCode2 = filter2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}