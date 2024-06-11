package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testEquals_SameInstance() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = filter1; // same instance

        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentInstances_SameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentInstances_DifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "testAccount2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");

        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_Null() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        assertFalse(filter1.equals(null));
    }

    @Test
    public void testEquals_DifferentClasses() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        Object other = new Object();

        assertFalse(filter1.equals(other));
    }
}