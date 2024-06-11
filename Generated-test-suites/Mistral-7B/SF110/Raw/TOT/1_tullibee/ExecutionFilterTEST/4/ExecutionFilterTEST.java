import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 @Test
 public void testEquals_sameInstance() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
assertTrue(filter1.equals(filter1));
 }

 @Test
 public void testEquals_sameValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentClientId() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentAccountCode() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentTime() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentSymbol() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "GOOG", "STK", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentSecType() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentExchange() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_differentSide() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
 assertFalse(filter1.equals(filter2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEquals_nullObject() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = null;
 assertFalse(filter1.equals(filter2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEquals_differentTypes() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Object filter2 = new Object();
 assertFalse(filter1.equals(filter2));
 }
}