import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

 @Test
 public void testConstructor() {
 ExecutionFilter ef = new ExecutionFilter();
 assertNotNull(ef);
 ExecutionFilter ef1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter ef2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter ef3 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMAART", "BUY");
 ExecutionFilter ef4 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");

 assertEquals(1, ef1.getm_clientId());
 assertEquals("account1", ef1.getm_acctCode());
 assertEquals("2022-01-01", ef1.getm_time());
 assertEquals("AAPL", ef1.getm_symbol());
 assertEquals("STK", ef1.getm_secType());
 assertEquals("SMART", ef1.getm_exchange());
 assertEquals("BUY", ef1.getm_side());

 assertEquals(1, ef2.getm_clientId());
 assertEquals("Account1", ef2.getm_acctCode());
 assertEquals("2022-01-01", ef2.getm_time());
 assertEquals("AAPL", ef2.getm_symbol());
 assertEquals("STK", ef2.getm_secType());
 assertEquals("SMART", ef2.getm_exchange());
 assertEquals("BUY", ef2.getm_side());

 assertFalse(ef.equals(ef1));
 assertFalse(ef.equals(null));
 assertTrue(ef1.equals(ef1));
 assertTrue(ef1.equals(ef2));
 assertFalse(ef1.equals(ef3));
 assertFalse(ef1.equals(ef4));
 }

 @Test
 public void testEquals() {
 ExecutionFilter ef1 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter ef2 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
 ExecutionFilter ef3 = new ExecutionFilter(1, "account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
 ExecutionFilter ef4 = new ExecutionFilter(2, "account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

 assertFalse(ef1.equals(null));
 assertFalse(ef1.equals(ef3));
 assertTrue(ef1.equals(ef2));
 assertFalse(ef1.equals(ef4));
 }

 @Test
 public void testGetters() {
 ExecutionFilter ef = new ExecutionFilter();
 ef.setm_clientId(1);
 ef.setm_acctCode("account1");
 ef.setm_time("2022-01-01");
 ef.setm_symbol("AAPL");
 ef.setm_secType("STK");
 ef.setm_exchange("SMART");
 ef.setm_side("BUY");

 assertEquals(1, ef.getm_clientId());
 assertEquals("account1", ef.getm_acctCode());
 assertEquals("2022-01-01", ef.getm_time());
 assertEquals("AAPL", ef.getm_symbol());
 assertEquals("STK", ef.getm_secType());
 assertEquals("SMART", ef.getm_exchange());
 assertEquals("BUY", ef.getm_side());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValues() {
 ExecutionFilter ef = new ExecutionFilter();
 ef.setm_clientId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyStringValues() {
 ExecutionFilter ef = new ExecutionFilter();
 ef.setm_acctCode("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullStringValues() {
 ExecutionFilter ef = new ExecutionFilter();
 ef.setm_symbol(null);
 }

 @Test
 public void testToString() {
 ExecutionFilter ef = new ExecutionFilter();
 ef.setm_clientId(1);
 ef.setm_acctCode("account1");
 ef.setm_time("2022-01-01");
 ef.setm_symbol("AAPL");
 ef.setm_secType("STK");
 ef.setm_exchange("SMART");
 ef.setm_side("BUY");

 String str = ef.toString();
 String expected = "ExecutionFilter{m_clientId=1, m_acctCode='account1', m_time='2022-01-01', m_symbol='AAPL', m_secType='STK', m_exchange='SMART', m_side='BUY'}";
 assertEquals(expected, str);
 }
}