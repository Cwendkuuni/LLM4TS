import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter m_executionFilter;

 @Test
 public void testExecutionFilterConstructorWithValidParams() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 Assert.assertEquals(expectedExecutionFilter, m_executionFilter);
 }

 @Test
 public void testExecutionFilterConstructorWithNullParams() {
 m_executionFilter = new ExecutionFilter();
 Assert.assertNotEquals(null, m_executionFilter);
 }

 @Test
 public void testExecutionFilterEqualsNullObject() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 Assert.assertFalse(m_executionFilter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsSameObject() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 ExecutionFilter sameObject = m_executionFilter;
 Assert.assertTrue(m_executionFilter.equals(sameObject));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjects() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 ExecutionFilter differentObject = new ExecutionFilter(2, "ACCT2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "SELL");
 Assert.assertFalse(m_executionFilter.equals(differentObject));
 }

 @Test
 public void testExecutionFilterEqualsDifferentObjectsWithSameValues() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 ExecutionFilter differentObjectWithSameValues = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 Assert.assertTrue(m_executionFilter.equals(differentObjectWithSameValues));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterNullClientId() {
 m_executionFilter = new ExecutionFilter(null, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterNullAcctCode() {
 m_executionFilter = new ExecutionFilter(1, null, "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterNullTime() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", null, "AAPL", "STK", "SMART_ routing_ID", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterNullSymbol() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", null, "STK", "SMART_ routing_ID", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterNullSecType() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", null, "SMART_ routing_ID", "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterNullExchange() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", null, "BUY");
 }

 @Test(expected = NullPointerException.class)
 public void testExecutionFilterNullSide() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", null);
 }

 @Test
 public void testExecutionFilterEmptyStringAcctCode() {
 m_executionFilter = new ExecutionFilter(1, "", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 Assert.assertEquals(expectedExecutionFilter, m_executionFilter);
 }

 @Test
 public void testExecutionFilterEmptyStringSymbol() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "", "STK", "SMART_ routing_ID", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "", "STK", "SMART_ routing_ID", "BUY");
 Assert.assertEquals(expectedExecutionFilter, m_executionFilter);
 }

 @Test
 public void testExecutionFilterEmptyStringSecType() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "", "SMART_ routing_ID", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "", "SMART_ routing_ID", "BUY");
 Assert.assertEquals(expectedExecutionFilter, m_executionFilter);
 }

 @Test
 public void testExecutionFilterEmptyStringExchange() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "", "BUY");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "", "BUY");
 Assert.assertEquals(expectedExecutionFilter, m_executionFilter);
 }

 @Test
 public void testExecutionFilterEmptyStringSide() {
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "");
 ExecutionFilter expectedExecutionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "");
 Assert.assertEquals(expectedExecutionFilter, m_executionFilter);
 }

 @Test
 public void testExecutionFilterLongClientId() {
 m_executionFilter = new ExecutionFilter(Integer.MAX_VALUE, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 }

 @Test
 public void testExecutionFilterLongSymbolLength() {
 String longSymbol = new String(new char[1025]).replace('\0', 'A');
 m_executionFilter = new ExecutionFilter(1, "ACCT1", "2022-01-01 10:30:00", longSymbol, "STK", "SMART_ routing_ID", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecutionFilterNegativeClientId() {
 m_executionFilter = new ExecutionFilter(-1, "ACCT1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_ID", "BUY");
 }
}