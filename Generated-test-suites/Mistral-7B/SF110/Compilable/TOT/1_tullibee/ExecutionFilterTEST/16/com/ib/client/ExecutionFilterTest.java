package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-10-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("testAccount", filter.m_acctCode);
        assertEquals("2022-10-01 10:30:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("SMART", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
}