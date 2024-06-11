package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "ACCT2", "2022-01-01 10:00:00", "GOOG", "ETF", "NASDAQ", "SELL");

        Assert.assertEquals(executionFilter1.getClientId(), 1);
        Assert.assertEquals(executionFilter1.getAcctCode(), "ACCT1");
        Assert.assertEquals(executionFilter1.getTime(), "2022-01-01 10:00:00");
        Assert.assertEquals(executionFilter1.getSymbol(), "AAPL");
        Assert.assertEquals(executionFilter1.getSecType(), "STK");
        Assert.assertEquals(executionFilter1.getExchange(), "SMART");
        Assert.assertEquals(executionFilter1.getSide(), "BUY");

        Assert.assertTrue(executionFilter1.equals(executionFilter2));
        Assert.assertFalse(executionFilter1.equals(executionFilter3));
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertFalse(executionFilter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsSameObject() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertTrue(executionFilter.equals(executionFilter));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentClientId() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentAcctCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT2", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentTime() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:01:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentSymbol() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "GOOG", "STK", "SMART", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentSecType() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "OPT", "SMART", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentExchange() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "NASDAQ", "BUY");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectDifferentSide() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "SELL");
        Assert.assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectSameFieldsDifferentCaseAcctCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertTrue(executionFilter1.equalsIgnoreCase(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectSameFieldsDifferentCaseExchange() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertTrue(executionFilter1.equalsIgnoreCase(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectSameFieldsDifferentCaseSymbol() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "aAPL", "STK", "SMART", "BUY");
        Assert.assertTrue(executionFilter1.equalsIgnoreCase(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectSameFieldsDifferentCaseSecType() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "sTk", "SMART", "BUY");
        Assert.assertTrue(executionFilter1.equalsIgnoreCase(executionFilter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjectSameFieldsDifferentCaseSide() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "sEll");
        Assert.assertTrue(executionFilter1.equalsIgnoreCase(executionFilter2));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
        Assert.assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
    }
}