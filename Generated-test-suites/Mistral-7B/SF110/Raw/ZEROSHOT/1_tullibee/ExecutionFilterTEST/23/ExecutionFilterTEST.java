import org.junit.Test;
import org.junit.Assert.*;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructionWithDefaultValues() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);
    }

    @Test
    public void testExecutionFilterConstructionWithGivenValues() {
        String acctCode = "123456";
        String timeStamp = "2022-01-01 12:34:56";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "SMART";
        String side = "BUY";
        int clientId = 1;

        ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, timeStamp, symbol, secType, exchange, side);

        assertEquals(clientId, filter.m_clientId);
        assertEquals(acctCode, filter.m_acctCode);
        assertEquals(timeStamp, filter.m_time);
        assertEquals(symbol, filter.m_symbol);
        assertEquals(secType, filter.m_secType);
        assertEquals(exchange, filter.m_exchange);
        assertEquals(side, filter.m_side);
    }

    @Test
    public void testEqualsMethod() {
        String acctCode1 = "123456";
        String acctCode2 = "654321";
        String timeStamp1 = "2022-01-01 12:34:56";
        String timeStamp2 = "2022-01-01 13:34:56";
        String symbol1 = "AAPL";
        String symbol2 = "MSFT";
        String secType1 = "STK";
        String secType2 = "OPT";
        String exchange1 = "SMART";
        String exchange2 = "BATS";
        String side1 = "BUY";
        String side2 = "SELL";
        int clientId1 = 1;
        int clientId2 = 2;

        ExecutionFilter filter1 = new ExecutionFilter(clientId1, acctCode1, timeStamp1, symbol1, secType1, exchange1, side1);
        ExecutionFilter filter2 = new ExecutionFilter(clientId2, acctCode2, timeStamp2, symbol2, secType2, exchange2, side2);
        ExecutionFilter filter3 = new ExecutionFilter(clientId1, acctCode1, timeStamp1, symbol1, secType1, exchange1, side1);

        assertTrue(filter1.equals(filter3));
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testHashCodeConsistency() {
        String acctCode = "123456";
        String timeStamp = "2022-01-01 12:34:56";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "SMART";
        String side = "BUY";
        int clientId = 1;

        ExecutionFilter filter1 = new ExecutionFilter(clientId, acctCode, timeStamp, symbol, secType, exchange, side);
        String symbol2 = "MSFT";
        ExecutionFilter filter2 = new ExecutionFilter(clientId, acctCode, timeStamp, symbol, secType, exchange, side);
        filter2.setSymbol(symbol2);

        int hashCode1 = filter1.hashCode();
        int hashCode2 = filter2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeConsistencyWithDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "timeStamp1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acctCode2", "timeStamp2", "symbol2", "secType2", "exchange2", "side2");

        int hashCode1 = filter1.hashCode();
        int hashCode2 = filter2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}