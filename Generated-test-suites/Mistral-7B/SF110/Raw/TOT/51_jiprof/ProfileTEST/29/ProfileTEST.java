package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mentorgen.tools.profile.Controller;
import com.mentorgen.tools.profile.output.ProfileDump;
import com.mentorgen.tools.profile.runtime.Frame;
import com.mentorgen.tools.profile.runtime.Method;
import com.mentorgen.tools.profile.runtime.Profile;
import com.mentorgen.tools.profile.runtime.ThreadDictionary;
import com.mentorgen.tools.profile.runtime.ThreadDictionary.ThreadEntry;
import com.mentorgen.tools.profile.runtime.ThreadDictionary.ThreadEntry.FrameData;

public class ProfileTest {

    private static final String TEST_CLASS_NAME = "TestClass";
    private static final String TEST_METHOD_NAME = "testMethod";
    private static final String TEST_OTHER_CLASS_NAME = "OtherClass";
    private static final String TEST_OTHER_METHOD_NAME = "otherMethod";
    private static final String TEST_EXCEPTION_CLASS_NAME = "TestException";

    private Controller controller;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @Before
    public void setUp() {
        controller = mock(Controller.class);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Profile.init();
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        Profile.shutdown();
    }

    @Test
    public void testInitialization() {
        Profile.init();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStartAndStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testStartAndEndMethod() {
        Profile.init();
        ThreadEntry entry = new ThreadEntry(1L);
        Frame expectedFrame = new Frame(null, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), 1L);
        when(controller.start()).thenReturn(entry);
        when(controller.setFileName(anyString())).thenReturn(true);

        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
        Frame actualFrame = findFrame(1L, TEST_CLASS_NAME, TEST_METHOD_NAME);
        assertEquals(expectedFrame, actualFrame);
        verify(controller).start();
        verify(controller).setFileName(eq("ProfileTest.txt"));
        verifyNoMoreInteractions();

        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);
        verify(controller).stop();
    }

    @Test
    public void testBeginAndEndWait() {
        Profile.init();
        ThreadEntry entry = new ThreadEntry(1L);
        when(controller.start()).thenReturn(entry);
        when(controller.setFileName(anyString())).thenReturn(true);

        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
        Frame startFrame = findFrame(1L, TEST_CLASS_NAME, TEST_METHOD_NAME);

        long beginWaitTime = System.nanoTime();
        Profile.beginWait(TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME);
        Frame waitFrame = findFrame(1L, TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME);
        long endWaitTime = System.nanoTime();

        Profile.endWait(TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME);

        assertEquals(2, entry.getFrames().size());
        FrameData startData = entry.getFrames().get(0);
        FrameData endData = entry.getFrames().get(1);
        assertEquals(TEST_METHOD_NAME, startData.getName());
        assertEquals(TEST_OTHER_METHOD_NAME, endData.getName());
        assertTrue(startData.getBeginTime() < beginWaitTime);
        assertTrue(startData.getEndTime() > beginWaitTime);
        assertTrue(endData.getBeginTime() < endWaitTime);
        assertTrue(endData.getEndTime() > endWaitTime);
    }

    @Test
    public void testUnwindException() {
        Profile.init();
        ThreadEntry entry = new ThreadEntry(1L);
        Frame expectedFrame = new Frame(null, new Method(TEST_CLASS_NAME, TEST_METHOD_NAME), 1L);
        when(controller.start()).thenReturn(entry);
        when(controller.setFileName(anyString())).thenReturn(true);

        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
        Frame startFrame = findFrame(1L, TEST_CLASS_NAME, TEST_METHOD_NAME);

        try {
            throw new TestException();
        } catch (TestException e) {
            Profile.unwind(TEST_CLASS_NAME, TEST_METHOD_NAME, e.getClass().getName());
        }

        Frame unwindFrame = findFrame(1L, TEST_CLASS_NAME, TEST_METHOD_NAME);
        assertEquals(expectedFrame, unwindFrame);
        verify(controller).stop();
        verify(controller).setFileName(eq("ProfileTest.txt"));
        verify(controller).close();

        String expectedOutput = String.format("Catch: %s%n", TEST_EXCEPTION_CLASS_NAME);
        String actualOutput = outputStream.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAllocations() {
        Profile.init();
        ThreadEntry entry = new ThreadEntry(1L);
        Frame expectedFrame = new Frame(null, new Method(TEST_CLASS_NAME, "<init>"), 1L);
        when(controller.start()).thenReturn(entry);
        when(controller.setFileName(anyString())).thenReturn(true);

        Profile.start(TEST_CLASS_NAME, "<init>");
        Frame actualFrame = findFrame(1L, TEST_CLASS_NAME, "<init>");
        assertEquals(expectedFrame, actualFrame);
        verify(controller).start();
        verify(controller).setFileName(eq("ProfileTest.txt"));

        Profile.alloc(TEST_CLASS_NAME);
        Map<String, ClassAllocation> allocList = Profile._allocList;
        ClassAllocation expectedAllocation = new ClassAllocation(TEST_CLASS_NAME);
        ClassAllocation actualAllocation = allocList.get(TEST_CLASS_NAME);

        assertEquals(expectedAllocation, actualAllocation);
        assertEquals(1, allocList.size());
        assertEquals(1, actualAllocation.getAllocCount());
    }

    @Test
    public void testMultipleMethodsInThread() {
        Profile.init();
        ThreadEntry entry = new ThreadEntry(1L);
        when(controller.start()).thenReturn(entry);
        when(controller.setFileName(anyString())).thenReturn(true);

        Profile.start(TEST_CLASS_NAME, TEST_METHOD_NAME);
        Frame method1Frame = findFrame(1L, TEST_CLASS_NAME, TEST_METHOD_NAME);

        Profile.start(TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME);
        Frame method2Frame = findFrame(1L, TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME);

        Frame parentFrame = findFrame(1L, TEST_CLASS_NAME, TEST_METHOD_NAME);
        assertEquals(method1Frame, parentFrame.getParent());
        assertEquals(method2Frame, method1Frame.getChild(new Method(TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME)));

        Profile.end(TEST_CLASS_NAME, TEST_METHOD_NAME);

        Profile.end(TEST_CLASS_NAME, TEST_OTHER_METHOD_NAME);

        assertEquals(2, entry.getFrames().size());
        FrameData method1Data = entry.getFrames().get(0);
        FrameData method2Data = entry.getFrames().get(1);
        assertEquals(TEST_METHOD_NAME, method1Data.getName());
        assertEquals(TEST_OTHER_METHOD_NAME, method2Data.getName());
    }

    @Test(expected = AssertionError.class)
    public void testExceptionInInit() {
        Profile.init();
        throw new AssertionError();
    }

    @Test(expected = AssertionError.class)
    public void testExceptionInClear() {
        Profile.init();
        Profile.clear();
        throw new AssertionError();
    }

    @Test(expected = AssertionError.class)
    public void testExceptionInStart() {
        Profile.init();
        Profile.start();
        throw new AssertionError();
    }

    @Test(expected = AssertionError.class)
    public void testExceptionInStop() {
        Profile.init();
        Profile.stop();
        throw new AssertionError();
    }

    @Test(expected = AssertionError.class)
    public void testExceptionInSetFileName() {
        Profile.init();
        when(controller.setFileName(anyString())).thenThrow(new AssertionError());
        Profile.setFileName("ProfileTest.txt");
    }

    @Test(expected = AssertionError.class)
    public void testExceptionInShutdown() {
        Profile.init();
        when(controller.close()).thenThrow(new AssertionError());
        Profile.shutdown();
    }

    private Frame findFrame(long threadId, String className, String methodName) {
        for (Frame frame : Profile._frameList) {
            if (frame.getThreadId() == threadId && frame.getMethodName().equals(methodName) && frame.getClassName().equals(className)) {
                return frame;
            }
        }
        return null;
    }
}