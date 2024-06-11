package com.mentorgen.tools.profile.runtime;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testStart() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {});
        thread.start();
        Profile.start();
        assertTrue(Profile.threads().contains(thread.getId()));
    }

    @Test
    public void testEnd() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {
            Profile.start("TestClassName", "testMethod");
            // Perform some work here
        });
        thread.start();
        thread.join();
        Frame frame = findFrame(thread.getId(), "TestClassName", "testMethod");
        assertNotNull(frame);
        assertTrue(frame.isEnded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullClassName() {
        Profile.initProfiler();
        Profile.start(null, "testMethod");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullMethodName() {
        Profile.initProfiler();
        Profile.start("TestClassName", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndNullClassName() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {
            Profile.start("TestClassName", "testMethod");
            // Perform some work here
        });
        thread.start();
        thread.join();
        try {
            findFrame(thread.getId(), null, "testMethod");
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got " + e.getClass().getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndNullMethodName() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {
            Profile.start("TestClassName", "testMethod");
            // Perform some work here
        });
        thread.start();
        thread.join();
        try {
            findFrame(thread.getId(), "TestClassName", null);
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got " + e.getClass().getName());
        }
    }

    @Test
    public void testEndNonExistentThread() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {});
        assertFalse(Profile.threads().contains(thread.getId()));
        try {
            findFrame(thread.getId(), "TestClassName", "testMethod");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testEndAlreadyEnded() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {
            Profile.start("TestClassName", "testMethod");
            Profile.end("TestClassName", "testMethod");
            // Perform some work here
            Profile.end("TestClassName", "testMethod");
        });
        thread.start();
        thread.join();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndNotProfiling() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {});
        assertFalse(Profile.threads().contains(thread.getId()));
        try {
            findFrame(thread.getId(), "TestClassName", "testMethod");
        } catch (Exception e) {
            fail("Expected IllegalStateException, but got " + e.getClass().getName());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testThreadInterrupted() {
        Profile.initProfiler();
        Thread thread = new Thread(() -> {
            try {
                Profile.start("TestClassName", "testMethod");
                Thread.currentThread().interrupt();
                Thread.sleep(100);
                Profile.end("TestClassName", "testMethod");
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected InterruptedException", e);
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testMaxThreads() {
        Profile.initProfiler();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            threads.add(new Thread(() -> {}));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        try {
            Profile.start("TestClassName", "testMethod");
            fail("Expected IllegalStateException, but no exception was thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMaxFrames() {
        Profile.initProfiler();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    Profile.start("TestClassName", "testMethod" + j);
                    Profile.end("TestClassName", "testMethod" + j);
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        try {
            Profile.start("TestClassName", "testMethod1000");
            fail("Expected IllegalStateException, but no exception was thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMaxMethods() {
        Profile.initProfiler();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 2002; j++) {
                    Profile.start("TestClassName" + i, "testMethod" + j);
                    Profile.end("TestClassName" + i, "testMethod" + j);
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        try {
            Profile.start("TestClassName1001", "testMethod2003");
            fail("Expected IllegalStateException, but no exception was thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    private Frame findFrame(long threadId, String className, String methodName) throws Exception {
        Frame frame = null;
        synchronized (Profile._lock) {
            for (Frame f : Profile._frameList) {
                if (f.getThreadId() == threadId && f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
                    frame = f;
                    break;
                }
            }
        }
        if (frame == null) {
            throw new Exception("Frame not found");
        }
        return frame;
    }
}