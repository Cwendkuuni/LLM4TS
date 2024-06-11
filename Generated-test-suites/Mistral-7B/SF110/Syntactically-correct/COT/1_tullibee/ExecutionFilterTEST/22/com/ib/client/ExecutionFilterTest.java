import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Test
 public void testConstructor() {
     executionFilter1 = new ExecutionFilter();
     Assert.assertEquals(0, executionFilter1.getClientId());
     Assert.assertNull(executionFilter1.getAcctCode());
     Assert.assertNull(executionFilter1.getTime());
     Assert.assertNull(executionFilter1.getSymbol());
     Assert.assertNull(executionFilter1.getSecType());
     Assert.assertNull(executionFilter1.getExchange());
     Assert.assertNull(executionFilter1.getSide());

     executionFilter2 = new ExecutionFilter(1, "testAccount", "2022-10-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
     Assert.assertEquals(1, executionFilter2.getClientId());
     Assert.assertEquals("testAccount", executionFilter2.getAcctCode());
     Assert.assertEquals("2022-10-01 10:30:00", executionFilter2.getTime());
     Assert.assertEquals("AAPL", executionFilter2.getSymbol());
     Assert.assertEquals("STK", executionFilter2.getSecType());
     Assert.assertEquals("SMART", executionFilter2.getExchange());
     Assert.assertEquals("BUY", executionFilter2.getSide());
 }

 @Test
 public void testEquals() {
     ExecutionFilter filter1 = new ExecutionFilter();
     ExecutionFilter filter2 = new ExecutionFilter();
     ExecutionFilter filter3 = new ExecutionFilter();
     ExecutionFilter filter4 = new ExecutionFilter(filter1.getClientId(), filter1.getAcctCode(), filter1.getTime(), filter1.getSymbol(), filter1.getSecType(), filter1.getExchange(), filter1.getSide());

     Assert.assertFalse(filter1.equals(null));
     Assert.assertFalse(filter1.equals(new Object()));

     Assert.assertTrue(filter1.equals(filter1));
     Assert.assertTrue(filter1.equals(filter4));

     filter3.setClientId(filter1.getClientId());
     filter3.setAcctCode(filter1.getAcctCode());
     filter3.setTime(filter1.getTime());
     filter3.setSymbol(filter1.getSymbol());
     filter3.setSecType(filter1.getSecType());
     filter3.setExchange(filter1.getExchange());
     filter3.setSide(filter1.getSide());

     Assert.assertTrue(filter1.equals(filter3));

     filter2.setClientId(filter1.getClientId() + 1);
     filter2.setAcctCode("testAccountX");

     Assert.assertFalse(filter1.equals(filter2));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsWithNullObject() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.equals(null);
 }

 @Test
 public void testGetClientId() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setClientId(1);
     Assert.assertEquals(1, filter.getClientId());
 }

 @Test
 public void testSetClientId() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setClientId(1);
     Assert.assertEquals(1, filter.getClientId());
 }

 @Test
 public void testGetAcctCode() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setAcctCode("testAccount");
     Assert.assertEquals("testAccount", filter.getAcctCode());
 }

 @Test
 public void testSetAcctCode() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setAcctCode("testAccount");
     Assert.assertEquals("testAccount", filter.getAcctCode());
 }

 @Test
 public void testGetTime() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setTime("2022-10-01 10:30:00");
     Assert.assertEquals("2022-10-01 10:30:00", filter.getTime());
 }

 @Test
 public void testSetTime() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setTime("2022-10-01 10:30:00");
     Assert.assertEquals("2022-10-01 10:30:00", filter.getTime());
 }

 @Test
 public void testGetSymbol() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setSymbol("AAPL");
     Assert.assertEquals("AAPL", filter.getSymbol());
 }

 @Test
 public void testSetSymbol() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setSymbol("AAPL");
     Assert.assertEquals("AAPL", filter.getSymbol());
 }

 @Test
 public void testGetSecType() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setSecType("STK");
     Assert.assertEquals("STK", filter.getSecType());
 }

 @Test
 public void testSetSecType() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setSecType("STK");
     Assert.assertEquals("STK", filter.getSecType());
 }

 @Test
 public void testGetExchange() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setExchange("SMART");
     Assert.assertEquals("SMART", filter.getExchange());
 }

 @Test
 public void testSetExchange() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setExchange("SMART");
     Assert.assertEquals("SMART", filter.getExchange());
 }

 @Test
 public void testGetSide() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setSide("BUY");
     Assert.assertEquals("BUY", filter.getSide());
 }

 @Test
 public void testSetSide() {
     ExecutionFilter filter = new ExecutionFilter();
     filter.setSide("BUY");
     Assert.assertEquals("BUY", filter.getSide());
 }
}