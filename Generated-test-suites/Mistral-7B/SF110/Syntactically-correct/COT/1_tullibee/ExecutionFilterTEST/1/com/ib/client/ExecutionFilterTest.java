import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructorWithValidArguments() {
 executionFilter = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertEquals(1, executionFilter.m_clientId);
 Assert.assertEquals("account1", executionFilter.m_acctCode);
 Assert.assertEquals("2022-01-01 10:30:00", executionFilter.m_time);
 Assert.assertEquals("AAPL", executionFilter.m_symbol);
 Assert.assertEquals("STK", executionFilter.m_secType);
 Assert.assertEquals("SMART_Routing_ID", executionFilter.m_exchange);
 Assert.assertEquals("BUY", executionFilter.m_side);
 }

 @Test
 public void testExecutionFilterConstructorWithDefaultArguments() {
 executionFilter = new ExecutionFilter();

 Assert.assertEquals(0, executionFilter.m_clientId);
 }

 @Test
 public void testExecutionFilterEqualsObjectWhenBothAreSameInstance() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = executionFilter1;

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsObjectWhenBothAreDifferentInstancesButSameValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsObjectWhenBothAreDifferentInstancesButDifferentValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "SELL");

 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsObjectWhenOtherIsNull() {
 ExecutionFilter executionFilter = new ExecutionFilter();

 Assert.assertFalse(executionFilter.equals(null));
 }

 @Test(expected = ClassCastException.class)
 public void testExecutionFilterEqualsObjectWhenOtherIsDifferentType() {
 ExecutionFilter executionFilter = new ExecutionFilter();

 Assert.assertFalse(executionFilter.equals("StringObject"));
 }

 @Test
 public void testExecutionFilterHashCode() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
 }

 @Test
 public void testExecutionFilterHashCodeWhenDifferentInstancesButSameValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");

 Assert.assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
 }

 @Test
 public void testExecutionFilterHashCodeWhenDifferentInstancesButDifferentValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_ID", "SELL");

 Assert.assertNotEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
 }
}