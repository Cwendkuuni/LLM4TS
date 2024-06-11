package com.google.common.util.concurrent;
import static org.junit.Assert.*;
import com.google.common.util.concurrent.Monitor;
import org.junit.Before;
import org.junit.Test;

public class MonitorTest {
    private Monitor monitor;

    @Before
    public void setUp() {
        monitor = new Monitor();
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }
}