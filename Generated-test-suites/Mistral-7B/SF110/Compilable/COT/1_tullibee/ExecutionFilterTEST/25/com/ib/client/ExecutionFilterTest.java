package com.ib.client;
import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter m_filter1;
 private ExecutionFilter m_filter2;

 @Test
 public void testConstructorWithDefaultValues() {
 m_filter1 = new ExecutionFilter();
 Assert.assertEquals(0, m_filter1.m_clientId);
 Assert.assertNull(m_filter1.m_acctCode);
 Assert.assertNull(m_filter1.m_time);
 Assert.assertNull(m_filter1.m_symbol);
 Assert.assertNull(m_filter1.m_secType);
 Assert.assertNull(m_filter1.m_exchange);
 Assert.assertNull(m_filter1.m_side);
 }

 @Test
 public void testConstructorWithValidParameters() {
 m_filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals(1, m_filter1.m_clientId);
 Assert.assertEquals("TestAccount", m_filter1.m_acctCode);
 Assert.assertEquals("2022-01-01", m_filter1.m_time);
 Assert.assertEquals("AAPL", m_filter1.m_symbol);
 Assert.assertEquals("STK", m_filter1.m_secType);
 Assert.assertEquals("SMART", m_filter1.m_exchange);
 Assert.assertEquals("BUY", m_filter1.m_side);
 }

 @Test
 public void testEqualsWithNullObject() {
 m_filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(m_filter1.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 m_filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(m_filter1.equals(new Object()));
 }

 @Test
 public void testEqualsWithSameInstance() {
 m_filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 m_filter2 = m_filter1;
 Assert.assertTrue(m_filter1.equals(m_filter2));
 }

 @Test
 public void testEqualsWithDifferentInstancesButEqualValues() {
 m_filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 m_filter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertTrue(m_filter1.equals(m_filter2));
 }

 @Test
 public void testEqualsWithDifferentInstancesButNotEqualValues() {
 m_filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 m_filter2 = new ExecutionFilter(2, "AnotherAccount", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");
 Assert.assertFalse(m_filter1.equals(m_filter2));
 }
}