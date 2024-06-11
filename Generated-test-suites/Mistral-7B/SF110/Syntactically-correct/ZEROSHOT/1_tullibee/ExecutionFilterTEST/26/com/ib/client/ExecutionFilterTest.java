import org.junit.Assert;import org.junit.Test;import org.junit.runner.RunWith;import org.junit.runners.Parameterized;

import java.util.Arrays;import java.util.Collections;import java.util.List;

@RunWith(Parameterized.class)
public class ExecutionFilterTest {

 private ExecutionFilter filter;
 private int clientId;
 private String acctCode;
 private String time;
 private String symbol;
 private String secType;
 private String exchange;
 private String side;
 private boolean expectedResult;

 @Parameterized.Parameters(name = "{index}: test{0}(clientId={1}, acctCode={2}, time={3}, symbol={4}, secType={5}, exchange={6}, side={7})")
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test case 1
 new Object[] { new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY"), true, new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY") },
 // Test case 2
 new Object[] { new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY"), false, new ExecutionFilter(1, "TestAcct2", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY") },
 // Test case 3
 new Object[] { new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY"), false, new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:31", "AAPL", "STK", "SMART", "BUY") },
 // Test case 4
 new Object[] { new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY"), false, new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "GOOG", "STK", "SMART", "BUY") },
 // Test case 5
 new Object[] { new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "OPT", "SMART", "BUY"), false, new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY") },
 // Test case 6
 new Object[] { new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "SELL"), false, new ExecutionFilter(1, "TestAcct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "BUY") }
 });
 }

 @Parameterized.Constructor
 public ExecutionFilterTest(ExecutionFilter filter, boolean expectedResult) {
 this.filter = filter;
 this.expectedResult = expectedResult;
 }

 @Parameterized.Constructor
 public ExecutionFilterTest(ExecutionFilter filter, ExecutionFilter otherFilter, boolean expectedResult) {
 this.filter = filter;
 this.clientId = filter.m_clientId;
 this.acctCode = filter.m_acctCode;
 this.time = filter.m_time;
 this.symbol = filter.m_symbol;
 this.secType = filter.m_secType;
 this.exchange = filter.m_exchange;
 this.side = filter.m_side;
 this.expectedResult = expectedResult;
 this.otherFilter = otherFilter;
 }

 @Test
 public void testEquals() {
 ExecutionFilter filter1 = filter;
 ExecutionFilter filter2 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 boolean actualResult = filter1.equals(filter2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNull() {
 filter.equals(null);
 }

 @Test
 public void testEqualsWithDifferentInstances() {
 ExecutionFilter filter1 = filter;
 ExecutionFilter filter2 = new ExecutionFilter(clientId, "TestAcct2", time, symbol, secType, exchange, side);
 boolean actualResult = filter1.equals(filter2);
 Assert.assertFalse(actualResult);
 }
}