package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        int expectedClientId = 0;
        String expectedAcctCode = null;
        String expectedTime = null;
        String expectedSymbol = null;
        String expectedSecType = null;
        String expectedExchange = null;
        String expectedSide = null;

        ExecutionFilter actualExecutionFilter = executionFilter;

        assertEquals(expectedClientId, actualExecutionFilter.m_clientId);
        assertNull(actualExecutionFilter.m_acctCode);
        assertNull(actualExecutionFilter.m_time);
        assertNull(actualExecutionFilter.m_symbol);
        assertNull(actualExecutionFilter.m_secType);
        assertNull(actualExecutionFilter.m_exchange);
        assertNull(actualExecutionFilter.m_side);
    }

    @Test
    public void testExecutionFilterParameterizedConstructor() {
        int clientId = 123;
        String acctCode = "TEST";
        String time = "2022-05-12 10:30:00";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "SMART";
        String side = "BUY";

        ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

        int expectedClientId = clientId;
        String expectedAcctCode = acctCode;
        String expectedTime = time;
        String expectedSymbol = symbol;
        String expectedSecType = secType;
        String expectedExchange = exchange;
        String expectedSide = side;

        ExecutionFilter actualExecutionFilter = executionFilter;

        assertEquals(expectedClientId, actualExecutionFilter.m_clientId);
        assertEquals(expectedAcctCode, actualExecutionFilter.m_acctCode);
        assertEquals(expectedTime, actualExecutionFilter.m_time);
        assertEquals(expectedSymbol, actualExecutionFilter.m_symbol);
        assertEquals(expectedSecType, actualExecutionFilter.m_secType);
        assertEquals(expectedExchange, actualExecutionFilter.m_exchange);
        assertEquals(expectedSide, actualExecutionFilter.m_side);
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterNullClientId() {
        int clientId = 0;
        String acctCode = "TEST";
        String time = "2022-05-12 10:30:00";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "SMART";
        String side = "BUY";

        ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

        ExecutionFilter nullClientIdExecutionFilter = new ExecutionFilter(null, acctCode, time, symbol, secType, exchange, side);
    }

    // Add more test cases for edge cases and exception handling...
}