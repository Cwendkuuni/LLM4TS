package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertNotEquals(filter, null);
    }

    @Test
    public void testEqualsSelf() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(filter, filter);
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, null, null, null, null, null);
        ExecutionFilter filter2 = new ExecutionFilter(2, null, null, null, null, null, null);
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AcctCode1", null, null, null, null, null);
        ExecutionFilter filter2 = new ExecutionFilter(1, "AcctCode2", null, null, null, null, null);
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, "Time1", null, null, null, null);
        ExecutionFilter filter2 = new ExecutionFilter(1, null, "Time2", null, null, null, null);
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, null, "Symbol1", null, null, null);
        ExecutionFilter filter2 = new ExecutionFilter(1, null, null, "Symbol2", null, null, null);
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, null, null, "SecType1", null, null);
        ExecutionFilter filter2 = new ExecutionFilter(1, null, null, null, "SecType2", null, null);
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, null, null, null, "Exchange1", null);
        ExecutionFilter filter2 = new ExecutionFilter(1, null, null, null, null, "Exchange2", null);
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, null, null, null, null, "Side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, null, null, null, null, null, "Side2");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsSameValuesIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCTCODE1", "TIME1", "SYMBOL1", "SECType1", "Exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acctcode1", "Time1", "Symbol1", "SecType1", "Exchange1", "Side1");
        assertEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentValuesIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCTCODE1", "TIME1", "SYMBOL1", "SecType1", "Exchange1", "Side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Acctcode2", "time2", "symbol2", "SecType2", "Exchange2", "Side2");
        assertNotEquals(filter1, filter2);
    }
}