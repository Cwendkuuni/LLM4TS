package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilter_DefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertEquals(0, filter.getClientId());
        Assert.assertNull(filter.getAcctCode());
        Assert.assertNull(filter.getTime());
        Assert.assertNull(filter.getSymbol());
        Assert.assertNull(filter.getSecType());
        Assert.assertNull(filter.getExchange());
        Assert.assertNull(filter.getSide());
    }

    @Test
    public void testExecutionFilter_ParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        Assert.assertEquals(1, filter.getClientId());
        Assert.assertEquals("AC123", filter.getAcctCode());
        Assert.assertEquals("2022-01-01 10:30:45", filter.getTime());
        Assert.assertEquals("IBM", filter.getSymbol());
        Assert.assertEquals("STK", filter.getSecType());
        Assert.assertEquals("SMART", filter.getExchange());
        Assert.assertEquals("BUY", filter.getSide());
    }

    @Test
    public void testEquals_DifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "AC456", "2022-01-02 11:30:00", "AAPL", "OPT", "NASDAQ", "SELL");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_SameObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = filter1;
        Assert.assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentFields() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "SELL");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_SameFields_DifferentId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_IgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:45", "IBM", "STK", "SMART", "sell");
        Assert.assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_Null() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertFalse(filter.equals(null));
    }
}