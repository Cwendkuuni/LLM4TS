import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructorWithValidArguments() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals("Test construction with valid arguments", expectedExecutionFilter, executionFilter);
 }

 @Test
 public void testExecutionFilterConstructorWithNullArguments() {
 executionFilter = new ExecutionFilter();
 Assert.assertFalse("Test construction with null arguments", executionFilter.m_acctCode.equalsIgnoreCase(null));
 Assert.assertEquals("Test construction with null arguments, clientId", 0, executionFilter.m_clientId);
 }

 @Test
 public void testExecutionFilterEqualsObjectWithNull() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse("Test equals with null object", executionFilter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsObjectWithSameInstance() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertTrue("Test equals with same instance", executionFilter.equals(executionFilter));
 }

 @Test
 public void testExecutionFilterEqualsObjectWithDifferentInstance() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "SELL");
 Assert.assertFalse("Test equals with different instance", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsObjectWithSameDataButDifferentInstance() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals("Test equals with same data but different instance", executionFilter, otherExecutionFilter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithInvalidArguments() {
 executionFilter = new ExecutionFilter(1, null, null, null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithEmptyString() {
 executionFilter = new ExecutionFilter(1, "", "", "", "", "", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterConstructorWithNullString() {
 executionFilter = new ExecutionFilter(1, null, "", "", "", "", "");
 }

 @Test
 public void testExecutionFilterGetters() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals("Test getter m_clientId", 1, executionFilter.getClientId());
 Assert.assertEquals("Test getter m_acctCode", "AC123", executionFilter.getAcctCode());
 Assert.assertEquals("Test getter m_time", "2022-01-01 10:00:00", executionFilter.getTime());
 Assert.assertEquals("Test getter m_symbol", "AAPL", executionFilter.getSymbol());
 Assert.assertEquals("Test getter m_secType", "STK", executionFilter.getSecType());
 Assert.assertEquals("Test getter m_exchange", "SMART", executionFilter.getExchange());
 Assert.assertEquals("Test getter m_side", "BUY", executionFilter.getSide());
 }

 @Test
 public void testExecutionFilterSetters() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 executionFilter.setClientId(2);
 executionFilter.setAcctCode("AC456");
 executionFilter.setTime("2022-01-02 11:11:11");
 executionFilter.setSymbol("GOOG");
 executionFilter.setSecType("OPT");
 executionFilter.setExchange("NASDAQ");
 executionFilter.setSide("SELL");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(2, "AC456", "2022-01-02 11:11:11", "GOOG", "OPT", "NASDAQ", "SELL");
 Assert.assertEquals("Test setters", expectedExecutionFilter, executionFilter);
 }

}