package com.ib.client;
import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertEquals(0, filter.m_clientId);
    }
}