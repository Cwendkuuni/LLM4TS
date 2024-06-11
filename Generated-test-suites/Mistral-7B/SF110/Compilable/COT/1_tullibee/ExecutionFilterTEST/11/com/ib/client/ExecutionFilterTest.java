package com.ib.client;
import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executor1;
 private ExecutionFilter executor2;

 @Test
 public void testConstructorWithValidParameters() {
 executor1 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertNotNull(executor1);
 }

 @Test
 public void testConstructorWithZeroParam() {
 executor1 = new ExecutionFilter();
 Assert.assertEquals(0, executor1.m_clientId);
 }

 @Test
 public void testEqualsMethodWithSameObject() {
 executor1 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executor3 = executor1;
 Assert.assertTrue(executor1.equals(executor3));
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 Assert.assertFalse(executor1.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentInstances() {
 executor2 = new ExecutionFilter(2, "acctCode2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentClientId() {
 executor2 = new ExecutionFilter(1, "acctCode2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentAcctCode() {
 executor2 = new ExecutionFilter(1, "acctCode1_diff", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentTime() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentSymbol() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentSecType() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentExchange() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "STK", "IBM", "BUY");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test
 public void testEqualsMethodWithDifferentSide() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
 Assert.assertFalse(executor1.equals(executor2));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullAcctCode() {
 executor2 = new ExecutionFilter(1, null, "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 executor1.equals(executor2);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullSymbol() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", null, "STK", "SMART", "BUY");
 executor1.equals(executor2);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullSecType() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", null, "SMART", "BUY");
 executor1.equals(executor2);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullExchange() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "STK", null, "BUY");
 executor1.equals(executor2);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullSide() {
 executor2 = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", null);
 executor1.equals(executor2);
 }
}