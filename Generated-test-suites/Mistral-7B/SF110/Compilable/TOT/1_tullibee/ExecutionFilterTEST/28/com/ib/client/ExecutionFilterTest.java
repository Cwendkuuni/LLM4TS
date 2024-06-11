package com.ib.client;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testConstructorWithValidArguments() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter anotherFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");

        // Test equals method with the same filter instance
        assertTrue(filter.equals(filter));

        // Test equals method with another instance with the same values
        assertTrue(filter.equals(anotherFilter));
    }

    @Test
    public void testConstructorWithNullAcctCode() {
        ExecutionFilter filter = new ExecutionFilter(1, null, "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        assertFalse(filter.equals(new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY")));
    }

    @Test
    public void testConstructorWithEmptySymbol() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "", "STK", "SMART", "BUY");
        assertFalse(filter.equals(new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY")));
    }

    @Test
    public void testConstructorWithNullSecType() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", null, "SMART", "BUY");
        assertFalse(filter.equals(new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY")));
    }

    @Test
    public void testConstructorWithNullExchange() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", null, "BUY");
        assertFalse(filter.equals(new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY")));
    }

    @Test
    public void testConstructorWithNullSide() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", null);
        assertFalse(filter.equals(new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY")));
    }

    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClientIds() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentAcctCodes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC456", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentTimes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:31:00", "IBM", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSymbols() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "IBM", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "AAPL", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSecTypes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "IBM", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "IBM", "OPT", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentExchanges() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "IBM", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "NASDAQ", "IBM", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSides() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "IBM", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "IBM", "IBM", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}