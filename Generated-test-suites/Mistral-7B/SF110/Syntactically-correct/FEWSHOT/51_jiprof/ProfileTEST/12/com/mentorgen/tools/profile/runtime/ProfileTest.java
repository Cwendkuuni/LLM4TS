package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mentorgen.tools.profile.*;
import java.util.*;
import java.io.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProfileTest {

    private static final String CLASS_NAME = "TestClass";
    private static final String METHOD_NAME = "testMethod";
    private static final String FILENAME = "test.prof";
    private static final long THREAD_ID = 1L;
    private static final long THREAD_ID_2 = 2L;

    private Profile _profile;
    private Controller _controllerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        _controllerMock = mock(Controller.class);
        _profile = new Profile();
        _profile.setFileName(FILENAME);
        _profile._controller = _controllerMock;
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        verify(Profile._controller, times(1)).start();
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._allocList);
        assertEquals(0, Profile._frameList.size());
    }

    @Test
    public void testStart() {
        Profile.init();
        _profile.start();
        verify(Profile._controller, times(1)).start();
    }

    @Test
    public void testStop() {
        Profile.init();
        _profile.start();
        _profile.stop();
        verify(Profile._controller, times(1)).stop();
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        _profile.setFileName(FILENAME);
        assertEquals(FILENAME, _profile.getFileName());
    }

    @Test
    public void testShutdown() throws IOException {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        verify(Profile._controller, times(1)).close();
    }

    @Test
    public void testThreads() {
        Profile.init();
        _profile.start();
        List<Long> threads = Profile.threads();
        assertEquals(1, threads.size());
        assertEquals(Thread.currentThread().getId(), threads.get(0));
    }

    @Test
    public void testInteractions() {
        Profile.init();
        _profile.start();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
        List<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        assertEquals(1, interactions.size());
        assertEquals(frame, interactions.get(0));
    }

    @Test
    public void testFrameList() {
        Profile.init();
        _profile.start();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
        Iterable<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.spliterator().getExactSizeIfKnown());
        assertEquals(frame, frameList.iterator().next());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        _profile.start();
        _profile.alloc(CLASS_NAME);
        _profile.alloc(CLASS_NAME);
        Iterable<ClassAllocation> allocations = Profile._allocList.values();
        assertEquals(1, allocations.spliterator().getExactSizeIfKnown());
        ClassAllocation ca = allocations.iterator().next();
        assertEquals(CLASS_NAME, ca.getClassname());
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        _profile.start();
        long threadId = Thread.currentThread().getId();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), threadId);
        Profile._threadActiveFrame.put(threadId, frame);
        when(Profile._controller.getThreadTotalTime(threadId)).thenReturn(100L);
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(100L, threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        _profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame2 = new Frame(null, new Method(CLASS_NAME, "anotherMethod"), THREAD_ID_2);
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame2);
        Comparator<Frame> comparator = mock(Comparator.class);
        Profile.sortFrameList(comparator);
        verify(comparator, times(2)).compare(frame1, frame2);
    }

    @Test
    public void testStartMethod() {
        Profile.init();
        _profile.start();
        _profile.start(CLASS_NAME, METHOD_NAME);
        Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertEquals(CLASS_NAME, frame.getClassName());
        assertEquals(METHOD_NAME, frame.getMethodName());
    }

    @Test
    public void testEndMethod() {
        Profile.init();
        _profile.start();
        Frame parentFrame = new Frame(null, new Method(CLASS_NAME, "parentMethod"), THREAD_ID);
        Frame childFrame = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadActiveFrame.put(THREAD_ID, childFrame);
        Profile._threadActiveFrame.put(THREAD_ID, parentFrame);
        Profile._frameList.add(parentFrame);
        Profile._frameList.add(childFrame);
        _profile.end(CLASS_NAME, METHOD_NAME);
        assertEquals(parentFrame, Profile._threadActiveFrame.get(THREAD_ID));
    }

    @Test
    public void testBeginWaitMethod() {
        Profile.init();
        _profile.start();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
        _profile.beginWait(CLASS_NAME, METHOD_NAME);
        assertEquals("waiting", frame.getState());
    }

    @Test
    public void testEndWaitMethod() {
        Profile.init();
        _profile.start();
        Frame frame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), Thread.currentThread().getId());
        Profile._threadActiveFrame.put(Thread.currentThread().getId(), frame);
        _profile.beginWait(CLASS_NAME, METHOD_NAME);
        _profile.endWait(CLASS_NAME, METHOD_NAME);
        assertEquals("running", frame.getState());
    }

    @Test
    public void testUnwindMethod() {
        Profile.init();
        _profile.start();
        Frame parentFrame = new Frame(null, new Method(CLASS_NAME, "parentMethod"), THREAD_ID);
        Frame childFrame = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadActiveFrame.put(THREAD_ID, childFrame);
        Profile._threadActiveFrame.put(THREAD_ID, parentFrame);
        _profile.end(CLASS_NAME, METHOD_NAME);
        _profile.unwind(CLASS_NAME, METHOD_NAME, "TestException");
        assertEquals(parentFrame, Profile._threadActiveFrame.get(THREAD_ID));
    }

    @Test
    public void testAllocMethod() {
        Profile.init();
        _profile.start();
        _profile.alloc(CLASS_NAME);
        ClassAllocation ca = Profile._allocList.get(CLASS_NAME);
        assertEquals(1, ca.getAllocCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethodAfterShutdown() {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        _profile.start(CLASS_NAME, METHOD_NAME);
    }

    @Test
    public void testEndMethodAfterShutdown() {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        _profile.end(CLASS_NAME, METHOD_NAME);
    }

    @Test
    public void testBeginWaitMethodAfterShutdown() {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        _profile.beginWait(CLASS_NAME, METHOD_NAME);
    }

    @Test
    public void testEndWaitMethodAfterShutdown() {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        _profile.endWait(CLASS_NAME, METHOD_NAME);
    }

    @Test
    public void testUnwindMethodAfterShutdown() {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        _profile.unwind(CLASS_NAME, METHOD_NAME, "TestException");
    }

    @Test
    public void testAllocMethodAfterShutdown() {
        Profile.init();
        _profile.start();
        _profile.shutdown();
        _profile.alloc(CLASS_NAME);
    }
}