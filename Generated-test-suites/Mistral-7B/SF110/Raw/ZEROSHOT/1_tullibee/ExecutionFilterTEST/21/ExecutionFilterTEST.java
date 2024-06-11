package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        assertNotNull(executionFilter);
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("AC123", executionFilter.m_acctCode);
        assertEquals("2022-01-01 12:30:00", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STK", executionFilter.m_secType);
        assertEquals("SMART", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter4 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "SELL");

        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(null));
        assertFalse(executionFilter1.equals(executionFilter3));
        assertFalse(executionFilter1.equals(executionFilter4));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter4 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "SELL");

        assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
        assertNotSame(executionFilter1.hashCode(), executionFilter3.hashCode());
        assertNotSame(executionFilter1.hashCode(), executionFilter4.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterNullInput() {
        new ExecutionFilter(null, null, null, null, null, null, null);
    }
}