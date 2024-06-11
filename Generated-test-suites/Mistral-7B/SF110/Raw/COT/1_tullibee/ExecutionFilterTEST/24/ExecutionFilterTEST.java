import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter filter1;
 private ExecutionFilter filter2;

 @Test
 public void testConstructorWithDefaultValues() {
 filter1 = new ExecutionFilter();
 Assert.assertEquals(0, filter1.getClientId());
 Assert.assertNull(filter1.getAcctCode());
 Assert.assertNull(filter1.getTime());
 Assert.assertNull(filter1.getSymbol());
 Assert.assertNull(filter1.getSecType());
 Assert.assertNull(filter1.getExchange());
 Assert.assertNull(filter1.getSide());
 }

 @Test
 public void testConstructorWithValidValues() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals(1, filter1.getClientId());
 Assert.assertEquals("AC123", filter1.getAcctCode());
 Assert.assertEquals("2022-01-01", filter1.getTime());
 Assert.assertEquals("AAPL", filter1.getSymbol());
 Assert.assertEquals("STK", filter1.getSecType());
 Assert.assertEquals("SMART", filter1.getExchange());
 Assert.assertEquals("BUY", filter1.getSide());
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentObjects() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(2, "AC456", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsMethodWithSameObjects() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Assert.assertTrue(filter1.equals(filter2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsMethodWithDifferentClientId() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(2, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter1.equals(filter2);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentAcctCode() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC456", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter1.equals(filter2);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentTime() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
 filter1.equals(filter2);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentSymbol() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01", "GOOG", "STK", "SMART", "BUY");
 filter1.equals(filter2);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentSecType() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");
 filter1.equals(filter2);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentExchange() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
 filter1.equals(filter2);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsMethodWithDifferentSide() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
 filter1.equals(filter2);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullFields() {
 filter1 = new ExecutionFilter(1, "AC123", null, "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, null, null, null, null, null, null);
 filter1.equals(filter2);
 }
}