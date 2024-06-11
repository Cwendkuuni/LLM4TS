
package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");

        // Test equals method with same instance
        assertTrue(filter1.equals(filter1));

        // Test equals method with equal values
        assertTrue(filter1.equals(filter2));

        // Test equals method with different clientId
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
        assertFalse(filter1.equals(filter3));

        // Test equals method with different account code
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACC456", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
        assertFalse(filter1.equals(filter4));

        // Test equals method with different time
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:01", "IBM", "STK", "IDEALPRO", "BUY");
        assertFalse(filter1.equals(filter5));

        // Test equals method with different symbol
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "AAPL", "STK", "IDEALPRO", "BUY");
        assertFalse(filter1.equals(filter6));

        // Test equals method with different secType
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "OPT", "IDEALPRO", "BUY");
        assertFalse(filter1.equals(filter7));

        // Test equals method with different exchange
        ExecutionFilter filter8 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter8));

        // Test equals method with different side
        ExecutionFilter filter9 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "SELL");
        assertFalse(filter1.equals(filter9));
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");

        ExecutionFilter filter3 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "SELL");
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "OPT", "IDEALPRO", "BUY");
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "NYSE", "BUY");

        ExecutionFilter filter6 = null;

        // Test equals method with same instance
        assertTrue(filter1.equals(filter1));

        // Test equals method with equal values
        assertTrue(filter1.equals(filter2));

        // Test equals method with different side
        assertFalse(filter1.equals(filter3));

        // Test equals method with different secType
        assertFalse(filter1.equals(filter4));

        // Test equals method with different exchange
        assertFalse(filter1.equals(filter5));

        // Test equals method with null object
        assertFalse(filter1.equals(filter6));
    }

    @Test
    public void testExecutionFilterEdgeCasesAndErrorScenarios() {
        // Test constructor with empty string for account code
        ExecutionFilter filter1 = new ExecutionFilter(1, "", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
        assertNotNull(filter1);
        assertEquals("", filter1.m_acctCode);

        // Test constructor with null clientId
        ExecutionFilter filter2 = new ExecutionFilter(null, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", "BUY");
        assertNotNull(filter2);
        assertEquals(0, filter2.m_clientId);

        // Test constructor with null symbol
        ExecutionFilter filter3 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", null, "STK", "IDEALPRO", "BUY");
        assertNotNull(filter3);
        assertNull(filter3.m_symbol);

        // Test constructor with null secType
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", null, "IDEALPRO", "BUY");
        assertNotNull(filter4);
        assertNull(filter4.m_secType);

        // Test constructor with null exchange
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", null, "BUY");
        assertNotNull(filter5);
        assertNull(filter5.m_exchange);

        // Test constructor with null side
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACC123", "2022-01-01 10:30:00", "IBM", "STK", "IDEALPRO", null);
        assertNotNull(filter6);
        assertNull(filter6.m_side);
    }
}