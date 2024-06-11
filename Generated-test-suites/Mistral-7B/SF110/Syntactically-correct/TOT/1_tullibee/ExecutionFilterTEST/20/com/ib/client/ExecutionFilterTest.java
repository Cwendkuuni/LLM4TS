import org.junit.Assert;
import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testConstructor() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_USA", "BUY");
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_USA", "SELL");

 Assert.assertNotEquals(executionFilter, executionFilter1); //Check different instances

 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_USA", "BUY");

 Assert.assertEquals(executationFilter, executionFilter2); //Check same instance
 }

 @Test
 public void testEquals() {
 ExecutionFilter executionFilter = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_USA", "BUY");
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_USA", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "AAPL", "STK", "SMART_Routing_USA", "SELL");
 ExecutionFilter executionFilter3 = new ExecutionFilter(1, "JohnDoe", "2022-01-01 10:30:00", "GOOG", "STK", "SMART_Routing_USA", "BUY");

 Assert.assertTrue(executionFilter.equals(executionFilter1)); //Check equals with same values
 Assert.assertFalse(executionFilter.equals(executionFilter2)); //Check not equals with different values
 Assert.assertFalse(executionFilter.equals(executionFilter3)); //Check not equals with different symbol
 }

 @Test
 public void testEmptyConstructor() {
 ExecutionFilter executionFilter = new ExecutionFilter();
 ExecutionFilter executionFilter1 = new ExecutionFilter();

 Assert.assertNotEquals(executionFilter, executionFilter1); //Check different instances

 ExecutionFilter executionFilter2 = new ExecutionFilter();

 Assert.assertEquals(executionFilter, executionFilter2); //Check same instance
 }
}