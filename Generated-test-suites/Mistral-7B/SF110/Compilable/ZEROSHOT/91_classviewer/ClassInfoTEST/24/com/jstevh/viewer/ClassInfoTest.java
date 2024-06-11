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
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
            assertTrue(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            assertTrue(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
            assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            // Printing methods for debugging purposes
            for (Method method : java.lang.String.class.getMethods()) {
                System.out.println(method.getName());
            }

            for (String methodName : methods) {
                Method method = java.lang.String.class.getMethod(methodName.substring(1));
                assertEquals(methodName.substring(0, 1), method.isAccessible() ? method.getModifiers() : method.getDeclaringClass().getName().charAt(0) == ' ' ? method.getDeclaringClass().getSimpleName().substring(0, 1) : method.getDeclaringClass().getName().charAt(0), methodName.charAt(0));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
            assertNotNull(methods);

            for (String methodName : methods) {
                Method method = java.util.List.class.getMethod(methodName);
                assertTrue(method.isAccessible());
                assertEquals(methodName, method.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintMethodsNoObject() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
            assertNotNull(methods);

            for (String methodName : methods) {
                Method method = java.util.List.class.getDeclaredMethod(methodName);
                assertFalse(method.isAccessible());
                assertEquals(methodName, method.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }
}