 package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    void testExecutionFilterConstruction() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertEquals(expectedExecutionFilter, executionFilter);
    }

    @Test
    void testEqualsMethod() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter4 = new ExecutionFilter(1, "Johndoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter executionFilter5 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "SELL");

        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
        assertFalse(executionFilter1.equals(executionFilter4));
        assertFalse(executionFilter1.equals(executionFilter5));
    }

    @Test
    void testEdgeCasesAndErrorScenarios() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(-1, null, "", "", "", "", "");
        ExecutionFilter executionFilter2 = new ExecutionFilter(0, "JohnDoe", "", "", "", "", "");
        ExecutionFilter executionFilter3 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", null, "", "", "");
        ExecutionFilter executionFilter4 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", null, "", "");
        ExecutionFilter executionFilter5 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "", "SMART_EXCHANGE", "");
        ExecutionFilter executionFilter6 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "", "SMART", "SELL_SIDE");

        assertFalse(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter2.equals(executionFilter3));
        assertFalse(executionFilter3.equals(executionFilter4));
        assertFalse(executionFilter4.equals(executionFilter5));
        assertFalse(executionFilter5.equals(executionFilter6));
    }
}