package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "testAccount2", "2022-01-01 10:10:11", "GOOG", "OPT", "NASDAQ", "SELL");

        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "APPLE", "EQUITY", "SMAART", "SELL");

        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertNull(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY")));
    }

    @Test
    public void testExecutionFilterSameConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter2));
    }
}