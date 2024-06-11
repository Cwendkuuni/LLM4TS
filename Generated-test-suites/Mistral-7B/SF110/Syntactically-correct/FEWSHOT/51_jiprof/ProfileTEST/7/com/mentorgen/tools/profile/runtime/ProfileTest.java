package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.mentorgen.tools.profile.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        if (Profile._controllerThread != null) {
            assertTrue(Profile._controllerThread.isAlive());
        }
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        String fileName = "profile.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() throws IOException {
        Profile.init();
        Profile.start();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Profile.shutdown();
        assertNotNull(outputStream.toString());
        System.setOut(System.out);
    }

    @Test
    public void testThreads() {
        Profile.init();
        List<Long> threads = Profile.threads();
        assertEquals(0, threads.size());
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Long threadId = 1L;
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(0, interactions.size());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Iterable<Frame> frameList = Profile.frameList();
        assertEquals(0, frameList.spliterator().getSize());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertEquals(0, allocations.spliterator().getSize());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Long threadId = 1L;
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        List<Frame> frameList = new ArrayList<>();
        Collections.sort(frameList, Util.comparatorFor(Frame.class));
        Profile.sortFrameList(Util.comparatorFor(Frame.class));
        assertTrue(frameList.toString().equals(Profile._frameList.toString()));
    }

    @Test
    public void testStartEndMethod() {
        Profile.init();
        String className = "com.mentorgen.tools.profile.runtime.ProfileTest";
        String methodName = "testStartEndMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertEquals(1, frame.getCallCount());
    }

    @Test
    public void testStartEndMethodWithException() {
        Profile.init();
        String className = "com.mentorgen.tools.profile.runtime.ProfileTest";
        String methodName = "testStartEndMethodWithException";
        Profile._debugException = true;
        Profile.start(className, methodName);
        // start method should not throw exception
        Profile.end(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertEquals(1, frame.getCallCount());
    }

    @Test
    public void testBeginEndWait() {
        Profile.init();
        String className = "com.mentorgen.tools.profile.runtime.ProfileTest";
        String methodName = "testBeginEndWait";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testBeginEndWaitWithException() {
        Profile.init();
        String className = "com.mentorgen.tools.profile.runtime.ProfileTest";
        String methodName = "testBeginEndWaitWithException";
        Profile._debugException = true;
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        // beginWait method should not throw exception
        Profile.endWait(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        String className = "com.mentorgen.tools.profile.runtime.ProfileTest";
        String methodName = "testUnwind";
        String exception = "TestException";
        Profile.start(className, methodName);
        Profile.unwind(className, methodName, exception);
        Frame frame = findFrame(className, methodName);
        assertNotNull(frame);
        assertEquals(1, frame.getCallCount());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        String className = "java.util.ArrayList";
        Profile.alloc(className);
        ClassAllocation ca = Profile._allocList.get(className);
        assertNotNull(ca);
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testRun() throws InterruptedException {
        Profile.init();
        Profile.start();
        Thread.sleep(100);
        Profile.shutdown();
        Profile._controller.join();
    }

    private Frame findFrame(String className, String methodName) {
        Frame foundFrame = null;
        for (Frame frame : Profile._frameList) {
            if (frame.getClassName().equals(className) && frame.getMethodName().equals(methodName)) {
                foundFrame = frame;
                break;
            }
        }
        return foundFrame;
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Profile.start();
        String className = "com.mentorgen.tools.profile.runtime.ProfileTest";
        String methodName = "testFindFrame";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Frame foundFrame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertNotNull(foundFrame);
    }

    @Test(expected = NullPointerException.class)
    public void testFindFrameNull() {
        Profile.init();
        Frame foundFrame = Profile.findFrame(null, "className", "methodName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameEmptyProfile() {
        Profile.init();
        Frame foundFrame = Profile.findFrame(Thread.currentThread().getId(), "className", "methodName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameInvalidThreadId() {
        Profile.init();
        Profile.start();
        Frame foundFrame = Profile.findFrame(-1L, "className", "methodName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameInvalidClassName() {
        Profile.init();
        Profile.start();
        Frame foundFrame = Profile.findFrame(Thread.currentThread().getId(), null, "methodName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFrameInvalidMethodName() {
        Profile.init();
        Profile.start();
        Frame foundFrame = Profile.findFrame(Thread.currentThread().getId(), "className", null);
    }

    private static <T> Comparator<T> comparatorFor(Class<T> clazz) {
        return Mockito.spy(Comparator.comparing(PropertyAccessorUtils.getPropertyAccessor(clazz)));
    }
}