package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Method;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isAbstract());

            ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isInterface());

            ClassInfo runnableInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(runnableInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang", classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testConstructor() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertNotNull(classInfo.c);
    }

    @Test
    public void testPrintMethods() throws ClassNotFoundException {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(151, methods.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testPrintConstructors() throws ClassNotFoundException {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(6, constructors.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testPrintFields() throws ClassNotFoundException {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] fields = classInfo.printFields();
            assertNotNull(fields);
            assertEquals(14, fields.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testPrintInterfaces() throws ClassNotFoundException {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.util.List", interfaces[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testSrchMethods() throws ClassNotFoundException {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String searchString = "add";
            String[] methods = classInfo.srchMethods(searchString);
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public void add(E e)", methods[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }

    @Test
    public void testGetFoundMethod() throws ClassNotFoundException {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.printMethods();
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("public java.util.ArrayList()", methodData.getMethClass() + " " + methodData.getMethName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException thrown");
        }
    }
}