import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructorWithValidValues() {
 executionFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("acctCode1", executionFilter.getm_acctCode());
 Assert.assertEquals("2022-01-01 10:30:00", executionFilter.getm_time());
 Assert.assertEquals("symbol1", executionFilter.getm_symbol());
 Assert.assertEquals("SECTYPE1", executionFilter.getm_secType());
 Assert.assertEquals("EXCHANGE1", executionFilter.getm_exchange());
 Assert.assertEquals("SIDE1", executionFilter.getm_side());
 }

 @Test
 public void testExecutionFilterConstructorWithDefaultValues() {
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
 public void testEqualsMethodWithNullObject() {
 executionFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertFalse(executionFilter.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentObjects() {
 ExecutionFilter otherFilter = new ExecutionFilter(2, "acctCode2", "2022-01-01 10:30:00", "symbol2", "SECTYPE2", "EXCHANGE2", "SIDE2");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithSameObject() {
 ExecutionFilter sameFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertTrue(executionFilter.equals(sameFilter));
 }

 @Test
 public void testEqualsMethodWithSameValuesButDifferentObjects() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 ExecutionFilter sameFilter = new ExecutionFilter(1, "ACCTCODE1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertTrue(executionFilter.equals(sameFilter));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsMethodWithDifferentClientId() {
 ExecutionFilter otherFilter = new ExecutionFilter(2, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithDifferentAccountCode() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode2", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithDifferentTime() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode1", "2022-01-02 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithDifferentSymbol() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol2", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithDifferentSecurityType() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE2", "EXCHANGE1", "SIDE1");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithDifferentExchange() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE2", "SIDE1");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithDifferentSide() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDEDIFERENT");
 Assert.assertFalse(executionFilter.equals(otherFilter));
 }

 @Test
 public void testEqualsMethodWithIgnoredCaseDifference() {
 ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCTCODE1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertTrue(executionFilter.equals(otherFilter));
 }

 @Test
 public void testGetters() {
 executionFilter = new ExecutionFilter(1, "acctCode1", "2022-01-01 10:30:00", "symbol1", "SECTYPE1", "EXCHANGE1", "SIDE1");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("acctCode1", executionFilter.getm_acctCode());
 Assert.assertEquals("2022-01-01 10:30:00", executionFilter.getm_time());
 Assert.assertEquals("symbol1", executionFilter.getm_symbol());
 Assert.assertEquals("SECTYPE1", executionFilter.getm_secType());
 Assert.assertEquals("EXCHANGE1", executionFilter.getm_exchange());
 Assert.assertEquals("SIDE1", executionFilter.getm_side());
 }

}