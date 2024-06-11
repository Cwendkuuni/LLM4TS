package com.ib.client;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertEquals(0, executionFilter.m_clientId);
        Assert.assertNull(executionFilter.m_acctCode);
        Assert.assertNull(executionFilter.m_time);
        Assert.assertNull(executionFilter.m_symbol);
        Assert.assertNull(executionFilter.m_secType);
        Assert.assertNull(executionFilter.m_exchange);
        Assert.assertNull(executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertEquals(1, executionFilter.m_clientId);
        Assert.assertEquals("ACCT123", executionFilter.m_acctCode);
        Assert.assertEquals("2022-01-01 10:30:00", executionFilter.m_time);
        Assert.assertEquals("AAPL", executionFilter.m_symbol);
        Assert.assertEquals("STK", executionFilter.m_secType);
        Assert.assertEquals("SMART", executionFilter.m_exchange);
        Assert.assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

        Assert.assertTrue(executionFilter1.equals(executionFilter2));

        ExecutionFilter executionFilter3 = new ExecutionFilter(1, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
        Assert.assertFalse(executionFilter1.equals(executionFilter3));

        ExecutionFilter executionFilter4 = new ExecutionFilter(2, "ACCT123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter4));
    }
}