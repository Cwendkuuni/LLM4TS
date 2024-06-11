package com.jstevh.viewer;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.Exception;
import java.lang.reflect.Method;
import java.util.StringList;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertTrue(classInfo.isAbstract() == false);

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract() == true);
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface() == true);

        classInfo = new ClassInfo("java.util.List");
        assertTrue(classInfo.isInterface() == false);
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassNameWithInvalidClassname() throws Exception {
        new ClassInfo("InvalidClassname");
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetMethodWithInvalidMethodName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        Method method = classInfo.c.getMethod("getInvalidMethodName");
    }

    @Test
    public void testPrintFields() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(fields.length, 11);
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(methods.length, 21);
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(methods.length, 5);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(constructors.length, 7);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(interfaces.length, 1);
        assertEquals(interfaces[0], "java.util.Collection");
    }

    @Test
    public void testSrchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(methods.length, 1);
        assertEquals(methods[0], "public synchronized void toString()");

        methods = classInfo.srchMethods("toString", new String[]{"void"});
        assertNotNull(methods);
        assertEquals(methods.length, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFoundMethodWithInvalidIndex() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        Method method = classInfo.getFoundMethod(100);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("java.util.ArrayList", methodData.getMethPackage());
        assertEquals("java.util.ArrayList", methodData.getMethClass());
        assertEquals("add", methodData.getMethName());
    }
}