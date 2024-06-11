package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter5 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter6 = null;

        Assert.assertTrue(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));
        Assert.assertFalse(filter1.equals(filter4));
        Assert.assertFalse(filter1.equals(filter5));
        Assert.assertFalse(filter1.equals(filter6));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");

        int hashCode1 = filter1.hashCode();
        int hashCode2 = filter2.hashCode();
        int hashCode3 = filter3.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
        Assert.neq(hashCode1, hashCode3);
    }

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STCK", "SMART", "BUY");

        Assert.assertEquals(1, filter.m_clientId);
        Assert.assertEquals("account1", filter.m_acctCode);
        Assert.assertEquals("2022-01-01 10:30:00", filter.m_time);
        Assert.assertEquals("AAPL", filter.m_symbol);
        Assert.assertEquals("STCK", filter.m_secType);
        Assert.assertEquals("SMART", filter.m_exchange);
        Assert.assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();

        Assert.assertEquals(0, filter.m_clientId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterNullArguments() {
        new ExecutionFilter(null, null, null, null, null, null, null);
    }
}