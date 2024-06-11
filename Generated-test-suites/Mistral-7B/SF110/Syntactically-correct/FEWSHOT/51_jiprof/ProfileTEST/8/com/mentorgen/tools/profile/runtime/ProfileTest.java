package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mentorgen.tools.profile.output.Frame;
import com.mentorgen.tools.profile.output.Method;

public final class ProfileTest {

    @Test
    public void testInitProfilerAndClear() {
        Profile.initProfiler();
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testInitAndStart() {
        Profile.init();
        Profile.start();
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testInitAndStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertTrue(Profile._controller.isStopped());
    }

    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Frame frame1 = startMethod(Thread.class, "run", new Object[0]);
        Frame frame2 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame3 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame4 = endMethod(String[].class, "toString");
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(4, interactions.size());
        Frame[] frames = interactions.toArray(new Frame[0]);
        assertArrayEquals(new Frame[] { frame1, frame2, frame3, frame4 }, frames);
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Frame frame1 = startMethod(Thread.class, "run", new Object[0]);
        Frame frame2 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame3 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame4 = endMethod(String[].class, "toString");
        List<Frame> frameList = Profile.frameList();
        assertEquals(4, frameList.size());
        Frame[] frames = frameList.toArray(new Frame[0]);
        assertArrayEquals(new Frame[] { frame1, frame2, frame3, frame4 }, frames);
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        alloc(String[].class);
        alloc(String[].class);
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(1, allocations.size());
        ClassAllocation allocation = allocations.get(0);
        assertEquals("class [Ljava.lang.String;", allocation.getClassName());
        assertEquals(2, allocation.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        startMethod(Thread.class, "run", new Object[0]);
        endMethod(Thread.class, "run");
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        Frame frame1 = startMethod(Thread.class, "run", new Object[0]);
        Frame frame2 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame3 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame4 = endMethod(String[].class, "toString");
        Frame frame5 = startMethod(Thread.class, "run", new Object[0]);
        Frame frame6 = endMethod(Thread.class, "run");
        List<Frame> frameList = new ArrayList<>(Profile.frameList());
        Collections.sort(frameList, Frame.COMPARATOR);
        List<Frame> sortedFrameList = frameList;
        assertEquals(6, sortedFrameList.size());
        Frame[] frames = sortedFrameList.toArray(new Frame[0]);
        assertArrayEquals(new Frame[] {
            frame5, frame1, frame6, frame2, frame3, frame4
        }, frames);
    }

    @Test
    public void testStartEndMethods() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        startMethod(Thread.class, "run", new Object[0]);
        Frame frame1 = startMethod(String[].class, "toString", new Object[0]);
        Frame frame2 = endMethod(String[].class, "toString");
        endMethod(Thread.class, "run");
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(3, interactions.size());
        Frame[] frames = interactions.toArray(new Frame[0]);
        assertArrayEquals(new Frame[] { frame1, frame2, frames[0] }, frames);
    }

    @Test
    public void testBeginWaitEndWaitMethods() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        startMethod(Thread.class, "run", new Object[0]);
        beginWaitMethod(Thread.class, "sleep", new Long(100));
        Frame frame1 = startMethod(Thread.class, "sleep", new Long(100));
        endWaitMethod(Thread.class, "sleep");
        Frame frame2 = endMethod(Thread.class, "run");
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(4, interactions.size());
        Frame[] frames = interactions.toArray(new Frame[0]);
        assertArrayEquals(new Frame[] { frame1, frames[1], frame2, frames[0] }, frames);
    }

    @Test
    public void testUnwindMethods() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        startMethod(Thread.class, "run", new Object[0]);
        startMethod(String[].class, "toString", new Object[0]);
        throw new RuntimeException("Test Exception");
        Frame frame1 = findFrame(Thread.class, "run", null);
        Frame frame2 = findFrame(String[].class, "toString", null);
        assertEquals(2, Profile._threadActiveFrame.size());
        assertEquals(frame1, Profile._threadActiveFrame.get(threadId));
    }

    @Test
    public void testAllocMethod() {
        Profile.init();
        Profile.start();
        alloc(String[].class);
        alloc(String[].class);
        Map<String, ClassAllocation> allocations = new HashMap<>(Profile._allocList);
        assertEquals(1, allocations.size());
        ClassAllocation allocation = allocations.values().iterator().next();
        assertEquals("class [Ljava.lang.String;", allocation.getClassName());
        assertEquals(2, allocation.getAllocCount());
    }

    private Frame startMethod(Class<?> clazz, String methodName, Object[] arguments) {
        long start = System.nanoTime();
        long threadId = Thread.currentThread().getId();
        String className = clazz.getName();
        String methodSignature = className + "." + methodName + "(" + Arrays.toString(arguments) + ")";
        Profile.start(className, methodSignature);
        return Profile._threadActiveFrame.get(threadId);
    }

    private Frame endMethod(Class<?> clazz, String methodName) {
        long threadId = Thread.currentThread().getId();
        String className = clazz.getName();
        String methodSignature = className + "." + methodName;
        Frame frame = Profile.findFrame(threadId, className, methodName);
        Profile.end(className, methodSignature);
        return frame;
    }

    private void beginWaitMethod(Class<?> clazz, String methodName, Long timeout) {
        long start = System.nanoTime();
        long threadId = Thread.currentThread().getId();
        String className = clazz.getName();
        String methodSignature = className + "." + methodName + "(" + timeout + ")";
        Profile.beginWait(className, methodSignature);
    }

    private void endWaitMethod(Class<?> clazz, String methodName) {
        long threadId = Thread.currentThread().getId();
        String className = clazz.getName();
        String methodSignature = className + "." + methodName;
        Profile.endWait(className, methodSignature);
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        return Profile.findFrame(threadId, className, methodName);
    }
}