package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");

        ExecutionFilter filter4 = new ExecutionFilter();

        assertEquals(1, filter1.getm_clientId());
        assertEquals("ACCT1", filter1.getm_acctCode());
        assertEquals("2022-01-01", filter1.getm_time());
        assertEquals("AAPL", filter1.getm_symbol());
        assertEquals("STK", filter1.getm_secType());
        assertEquals("SMART", filter1.getm_exchange());
        assertEquals("BUY", filter1.getm_side());

        assertEquals(1, filter2.getm_clientId());
        assertEquals("ACCT1", filter2.getm_acctCode());
        assertEquals("2022-01-01", filter2.getm_time());
        assertEquals("AAPL", filter2.getm_symbol());
        assertEquals("STK", filter2.getm_secType());
        assertEquals("SMART", filter2.getm_exchange());
        assertEquals("BUY", filter2.getm_side());

        assertNotEquals(filter1, filter3);
        assertNotEquals(filter1, filter4);
        assertNotEquals(filter2, filter3);
        assertNotEquals(filter2, filter4);
        assertNotEquals(filter3, filter4);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");

        ExecutionFilter filter4 = new ExecutionFilter();

        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertEquals(filter1, filter5);
        assertEquals(filter1, filter1);
        assertNotEquals(filter1, filter2);
        assertNotEquals(filter1, filter3);
        assertNotEquals(filter1, filter4);
    }

    @Test
    public void testGetters() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertEquals(1, filter.getm_clientId());
        assertEquals("ACCT1", filter.getm_acctCode());
        assertEquals("2022-01-01", filter.getm_time());
        assertEquals("AAPL", filter.getm_symbol());
        assertEquals("STK", filter.getm_secType());
        assertEquals("SMART", filter.getm_exchange());
        assertEquals("BUY", filter.getm_side());
    }
}