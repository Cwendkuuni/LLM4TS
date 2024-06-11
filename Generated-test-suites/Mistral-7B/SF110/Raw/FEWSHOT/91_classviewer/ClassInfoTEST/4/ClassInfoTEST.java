package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;
import java.lang.reflect.Method;
import org.junit.Test;

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
            fail("ClassNotFoundException should not be thrown.");
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
            fail("ClassNotFoundException should not be thrown.");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.Collection", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(26, methods.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods(1);
            assertNotNull(methods);
            assertEquals(6, methods.length);
            Method method = classInfo.c.getMethod("charAt", int.class);
            assertEquals(method.toString(), methods[0]);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            fail("ClassNotFoundException or NoSuchMethodException should not be thrown.");
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(1, constructors.length);
            Method method = classInfo.c.getConstructor();
            assertEquals(method.toString(), constructors[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
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
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown.");
        }
    }
}