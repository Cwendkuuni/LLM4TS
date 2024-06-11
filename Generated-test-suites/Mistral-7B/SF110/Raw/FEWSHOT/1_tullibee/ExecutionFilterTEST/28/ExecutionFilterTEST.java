package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testExecutionFilterNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT2", "2022-01-01", "GOOG", "OPT", "NASDAQ", "SELL");

        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "APPLE", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEmptyString() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "", "", "", "", "", "");
        ExecutionFilter filter2 = new ExecutionFilter(1, "", "", "", "", "", "");

        assertTrue(filter1.equals(filter2));
    }
}