package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilter() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "2021-10-01", "MSFT", "ETF", "ISLAND", "SELL");

        assertEquals(filter1.getClientId(), 1);
        assertEquals(filter1.getAcctCode(), "ACCT1");
        assertEquals(filter1.getTime(), "2021-10-01");
        assertEquals(filter1.getSymbol(), "AAPL");
        assertEquals(filter1.getSecType(), "STK");
        assertEquals(filter1.getExchange(), "SMART");
        assertEquals(filter1.getSide(), "BUY");

        assertEquals(filter1, filter2);
        assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(filter.getClientId(), 0);
        assertNull(filter.getAcctCode());
        assertNull(filter.getTime());
        assertNull(filter.getSymbol());
        assertNull(filter.getSecType());
        assertNull(filter.getExchange());
        assertNull(filter.getSide());
    }

    @Test
    public void testEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-02", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "MSFT", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "OPT", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "ISLAND", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-10-01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
    }
}