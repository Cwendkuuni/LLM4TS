package org.mockito.internal.util; // import statements here

public class TimerTest {

    

    @Test
    public void testIsCounting_TimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
        Thread.yield(); // yield to allow the timer to advance
        assertTrue(timer.isCounting());
    }

    

}