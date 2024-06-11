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

            ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceInfo.isAbstract());

            ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractList");
            assertTrue(abstractClassInfo.isAbstract());

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

            ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceInfo.isInterface());

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

            ClassInfo arrayListClassInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.ArrayList", arrayListClassInfo.getClassName());

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

            ClassInfo arrayListClassInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util", arrayListClassInfo.getClassPackage());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            assertNull(stringClassInfo.getSuperClassName());

            ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", listClassInfo.getSuperClassName());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintFields() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            String[] stringData = stringClassInfo.printFields();
            assertNull(stringData);

            ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
            stringData = listClassInfo.printFields();
            assertNotNull(stringData);
            assertEquals(10, stringData.length);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            String[] stringData = stringClassInfo.printMethods();
            assertNotNull(stringData);
            assertEquals(0, stringData.length);

            ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
            stringData = listClassInfo.printMethods();
            assertNotNull(stringData);
            assertEquals(26, stringData.length);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            String[] stringData = stringClassInfo.printMethods(1);
            assertNull(stringData);

            ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
            stringData = listClassInfo.printMethods(1);
            assertNotNull(stringData);
            assertEquals(6, stringData.length);

            String[] methods = listClassInfo.printMethods();
            Method method = null;
            for (String methodName : methods) {
                try {
                    method = listClassInfo.c.getMethod(methodName);
                    break;
                } catch (NoSuchMethodException e) {
                    // continue
                }
            }
            assertEquals("removeRange", method.getName());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            String[] stringData = stringClassInfo.printConstructors();
            assertNull(stringData);

            ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
            stringData = listClassInfo.printConstructors();
            assertNotNull(stringData);
            assertEquals(6, stringData.length);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            String[] stringData = stringClassInfo.printInterfaces();
            assertNull(stringData);

            ClassInfo listClassInfo = new ClassInfo("java.util.ArrayList");
            stringData = listClassInfo.printInterfaces();
            assertNotNull(stringData);
            assertEquals(1, stringData.length);
            assertEquals("java.util.List", stringData[0]);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            String searchString = "value";
            String[] foundMethods = classInfo.srchMethods(searchString);
            assertNotNull(foundMethods);
            assertEquals(1, foundMethods.length);
            assertEquals("charAt", foundMethods[0]);

            searchString = "equals";
            foundMethods = classInfo.srchMethods(searchString, new String[]{"Object", "String"});
            assertNotNull(foundMethods);
            assertEquals(2, foundMethods.length);
            assertEquals("equals", foundMethods[0]);
            assertEquals("equals", foundMethods[1]);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            String methodName = "length";
            MethodData methodData = classInfo.getFoundMethod(Arrays.asList(methods).indexOf(methodName));
            assertEquals(methodName, methodData.getMethName());
            assertEquals("java.lang.String", methodData.getMethClass());
            assertEquals("java.lang.String", methodData.getMethPackage());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }
}