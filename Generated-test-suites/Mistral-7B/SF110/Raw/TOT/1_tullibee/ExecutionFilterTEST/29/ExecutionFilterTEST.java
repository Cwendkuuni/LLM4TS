import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testConstructorWithValidParameters() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 assertNotNull(filter);
 }

 @Test
 public void testEqualsWithSameObject() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 ExecutionFilter filter2 = filter1;
 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentObjectsButSameValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 assertTrue(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentObjectsButDifferentValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "GOOG", "OPT", "NYSE", "SELL");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithNullObject() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 assertFalse(filter.equals(null));
 }

 @Test
 public void testHashCodeConsistency() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 assertTrue(filter1.hashCode() == filter2.hashCode());
 }

 @Test
 public void testToString() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 String expected = "ExecutionFilter{m_clientId=1, m_acctCode='AC123', m_time='2022-01-01 10:30:00', m_symbol='AAPL', m_secType='STK', m_exchange='SMART_ routing_test_exchange', m_side='BUY'}";
 assertTrue(filter.toString().equals(expected));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullClientId() {
 new ExecutionFilter(null, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullAccountCode() {
 new ExecutionFilter(1, null, "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullTime() {
 new ExecutionFilter(1, "AC123", null, "AAPL", "STK", "SMART_ routing_test_exchange", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSymbol() {
 new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", null, "STK", "SMART_ routing_test_exchange", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSecType() {
 new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", null, "SMART_ routing_test_exchange", "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullExchange() {
 new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", null, "BUY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSide() {
 new ExecutionFilter(1, "AC123", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routing_test_exchange", null);
 }
}