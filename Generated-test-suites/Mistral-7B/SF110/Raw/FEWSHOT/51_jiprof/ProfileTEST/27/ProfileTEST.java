package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.*;
import java.io.*;
import org.junit.Test;
import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        assertFalse(Controller._profile);
        Profile.initProfiler();
        assertTrue(Controller._profile);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitTwice() {
        Profile.init();
        Profile.init();
    }

    @Test
    public void testShutdown() throws IOException {
        Profile.init();
        Profile.shutdown();
        assertTrue(Controller._profile);
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testThreads() {
        Profile.init();
        assertEquals(0, Profile._threadDictionary.threads().size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod2");
                Profile.end("TestClass", "testMethod2");
            }
        }).start();
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
        assertEquals(Thread.currentThread().getId() + 1, threads.get(1));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod");
                Profile.endWait("TestClass", "testMethodWait");
                Profile.end("TestClass", "testMethod");
            }
        }).start();
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(3, interactions.size());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod");
            }
        }).start();
        List<Frame> frameList = Profile.frameList();
        assertEquals(0, frameList.size());
        Profile.shutdown();
        frameList = Profile.frameList();
        assertEquals(0, frameList.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod");
                Profile.end("TestClass", "testMethod2");
                Profile.end("TestClass", "testMethod");
            }
        }).start();
        frameList = Profile.frameList();
        assertEquals(3, frameList.size());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.alloc("TestClass");
                Profile.alloc("TestClass");
                Profile.alloc("TestClass2");
            }
        }).start();
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(3, allocations.size());
        assertEquals("TestClass", allocations.get(0).getClassname());
        assertEquals(2, allocations.get(0).getAllocationCount());
        assertEquals("TestClass2", allocations.get(2).getClassname());
        assertEquals(0, allocations.get(2).getAllocationCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                long start = System.nanoTime();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                Profile.end("TestClass", "testMethod");
            }
        }).start();
        long threadTotalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
        assertTrue(threadTotalTime > 1000000000L);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod1");
                Profile.end("TestClass", "testMethod1");
                Profile.start("TestClass", "testMethod2");
                Profile.end("TestClass", "testMethod2");
                Profile.start("TestClass", "testMethod1");
                Profile.end("TestClass", "testMethod1");
            }
        }).start();
        Collections.sort(Profile._frameList, new Comparator<Frame>() {
            @Override
            public int compare(Frame f1, Frame f2) {
                return Long.compare(f1.getBeginTime(), f2.getBeginTime());
            }
        });
        Frame firstFrame = Profile._frameList.get(0);
        Frame lastFrame = Profile._frameList.get(Profile._frameList.size() - 1);
        assertEquals("TestClass", firstFrame.getClassName());
        assertEquals("testMethod1", firstFrame.getMethodName());
        assertEquals("TestClass", lastFrame.getClassName());
        assertEquals("testMethod1", lastFrame.getMethodName());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                long start = System.nanoTime();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                Profile.end("TestClass", "testMethod");
                assertEquals(Profile.getThreadTotalTime(Thread.currentThread().getId()), start + 1000000000L, 0);
            }
        }).start();
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start("TestClass", "testMethod");
                long start = System.nanoTime();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                Profile.beginWait("TestClass", "testMethodWait");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                Profile.endWait("TestClass", "testMethodWait");
                Profile.end("TestClass", "testMethod");
                assertEquals(Profile.getThreadTotalTime(Thread.currentThread().getId()), start + 2000000000L, 0);
            }
        }).start();
    }

    @Test
    public void testUnwind() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    throw new RuntimeException("TestException");
                } catch (RuntimeException e) {
                    Profile.unwind("TestClass", "testMethod", "TestException");
                }
            }
        }).start();
    }

    @Test
    public void testAlloc() {
        Profile.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.alloc("TestClass");
                Profile.alloc("TestClass");
                Profile.alloc("TestClass2");
            }
        }).start();
        ClassAllocation ca = Profile._allocList.get("TestClass");
        assertEquals(2, ca.getAllocationCount());
        ca = Profile._allocList.get("TestClass2");
        assertEquals(0, ca.getAllocationCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.shutdown();
    }
}