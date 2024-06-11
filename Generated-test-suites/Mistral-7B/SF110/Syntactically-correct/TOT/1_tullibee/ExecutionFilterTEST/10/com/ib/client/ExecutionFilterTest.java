package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstruction() {
        ExecutionFilter filter = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("account1", filter.m_acctCode);
        assertEquals("2022-01-01 10:30:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("SMART", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEqualsMethod() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
        ExecutionFilter filter4 = new ExecutionFilter(2, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
    }

    @Test
    public void testExecutionFilterEdgeCases() {
        // Test null values
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = null;
        assertFalse(filter1.equals(filter2));

        // Test empty strings
        ExecutionFilter filter3 = new ExecutionFilter(1, "", "", "", "", "", "");
        ExecutionFilter filter4 = new ExecutionFilter(1, "", "2022-01-01 10:30:00", "", "", "", "");
        ExecutionFilter filter5 = new ExecutionFilter(1, "2022-01-01 10:30:00", "", "", "", "", "");
        ExecutionFilter filter6 = new ExecutionFilter(1, "2022-01-01 10:30:00", "AAPL", "", "", "", "");
        ExecutionFilter filter7 = new ExecutionFilter(1, "2022-01-01 10:30:00", "AAPL", "STK", "", "", "");
        ExecutionFilter filter8 = new ExecutionFilter(1, "2022-01-01 10:30:00", "AAPL", "STK", "SMART", null, "");
        ExecutionFilter filter9 = new ExecutionFilter(1, "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "", "");

        assertFalse(filter1.equals(filter2));
        assertFalse(filter3.equals(filter4));
        assertFalse(filter5.equals(filter6));
        assertFalse(filter6.equals(filter7));
        assertFalse(filter7.equals(filter8));
        assertFalse(filter8.equals(filter9));
    }
}