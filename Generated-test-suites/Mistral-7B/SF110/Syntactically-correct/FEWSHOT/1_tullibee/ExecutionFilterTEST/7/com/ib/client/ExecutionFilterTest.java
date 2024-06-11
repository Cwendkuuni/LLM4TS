package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEqualsWithSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "IDEAL", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");

        assertNotEquals(filter1, filter2);
    }
}