
package com.ib.client;

import org.junit.Test;
import org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAcctCode1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "testAcctCode1", "2022-01-01", "GOOG", "STK", "NASDAQ", "SELL");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertEquals(1, filter.m_clientId);
        assertEquals("testAcctCode1", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("SMART", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();

        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
    }
}