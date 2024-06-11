package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

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
            fail("Failed to load class");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isInterface());

            ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util", classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(45, methods.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.printMethods(1);
            assertNotNull(methods);
            assertEquals(2, methods.length);
            assertTrue(Arrays.asList(methods).contains("public int size()"));
            assertTrue(Arrays.asList(methods).contains("public boolean isEmpty()"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
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
            fail("Failed to load class");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(8, constructors.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] interfaces = classInfo.printInterfaces();
            assertNull(interfaces);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.util.List", interfaces[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.srchMethods("equals");
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public boolean equals(java.lang.Object)", methods[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.srchMethods("add", new String[]{"int", "E"});
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public void add(int", methods[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("public int size()", methodData.getMethName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class");
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Invalid number of found methods");
        }
    }
}