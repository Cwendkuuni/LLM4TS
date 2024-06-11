package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        assertNotEquals(0, filter.getM_clientId());
        assertEquals("testAcctCode", filter.getM_acctCode());
        assertEquals("testTime", filter.getM_time());
        assertEquals("testSymbol", filter.getM_symbol());
        assertEquals("testSecType", filter.getM_secType());
        assertEquals("testExchange", filter.getM_exchange());
        assertEquals("testSide", filter.getM_side());
    }

    @Test
    public void testExecutionFilterEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsSameInstance() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstanceSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstanceDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAcctCode2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
        assertFalse(filter1.equals(filter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentInstanceDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        ExecutionFilter filter2 = new ExecutionFilter(2, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        int hashCode = filter.hashCode();
        ExecutionFilter filterCopy = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        int hashCodeCopy = filterCopy.hashCode();
        assertEquals(hashCode, hashCodeCopy);
    }
}