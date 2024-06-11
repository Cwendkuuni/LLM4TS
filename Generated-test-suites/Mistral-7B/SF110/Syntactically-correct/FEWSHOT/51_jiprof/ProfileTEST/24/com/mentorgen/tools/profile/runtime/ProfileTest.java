package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private static Map<String, Method> methodDictionary;
    private static Map<Long, Frame> threadActiveFrame;
    private static Map<String, ClassAllocation> allocList;

    @Before
    public void setup() {
        init();
        methodDictionary = new HashMap<String, Method>(2003);
        threadActiveFrame = new HashMap<Long, Frame>(1001);
        allocList = new HashMap<String, ClassAllocation>();
    }

    @Test
    public void initTest() {
        init();
        assertEquals(1, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void initProfilerTest() {
        initProfiler();
        assertTrue(Profile._controller != null);
        assertTrue(Profile._controllerThread != null);
    }

    @Test
    public void clearTest() {
        init();
        start();
        end("TestClass", "testMethod");
        clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void startTest() {
        init();
        start();
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void stopTest() {
        init();
        start();
        stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void setFileNameTest() {
        init();
        setFileName("test.txt");
        assertEquals("test.txt", Profile._controller.getFileName());
    }

    @Test
    public void threadsTest() {
        init();
        start();
        List<Long> threadIds = threads();
        assertEquals(1, threadIds.size());
        assertEquals(Thread.currentThread().getId(), threadIds.get(0));
    }

    @Test
    public void interactionsTest() {
        init();
        start();
        start("TestClass", "testMethod");
        end("TestClass", "testMethod");
        List<Frame> frames = interactions(Thread.currentThread().getId());
        assertEquals(1, frames.size());
        assertNotNull(frames.get(0));
    }

    @Test
    public void frameListTest() {
        init();
        start();
        start("TestClass", "testMethod");
        end("TestClass", "testMethod");
        List<Frame> frames = frameList();
        assertEquals(1, frames.size());
        assertNotNull(frames.get(0));
    }

    @Test
    public void allocationsTest() {
        init();
        alloc("TestClass");
        alloc("TestClass");
        alloc("TestClass");
        List<ClassAllocation> classAllocations = allocations();
        assertEquals(1, classAllocations.size());
        assertEquals("TestClass", classAllocations.get(0).getClassName());
        assertEquals(3, classAllocations.get(0).getTotalAllocationCount());
    }

    @Test
    public void getThreadTotalTimeTest() {
        init();
        start();
        start("TestClass", "testMethod");
        end("TestClass", "testMethod");
        start("TestClass", "testMethod");
        end("TestClass", "testMethod");
        long threadId = Thread.currentThread().getId();
        long threadTotalTime = getThreadTotalTime(threadId);
        assertEquals(2 * 1000_000L, threadTotalTime); // 2 method calls * 1000_000L nanos to millis
    }

    @Test
    public void sortFrameListTest() {
        init();
        start();
        start("TestClass1", "testMetho1");
        end("TestClass1", "testMetho1");
        start("TestClass2", "testMetho2");
        end("TestClass2", "testMetho2");
        start("TestClass1", "testMetho1");
        end("TestClass1", "testMetho1");
        Collections.sort(frameList(), new Comparator<Frame>() {
            @Override
            public int compare(Frame f1, Frame f2) {
                return (int) (f1.getBeginTime() - f2.getBeginTime());
            }
        });
        Frame firstFrame = frameList().get(0);
        Frame secondFrame = frameList().get(1);
        Frame thirdFrame = frameList().get(2);
        assertEquals("TestClass1", firstFrame.getClassName());
        assertEquals("testMetho1", firstFrame.getMethodName());
        assertEquals("TestClass2", secondFrame.getClassName());
        assertEquals("testMetho2", secondFrame.getMethodName());
        assertEquals("TestClass1", thirdFrame.getClassName());
        assertEquals("testMetho1", thirdFrame.getMethodName());
    }

    @Test
    public void startTestWithParentFrame() {
        init();
        Frame parentFrame = new Frame(null, new Method("ParentClass", "parentMethod"), Thread.currentThread().getId());
        start("TestClass", "testMethod");
        Frame childFrame = new Frame(parentFrame, new Method("TestClass", "testMethod"), Thread.currentThread().getId());
        assertEquals(parentFrame, childFrame.getParent());
    }

    @Test
    public void findFrameTest() {
        init();
        start();
        start("TestClass1", "testMetho1");
        Frame testClass1Frame1 = findFrame(Thread.currentThread().getId(), "TestClass1", "testMetho1");
        start("TestClass1", "testMetho1");
        Frame testClass1Frame2 = findFrame(Thread.currentThread().getId(), "TestClass1", "testMetho1");
        assertEquals(testClass1Frame1, testClass1Frame2);
        start("TestClass2", "testMetho2");
        Frame testClass2Frame = findFrame(Thread.currentThread().getId(), "TestClass2", "testMetho2");
        assertNull(testClass2Frame);
    }

    @Test
    public void allocTest() {
        init();
        alloc("TestClass");
        alloc("TestClass");
        ClassAllocation ca = allocList.get("TestClass");
        assertEquals("TestClass", ca.getClassName());
        assertEquals(2, ca.getTotalAllocationCount());
    }
}