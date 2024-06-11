package com.ib.client;
import org.junit.Test;
import org.junit.Assert;
import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
         Assert.assertTrue(filter1.equals(filter2));

        ExecutionFilter filter3 = new ExecutionFilter(2, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        Assert.assertFalse(filter1.equals(filter3));

        ExecutionFilter filter4 = new ExecutionFilter();
        Assert.assertFalse(filter1.equals(filter4));
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter3 = new ExecutionFilter(1, "acct2", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter4 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:31", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter5 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Sell");

        Assert.assertTrue(filter1.equals(filter2));
        Assert.assertFalse(filter1.equals(filter3));
        Assert.assertFalse(filter1.equals(filter4));
        Assert.assertFalse(filter1.equals(filter5));
    }

    @Test
    public void testExecutionFilterHashCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter3 = new ExecutionFilter(1, "acct2", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter4 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:31", "AAPL", "STK", "SMART", "Buy");
        ExecutionFilter filter5 = new ExecutionFilter(1, "acct1", "2022-01-01 10:15:30", "AAPL", "STK", "SMART", "Sell");

        int hashCode1 = filter1.hashCode();
        int hashCode2 = filter2.hashCode();
        int hashCode3 = filter3.hashCode();
        int hashCode4 = filter4.hashCode();
        int hashCode5 = filter5.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
        Assert.assertNotEquals(hashCode1, hashCode3);
        Assert.assertNotEquals(hashCode1, hashCode4);
        Assert.assertNotEquals(hashCode1, hashCode5);
    }
}