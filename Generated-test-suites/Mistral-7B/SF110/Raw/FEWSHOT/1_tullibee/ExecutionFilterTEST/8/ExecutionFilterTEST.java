package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "acct2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");

        // Test equality with same instance
        assertTrue(filter1.equals(filter1));

        // Test equality with equal values
        assertTrue(filter1.equals(filter2));

        // Test inequality with different values
        assertFalse(filter1.equals(filter3));

        // Test inequality with null
        assertFalse(filter1.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        // Test equality ignoring case
        assertTrue(filter1.equalsIgnoreCase(filter2));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        // Test that equal objects have same hash code
        assertTrue(filter1.hashCode() == filter2.hashCode());
    }

    @Test
    public void testExecutionFilterNull() {
        ExecutionFilter filter = null;
        assertFalse(filter.equals(new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY")));
        assertFalse(new ExecutionFilter(1, "acct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY").equals(filter));
        assertNull(filter.hashCode());
    }
}