import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testConstructorWithValidArguments() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals("ExecutionFilter instances should be equal when constructed with same arguments", expectedExecutionFilter, executionFilter);
 }

 @Test
 public void testConstructorWithNullArguments() {
 executionFilter = new ExecutionFilter();
 Assert.assertEquals("m_clientId should be zero by default", 0, executionFilter.m_clientId);
 Assert.assertNull("m_acctCode should be null by default", executionFilter.m_acctCode);
 Assert.assertNull("m_time should be null by default", executionFilter.m_time);
 Assert.assertNull("m_symbol should be null by default", executionFilter.m_symbol);
 Assert.assertNull("m_secType should be null by default", executionFilter.m_secType);
 Assert.assertNull("m_exchange should be null by default", executionFilter.m_exchange);
 Assert.assertNull("m_side should be null by default", executionFilter.m_side);
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse("ExecutionFilter instances should not be equal to null", executionFilter.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentInstances() {
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter3 = new ExecutionFilter(2, "AC123", "2022-01-01 10:30:00", "GOOG", "ETF", "NASDAQ", "SELL");
 Assert.assertFalse("ExecutionFilter instances with different values should not be equal", executionFilter.equals(executionFilter2));
 Assert.assertFalse("ExecutionFilter instances with different values should not be equal", executionFilter.equals(executionFilter3));
 }

 @Test
 public void testEqualsMethodWithSameInstance() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilterCopy = new ExecutionFilter(executionFilter);
 Assert.assertTrue("ExecutionFilter instances with same values should be equal", executionFilter.equals(executionFilterCopy));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsMethodWithDifferentClientId() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(2, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse("ExecutionFilter instances with different clientIds should not be equal and should throw IllegalArgumentException", executionFilter.equals(otherExecutionFilter));
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentSymbolsAndSameClientId() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
 Assert.assertFalse("ExecutionFilter instances with different symbols but same clientId should not be equal and should throw IllegalStateException", executionFilter.equals(otherExecutionFilter));
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentSecTypesAndSameClientId() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "ETF", "SMART", "BUY");
 Assert.assertFalse("ExecutionFilter instances with different secTypes but same clientId should not be equal and should throw IllegalStateException", executionFilter.equals(otherExecutionFilter));
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentExchangesAndSameClientId() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "NASDAQ", "BUY");
 Assert.assertFalse("ExecutionFilter instances with different exchanges but same clientId should not be equal and should throw IllegalStateException", executionFilter.equals(otherExecutionFilter));
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentSidesAndSameClientId() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
 Assert.assertFalse("ExecutionFilter instances with different sides but same clientId should not be equal and should throw IllegalStateException", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testToStringMethod() {
 executionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 String expectedToString = "ExecutionFilter{m_clientId=1, m_acctCode='AC123', m_time='2022-01-01 10:30:00', m_symbol='AAPL', m_secType='STK', m_exchange='SMART', m_side='BUY'}";
 String actualToString = executionFilter.toString();
 Assert.assertEquals("toString method should return expected string representation", expectedToString, actualToString);
 }
}