package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "AC456", "2022-01-01 10:30:00", "GOOG", "ETF", "NASDAQ", "SELL");

        assertEquals(filter1, filter2); // Testing equality with same parameters
        assertNotEquals(filter1, filter3); // Testing equality with different parameters
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
    public void testExecutionFilterEqualsDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}