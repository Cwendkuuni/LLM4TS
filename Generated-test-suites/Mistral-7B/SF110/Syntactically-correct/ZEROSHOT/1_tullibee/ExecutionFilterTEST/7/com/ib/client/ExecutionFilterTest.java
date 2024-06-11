import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterConstructorWithValidArguments() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertEquals("ExecutionFilter created with valid arguments", expectedExecutionFilter, executionFilter);
 }

 @Test
 public void testExecutionFilterConstructorWithNullArguments() {
 ExecutionFilter executionFilter = new ExecutionFilter(null, null, null, null, null, null, null);
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter();

 Assert.assertEquals("ExecutionFilter created with default values when constructor arguments are null", expectedExecutionFilter, executionFilter);
 }

 @Test
 public void testExecutionFilterEqualsWithNullObject() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertFalse("ExecutionFilter should not equal null object", executionFilter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsWithSameInstance() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertTrue("ExecutionFilter should equal itself", executionFilter.equals(executionFilter));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentInstances() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(2, "AC456", "2022-10-01 11:15:00", "GOOG", "OPT", "NSFX_Routing_ID", "SELL");

 Assert.assertFalse("ExecutionFilter should not equal another ExecutionFilter with different values", executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsWithEqualValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertTrue("ExecutionFilter should equal another ExecutionFilter with equal values", executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterHashCode() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 int hashCode = executionFilter.hashCode();

 ExecutionFilter executionFilterWithSameValues = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 int hashCode2 = executionFilterWithSameValues.hashCode();

 Assert.assertEquals("ExecutionFilter hashCode should be equal for similar objects", hashCode, hashCode2);
 }

 @Test
 public void testExecutionFilterHashCodeWithDifferentInstances() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "AC123", "2022-10-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(2, "AC456", "2022-10-01 11:15:00", "GOOG", "OPT", "NSFX_Routing_ID", "SELL");

 int hashCode1 = executionFilter1.hashCode();
 int hashCode2 = executionFilter2.hashCode();

 Assert.assertNotEquals("ExecutionFilter hashCode should not be equal for different objects", hashCode1, hashCode2);
 }
}