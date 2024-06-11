package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

@Test
public void testExecutionFilterDefaultConstructor() {
// Test case for default constructor
ExecutionFilter filter = new ExecutionFilter();
Assert.assertNull(filter.m_clientId);
Assert.assertNull(filter.m_acctCode);
Assert.assertNull(filter.m_time);
Assert.assertNull(filter.m_symbol);
Assert.assertNull(filter.m_secType);
Assert.assertNull(filter.m_exchange);
Assert.assertNull(filter.m_side);
}

@Test
public void testExecutionFilterConstructor() {
// Test case for constructor with arguments
int clientId = 123;
String acctCode = "testAcctCode";
String time = "testTime";
String symbol = "testSymbol";
String secType = "testSecType";
String exchange = "testExchange";
String side = "testSide";
ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
Assert.assertEquals(clientId, filter.m_clientId);
Assert.assertEquals(acctCode, filter.m_acctCode);
Assert.assertEquals(time, filter.m_time);
Assert.assertEquals(symbol, filter.m_symbol);
Assert.assertEquals(secType, filter.m_secType);
Assert.assertEquals(exchange, filter.m_exchange);
Assert.assertEquals(side, filter.m_side);
}

@Test(expected = NullPointerException.class)
public void testEqualsWithNullObject() {
// Test case for equals method with null object
ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
Assert.assertFalse(filter.equals(null));
}

@Test
public void testEqualsWithEqualValuesDifferentObjects() {
// Test case for equals method with different objects but equal values
ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
ExecutionFilter filter2 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
Assert.assertTrue(filter1.equals(filter2));
}

@Test
public void testEqualsWithDifferentValuesDifferentObjects() {
// Test case for equals method with different objects but different values
ExecutionFilter filter1 = new ExecutionFilter(1, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
ExecutionFilter filter2 = new ExecutionFilter(1, "testAcctCode2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
Assert.assertFalse(filter1.equals(filter2));
}

}