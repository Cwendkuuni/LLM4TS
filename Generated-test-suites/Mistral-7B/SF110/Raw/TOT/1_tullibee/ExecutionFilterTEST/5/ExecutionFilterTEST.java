import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter filter1;
 private ExecutionFilter filter2;

 @Test
 public void testConstructor() {
 filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals(1, filter1.getClientId());
 Assert.assertEquals("Acct1", filter1.getAcctCode());
 Assert.assertEquals("2022-01-01 10:00:00", filter1.getTime());
 Assert.assertEquals("AAPL", filter1.getSymbol());
 Assert.assertEquals("STK", filter1.getSecType());
 Assert.assertEquals("SMART", filter1.getExchange());
 Assert.assertEquals("BUY", filter1.getSide());
 }

 @Test
 public void testEquals() {
 filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertTrue(filter1.equals(filter2));

 filter2 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "SELL");

 Assert.assertFalse(filter1.equals(filter2));

 filter2 = new ExecutionFilter(2, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testNull() {
 filter1 = new ExecutionFilter(1, "Acct1", "2022-01-01 10:00:00", "AAPL", "STK", "SMART", "BUY");

 Assert.assertFalse(filter1.equals(null));
 }

 @Test
 public void testEmptyStrings() {
 filter1 = new ExecutionFilter(1, "", "", "", "", "", "");
 filter2 = new ExecutionFilter(1, "", "", "", "", "", "");

 Assert.assertTrue(filter1.equals(filter2));
 }

 @Test(expected = NullPointerException.class)
 public void testNullValues() {
 filter1 = new ExecutionFilter(1, null, null, null, null, null, null);
 }
}