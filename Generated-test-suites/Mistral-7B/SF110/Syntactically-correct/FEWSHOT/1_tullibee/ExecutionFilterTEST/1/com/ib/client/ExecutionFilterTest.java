package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstruction() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "SELL");

        assertEquals(filter1.getClientId(), filter2.getClientId());
        assertEquals(filter1.getAcctCode(), filter2.getAcctCode());
        assertEquals(filter1.getTime(), filter2.getTime());
        assertEquals(filter1.getSymbol(), filter2.getSymbol());
        assertEquals(filter1.getSecType(), filter2.getSecType());
        assertEquals(filter1.getExchange(), filter2.getExchange());
        assertEquals(filter1.getSide(), filter2.getSide());

        ExecutionFilter filter3 = new ExecutionFilter();
        assertNull(filter3.getClientId());
        assertNull(filter3.getAcctCode());
        assertNull(filter3.getTime());
        assertNull(filter3.getSymbol());
        assertNull(filter3.getSecType());
        assertNull(filter3.getExchange());
        assertNull(filter3.getSide());
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "account2", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "SELL");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
        assertFalse(filter1.equals(null));
        assertFalse(filter1.equals(""));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "EQ", "SMART", "BUY");

        int hashCode1 = filter1.hashCode();
        int hashCode2 = filter2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}