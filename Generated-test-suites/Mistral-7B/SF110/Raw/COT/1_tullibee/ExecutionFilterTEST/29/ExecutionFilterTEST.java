import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructorWithValidArguments() {
 executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertEquals("ExecutionFilter instances are not equal", expectedExecutionFilter, executionFilter);
 }

 @Test
 public void testExecutionFilterConstructorWithInvalidClientId() {
 executionFilter = new ExecutionFilter(0, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertNotEquals("ExecutionFilter instances are equal when clientId is zero", new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY"), executionFilter);
 }

 @Test
 public void testExecutionFilterConstructorWithNullAccountCode() {
 executionFilter = new ExecutionFilter(1, null, "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertNotNull("ExecutionFilter instance should not be null when accountCode is null", executionFilter);
 Assert.assertNull("AccountCode should be null in the instance", executionFilter.m_acctCode);
 }

 @Test
 public void testExecutionFilterConstructorWithEmptySymbol() {
 executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "", "STK", "SMART", "BUY");

 Assert.assertNotNull("ExecutionFilter instance should not be null when symbol is empty", executionFilter);
 Assert.assertEquals("Symbol should be empty in the instance", "", executionFilter.m_symbol);
 }

 @Test
 public void testExecutionFilterEqualsWithNullObject() {
 executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse("ExecutionFilter instance should not be equal to null", executionFilter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsWithSelf() {
 executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue("ExecutionFilter instance should be equal to itself", executionFilter.equals(executionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentClientId() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(2, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse("ExecutionFilter instances with different clientId should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentAccountCode() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse("ExecutionFilter instances with different accountCode should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentTime() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse("ExecutionFilter instances with different time should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentSymbol() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");

 Assert.assertFalse("ExecutionFilter instances with different symbol should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentSecType() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");

 Assert.assertFalse("ExecutionFilter instances with different secType should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentExchange() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "BATS", "BUY");

 Assert.assertFalse("ExecutionFilter instances with different exchange should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentSide() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");

 Assert.assertFalse("ExecutionFilter instances with different side should not be equal", executionFilter.equals(otherExecutionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithEqualInstances() {
 ExecutionFilter otherExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue("ExecutionFilter instances with equal values should be equal", executionFilter.equals(otherExecutionFilter));
 }
}