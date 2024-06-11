import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter filter1;
 private ExecutionFilter filter2;

 @Test
 public void testConstructionWithValidParameters() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertEquals(1, filter1.getm_clientId());
 Assert.assertEquals("AC123", filter1.getm_acctCode());
 Assert.assertEquals("2022-01-01 10:30:00", filter1.getm_time());
 Assert.assertEquals("AAPL", filter1.getm_symbol());
 Assert.assertEquals("STK", filter1.getm_secType());
 Assert.assertEquals("SMART", filter1.getm_exchange());
 Assert.assertEquals("BUY", filter1.getm_side());
 }

 @Test
 public void testConstructionWithInvalidParameters() {
 try {
 filter1 = new ExecutionFilter(0, null, null, null, null, null, null);
 Assert.fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testEqualsWithSameObject() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 filter2 = filter1;
 Assert.assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentObjectSameValues() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentObjectDifferentValues() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 filter2 = new ExecutionFilter(2, "AC456", "2022-01-02 11:45:00", "GOOG", "BND", "NASDAQ", "SELL");
 Assert.assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithNullObject() {
 filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(filter1.equals(null));
 }
}