package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
            assertTrue(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo(String.class);
        assertFalse(classInfo.isInterface());

        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            assertTrue(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        ClassInfo classInfo2 = new ClassInfo("java.lang.String");
        assertEquals("java.lang.Object", classInfo2.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testInvalidClassName() {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(29, methods.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintMethodsWithParameter() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods(1);
            assertNotNull(methods);
            assertEquals(3, methods.length);
            assertEquals("length()", methods[0]);
            assertEquals("charAt(int)", methods[1]);
            assertEquals("substring(int, int)", methods[2]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(0, constructors.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo classInfo = new ClassInfo(String.class);
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(0, interfaces.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.srchMethods("length");
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("length()", methods[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("java.lang.String", methodData.getMethClass());
            assertEquals("length()", methodData.getMethName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNewInstance() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        Constructor constructor = classInfo.c.getConstructor(new Class[] { String.class });
        Object o = constructor.newInstance(new Object[] { "Hello, World!" });
        assertEquals("Hello, World!", o.toString());
    }
}