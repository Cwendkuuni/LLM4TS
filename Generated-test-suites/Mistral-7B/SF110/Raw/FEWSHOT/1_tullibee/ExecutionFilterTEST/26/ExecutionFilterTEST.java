package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilter() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "acct2", "2022-01-01 10:30:00", "GOOG", "OPT", "NASDAQ", "SELL");

        assertEquals(filter1.m_clientId, 1);
        assertEquals(filter1.m_acctCode, "acct1");
        assertEquals(filter1.m_time, "2022-01-01 10:30:00");
        assertEquals(filter1.m_symbol, "AAPL");
        assertEquals(filter1.m_secType, "STK");
        assertEquals(filter1.m_exchange, "SMART");
        assertEquals(filter1.m_side, "BUY");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));

        ExecutionFilter filter4 = new ExecutionFilter();
        assertFalse(filter1.equals(filter4));
    }

    @Test
    public void testEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(filter.m_clientId, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters() {
        new ExecutionFilter(0, null, null, null, null, null, null);
    }
}