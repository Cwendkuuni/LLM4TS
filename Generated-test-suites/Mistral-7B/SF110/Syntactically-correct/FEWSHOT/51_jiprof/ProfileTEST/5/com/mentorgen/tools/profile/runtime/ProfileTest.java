package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mentorgen.tools.profile.*;
import java.util.*;
import com.mentorgen.tools.profile.output.*;
import java.io.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProfileTest {

    private Profile _profile;
    private ThreadDictionary _threadDictionary;
    private List<Frame> _frameList;
    private Map<Long, Frame> _threadActiveFrame;
    private Map<String, Method> _methodDictionary;
    private Map<String, ClassAllocation> _allocList;
    private Controller _controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _threadDictionary = mock(ThreadDictionary.class);
        _frameList = new ArrayList<>();
        _threadActiveFrame = new HashMap<>();
        _methodDictionary = new HashMap<>();
        _allocList = new HashMap<>();
        _profile = new Profile() {
            {
                _threadDictionary = this._threadDictionary;
                _frameList = this._frameList;
                _threadActiveFrame = this._threadActiveFrame;
                _methodDictionary = this._methodDictionary;
                _allocList = this._allocList;
                _controller = _controller;
            }
        };
    }

    @Test
    public void testInitProfiler() {
        _profile.initProfiler();
        verify(_controller).start();
        verify(_controllerThread).start();
    }

    @Test
    public void testInit() {
        _profile.init();
        assertEquals(1001, _threadDictionary.size());
        assertEquals(2003, _methodDictionary.size());
    }

    @Test
    public void testClear() {
        _profile.init();
        _profile.clear();
        assertEquals(0, _threadDictionary.size());
        assertEquals(0, _methodDictionary.size());
    }

    @Test
    public void testStart() {
        _profile.init();
        _profile.start();
        verify(_controller).start();
    }

    @Test
    public void testStop() {
        _profile.init();
        _profile.start();
        _profile.stop();
        verify(_controller).stop();
    }

    @Test
    public void testSetFileName() {
        _profile.init();
        _profile.setFileName("fileName.txt");
        verify(_controller).setFileName("fileName.txt");
    }

    @Test
    public void testShutdown() {
        _profile.init();
        _profile.start();
        _profile.shutdown();
        verify(_controller).close();
        verify(_controllerThread).interrupt();
    }

    @Test
    public void testThreads() {
        _profile.init();
        _profile.start();
        when(_threadDictionary.threads()).thenReturn(Arrays.asList(1L, 2L, 3L));
        assertEquals(Arrays.asList(1L, 2L, 3L), _profile.threads());
    }

    @Test
    public void testInteractions() {
        _profile.init();
        _profile.start();
        Frame frame1 = mock(Frame.class);
        Frame frame2 = mock(Frame.class);
        when(_threadDictionary.interactions(1L)).thenReturn(Arrays.asList(frame1, frame2));
        assertEquals(Arrays.asList(frame1, frame2), _profile.interactions(1L));
    }

    @Test
    public void testFrameList() {
        _profile.init();
        _profile.start();
        Frame frame1 = mock(Frame.class);
        Frame frame2 = mock(Frame.class);
        when(_frameList).thenReturn(Arrays.asList(frame1, frame2));
        assertEquals(Arrays.asList(frame1, frame2), _profile.frameList());
    }

    @Test
    public void testAllocations() {
        _profile.init();
        _profile.start();
        when(_allocList.values()).thenReturn(Arrays.asList(new ClassAllocation("className1"), new ClassAllocation("className2")));
        assertEquals(Arrays.asList(new ClassAllocation("className1"), new ClassAllocation("className2")), _profile.allocations());
    }

    @Test
    public void testGetThreadTotalTime() {
        _profile.init();
        _profile.start();
        when(_threadDictionary.getThreadTotalTime(1L)).thenReturn(1000L);
        assertEquals(1000L, _profile.getThreadTotalTime(1L));
    }

    @Test
    public void testSortFrameList() {
        _profile.init();
        _profile.start();
        Frame frame1 = mock(Frame.class);
        Frame frame2 = mock(Frame.class);
        when(frame1.getNetTime()).thenReturn(1L);
        when(frame2.getNetTime()).thenReturn(2L);
        _frameList.add(frame1);
        _frameList.add(frame2);
        _profile.sortFrameList((frame1, frame2) -> frame1.getNetTime() - frame2.getNetTime());
        assertEquals(frame1, _frameList.get(0));
        assertEquals(frame2, _frameList.get(1));
    }

    @Test
    public void testStartMethod() {
        _profile.init();
        _profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        _profile.start(className, methodName);
        Frame frame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
        verify(
                _threadDictionary,
                times(1)
        ).add(eq(Thread.currentThread().getId()), eq(frame));
        verify(_threadActiveFrame, times(1)).put(eq(Thread.currentThread().getId()), eq(frame));
    }

    @Test
    public void testEndMethod() {
        _profile.init();
        _profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Frame parentFrame = new Frame(null, new Method("ParentClass", "parentMethod"), Thread.currentThread().getId());
        Frame targetFrame = new Frame(parentFrame, new Method(className, methodName), Thread.currentThread().getId());
        _frameList.add(parentFrame);
        _frameList.add(targetFrame);
        _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);
        _profile.end(className, methodName);
        verify(_threadActiveFrame, times(1)).put(eq(Thread.currentThread().getId()), eq(parentFrame));
        verify(parentFrame, times(1)).setEndTime(anyLong());
    }

    @Test
    public void testBeginWaitMethod() {
        _profile.init();
        _profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Frame targetFrame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
        _frameList.add(targetFrame);
        _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);
        _profile.beginWait(className, methodName);
        verify(targetFrame, times(1)).beginWait(anyLong());
    }

    @Test
    public void testEndWaitMethod() {
        _profile.init();
        _profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        Frame targetFrame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
        _frameList.add(targetFrame);
        _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);
        _profile.endWait(className, methodName);
        verify(targetFrame, times(1)).endWait(anyLong());
    }

    @Test
    public void testUnwindMethod() {
        _profile.init();
        _profile.start();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        Frame targetFrame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
        _frameList.add(targetFrame);
        _threadActiveFrame.put(Thread.currentThread().getId(), targetFrame);
        _profile.unwind(className, methodName, exception);
        verify(_threadActiveFrame, times(1)).put(eq(Thread.currentThread().getId()), eq(targetFrame));

        if (_profile._debugException) {
            verify(System.err, times(1)).println("Catch: " + exception);
        }
    }

    @Test
    public void testAllocMethod() {
        _profile.init();
        _profile.start();
        String className = "TestClass";
        _profile.alloc(className);
        verify(_allocList, times(1)).get(eq(className));
        verify(_allocList, times(1)).get(eq(className)).incAllocCount();
    }
}