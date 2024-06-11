package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructorWithValidArguments() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        assertNotNull(filter);
        assertEquals(1, filter.getClientId());
        assertEquals("AC123", filter.getAcctCode());
        assertEquals("2022-01-01 12:30:00", filter.getTime());
        assertEquals("AAPL", filter.getSymbol());
        assertEquals("STK", filter.getSecType());
        assertEquals("SMART", filter.getExchange());
        assertEquals("BUY", filter.getSide());
    }

    @Test
    public void testExecutionFilterEqualsWithSameInstance() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = filter1;
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentInstancesSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentInstancesDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "AC456", "2022-01-01 12:30:01", "GOOG", "OPT", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test(expected = ClassCastException.class)
    public void testExecutionFilterEqualsWithDifferentTypeObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 12:30:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter.equals("Test String"));
    }
}