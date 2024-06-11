import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;

 @Test
 public void testExecutionFilterConstructorWithValidArguments() {
 executionFilter = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("Acct1", executionFilter.getm_acctCode());
 Assert.assertEquals("2022-01-01", executionFilter.getm_time());
 Assert.assertEquals("IBM", executionFilter.getm_symbol());
 Assert.assertEquals("STK", executionFilter.getm_secType());
 Assert.assertEquals("IDEALPRO", executionFilter.getm_exchange());
 Assert.assertEquals("Buy", executionFilter.getm_side());
 }

 @Test
 public void testExecutionFilterConstructorWithNullArguments() {
 executionFilter = new ExecutionFilter(null, null, null, null, null, null, null);
 Assert.assertEquals(0, executionFilter.getm_clientId());
 Assert.assertNull(executionFilter.getm_acctCode());
 Assert.assertNull(executionFilter.getm_time());
 Assert.assertNull(executionFilter.getm_symbol());
 Assert.assertNull(executionFilter.getm_secType());
 Assert.assertNull(executionFilter.getm_exchange());
 Assert.assertNull(executionFilter.getm_side());
 }

 @Test
 public void testExecutionFilterConstructorWithEmptyStrings() {
 executionFilter = new ExecutionFilter(1, "", "", "", "", "", "");
 Assert.assertEquals(1, executionFilter.getm_clientId());
 Assert.assertEquals("", executionFilter.getm_acctCode());
 Assert.assertEquals("", executionFilter.getm_time());
 Assert.assertEquals("", executionFilter.getm_symbol());
 Assert.assertEquals("", executionFilter.getm_secType());
 Assert.assertEquals("", executionFilter.getm_exchange());
 Assert.assertEquals("", executionFilter.getm_side());
 }

 @Test
 public void testExecutionFilterEqualsMethodWithSameObject() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 ExecutionFilter executionFilter2 = executionFilter1;
 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsMethodWithNullObject() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 Assert.assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsMethodWithDifferentObjects() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 ExecutionFilter executionFilter2 = new ExecutionFilter(2, "Acct2", "2022-01-02", "IBM", "STK", "IDEALPRO", "Buy");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsMethodWithSameValuesButDifferentObjects() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsMethodWithDifferentValues() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Buy");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "Acct1", "2022-01-01", "IBM", "STK", "IDEALPRO", "Sell");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 private int getm_clientId() {
 return m_clientId;
 }

 private String getm_acctCode() {
 return m_acctCode;
 }

 private String getm_time() {
 return m_time;
 }

 private String getm_symbol() {
 return m_symbol;
 }

 private String getm_secType() {
 return m_secType;
 }

 private String getm_exchange() {
 return m_exchange;
 }

 private String getm_side() {
 return m_side;
 }

}