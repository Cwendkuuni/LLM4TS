package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructorWithValidArguments() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("AC123", executionFilter.m_acctCode);
        assertEquals("2022-01-01 10:00:00", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STK", executionFilter.m_secType);
        assertEquals("SMART_Routing", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterEqualsObjectSelf() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
        assertTrue(executionFilter.equals(executionFilter));
    }

    @Test
    public void testExecutionFilterEqualsObjectDifferentObjectType() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
        assertFalse(executionFilter.equals(new Object()));
    }

    @Test
    public void testExecutionFilterEqualsObjectNull() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
        assertFalse(executionFilter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsObjectDifferentValues() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "AC456", "2022-01-01 10:01:00", "GOOG", "OPT", "CBOE", "SELL");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithInvalidClientId() {
        new ExecutionFilter(-1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullAccountCode() {
        new ExecutionFilter(1, null, "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithEmptyAccountCode() {
        new ExecutionFilter(1, "", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullTime() {
        new ExecutionFilter(1, "AC123", null, "AAPL", "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithEmptyTime() {
        new ExecutionFilter(1, "AC123", "", "AAPL", "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullSymbol() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", null, "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithEmptySymbol() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "", "STK", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullSecType() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", null, "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithEmptySecType() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "", "SMART_Routing", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullExchange() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", null, "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithEmptyExchange() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "", "BUY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullSide() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithEmptySide() {
        new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART_Routing", "");
    }
}