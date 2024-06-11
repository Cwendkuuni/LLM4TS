package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
    }

    @Test
    public void testExecutionFilterWithParametersConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("testAccount", filter.m_acctCode);
        assertEquals("2022-01-01 12:34:56", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("SMART", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsSelf() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testExecutionFilterEqualsDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount1", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "testAccount2", "2022-01-02 12:34:56", "MSFT", "OPT", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount1", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount2", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-02 12:34:56", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "MSFT", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "OPT", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentAndSameValuesIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "TESTACCOUNT", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "bUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        int hashCode1 = filter1.hashCode();
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
        int hashCode2 = filter2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}