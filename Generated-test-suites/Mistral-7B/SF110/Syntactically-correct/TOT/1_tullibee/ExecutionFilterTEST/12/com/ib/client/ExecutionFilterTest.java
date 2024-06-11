import org.junit.Test;
import static org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testConstructorWithValidArgs() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter expectedFilter = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 assertTrue(filter.equals(expectedFilter));
 }

 @Test
 public void testEqualsWithNullObject() {
 ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 assertFalse(filter.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClientId() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(2, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentAccountCode() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC456", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentTime() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-02 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentSymbol() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "GOOG", "STK", "IDEALPRO", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentSecType() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "OPT", "IDEALPRO", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentExchange() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "NYSE", "BUY");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithDifferentSide() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "SELL");
 assertFalse(filter1.equals(filter2));
 }

 @Test
 public void testEqualsWithSameValues() {
 ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2022-01-01 12:34:56", "AAPL", "STK", "IDEALPRO", "BUY");
 assertTrue(filter1.equals(filter2));
 }
}