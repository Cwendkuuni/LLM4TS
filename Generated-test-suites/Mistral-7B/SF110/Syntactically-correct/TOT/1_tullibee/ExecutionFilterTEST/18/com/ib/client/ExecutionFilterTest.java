import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals_SameInstance() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter1));
    }

    @Test
    public void testEquals_SameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentTypes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        Object obj = new Object();
        assertFalse(filter1.equals(obj));
    }

    @Test
    public void testEquals_DifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Account2", "2022-01-02", "GOOG", "OPT", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_Null() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(null));
    }

    @Test
    public void testEquals_NullOther() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = null;
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEquals_DifferentClientIds() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentAccountCodes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account2", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentTimes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentSymbols() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "GOOG", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentSecTypes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "OPT", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentExchanges() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentSides() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_EmptyStringForAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_NullForAccountCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_EmptyStringForTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", "", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_NullForTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Account1", null, "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testToString() {
        ExecutionFilter filter = new ExecutionFilter(1, "Account1", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        String expected = "ExecutionFilter{m_clientId=1, m_acctCode='Account1', m_time='2022-01-01', m_symbol='AAPL', m_secType='STK', m_exchange='SMART', m_side='BUY'}";
        assertEquals(expected, filter.toString());
    }
}