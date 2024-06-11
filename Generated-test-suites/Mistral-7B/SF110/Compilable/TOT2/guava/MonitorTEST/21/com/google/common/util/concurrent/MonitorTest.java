package com.google.common.util.concurrent;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import com.google.common.util.concurrent.Monitor;
import org.junit.Test;

public class MonitorTest {
    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        boolean isLocked = monitor.isOccupiedByCurrentThread();
        assertThat(isLocked, is(true));
    }
}