package com.ib.client.test;

import org.junit.Test;
import com.ib.client.ExecutionFilter;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        assertEquals(1, filter.m_clientId);
        assertEquals("testAcctCode", filter.m_acctCode);
        assertEquals("testTime", filter.m_time);
        assertEquals("testSymbol", filter.m_symbol);
        assertEquals("testSecType", filter.m_secType);
        assertEquals("testExchange", filter.m_exchange);
        assertEquals("testSide", filter.m_side);
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
    public void testExecutionFilterEqualsDifferentInstancesWithSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        ExecutionFilter filter2 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsDifferentInstancesWithDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
        ExecutionFilter filter2 = new ExecutionFilter(2, "testAcctCode2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
        assertFalse(filter1.equals(filter2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEqualsDifferentTypes() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals("testString"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterConstructorWithNullClientId() {
        ExecutionFilter filter = new ExecutionFilter(null, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        fail("Expected IllegalArgumentException but got none");
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterConstructorWithNullAccountCode() {
        ExecutionFilter filter = new ExecutionFilter(1, null, "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        fail("Expected NullPointerException but got none");
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterConstructorWithNullTime() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", null, "testSymbol", "testSecType", "testExchange", "testSide");
        fail("Expected NullPointerException but got none");
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterConstructorWithNullSymbol() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", null, "testSecType", "testExchange", "testSide");
        fail("Expected NullPointerException but got none");
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterConstructorWithNullSecType() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", null, "testExchange", "testSide");
        fail("Expected NullPointerException but got none");
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterConstructorWithNullExchange() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", null, "testSide");
        fail("Expected NullPointerException but got none");
    }

    @Test(expected = NullPointerException.class)
    public void testExecutionFilterConstructorWithNullSide() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", null);
        fail("Expected NullPointerException but got none");
    }
}