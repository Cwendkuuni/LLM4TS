import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

public class MonitorTest {

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertThat(monitor.isOccupiedByCurrentThread(), is(true));
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterDifferentThread() {
        Monitor monitor = new Monitor();
        Thread otherThread = new Thread(() -> monitor.enter());
        otherThread.start();
        monitor.enter();
    }

    @Test
    public void testEnterMultipleTimes() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        assertThat(monitor.isOccupiedByCurrentThread(), is(true));
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterAlreadyLocked() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.enter();
        Thread otherThread = new Thread(() -> monitor.enter());
        otherThread.start();
    }

    @Test
    public void testEnterBlockedByCondition() {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Thread otherThread = new Thread(() -> {
            monitor.enter();
            monitor.leave();
            condition.signal();
        });
        otherThread.start();
        monitor.enterWhen(condition);
    }
}