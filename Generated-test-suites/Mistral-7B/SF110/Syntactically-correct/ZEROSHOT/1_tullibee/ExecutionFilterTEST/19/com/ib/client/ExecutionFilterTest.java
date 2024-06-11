package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.getClientId());
        assertNull(executionFilter.getAcctCode());
        assertNull(executionFilter.getTime());
        assertNull(executionFilter.getSymbol());
        assertNull(executionFilter.getSecType());
        assertNull(executionFilter.getExchange());
        assertNull(executionFilter.getSide());
    }

    @Test
    public void testExecutionFilterConstructor() {
        String acctCode = "TEST_ACCT_CODE";
        String time = "2022-01-01 12:12:12";
        String symbol = "TEST_SYMBOL";
        String secType = "TEST_SEC_TYPE";
        String exchange = "TEST_EXCHANGE";
        String side = "TestSide";

        int clientId = 123;

        ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

        assertEquals(clientId, executionFilter.getClientId());
        assertEquals(acctCode, executionFilter.getAcctCode());
        assertEquals(time, executionFilter.getTime());
        assertEquals(symbol, executionFilter.getSymbol());
        assertEquals(secType, executionFilter.getSecType());
        assertEquals(exchange, executionFilter.getExchange());
        assertEquals(side, executionFilter.getSide());
    }

    @Test
    public void testEqualsNull() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertFalse(executionFilter.equals(null));
    }

    @Test
    public void testEqualsSelf() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertTrue(executionFilter.equals(executionFilter));
    }

    @Test
    public void testEqualsDifferentObjects() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:12:12", "SYMBOL1", "SEC_TYPE1", "EXCHANGE1", "SIDE1");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT2", "2022-01-02 12:12:12", "SYMBOL2", "SEC_TYPE2", "EXCHANGE2", "SIDE2");

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsSameValues() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:12:12", "SYMBOL1", "SEC_TYPE1", "EXCHANGE1", "SIDE1");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:12:12", "SYMBOL1", "SEC_TYPE1", "EXCHANGE1", "SIDE1");

        assertTrue(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsDifferentValuesButSameReference() {
        String acctCode = "TEST_ACCT_CODE";
        String time = "2022-01-01 12:12:12";
        String symbol = "TEST_SYMBOL";
        String secType = "TEST_SEC_TYPE";
        String exchange = "TEST_EXCHANGE";
        String side = "TestSide";

        ExecutionFilter executionFilter1 = new ExecutionFilter(1, acctCode, time, symbol, secType, exchange, side);
        ExecutionFilter executionFilter2 = executionFilter1;

        assertTrue(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testNotEqualsDifferentObjectsButSameValues() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:12:12", "SYMBOL1", "SEC_TYPE1", "EXCHANGE1", "SIDE1");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:12:12", "SYMBOL1", "SEC_TYPE1", "EXCHANGE1", "SIDE1");

        assertNotSame(executionFilter1, executionFilter2);
    }
}