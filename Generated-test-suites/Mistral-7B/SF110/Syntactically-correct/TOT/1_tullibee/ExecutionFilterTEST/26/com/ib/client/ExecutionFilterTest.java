import org.junit.Test;
import org.junit.Assert.*;
import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testEquals_SameObject() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = filter1;

 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEquals_DifferentObjects_SameValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEquals_DifferentObjects_DifferentValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "testAccount2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_DifferentObjects_SameValues_Null() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertFalse(filter1.equals(null));
 }

 @Test
 public void testEquals_DifferentObjects_DifferentValues_Null() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter filter2 = null;

 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEquals_DifferentTypes() {
 ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 Object obj = new Object();

 assertFalse(filter.equals(obj));
 }
}