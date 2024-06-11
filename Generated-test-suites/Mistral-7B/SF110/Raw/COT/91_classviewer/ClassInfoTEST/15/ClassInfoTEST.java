package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.ClassInfo.MethodData;

@RunWith(Parameterized.class)
public class ClassInfoTest {

    private ClassInfo classInfo;
    private String className;
    private boolean isInterfaceExpected;
    private boolean isAbstractExpected;
    private String superClassNameExpected;
    private String[] expectedMethods;
    private String[] expectedInterfaces;

    public ClassInfoTest(String className, boolean isInterfaceExpected, boolean isAbstractExpected, String superClassNameExpected, String[] expectedMethods, String[] expectedInterfaces) {
        this.className = className;
        this.isInterfaceExpected = isInterfaceExpected;
        this.isAbstractExpected = isAbstractExpected;
        this.superClassNameExpected = superClassNameExpected;
        this.expectedMethods = expectedMethods;
        this.expectedInterfaces = expectedInterfaces;
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "java.lang.String", false, false, null, new String[] {}, new String[] {} },
            { "java.util.ArrayList", false, false, "java.util.AbstractList", new String[] { "add", "size", "get" }, new String[] { "java.util.List", "java.util.RandomAccess", "java.io.Serializable" } },
            { "java.lang.Object", true, false, null, new String[] {}, new String[] {} },
            { "com.jstevh.viewer.ClassInfoTest$DummyClass", false, false, null, new String[] { "testMethod" }, new String[] {} }
        });
    }

    @Test
    public void testClassInfo() throws Exception {
        classInfo = new ClassInfo();
        classInfo.printClassInfo();
        assertEquals(isInterfaceExpected, classInfo.isInterface());
        assertEquals(isAbstractExpected, classInfo.isAbstract());
        assertEquals(superClassNameExpected, classInfo.getSuperClassName());
        assertArrayEquals(expectedMethods, classInfo.printMethods());
        assertArrayEquals(expectedInterfaces, classInfo.printInterfaces());
    }

    @Test
    public void testClassInfoWithLocation() throws Exception {
        ClassInfo classInfo = new ClassInfo(className, new DirManager() { /* stub */ });
        classInfo.printClassInfo();
        assertEquals(isInterfaceExpected, classInfo.isInterface());
        assertEquals(isAbstractExpected, classInfo.isAbstract());
        assertEquals(superClassNameExpected, classInfo.getSuperClassName());
        assertArrayEquals(expectedMethods, classInfo.printMethods());
        assertArrayEquals(expectedInterfaces, classInfo.printInterfaces());
    }

    // Add more test cases here for other methods and edge cases

    private static class DummyClass {
        public void testMethod() {
        }
    }
}