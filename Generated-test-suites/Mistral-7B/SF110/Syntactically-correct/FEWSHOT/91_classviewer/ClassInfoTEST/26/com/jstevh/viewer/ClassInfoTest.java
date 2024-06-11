package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
            assertTrue(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.Collection");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.Map");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.HashMap");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.io.Serializable");
            assertTrue(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util", classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.LinkedList");
            assertEquals("java.util.AbstractSequentialList", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(56, methods.length);
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods(1);
            assertNotNull(methods);
            assertEquals(2, methods.length);
            assertEquals("valueOf(char[])", methods[0]);
            assertEquals("valueOf(String)", methods[1]);
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(7, constructors.length);
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.util.List", interfaces[0]);
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.srchMethods("substring");
            assertNotNull(methods);
            assertEquals(3, methods.length);
            assertEquals("substring(int)", methods[0]);
            assertEquals("substring(int, int)", methods[1]);
            assertEquals("substring(int, int)", methods[2]);
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("substring(int)", methodData.getMethName());
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndex() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            MethodData methodData = classInfo.getFoundMethod(100);
        } catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException");
        }
    }
}