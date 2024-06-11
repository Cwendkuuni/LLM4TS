package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2023-03-31 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertEquals(1, filter.getClientId());
        assertEquals("TestAccount", filter.getAcctCode());
        assertEquals("2023-03-31 10:30:00", filter.getTime());
        assertEquals("AAPL", filter.getSymbol());
        assertEquals("STK", filter.getSecType());
        assertEquals("SMART", filter.getExchange());
        assertEquals("BUY", filter.getSide());
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2023-03-31 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount", "2023-03-31 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));

        filter2 = new ExecutionFilter(1, "TestAccount", "2023-03-31 10:30:01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));

        filter2 = new ExecutionFilter(2, "TestAccount", "2023-03-31 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterInvalidConstructorParameters() {
        ExecutionFilter filter = new ExecutionFilter();
        try {
            new ExecutionFilter(-1, null, null, null, null, null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testExecutionFilterGetters() {
        ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2023-03-31 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertEquals(1, filter.getClientId());
        assertEquals("TestAccount", filter.getAcctCode());
        assertEquals("2023-03-31 10:30:00", filter.getTime());
        assertEquals("AAPL", filter.getSymbol());
        assertEquals("STK", filter.getSecType());
        assertEquals("SMART", filter.getExchange());
        assertEquals("BUY", filter.getSide());

        filter.setClientId(2);
        assertEquals(2, filter.getClientId());

        filter.setAcctCode("NewAccount");
        assertEquals("NewAccount", filter.getAcctCode());

        filter.setTime("2023-03-31 10:30:01");
        assertEquals("2023-03-31 10:30:01", filter.getTime());

        filter.setSymbol("GOOG");
        assertEquals("GOOG", filter.getSymbol());

        filter.setSecType("OPT");
        assertEquals("OPT", filter.getSecType());

        filter.setExchange("IDEALPRO");
        assertEquals("IDEALPRO", filter.getExchange());

        filter.setSide("SELL");
        assertEquals("SELL", filter.getSide());
    }
}