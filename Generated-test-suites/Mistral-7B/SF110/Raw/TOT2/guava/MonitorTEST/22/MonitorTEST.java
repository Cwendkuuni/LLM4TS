
package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import org.junit.Test;

public class MonitorTest {

  @Test
  public void testMonitorConstruction() {
    Monitor monitor = new Monitor();
    assertFalse(monitor.isOccupied());
  }

  @Test
  public void testEnterMethodBasicFunctionality() throws InterruptedException {
    Monitor monitor = new Monitor();
    monitor.enter();
    assertTrue(monitor.isOccupiedByCurrentThread());
    monitor.leave();
    assertFalse(monitor.isOccupiedByCurrentThread());
  }

  @Test
  public void testEnterInterruptibly() throws InterruptedException {
    Monitor monitor = new Monitor();
    Thread thread = new Thread(() -> {
      monitor.enterInterruptibly();
    });
    monitor.enter();
    thread.start();
    thread.join();
    assertFalse(monitor.isOccupiedByCurrentThread());
    assertFalse(thread.isInterrupted());

    monitor.enter();
    thread.interrupt();
    monitor.enterInterruptibly();
    assertTrue(thread.isInterrupted());
  }

  @Test
  public void testTryEnter() throws InterruptedException {
    Monitor monitor = new Monitor();
    assertFalse(monitor.tryEnter());
    monitor.enter();
    monitor.leave();
    assertTrue(monitor.tryEnter());
  }

  @Test
  public void testTryEnterIf() throws InterruptedException {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return true;
      }
    };
    assertFalse(monitor.tryEnterIf(guard));
    monitor.enter();
    monitor.leave();
    assertTrue(monitor.tryEnterIf(guard));
  }

  @Test
  public void testEnterWhenWithInterrupt() throws InterruptedException {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return false;
      }
    };
    monitor.enterWhen(guard);
    monitor.enterInterruptibly();
    assertTrue(Thread.interrupted());
  }

  @Test
  public void testEnterWhenWithTimeout() throws InterruptedException {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return false;
      }
    };
    monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
    assertTrue(Thread.interrupted());
  }

  @Test
  public void testEnterWhenUninterruptiblyWithTimeout() {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return false;
      }
    };
    monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
    assertFalse(Thread.interrupted());
  }

  @Test
  public void testEnterWhenAndAwaitNanosWithInterrupt() throws InterruptedException {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return false;
      }
    };
    monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
    Thread.sleep(2);
    monitor.enterInterruptibly();
    assertTrue(Thread.interrupted());
  }

  @Test
  public void testEnterWhenAndAwaitNanosUninterruptiblyWithTimeout() {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return false;
      }
    };
    monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
    Thread.sleep(2);
    assertFalse(Thread.interrupted());
  }

  @Test
  public void testEnterWhenAndAwaitNanosUninterruptiblyWithNoTimeout() {
    Monitor monitor = new Monitor();
    Monitor.Guard guard = new Monitor.Guard() {
      @Override
      public boolean isSatisfied() {
        return false;
      }
    };
    monitor.enterWhenUninterruptibly(guard);
    Thread.sleep(2);
    assertFalse(Thread.interrupted());
  }
}