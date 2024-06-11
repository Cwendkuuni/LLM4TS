package com.ib.client;
import org.junit.Assert;import org.junit.Test;import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 private ExecutionFilter filter1;
 private ExecutionFilter filter2;

 @Test
 public void testConstructorWithAllArguments() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals(1, filter1.m_clientId);
 Assert.assertEquals("AC123", filter1.m_acctCode);
 Assert.assertEquals("2022-01-01 10:10:11", filter1.m_time);
 Assert.assertEquals("AAPL", filter1.m_symbol);
 Assert.assertEquals("STK", filter1.m_secType);
 Assert.assertEquals("SMART", filter1.m_exchange);
 Assert.assertEquals("BUY", filter1.m_side);
 }

 @Test
 public void testDefaultConstructor() {
 filter1 = new ExecutionFilter();
 Assert.assertEquals(0, filter1.m_clientId);
 Assert.assertNull(filter1.m_acctCode);
 Assert.assertNull(filter1.m_time);
 Assert.assertNull(filter1.m_symbol);
 Assert.assertNull(filter1.m_secType);
 Assert.assertNull(filter1.m_exchange);
 Assert.assertNull(filter1.m_side);
 }

 @Test
 public void testEqualsMethod() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentClientId() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(2, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentAccountCode() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC456", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentTime() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:12", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentSymbol() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "GOOG", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentSecurityType() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "OPT", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentExchange() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "NYSE", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithDifferentSide() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "SELL");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullObject() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(null));
 }

 @Test
 public void testHashCodeMethod() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 int hashCode1 = filter1.hashCode();
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:10:11", "AAPL", "STK", "SMART", "BUY");
 int hashCode2 = filter2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 }

}