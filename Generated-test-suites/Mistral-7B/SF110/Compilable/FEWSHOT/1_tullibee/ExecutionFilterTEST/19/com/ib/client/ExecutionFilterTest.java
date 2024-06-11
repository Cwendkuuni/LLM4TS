package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertNotEquals(filter, null);
    }

    @Test
    public void testEqualsSameInstance() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(filter, filter);
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account2", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:02", "AAPL", "STK", "SMART", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "GOOG", "STK", "SMART", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "OPT", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "NASDAQ", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "SELL");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");
        assertEquals(filter1, filter2);
    }
}