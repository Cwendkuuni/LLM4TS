package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsSelf() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "GOOG", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "OPC", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testHashCode() {
        ExecutionFilter filter = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        int hashCode = filter.hashCode();
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        int hashCode2 = filter2.hashCode();
        assertEquals(hashCode, hashCode2);
    }
}