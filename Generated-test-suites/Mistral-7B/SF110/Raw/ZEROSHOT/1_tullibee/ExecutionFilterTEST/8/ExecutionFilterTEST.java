import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterConstructionWithDefaultValues() {
 ExecutionFilter executionFilter = new ExecutionFilter();
 Assert.assertEquals(0, executionFilter.getClientId());
 Assert.assertNull(executionFilter.getAcctCode());
 Assert.assertNull(executionFilter.getTime());
 Assert.assertNull(executionFilter.getSymbol());
 Assert.assertNull(executionFilter.getSecType());
 Assert.assertNull(executionFilter.getExchange());
 Assert.assertNull(executionFilter.getSide());
 }

 @Test
 public void testExecutionFilterConstructionWithCustomValues() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT123", "2022-11-14 15:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals(1, executionFilter.getClientId());
 Assert.assertEquals("ACCT123", executionFilter.getAcctCode());
 Assert.assertEquals("2022-11-14 15:30:00", executionFilter.getTime());
 Assert.assertEquals("AAPL", executionFilter.getSymbol());
 Assert.assertEquals("STK", executionFilter.getSecType());
 Assert.assertEquals("SMART", executionFilter.getExchange());
 Assert.assertEquals("BUY", executionFilter.getSide());
 }

 @Test
 public void testExecutionFilterEquals() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2022-11-14 15:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-11-14 15:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter3 = new ExecutionFilter(1, "ACCT123", "2022-11-14 15:30:00", "AAPL", "STK", "SMART", "SELL");

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 Assert.assertFalse(executionFilter1.equals(executionFilter3));
 Assert.assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testExecutionFilterHashCode() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2022-11-14 15:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-11-14 15:30:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
 }
}