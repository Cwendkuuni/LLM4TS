package com.ib.client;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExecutionFilterTest {

    ExecutionFilter filter1, filter2;

    @Before
    public void setUp() {
        filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "IBM", "STK", "SMART", "BUY");
        filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "IBM", "STK", "SMART", "BUY");
    }

    @Test
    public void testConstructor() {
        assertEquals(0, new ExecutionFilter().m_clientId);
        assertNull(new ExecutionFilter().m_acctCode);
        assertNull(new ExecutionFilter().m_time);
        assertNull(new ExecutionFilter().m_symbol);
        assertNull(new ExecutionFilter().m_secType);
        assertNull(new ExecutionFilter().m_exchange);
        assertNull(new ExecutionFilter().m_side);
    }

    @Test
    public void testEquals() {
        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(new Object()));
        assertFalse(filter1.equals(null));
    }

    @Test
    public void testSettersAndGetters() {
        ExecutionFilter filter3 = new ExecutionFilter();
        filter3.setClientId(1);
        filter3.setAcctCode("account1");
        filter3.setTime("2022-01-01");
        filter3.setSymbol("IBM");
        filter3.setSecType("STK");
        filter3.setExchange("SMART");
        filter3.setSide("BUY");

        assertEquals(1, filter1.getClientId());
        assertEquals("account1", filter1.getAcctCode());
        assertEquals("2022-01-01", filter1.getTime());
        assertEquals("IBM", filter1.getSymbol());
        assertEquals("STK", filter1.getSecType());
        assertEquals("SMART", filter1.getExchange());
        assertEquals("BUY", filter1.getSide());

        assertEquals(1, filter3.getClientId());
        assertEquals("account1", filter3.getAcctCode());
        assertEquals("2022-01-01", filter3.getTime());
        assertEquals("IBM", filter3.getSymbol());
        assertEquals("STK", filter3.getSecType());
        assertEquals("SMART", filter3.getExchange());
        assertEquals("BUY", filter3.getSide());
    }
}