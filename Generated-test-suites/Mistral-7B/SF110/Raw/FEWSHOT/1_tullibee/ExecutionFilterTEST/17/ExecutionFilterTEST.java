package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");

        assertEquals(1, filter1.m_clientId);
        assertEquals("ACCT1", filter1.m_acctCode);
        assertEquals("2022-01-01 10:30:00", filter1.m_time);
        assertEquals("AAPL", filter1.m_symbol);
        assertEquals("STK", filter1.m_secType);
        assertEquals("SMART", filter1.m_exchange);
        assertEquals("BUY", filter1.m_side);

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsSameInstance() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstances() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");

        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");

        assertFalse(filter1.equals(filter2));

        filter2.m_time = "2022-01-01 10:30:00";
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterNullFields() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(null, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertFalse(filter1.equals(filter2));

        filter1.m_clientId = 1;
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEmptyFields() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();

        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }
}