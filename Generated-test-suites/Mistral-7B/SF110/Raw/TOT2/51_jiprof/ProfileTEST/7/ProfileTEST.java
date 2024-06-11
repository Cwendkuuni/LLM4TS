package com.mentorgen.tools.profile.runtime;

import org.junit.Test;
import org.mockito.*;
import java.io.*;
import java.lang.Class;
import java.lang.reflect.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._threadDictionary.size() > 0);
        assertTrue(Profile._methodDictionary.size() > 0);
        assertTrue(Profile._frameList.size() > 0);
        assertTrue(Profile._allocList.size() > 0);
    }

    @Test
    public void testStart() throws Exception {
        String className = "TestClass";
        String methodName = "testMethod";
        Class testClass = Mockito.mock(Class.class);
        Method testMethod = Mockito.spy(Mockito.method(className, methodName));

        Profile.start(className, methodName);
        Frame frame = Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId());
        assertNotNull(frame);
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
    }

    @Test
    public void testEnd() {
        // TODO: Implement testEnd test case
    }

    @Test
    public void testSortFrameList() {
        // TODO: Implement testSortFrameList test case
    }

    @Test
    public void testFindFrame() {
        // TODO: Implement testFindFrame test case
    }

    @Test
    public void testEndWait() {
        // TODO: Implement testEndWait test case
    }

    @Test
    public void testUnwind() throws Exception {
        String className = "TestClass";
        String methodName = "testMethod";
        String exceptionClassName = "TestException";

        Class testClass = Class.forName(className);
        Method testMethod = testClass.getMethod(methodName);
        testMethod.setAccessible(true);

        try {
            testMethod.invoke(testClass.newInstance());
            fail("Expected an exception to be thrown.");
        } catch (InvocationTargetException e) {
            InstanceThrowable ite = (InstanceThrowable) e.getCause();
            Profile.unwind(className, methodName, exceptionClassName);
            Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
            assertNotNull(frame);
            assertEquals(className, frame.getClassName());
            assertEquals(methodName, frame.getMethodName());
            assertEquals(exceptionClassName, frame.getExceptionClassName());
        }
    }

    @Test
    public void testInitAndShutdown() throws IOException, InterruptedException {
        Profile.initProfiler();
        Profile.shutdown();
        assertTrue(Profile._threadDictionary.size() == 0);
        assertTrue(Profile._methodDictionary.size() == 0);
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._allocList.size() == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testShutdownWhileProfilerRunning() throws IOException {
        Profile.initProfiler();
        Profile.shutdown();
        Profile.initProfiler();
    }
}