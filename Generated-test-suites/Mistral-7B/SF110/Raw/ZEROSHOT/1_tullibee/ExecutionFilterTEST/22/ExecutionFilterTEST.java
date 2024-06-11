import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Test
 public void testConstructor() {
 executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertEquals(expectedExecutionFilter, executionFilter1);
 }

 @Test
 public void testEqualsWhenObjectsAreSame() {
 ExecutionFilter executionFilter1 = new ExecutionFilter();
 ExecutionFilter executionFilter2 = executionFilter1;

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWhenThisIsOther() {
 ExecutionFilter executionFilter1 = new ExecutionFilter();
 ExecutionFilter executionFilter2 = new ExecutionFilter();

 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWhenFieldsAreSame() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWhenFieldsAreDifferent() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:01", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWhenDifferentObjectsWithSameValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCOUNT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWhenDifferentObjectsWithSameValuesIgnoreCase() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCOUNT1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testHashCode() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 int expectedHashCode = 31 * (31 + executionFilter1.hashCode());

 Assert.assertEquals(expectedHashCode, executionFilter1.hashCode());
 }
}