import org.junit.Test;
import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testExecutionFilterConstructor() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter expected = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertEquals(expected, filter);
 }

 @Test
 public void testExecutionFilterEqualsNull() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsSelf() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertTrue(filter.equals(filter));
 }

 @Test
 public void testExecutionFilterEqualsDifferentClientId() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentAcctCode() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC456", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentTime() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:31:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentSymbol() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "GOOG", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentSecType() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "OPT", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentExchange() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "NYSE", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsDifferentSide() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "SELL");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsSameValuesInDifferentOrder() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "BUY", "SMART", "AAPL", "STK", "SMART", 0, "01-01-2022", "AC123");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testExecutionFilterEqualsSameValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART", "BUY");
 assertTrue(filter1.equals(filter2));
 }
}