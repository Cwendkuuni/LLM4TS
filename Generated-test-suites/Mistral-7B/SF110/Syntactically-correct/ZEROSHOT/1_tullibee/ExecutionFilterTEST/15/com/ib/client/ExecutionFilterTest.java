import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

 @Test
 public void testConstructorWithDefaultValues() {
 ExecutionFilter executionFilter = new ExecutionFilter();
 Assert.assertEquals(0, executionFilter.getClientId());
 Assert.assertNull(executionFilter.getAcctCode());
 Assert.assertNull(executionFilter.getTime());
 Assert.assertNull(executionFilter.getSymbol());
 Assert.assertNull(executionFilter.getSecType());
 Assert.assertNull(executionFilter.getExchange());
 Assert.assertNull(executionFilter.getSide());
 }

 @Test
 public void testConstructorWithValidValues() {
 final int clientId = 1;
 final String acctCode = "TestAcctCode";
 final String time = "2022-01-01 12:34:56";
 final String symbol = "IBM";
 final String secType = "STK";
 final String exchange = "SMART";
 final String side = "BUY";
 ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 Assert.assertEquals(clientId, executionFilter.getClientId());
 Assert.assertEquals(acctCode, executionFilter.getAcctCode());
 Assert.assertEquals(time, executionFilter.getTime());
 Assert.assertEquals(symbol, executionFilter.getSymbol());
 Assert.assertEquals(secType, executionFilter.getSecType());
 Assert.assertEquals(exchange, executionFilter.getExchange());
 Assert.assertEquals(side, executionFilter.getSide());
 }

 @Test
 public void testEqualsWithSameObject() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = executionFilter1;
 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithNull() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(2, "TestAcctCodeDifferent", "2022-01-01 12:34:57", "IBM", "STK", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentClientId() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(2, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentAcctCode() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCodeDifferent", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentTime() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:57", "IBM", "STK", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentSymbol() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "AAPL", "STK", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentSecType() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "OPT", "SMART", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentExchange() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "NASDAQ", "BUY");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithDifferentSide() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "SELL");
 Assert.assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testEqualsWithSameValuesButDifferentObjects() {
 ExecutionFilter executionFilter1 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 ExecutionFilter executionFilter2 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 Assert.assertTrue(executionFilter1.equals(executionFilter2));
 ExecutionFilter executionFilter3 = new ExecutionFilter(1, "TestAcctCode", "2022-01-01 12:34:56", "IBM", "STK", "SMART", "BUY");
 Assert.assertNotSame(executionFilter1, executionFilter3);
 Assert.assertTrue(executionFilter1.equals(executionFilter3));
 }
}