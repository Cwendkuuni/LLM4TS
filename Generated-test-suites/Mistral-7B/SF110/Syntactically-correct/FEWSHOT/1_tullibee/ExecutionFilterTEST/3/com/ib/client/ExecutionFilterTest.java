package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "Account2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");

        Assert.assertEquals(filter1.getClientId(), 1);
        Assert.assertEquals(filter1.getAcctCode(), "Account1");
        Assert.assertEquals(filter1.getTime(), "2022-01-01");
        Assert.assertEquals(filter1.getSymbol(), "AAPL");
        Assert.assertEquals(filter1.getSecType(), "STCK");
        Assert.assertEquals(filter1.getExchange(), "SMART");
        Assert.assertEquals(filter1.getSide(), "BUY");

        Assert.assertEquals(filter1, filter2);
        Assert.assertNotEquals(filter1, filter3);
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertTrue(filter.equals(filter));
    }

    @Test
    public void testExecutionFilterEqualsDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Account2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account2", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-02", "AAPL", "STCK", "SMART", "BUY");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "GOOG", "STCK", "SMART", "BUY");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "NASDAQ", "BUY");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "SELL");

        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsSameValuesIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "bUy");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STCK", "SMART", "BUY");

        Assert.assertEquals(filter1, filter2);
    }
}