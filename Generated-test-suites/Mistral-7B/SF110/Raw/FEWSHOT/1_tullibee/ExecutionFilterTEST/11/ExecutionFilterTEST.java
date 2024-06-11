package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "acct2", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(2, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
    }

    @Test
    public void testEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct2", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "GOOG", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "OPT", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "IDEALPRO", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "SELL");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }
}