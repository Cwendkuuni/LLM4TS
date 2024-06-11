package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {

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

    @Test
    public void testExecutionFilterEqualsDifferentInstanceWithSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstanceWithDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acct2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstanceWithNullValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, null, null, null, null, null);
        ExecutionFilter filter2 = new ExecutionFilter(1, null, null, null, null, null, null);
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstanceWithEmptyValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "", "", "", "", "", "");
        ExecutionFilter filter2 = new ExecutionFilter(1, "", "", "", "", "", "");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstanceWithDifferentDataTypes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        Object other = new Object();
        assertFalse(filter1.equals(other));
    }
}