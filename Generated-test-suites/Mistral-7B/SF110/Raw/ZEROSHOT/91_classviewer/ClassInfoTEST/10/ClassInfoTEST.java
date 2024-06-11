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
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
            assertTrue(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            assertTrue(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util", classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            assertEquals("java.lang.AbstractStringBuilder", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testConstructor() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNotNull(classInfo);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String", null);
            assertNotNull(classInfo);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String", new DirManager());
            assertNotNull(classInfo);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo(null);
            fail("ClassNotFoundException should be thrown");
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String", null);
            fail("ClassNotFoundException should be thrown");
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String", new DirManager());
            fail("ClassNotFoundException should be thrown");
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testPrintFields() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] fields = classInfo.printFields();
            assertNotNull(fields);
            assertEquals(0, fields.length);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] fields = classInfo.printFields();
            assertNotNull(fields);
            assertFalse(Arrays.equals(new String[]{}, fields));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testPrintMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(0, methods.length);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertFalse(Arrays.equals(new String[0], methods));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testPrintMethodsNoObjectMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
            assertNotNull(methods);
            assertFalse(Arrays.equals(new String[0], methods));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testPrintMethodsNoInheritedMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
            assertNotNull(methods);
            assertEquals(0, methods.length);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
            assertNotNull(methods);
            assertFalse(Arrays.equals(new String[0], methods));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testPrintConstructors() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(0, constructors.length);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertFalse(Arrays.equals(new String[0], constructors));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] interfaces = classInfo.printInterfaces();
            assertNull(interfaces);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertFalse(Arrays.equals(new String[0], interfaces));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test
    public void testSrchMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] methods = classInfo.srchMethods("toString");
            assertNotNull(methods);
            assertFalse(Arrays.equals(new String[0], methods));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] methods = classInfo.srchMethods("toString", new String[]{"java.lang.StringBuilder"});
            assertNotNull(methods);
            assertFalse(Arrays.equals(new String[0], methods));
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData.getMethPackage());
            assertNotNull(methodData.getMethClass());
            assertNotNull(methodData.getMethName());
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        } catch (IndexOutOfBoundsException e) {
            fail("Invalid index exception should not be thrown");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSrchMethodsNull() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            String[] methods = classInfo.srchMethods(null);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodInvalidIndex() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
            MethodData methodData = classInfo.getFoundMethod(-1);
        } catch (ClassNotFoundException e) {
            fail("Class not found exception should not be thrown");
        }
    }
}