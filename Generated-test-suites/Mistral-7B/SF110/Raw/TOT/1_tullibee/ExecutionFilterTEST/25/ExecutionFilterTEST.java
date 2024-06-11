import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Test
 public void testConstructor_ValidArguments() {
 executionFilter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");

 Assert.assertEquals("ExecutionFilters should be equal after constructing with valid arguments", expectedExecutionFilter, executionFilter1);
 }

 @Test
 public void testConstructor_ZeroClientId() {
 executionFilter1 = new ExecutionFilter(0, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter();

 Assert.assertEquals("ExecutionFilters should be equal when clientId is 0 and no other argument is passed", expectedExecutionFilter, executionFilter1);
 }

 @Test
 public void testEquals() {
 executionFilter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");
 executionFilter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");

 Assert.assertTrue("ExecutionFilters should be equal when all fields are the same", executionFilter1.equals(executionFilter2));

 executionFilter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "SELL");

 Assert.assertFalse("ExecutionFilters should not be equal when side is different", executionFilter1.equals(executionFilter2));

 executionFilter2 = new ExecutionFilter(2, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");

 Assert.assertFalse("ExecutionFilters should not be equal when clientId is different", executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEquals_NullObject() {
 executionFilter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_EXCHANGE", "BUY");

 Assert.assertFalse("ExecutionFilter should not be equal to null", executionFilter1.equals(null));
 }
}