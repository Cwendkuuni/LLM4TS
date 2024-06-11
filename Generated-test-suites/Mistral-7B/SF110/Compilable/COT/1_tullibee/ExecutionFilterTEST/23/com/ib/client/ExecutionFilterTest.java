package com.ib.client;
import org.junit.Assert;import org.junit.Test;import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 private ExecutionFilter getExecutionFilter(int clientId, String acctCode, String time, String symbol, String secType, String exchange, String side) {
 return new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 }

 @Test
 public void testExecutionFilterConstructorWithNullValues() {
 ExecutionFilter filter = new ExecutionFilter();
 Assert.assertEquals(0, filter.m_clientId);
 Assert.assertNull(filter.m_acctCode);
 Assert.assertNull(filter.m_time);
 Assert.assertNull(filter.m_symbol);
 Assert.assertNull(filter.m_secType);
 Assert.assertNull(filter.m_exchange);
 Assert.assertNull(filter.m_side);
 }

 @Test
 public void testExecutionFilterEqualsNullObject() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsSameObject() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = filter1;
 Assert.assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsDifferentIDs() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsDifferentAccountCodes() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount1", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount2", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsDifferentTimes() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:01:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsDifferentSymbols() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "GOOG", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsDifferentSecTypes() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "OPT", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsDifferentExchanges() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "NYSE", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "NASDAQ", "BUY");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithEmptySymbol() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "", "STK", "SMART", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterConstructorWithNullSymbol() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", null, "STK", "SMART", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithEmptySecType() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "", "SMART", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterConstructorWithNullSecType() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", null, "SMART", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithEmptyExchange() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterConstructorWithNullExchange() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", null, "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithEmptySide() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", "");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterConstructorWithNullSide() {
 ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-10-01 10:00:00", "AAPL", "STK", "SMART", null);
 }
}