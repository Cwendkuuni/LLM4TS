package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        assertEquals(1, filter.getm_clientId());
        assertEquals("Account1", filter.getm_acctCode());
        assertEquals("2021-01-01", filter.getm_time());
        assertEquals("AAPL", filter.getm_symbol());
        assertEquals("STK", filter.getm_secType());
        assertEquals("SMART", filter.getm_exchange());
        assertEquals("BUY", filter.getm_side());
    }

    @Test
    public void testEqualsMethod() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "SELL");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testEqualsMethodWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsMethodWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsMethodWithDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account2", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsMethodWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2021-01-02", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsMethodWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL1", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsMethodWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "OPT", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsMethodWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsMethodWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2021-01-01", "AAPL", "STK", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}