package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ProfileTest {

    @Test
    public void testInit() {
        Profile.init();
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
        assertEquals(1001, Profile._threadActiveFrame.size());
        assertEquals(1, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);

        Frame threadFrame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
        Frame parentFrame = mock(Frame.class);
        when(parentFrame.getClassName()).thenReturn(className);
        when(parentFrame.getMethodName()).thenReturn("parentMethod");
        when(parentFrame.getParent()).thenReturn(null);
        threadFrame.setParent(parentFrame);

        assertEquals(1, Profile._threadDictionary.size());
        assertSame(threadFrame, Profile._threadDictionary.get(Thread.currentThread().getId()));
        assertEquals(1, Profile._frameList.size());
        assertSame(threadFrame, Profile._frameList.get(0));
    }

    @Test
    public void testStartEndWithParent() {
        Profile.init();
        String parentClassName = "ParentClass";
        String parentMethodName = "parentMethod";
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);

        Frame parentThreadFrame = new Frame(null, new Method(parentClassName, parentMethodName), Thread.currentThread().getId());
        Frame threadFrame = new Frame(parentThreadFrame, new Method(className, methodName), Thread.currentThread().getId());

        assertEquals(1, Profile._threadDictionary.size());
        assertSame(parentThreadFrame, Profile._threadDictionary.get(Thread.currentThread().getId()));
        assertEquals(2, Profile._frameList.size());
        assertSame(threadFrame, Profile._frameList.get(1));
        assertSame(parentThreadFrame, Profile._frameList.get(0));
    }

    @Test
    public void testStartEndWithSameMethodButDifferentClass() {
        Profile.init();
        String className1 = "Class1";
        String methodName = "testMethod";
        Profile.start(className1, methodName);
        Profile.end(className1, methodName);

        String className2 = "Class2";
        Profile.start(className2, methodName);
        Profile.end(className2, methodName);

        assertEquals(2, Profile._threadDictionary.size());
        assertEquals(2, Profile._frameList.size());
        Frame threadFrame1 = Profile._frameList.get(0);
        Frame threadFrame2 = Profile._frameList.get(1);
        assertNotSame(threadFrame1, threadFrame2);
    }

    @Test
    public void testStartEndWithSameMethodButSameClass() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Profile.start(className, methodName);
        Profile.end(className, methodName);

        assertEquals(1, Profile._threadDictionary.size());
        assertEquals(1, Profile._frameList.size());
    }

    @Test
    public void testStartEndWithParentAndSameMethodButDifferentClass() {
        Profile.init();
        String parentClassName = "ParentClass";
        String parentMethodName = "parentMethod";
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.start(className, methodName);
        Profile.end(className, methodName);

        Profile.start(className, methodName);
        Frame parentThreadFrame = new Frame(null, new Method(parentClassName, parentMethodName), Thread.currentThread().getId());
        Frame threadFrame = new Frame(parentThreadFrame, new Method(className, methodName), Thread.currentThread().getId());

        assertEquals(1, Profile._threadDictionary.size());
        assertSame(parentThreadFrame, Profile._threadDictionary.get(Thread.currentThread().getId()));
        assertEquals(3, Profile._frameList.size());
        assertSame(threadFrame, Profile._frameList.get(2));
        assertSame(parentThreadFrame, Profile._frameList.get(1));
        assertSame(Profile._frameList.get(0), Profile._threadDictionary.getMostRecentFrame(Thread.currentThread().getId()));
    }

    @Test
    public void testFindFrame() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        String parentClassName = "ParentClass";
        String parentMethodName = "parentMethod";

        Frame threadFrame = new Frame(null, new Method(className, methodName), Thread.currentThread().getId());
        Frame parentThreadFrame = new Frame(null, new Method(parentClassName, parentMethodName), Thread.currentThread().getId());
        parentThreadFrame.setParent(null);
        threadFrame.setParent(parentThreadFrame);

        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Profile.start(className, methodName);
        Profile.end(className, methodName);

        Frame frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertSame(threadFrame, frame);

        frame = Profile.findFrame(Thread.currentThread().getId(), className, "wrongMethodName");
        assertNull(frame);

        frame = Profile.findFrame(Thread.currentThread().getId(), "wrongClassName", methodName);
        assertNull(frame);

        frame = Profile.findFrame(Thread.currentThread().getId(), className, methodName);
        assertSame(threadFrame, frame);

        frame = Profile.findFrame(Thread.currentThread().getId(), parentClassName, parentMethodName);
        assertSame(parentThreadFrame, frame);
    }

    @Test
    public void testAlloc() {
        Profile.init();
        String className = "TestClass";
        Profile.alloc(className);
        Profile.alloc(className);

        ClassAllocation ca = Profile._allocList.get(className);
        assertEquals(2, ca.getAllocCount());
    }

    @Test
    public void testAllocWithSameClassNameButDifferentClassObject() {
        Profile.init();
        String className = "TestClass";
        Profile.alloc(className);
        Object newClassName = new Object() {};
        Profile.alloc(newClassName.getClass().getName());

        ClassAllocation ca = Profile._allocList.get(className);
        assertEquals(1, ca.getAllocCount());

        ca = Profile._allocList.get(newClassName.getClass().getName());
        assertEquals(1, ca.getAllocCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWithoutInit() {
        Profile.start("TestClass", "testMethod");
    }

    @Test(expected = IllegalStateException.class)
    public void testEndWithoutStart() {
        Profile.end("TestClass", "testMethod");
    }
}