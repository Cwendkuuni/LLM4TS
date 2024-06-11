package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "account2", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter5 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "GOOG", "STK", "SMART", "BUY");

        Assert.assertTrue(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));
        Assert.assertFalse(filter1.equals(filter4));
        Assert.assertFalse(filter1.equals(filter5));
        Assert.assertFalse(filter1.equals(null));
    }

    @Test
    public void testExecutionFilterNull() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        Assert.assertFalse(filter1.equals(null));
    }

    @Test
    public void testExecutionFilterEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertEquals(0, filter.m_clientId);
    }

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "STK", "SMART", "BUY");
        Assert.assertEquals(1, filter.m_clientId);
        Assert.assertEquals("account1", filter.m_acctCode);
        Assert.assertEquals("2022-01-01 10:10:10", filter.m_time);
        Assert.assertEquals("AAPL", filter.m_symbol);
        Assert.assertEquals("STK", filter.m_secType);
        Assert.assertEquals("SMART", filter.m_exchange);
        Assert.assertEquals("BUY", filter.m_side);
    }
}