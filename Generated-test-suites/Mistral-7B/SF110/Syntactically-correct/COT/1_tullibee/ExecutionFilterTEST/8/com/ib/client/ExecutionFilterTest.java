import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructor() {
 executionFilter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("testAcctCode", executionFilter.getm_acctCode());
 Assert.assertEquals("testTime", executionFilter.getm_time());
 Assert.assertEquals("testSymbol", executionFilter.getm_symbol());
 Assert.assertEquals("testSecType", executionFilter.getm_secType());
 Assert.assertEquals("testExchange", executionFilter.getm_exchange());
 Assert.assertEquals("testSide", executionFilter.getm_side());
 }

 @Test
 public void testExecutionFilterDefaultConstructor() {
 executionFilter = new ExecutionFilter();
 Assert.assertEquals(0, executionFilter.getm_clientId());
 Assert.assertNull(executionFilter.getm_acctCode());
 Assert.assertNull(executionFilter.getm_time());
 Assert.assertNull(executionFilter.getm_symbol());
 Assert.assertNull(executionFilter.getm_secType());
 Assert.assertNull(executionFilter.getm_exchange());
 Assert.assertNull(executionFilter.getm_side());
 }

 @Test
 public void testEquals() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
 ExecutionFilter executionFilter3 = new ExecutionFilter(2, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");

 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 Assert.assertFalse(executionFilter1.equals(executionFilter3));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsWithNull() {
 ExecutionFilter executionFilter = new ExecutionFilter();
 Assert.assertFalse(executionFilter.equals(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithDifferentValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "testAcctCode1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "testAcctCode2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");

 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testGetters() {
 executionFilter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");

 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("testAcctCode", executionFilter.getm_acctCode());
 Assert.assertEquals("testTime", executionFilter.getm_time());
 Assert.assertEquals("testSymbol", executionFilter.getm_symbol());
 Assert.assertEquals("testSecType", executionFilter.getm_secType());
 Assert.assertEquals("testExchange", executionFilter.getm_exchange());
 Assert.assertEquals("testSide", executionFilter.getm_side());
 }
}