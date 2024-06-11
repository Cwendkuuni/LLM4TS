import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExecutionFilterTest {

 private ExecutionFilter executionFilter1;
 private ExecutionFilter executionFilter2;

 @Before
 public void setUp() {
 executionFilter1 = new ExecutionFilter(1, "AC12345", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routed_FE", "BUY");
 executionFilter2 = new ExecutionFilter(1, "AC12345", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routed_FE", "BUY");
 }

 @Test
 public void testExecutionFilterConstructor() {
 ExecutionFilter executionFilter3 = new ExecutionFilter(2, "AC67890", "2022-01-02 11:45:00", "GOOG", "OPT", "NASDAQ", "SELL");

 assertEquals(2, executionFilter3.getClientId());
 assertEquals("AC67890", executionFilter3.getAcctCode());
 assertEquals("2022-01-02 11:45:00", executionFilter3.getTime());
 assertEquals("GOOG", executionFilter3.getSymbol());
 assertEquals("OPT", executionFilter3.getSecType());
 assertEquals("NASDAQ", executionFilter3.getExchange());
 assertEquals("SELL", executionFilter3.getSide());
 }

 @Test
 public void testExecutionFilterEquals() {
 //same object
 assertTrue(executionFilter1.equals(executionFilter1));

 //same values
 assertTrue(executionFilter1.equals(executionFilter2));

 //different objects with same values
 assertTrue(executionFilter1.equals(new ExecutionFilter(1, "AC12345", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routed_FE", "BUY")));

 //different values
 assertFalse(executionFilter1.equals(new ExecutionFilter(1, "AC12345", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_ routed_FE", "SELL")));

 //null
 assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testExecutionFilterHashCode() {
 int hashCode1 = executionFilter1.hashCode();
 int hashCode2 = executionFilter2.hashCode();

 //same values should have same hashcode
 assertEquals(hashCode1, hashCode2);
 }
}