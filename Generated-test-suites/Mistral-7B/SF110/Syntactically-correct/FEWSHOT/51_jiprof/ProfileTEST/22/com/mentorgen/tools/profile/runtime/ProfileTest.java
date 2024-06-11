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
    private static final long THREAD_ID = 1;
    private static final String FILENAME = "test.prof";
    private static final boolean DEBUG_START = false;
    private static final boolean DEBUG_EXCEPTION = false;
    private static final int FRAME_COUNT = 5;
    private static final long[] START_TIMES = new long[FRAME_COUNT];
    private static final long[] END_TIMES = new long[FRAME_COUNT];

    private Controller mockController;
    private Profile profile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockController = mock(Controller.class);
        when(mockController.isProfile()).thenReturn(true);
        when(mockController.setFileName(anyString())).thenReturn(mockController);
        profile = new Profile();
        profile.setController(mockController);
        profile.init();
    }

    @Test
    public void testInit() {
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        verify(mockController, times(1)).start();
        verify(mockController, times(1)).setFileName(eq(FILENAME));
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.start();
        Profile.end(CLASS_NAME, METHOD_NAME);
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        verify(mockController, times(1)).start();
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        verify(mockController, times(1)).stop();
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        Profile.setFileName(FILENAME);
        verify(mockController, times(1)).setFileName(eq(FILENAME));
    }

    @Test
    public void testShutdown() throws IOException {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile._threadActiveFrame.put(THREAD_ID, frame2);
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame2);
        Profile.shutdown();
        for (long threadId : Profile._threadDictionary.threads()) {
            Frame frame = Profile._threadDictionary.getMostRecentFrame(threadId);
            frame.close();
        }
        for (Frame frame : Profile.frameList()) {
            frame.computeNetTime();
        }
        verify(mockController, times(1)).close();
        verify(mockController, times(FRAME_COUNT)).writeFrame(any(Frame.class));
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID + 1);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile._threadDictionary.put(THREAD_ID + 1, frame2);
        Iterable<Long> threads = Profile.threads();
        assertEquals(2, Iterables.size(threads));
        assertEquals(THREAD_ID, threads.iterator().next().longValue());
        assertEquals(THREAD_ID + 1, threads.iterator().next().longValue());
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile._threadDictionary.put(THREAD_ID, frame2);
        Iterable<Frame> interactions = Profile.interactions(THREAD_ID);
        assertEquals(2, Iterables.size(interactions));
        assertEquals(frame2, interactions.iterator().next());
        assertEquals(frame1, interactions.iterator().next());
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile._threadDictionary.put(THREAD_ID, frame2);
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame2);
        Iterable<Frame> frameList = Profile.frameList();
        assertEquals(2, Iterables.size(frameList));
        assertEquals(frame2, frameList.iterator().next());
        assertEquals(frame1, frameList.iterator().next());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        Profile.alloc(CLASS_NAME);
        Profile.alloc(CLASS_NAME);
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertEquals(1, Iterables.size(allocations));
        ClassAllocation ca = allocations.iterator().next();
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame1.setBeginTime(START_TIMES[0]);
        frame1.setEndTime(END_TIMES[0]);
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame2.setBeginTime(START_TIMES[1]);
        frame2.setEndTime(END_TIMES[1]);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile._threadDictionary.put(THREAD_ID, frame2);
        long threadTotalTime = Profile.getThreadTotalTime(THREAD_ID);
        assertEquals(END_TIMES[1] - START_TIMES[0] + END_TIMES[1] - START_TIMES[1], threadTotalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame1.setBeginTime(START_TIMES[1]);
        Frame frame2 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame2.setBeginTime(START_TIMES[0]);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile._threadDictionary.put(THREAD_ID, frame2);
        Collections.sort(Profile._frameList, new Comparator<Frame>() {
            @Override
            public int compare(Frame f1, Frame f2) {
                return (int) (f1.getBeginTime() - f2.getBeginTime());
            }
        });
        Frame sortedFrame1 = Profile._frameList.get(0);
        Frame sortedFrame2 = Profile._frameList.get(1);
        assertEquals(START_TIMES[0], sortedFrame1.getBeginTime());
        assertEquals(START_TIMES[1], sortedFrame2.getBeginTime());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        Profile.start();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame1.setBeginTime(START_TIMES[0]);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile.end(CLASS_NAME, METHOD_NAME);
        Frame frame2 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame2.setEndTime(END_TIMES[0]);
        assertEquals(START_TIMES[0], frame1.getBeginTime());
        assertEquals(END_TIMES[0], frame2.getEndTime());
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        Profile.start();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame1.setBeginTime(START_TIMES[0]);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile.beginWait(CLASS_NAME, METHOD_NAME);
        Frame waitFrame1 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        waitFrame1.setBeginWait(START_TIMES[1]);
        Profile._threadDictionary.put(THREAD_ID, waitFrame1);
        Profile.endWait(CLASS_NAME, METHOD_NAME);
        Frame endWaitFrame1 = new Frame(waitFrame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        endWaitFrame1.setEndWait(END_TIMES[1]);
        assertEquals(START_TIMES[0], frame1.getBeginTime());
        assertEquals(START_TIMES[1], waitFrame1.getBeginWait());
        assertEquals(END_TIMES[1], endWaitFrame1.getEndWait());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        Profile.start();
        Profile.start(CLASS_NAME, METHOD_NAME);
        Frame frame1 = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        frame1.setBeginTime(START_TIMES[0]);
        Profile._threadDictionary.put(THREAD_ID, frame1);
        Profile.unwind(CLASS_NAME, METHOD_NAME, "TestException");
        Frame unwindFrame1 = new Frame(frame1, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        unwindFrame1.setEndTime(END_TIMES[0]);
        assertEquals(START_TIMES[0], frame1.getBeginTime());
        assertEquals(END_TIMES[0], unwindFrame1.getEndTime());
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        Profile.start();
        Frame parentFrame = new Frame(null, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame childFrame1 = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Frame childFrame2 = new Frame(parentFrame, new Method(CLASS_NAME, METHOD_NAME), THREAD_ID);
        Profile._threadDictionary.put(THREAD_ID, parentFrame);
        Profile._threadDictionary.put(THREAD_ID, childFrame1);
        Profile._threadDictionary.put(THREAD_ID, childFrame2);
        Frame targetFrame = Profile.findFrame(THREAD_ID, CLASS_NAME, METHOD_NAME);
        assertEquals(childFrame2, targetFrame);
    }

    @Test
    public void testAlloc() {
        Profile.init();
        Profile.start();
        Profile.alloc(CLASS_NAME);
        ClassAllocation ca = Profile._allocList.get(CLASS_NAME);
        assertEquals(1, ca.getAllocCount());
    }

    @Test
    public void testRun() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        verify(mockController, times(1)).close();
        verify(mockController, times(FRAME_COUNT)).writeFrame(any(Frame.class));
    }
}