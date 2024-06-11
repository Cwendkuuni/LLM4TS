package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "Account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(2, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
        assertFalse(filter1.equals(null));
        assertFalse(filter1.equals(""));
    }

    @Test
    public void testExecutionFilterNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertNull(filter.getM_acctCode());
        assertEquals(0, filter.getM_clientId());
        assertNull(filter.getM_time());
        assertNull(filter.getM_symbol());
        assertNull(filter.getM_secType());
        assertNull(filter.getM_exchange());
        assertNull(filter.getM_side());
    }

    @Test
    public void testExecutionFilterGettersAndSetters() {
        ExecutionFilter filter = new ExecutionFilter();

        filter.setM_clientId(1);
        filter.setM_acctCode("Account1");
        filter.setM_time("2022-01-01");
        filter.setM_symbol("AAPL");
        filter.setM_secType("STK");
        filter.setM_exchange("SMART");
        filter.setM_side("BUY");

        assertEquals(1, filter.getM_clientId());
        assertEquals("Account1", filter.getM_acctCode());
        assertEquals("2022-01-01", filter.getM_time());
        assertEquals("AAPL", filter.getM_symbol());
        assertEquals("STK", filter.getM_secType());
        assertEquals("SMART", filter.getM_exchange());
        assertEquals("BUY", filter.getM_side());
    }
}