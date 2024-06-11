import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructor() {
 executionFilter = new ExecutionFilter(1, "testAccount", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("testAccount", executionFilter.getm_acctCode());
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
 public void testEqualsMethod() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
 ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
 ExecutionFilter filter3 = new ExecutionFilter(1, "testAccount1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");

 Assert.assertFalse(filter1.equals(filter2));
 Assert.assertTrue(filter1.equals(filter3));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullObject() {
 executionFilter = new ExecutionFilter();
 Assert.assertFalse(executionFilter.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount1", "testTime1", "testSymbol1", "testSecType1", "testExchange1", "testSide1");
 ExecutionFilter filter2 = new ExecutionFilter(2, "testAccount2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testGetters() {
 executionFilter = new ExecutionFilter(1, "testAccount", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("testAccount", executionFilter.getm_acctCode());
 Assert.assertEquals("testTime", executionFilter.getm_time());
 Assert.assertEquals("testSymbol", executionFilter.getm_symbol());
 Assert.assertEquals("testSecType", executionFilter.getm_secType());
 Assert.assertEquals("testExchange", executionFilter.getm_exchange());
 Assert.assertEquals("testSide", executionFilter.getm_side());
 }

 @Test(expected = IllegalStateException.class)
 public void testSettersWithNullValues() {
 executionFilter = new ExecutionFilter(1, "testAccount", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 executionFilter.setm_clientId(null);
 executionFilter.setm_acctCode(null);
 executionFilter.setm_time(null);
 executionFilter.setm_symbol(null);
 executionFilter.setm_secType(null);
 executionFilter.setm_exchange(null);
 executionFilter.setm_side(null);
 }

 @Test
 public void testSettersWithValidValues() {
 executionFilter = new ExecutionFilter(1, "testAccount", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 executionFilter.setm_clientId(2);
 executionFilter.setm_acctCode("newAccountCode");
 executionFilter.setm_time("newTime");
 executionFilter.setm_symbol("newSymbol");
 executionFilter.setm_secType("newSecType");
 executionFilter.setm_exchange("newExchange");
 executionFilter.setm_side("newSide");
 Assert.assertEquals(2, executionFilter.getm_clientId());
 Assert.assertEquals("newAccountCode", executionFilter.getm_acctCode());
 Assert.assertEquals("newTime", executionFilter.getm_time());
 Assert.assertEquals("newSymbol", executionFilter.getm_symbol());
 Assert.assertEquals("newSecType", executionFilter.getm_secType());
 Assert.assertEquals("newExchange", executionFilter.getm_exchange());
 Assert.assertEquals("newSide", executionFilter.getm_side());
 }
}