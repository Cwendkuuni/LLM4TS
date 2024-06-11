import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterConstructor() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter3 = new ExecutionFilter(2, "TestAccount2", "2022-01-01 10:30:00", "GOOG", "ETF", "NASDAQ", "SELL");

 Assert.assertEquals(executionFilter, executionFilter2); // Testing if two equal ExecutionFilters are the same object
 Assert.assertNotEquals(executionFilter, executionFilter3); // Testing if two non-equal ExecutionFilters are not the same object
 }

 @Test
 public void testExecutionFilterEqualsNull() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsSameObject() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue(executionFilter.equals(executionFilter));
 }

 @Test
 public void testExecutionFilterEqualsDifferentClientId() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(2, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentAccount() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentTime() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentSymbol() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "MSFT", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentSecType() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentExchange() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "NASDAQ", "BUY");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentSide() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");

 Assert.assertFalse(executionFilter.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsEqualValuesIgnoreCase() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMArt", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAccount", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "bUY");

 Assert.assertTrue(executionFilter.equals(executionFilter2));
 }
}