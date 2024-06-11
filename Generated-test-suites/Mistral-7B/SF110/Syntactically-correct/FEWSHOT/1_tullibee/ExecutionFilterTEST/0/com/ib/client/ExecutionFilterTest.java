package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "Acct2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
        ExecutionFilter filter5 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");

        Assert.assertTrue(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));
        Assert.assertFalse(filter1.equals(filter4));
        Assert.assertFalse(filter1.equals(filter5));
        Assert.assertFalse(filter1.equals(null));
    }

    @Test
    public void testEqualsWithIgnoredCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "aApL", "stk", "SMart", "bUy");

        Assert.assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertEquals(0, filter.m_clientId);
    }

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "Acct1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertEquals(1, filter.m_clientId);
        Assert.assertEquals("Acct1", filter.m_acctCode);
        Assert.assertEquals("2022-01-01 10:30:00", filter.m_time);
        Assert.assertEquals("AAPL", filter.m_symbol);
        Assert.assertEquals("STK", filter.m_secType);
        Assert.assertEquals("SMART", filter.m_exchange);
        Assert.assertEquals("BUY", filter.m_side);
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectPassedToEqualsMethod() {
        ExecutionFilter filter = new ExecutionFilter();
        filter.equals(null);
    }
}