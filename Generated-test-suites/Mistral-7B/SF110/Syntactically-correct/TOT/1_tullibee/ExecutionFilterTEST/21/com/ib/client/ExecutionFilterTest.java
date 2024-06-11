import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterConstructor() {
 ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertNotNull(filter);
 assertEquals(1, filter.getClientId());
 assertEquals("Account1", filter.getAcctCode());
 assertEquals("2022-01-01 10:30:00", filter.getTime());
 assertEquals("AAPL", filter.getSymbol());
 assertEquals("STK", filter.getSecType());
 assertEquals("SMART", filter.getExchange());
 assertEquals("BUY", filter.getSide());
 }

 @Test
 public void testExecutionFilterEquals() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter3 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
 ExecutionFilter filter4 = new ExecutionFilter(2, "Account2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");

 assertTrue(filter1.equals(filter2));
 assertFalse(filter1.equals(filter3));
 assertFalse(filter1.equals(filter4));
 }

 @Test
 public void testExecutionFilterNull() {
 ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter.equals(null));
 }

 @Test
 public void testExecutionFilterEmptyString() {
 ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "", "STK", "SMART", "BUY");
 assertFalse(filter.equals(new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "", "STK", "SMART", "BUY")));
 }

 @Test
 public void testExecutionFilterDifferentClientId() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterDifferentAcctCode() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account2", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterDifferentTime() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterDifferentSymbol() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterDifferentSecType() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterDifferentExchange() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "NASDAQ", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterDifferentSide() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterSameDataButDifferentObjects() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(new ExecutionFilter(1, "Account1", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY"));
 assertFalse(filter1.equals(filter2));
 }
}