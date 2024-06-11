package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "Acct2", "2022-01-01 10:30:01", "IBM", "OPT", "SFE", "SELL");

        Assert.assertEquals(filter1.getClientId(), 1);
        Assert.assertEquals(filter1.getAcctCode(), "Acct1");
        Assert.assertEquals(filter1.getTime(), "2022-01-01 10:30:00");
        Assert.assertEquals(filter1.getSymbol(), "AAPL");
        Assert.assertEquals(filter1.getSecType(), "STK");
        Assert.assertEquals(filter1.getExchange(), "SMART");
        Assert.assertEquals(filter1.getSide(), "BUY");

        Assert.assertNotEquals(filter1, filter3);
        Assert.assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPLe", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "SToK", "SMART", "BUY");

        Assert.assertTrue(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testExecutionFilterNull() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertFalse(filter.equals(null));
    }
}