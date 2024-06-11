package com.ib.client.test;

import org.junit.Assert;
import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructorWithValidArguments() {
        int clientId = 1;
        String acctCode = "ABC123";
        String time = "2022-12-31 15:30:00";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "SMART";
        String side = "BUY";

        ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

        Assert.assertEquals(clientId, executionFilter.m_clientId);
        Assert.assertEquals(acctCode, executionFilter.m_acctCode);
        Assert.assertEquals(time, executionFilter.m_time);
        Assert.assertEquals(symbol, executionFilter.m_symbol);
        Assert.assertEquals(secType, executionFilter.m_secType);
        Assert.assertEquals(exchange, executionFilter.m_exchange);
        Assert.assertEquals(side, executionFilter.m_side);
    }

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

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterEqualsWithNullObject() {
        ExecutionFilter executionFilter = new ExecutionFilter();

        executionFilter.equals(null);
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentObjects() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "XYZ456", "2022-12-31 15:30:01", "GOOG", "OPT", "NASDAQ", "SELL");

        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsWithSameObjects() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = executionFilter1;

        Assert.assertTrue(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsWithSameValuesButDifferentObjects() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", "STK", "SMART", "BUY");

        Assert.assertTrue(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullOrEmptySymbol() {
        String symbol = null;
        // or
        String symbol = "";

        new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", symbol, "STK", "SMART", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullOrEmptySecType() {
        String secType = null;
        // or
        String secType = "";

        new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", secType, "SMART", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullOrEmptyExchange() {
        String exchange = null;
        // or
        String exchange = "";

        new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", "STK", exchange, "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullOrEmptySide() {
        String side = null;
        // or
        String side = "";

        new ExecutionFilter(1, "ABC123", "2022-12-31 15:30:00", "AAPL", "STK", "SMART", side);
    }

}