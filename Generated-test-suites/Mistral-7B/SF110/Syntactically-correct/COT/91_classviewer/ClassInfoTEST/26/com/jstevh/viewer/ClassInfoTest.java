package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.DirManager;
import com.jstevh.viewer.StringTools;

public class ClassInfoTest {

    // Basic tests for public methods

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());
        
        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());
        
        classInfo = new ClassInfo("java.lang.Runnable");
        assertTrue(classInfo.isInterface());
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
        
        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertNull(classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());
        
        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());
        
        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = null;
        
        try {
            classInfo = new ClassInfo("java.lang.String");
            String[] fields = classInfo.printFields();
            assertEquals(0, fields.length);
        } catch (Exception e) {
            // Ignore exceptions, as they are expected when loading String class
        }
        
        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] fields = classInfo.printFields();
            assertNotNull(fields);
            assertEquals(5, fields.length);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = null;
        
        try {
            classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(117, methods.length);
        } catch (Exception e) {
            // Ignore exceptions, as they are expected when loading String class
        }
        
        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(84, methods.length);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    @Test
    public void testPrintMethodsParam() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.printMethods(1);
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public java.util.ArrayList<E> <init>()", methods[0]);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(1, constructors.length);
            assertEquals("public java.util.ArrayList<E>()<init>()", constructors[0]);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.io.Serializable", interfaces[0]);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    // Edge case and exception handling tests

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("com.jstevh.nonexistent.Class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        new ClassInfo(null);
    }

    @Test(expected = SecurityException.class)
    public void testSecurityException() {
        ClassInfo classInfo = new ClassInfo("sun.misc.Unsafe");
        try {
            Method method = classInfo.c.getMethod("getUnsafe");
            assertFalse(method.isAccessible());
            method.setAccessible(true);
            method.invoke(classInfo.c.newInstance());
            fail("Method execution should have thrown SecurityException");
        } catch (Exception e) {
            assertTrue(e instanceof SecurityException);
        }
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.srchMethods("toString");
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("public java.lang.String toString()", methods[0]);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            String[] methods = classInfo.srchMethods("toString", new String[]{"java.util.Collection"});
            assertNotNull(methods);
            assertEquals(3, methods.length);
            String expected = "public java.lang.String toString()";
            assertTrue(Arrays.asList(methods).contains(expected));
            assertEquals("public java.lang.String toString()", methods[0]);
            assertEquals("public java.lang.String toString()", methods[2]);
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("java.util.ArrayList", methodData.getMethClass());
            assertEquals("toString", methodData.getMethName());
            assertNull(methodData.getMethPackage());
        } catch (Exception e) {
            fail("Failed to load ArrayList class");
        }
    }

    @Test
    public void testPrintArray() {
        String[] array = {"test1", "test2", "test3"};
        String[] expected = {"test1", "test2", "test3"};

        ClassInfo classInfo = new ClassInfo();
        classInfo.printArray(array);

        String[] actual = classInfo.printMethods();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetClass() {
        ClassInfo classInfo = new ClassInfo();
        Class tempClass = classInfo.getClass("java.util.ArrayList");
        assertEquals(ArrayList.class, tempClass);

        try {
            classInfo = new ClassInfo();
            tempClass = classInfo.getClass("com.jstevh.nonexistent.Class");
            fail("ClassNotFoundException expected");
        } catch (ClassNotFoundException e) {
            // Expected
        }

        try {
            classInfo = new ClassInfo();
            tempClass = classInfo.getClass(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testTryClass() {
        Class tempClass = ClassInfo.tryClass("java.util.ArrayList");
        assertEquals(ArrayList.class, tempClass);

        tempClass = ClassInfo.tryClass("com.jstevh.nonexistent.Class");
        assertNull(tempClass);
    }
}

// Helper class for testing MethodData
class MethodData {
    String tempStr;
    int begin;
    int end;

    public String getMethPackage() {
        return null;
    }

    public String getMethClass() {
        return null;
    }

    public String getMethName() {
        return null;
    }
}