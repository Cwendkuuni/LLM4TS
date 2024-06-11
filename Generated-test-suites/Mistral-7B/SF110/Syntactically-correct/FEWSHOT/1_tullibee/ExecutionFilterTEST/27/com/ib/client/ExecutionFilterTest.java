package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstruction() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct1", "2021-12-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acct1", "2021-12-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "Acct2", "2021-12-01 11:00:00", "GOOG", "ETF", "NASDAQ", "SELL");

        Assert.assertEquals(filter1.getClientId(), 1);
        Assert.assertEquals(filter1.getAcctCode(), "Acct1");
        Assert.assertEquals(filter1.getTime(), "2021-12-01 10:30:00");
        Assert.assertEquals(filter1.getSymbol(), "AAPL");
        Assert.assertEquals(filter1.getSecType(), "STK");
        Assert.assertEquals(filter1.getExchange(), "SMART");
        Assert.assertEquals(filter1.getSide(), "BUY");

        Assert.assertTrue(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));

        filter1.setClientId(2);
        filter1.setAcctCode("Acct3");
        filter1.setTime("2022-01-01 09:45:00");
        filter1.setSymbol("MSFT");
        filter1.setSecType("OPT");
        filter1.setExchange("NYSE");
        filter1.setSide("SELL");

        Assert.assertFalse(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));
    }
}