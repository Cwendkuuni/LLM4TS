import org.junit.Assert;import org.junit.Test;import org.junit.runner.RunWith;import org.junit.runners.Parameterized;import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;import java.util.Collections;import java.util.List;

@RunWith(Parameterized.class)
public class ExecutionFilterTest {

 private ExecutionFilter executionFilter;
 private int clientId;
 private String acctCode;
 private String time;
 private String symbol;
 private String secType;
 private String exchange;
 private String side;
 private boolean expectedResult;

 @Parameters
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Add test data here
 { 1, "TestAcct1", "2022-01-01", "AAPL", "STK", "SMART", "BUY" }, { 1, "TestAcct1", "2022-01-01", "AAPL", "STK", "SMART", "SELL" },
 { 1, "TestAcct2", "2022-01-01", "AAPL", "OPT", "SMART", "BUY" }, { 1, "TestAcct1", "2022-01-02", "AAPL", "STK", "SMART", "BUY" },
 { 1, "TestAcct1", "2022-01-01", "GOOG", "STK", "SMART", "BUY" }, { 1, "TestAcct1", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY" },
 { 1, "TestAcct1", "2022-01-01", "AAPL", "STK", "SMART", null }, { 1, null, "2022-01-01", "AAPL", "STK", "SMART", "BUY" }
 });
 }

 public ExecutionFilterTest(int p_clientId, String p_acctCode, String p_time, String p_symbol, String p_secType, String p_exchange, String p_side, boolean p_expectedResult) {
 clientId = p_clientId;
 acctCode = p_acctCode;
 time = p_time;
 symbol = p_symbol;
 secType = p_secType;
 exchange = p_exchange;
 side = p_side;
 expectedResult = p_expectedResult;
 executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 }

 @Test
 public void testEqualsNull() {
 // Test case for null comparison
 ExecutionFilter l_other = null;
 Assert.assertFalse(executionFilter.equals(l_other));
 }

 @Test
 public void testEqualsSameObject() {
 // Test case for self comparison
 Assert.assertTrue(executionFilter.equals(executionFilter));
 }

 @Test
 public void testEqualsDifferentObjectsWithSameValues() {
 // Test case for equals comparison with same values
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 Assert.assertTrue(executionFilter.equals(l_other));
 }

 @Test
 public void testEqualsDifferentObjectsWithDifferentValues() {
 // Test case for equals comparison with different values
 ExecutionFilter l_other = new ExecutionFilter(clientId, "TestAcct2", time, symbol, secType, exchange, side);
 Assert.assertFalse(executionFilter.equals(l_other));
 }

 @Test
 public void testEqualsDifferentObjectsWithNullSide() {
 // Test case for equals comparison with null side
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, null, true);
 if (side != null) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executionFilter.equals(l_other));
 }
 }

 @Test
 public void testEqualsDifferentObjectsWithNullClientId() {
 // Test case for equals comparison with null clientId
 ExecutionFilter l_other = new ExecutionFilter(0, acctCode, time, symbol, secType, exchange, side);
 if (clientId != 0) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executationFilter.equals(l_other));
 }
 }

 @Test
 public void testEqualsDifferentObjectsWithNullAcctCode() {
 // Test case for equals comparison with null acctCode
 ExecutionFilter l_other = new ExecutionFilter(clientId, null, time, symbol, secType, exchange, side);
 if (acctCode != null) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executionFilter.equals(l_other));
 }
 }

 @Test
 public void testEqualsDifferentObjectsWithNullTime() {
 // Test case for equals comparison with null time
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, null, symbol, secType, exchange, side);
 if (time != null) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executionFilter.equals(l_other));
 }
 }

 @Test
 public void testEqualsDifferentObjectsWithNullSymbol() {
 // Test case for equals comparison with null symbol
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, time, null, secType, exchange, side);
 if (symbol != null) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executionFilter.equals(l_other));
 }
 }

 @Test
 public void testEqualsDifferentObjectsWithNullSecType() {
 // Test case for equals comparison with null secType
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, time, symbol, null, exchange, side);
 if (secType != null) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executionFilter.equals(l_other));
 }
 }

 @Test
 public void testEqualsDifferentObjectsWithNullExchange() {
 // Test case for equals comparison with null exchange
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, time, symbol, secType, null, side);
 if (exchange != null) {
 Assert.assertFalse(executionFilter.equals(l_other));
 } else {
 Assert.assertTrue(executionFilter.equals(l_other));
 }
 }

 @Test
 public void testHashCode() {
 // Test case for hashCode method
 int l_hashCode = executionFilter.hashCode();
 ExecutionFilter l_other = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 int l_otherHashCode = l_other.hashCode();
 Assert.assertEquals(l_hashCode, l_otherHashCode);
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 // Test case for equals method with different objects
 ExecutionFilter l_other = new ExecutionFilter(clientId + 1, acctCode, time, symbol, secType, exchange, side);
 Assert.assertFalse(executionFilter.equals(l_other));
 }
}