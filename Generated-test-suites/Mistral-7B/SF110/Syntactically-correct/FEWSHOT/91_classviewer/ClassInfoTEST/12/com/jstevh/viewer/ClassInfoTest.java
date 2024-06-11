package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.jstevh.viewer.ClassInfo;
import java.lang.reflect.Method;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            assertFalse(ci.isAbstract());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.lang.AbstractString");
            assertTrue(ci.isAbstract());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.AbstractString");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            assertFalse(ci.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.List");
            assertTrue(ci.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.List");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", ci.getClassName());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            assertNull(ci.getClassPackage());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("com.jstevh.viewer.ClassInfo");
            assertEquals("com.jstevh.viewer", ci.getClassPackage());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: com.jstevh.viewer.ClassInfo");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.Object");
            assertNull(ci.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.Object");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", ci.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.ArrayList");
        }
    }

    @Test
    public void testPrintFields() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] fields = ci.printFields();
            assertEquals(0, fields.length);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] fields = ci.printFields();
            assertNotNull(fields);
            assertEquals(11, fields.length);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] methods = ci.printMethods();
            assertNotNull(methods);
            assertEquals(0, methods.length);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] methods = ci.printMethods();
            assertNotNull(methods);
            assertEquals(21, methods.length);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] methods = ci.printMethods(1);
            assertNotNull(methods);
            assertEquals(0, methods.length);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] methods = ci.printMethods(1);
            assertNotNull(methods);
            assertEquals(2, methods.length);
            String method1 = methods[0];
            String method2 = methods[1];
            assertTrue(method1.contains("long"));
            assertTrue(method2.contains("int"));
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] constructors = ci.printConstructors();
            assertNotNull(constructors);
            assertEquals(0, constructors.length);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] constructors = ci.printConstructors();
            assertNotNull(constructors);
            assertEquals(2, constructors.length);
            String constructor1 = constructors[0];
            String constructor2 = constructors[1];
            assertTrue(constructor1.contains("long"));
            assertTrue(constructor2.contains("int, int"));
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] interfaces = ci.printInterfaces();
            assertNull(interfaces);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.List");
            String[] interfaces = ci.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.util.Collection", interfaces[0]);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.List");
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] methods = ci.srchMethods(null);
            assertNull(methods);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] methods = ci.srchMethods("set");
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public void set(long time)", methods[0]);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }
    }

    @Test
    public void testSrchMethodsWithIndex() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] methods = ci.srchMethods(null, new String[]{"valueOf"});
            assertNull(methods);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.lang.String");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] methods = ci.srchMethods("parse", new String[]{"String"});
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public static Date parse(String dateString) throws ParseException", methods[0]);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            MethodData md = ci.getFoundMethod(0);
            assertNull(md);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            MethodData md = ci.getFoundMethod(1);
            assertNotNull(md);
            assertEquals("public void set(long time)", md.getMethodName());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class: java.util.Date");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}