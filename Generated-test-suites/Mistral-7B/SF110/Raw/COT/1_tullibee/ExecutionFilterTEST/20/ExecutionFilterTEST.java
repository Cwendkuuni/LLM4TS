package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertEquals(1, executionFilter.getm_clientId());
        assertEquals("testAccount", executionFilter.getm_acctCode());
        assertEquals("2022-01-01 10:30:00", executionFilter.getm_time());
        assertEquals("AAPL", executionFilter.getm_symbol());
        assertEquals("STK", executionFilter.getm_secType());
        assertEquals("SMART", executionFilter.getm_exchange());
        assertEquals("BUY", executionFilter.getm_side());
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter4 = new ExecutionFilter(2, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
        assertFalse(executionFilter1.equals(executionFilter4));
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterEqualsNullObject() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertFalse(executionFilter.equals(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentClientIds() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
        executionFilter2.setm_clientId(1);
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentAccounts() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentTimes() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentSymbols() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentSecTypes() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentExchanges() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentSides() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
        assertFalse(executionFilter1.equals(executionFilter2));
    }
}