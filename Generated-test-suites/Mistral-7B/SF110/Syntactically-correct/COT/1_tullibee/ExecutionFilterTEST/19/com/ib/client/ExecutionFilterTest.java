package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ExecutionFilter;

/**
 * ExecutionFilterTest.java - JUnit test file for ExecutionFilter class.
 *
 * @author [Your Name]
 */
public class ExecutionFilterTest {

    // Test constructor with valid parameters
    @Test
    public void testExecutionFilter() {
        int clientId = 1;
        String acctCode = "ABC123";
        String time = "2022-01-01 10:30:00";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "SMART";
        String side = "BUY";

        ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

        assertEquals(clientId, filter.getClientId());
        assertEquals(acctCode, filter.getAcctCode());
        assertEquals(time, filter.getTime());
        assertEquals(symbol, filter.getSymbol());
        assertEquals(secType, filter.getSecType());
        assertEquals(exchange, filter.getExchange());
        assertEquals(side, filter.getSide());
    }

    // Test constructor with invalid parameters
    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterWithInvalidParameters() {
        new ExecutionFilter(0, null, "", "", "", "", null);
    }

    // Test equals method with identical objects
    @Test
    public void testEqualsWithIdenticalObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = filter1;

        assertTrue(filter1.equals(filter2));
    }

    // Test equals method with null object
    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertFalse(filter.equals(null));
    }

    // Test equals method with different objects
    @Test
    public void testEqualsWithDifferentObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "DEF456", "2022-01-01 10:30:01", "GOOG", "OPT", "NASDAQ", "SELL");

        assertFalse(filter1.equals(filter2));
    }

    // Test equals method with same data but different object
    @Test
    public void testEqualsWithSameDataDifferentObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter2));
    }

    // Test equals method with same data but different case
    @Test
    public void testEqualsWithSameDataDifferentCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "bUy");

        assertTrue(filter1.equals(filter2));
    }

    // Test equals method with null fields
    @Test
    public void testEqualsWithNullFields() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", null, "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC123", "2022-01-01 10:30:00", null, null, null, null);

        assertTrue(filter1.equals(filter2));
    }
}