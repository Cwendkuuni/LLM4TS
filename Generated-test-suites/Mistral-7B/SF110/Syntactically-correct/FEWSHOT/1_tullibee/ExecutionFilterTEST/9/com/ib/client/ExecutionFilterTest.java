package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "SELL");

        assertEquals(filter1.getClientId(), filter2.getClientId());
        assertNotEquals(filter1.getSide(), filter2.getSide());
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

        assertEquals(filter1, filter2);
        assertNotEquals(filter1, filter3);
    }

    @Test
    public void testExecutionFilterEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        assertNotEquals(filter, null);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct2", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:01:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "GOOG", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "OPT", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "SELL");

        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

        int hashCode1 = filter.hashCode();
        filter.setClientId(2);
        int hashCode2 = filter.hashCode();

        assertEquals(hashCode1, filter.hashCode());
        assertNotEquals(hashCode1, hashCode2);
    }
}