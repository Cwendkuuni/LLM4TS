package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.mentorgen.tools.profile.*;
import java.util.*;
import java.io.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ProfileTest {

    private static final String CLASS_NAME = "TestClass";
    private static final String METHOD_NAME = "testMethod";
    private static final String THREAD_ID = "12345";
    private static final String FILENAME = "test.profile";
    private static final boolean DEBUG_START = true;
    private static final boolean DEBUG_EXCEPTION = false;
    private static final Controller MOCK_CONTROLLER = mock(Controller.class);

    @Before
    public void setUp() {
        Profile.init();
        when(MOCK_CONTROLLER.start()).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Profile.start();
                return null;
            }
        });
        when(MOCK_CONTROLLER.stop()).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Profile.stop();
                return null;
            }
        });
        Profile._controller = MOCK_CONTROLLER;
    }

    @After
    public void tearDown() {
        Profile.clear();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        verify(MOCK_CONTROLLER, times(1)).start();
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.stop();
        Profile.clear();
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStartEndMethods() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(CLASS_NAME, METHOD_NAME);
                // Run some code here to test the method
                Profile.end(CLASS_NAME, METHOD_NAME);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(Profile._threadDictionary.containsKey(Long.parseLong(THREAD_ID)));
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Long.parseLong(THREAD_ID));
        assertNotNull(frame);
        assertEquals(CLASS_NAME, frame.getClassName());
        assertEquals(METHOD_NAME, frame.getMethodName());
        assertEquals(THREAD_ID, frame.getThreadId());
        assertTrue(Profile._frameList.contains(frame));
    }

    @Test
    public void testThreadsInteractions() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(CLASS_NAME, METHOD_NAME);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end(CLASS_NAME, METHOD_NAME);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(CLASS_NAME, METHOD_NAME);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Profile.end(CLASS_NAME, METHOD_NAME);
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Long> threads = new ArrayList<>(Profile.threads());
        assertTrue(threads.contains(Long.parseLong(THREAD_ID + thread1)));
        assertTrue(threads.contains(Long.parseLong(THREAD_ID + thread2)));
        List<Frame> interactions1 = new ArrayList<>(Profile.interactions(Long.parseLong(THREAD_ID + thread1)));
        List<Frame> interactions2 = new ArrayList<>(Profile.interactions(Long.parseLong(THREAD_ID + thread2)));
        assertEquals(1, interactions1.size());
        assertEquals(1, interactions2.size());
        Frame interaction1 = interactions1.get(0);
        Frame interaction2 = interactions2.get(0);
        assertEquals(interaction1, interaction2);
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.alloc(CLASS_NAME);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClassAllocation allocation = Profile._allocList.get(CLASS_NAME);
        assertNotNull(allocation);
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testFileName() {
        Profile.init();
        Profile.initProfiler();
        Profile.start();
        Profile.setFileName(FILENAME);
        try (FileOutputStream outputStream = new FileOutputStream(FILENAME)) {
            outputStream.write("Test data.".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Profile.shutdown();
        assertTrue(new File(FILENAME).exists());
    }

    @Test
    public void testDebugStart() {
        Profile.init();
        Profile._debugStart = DEBUG_START;
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile.start(CLASS_NAME, METHOD_NAME);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (DEBUG_START) {
            List<Frame> frames = new ArrayList<>(Profile.frameList());
            Frame frame = frames.get(frames.size() - 1);
            assertTrue(frame.isDebugStart());
        } else {
            assertTrue(Profile.frameList().isEmpty());
        }
    }

    @Test
    public void testDebugException() {
        Profile.init();
        Profile._debugException = DEBUG_EXCEPTION;
        Profile.initProfiler();
        Profile.start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    throw new RuntimeException("Test exception");
                } catch (RuntimeException e) {
                    Profile.unwind(CLASS_NAME, METHOD_NAME, e.getMessage());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (DEBUG_EXCEPTION) {
            List<Frame> frames = new ArrayList<>(Profile.frameList());
            Frame frame = frames.get(frames.size() - 1);
            assertTrue(frame.isDebugException());
        } else {
            assertTrue(Profile.frameList().isEmpty());
        }
    }
}