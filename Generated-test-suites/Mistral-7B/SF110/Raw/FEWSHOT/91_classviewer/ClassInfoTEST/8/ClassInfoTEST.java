package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInfoTest {

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

        classInfo = new ClassInfo("java.util.List");
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
        assertNull(classInfo.getClassPackage());

        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassThrowsException() {
        new ClassInfo("com.jstevh.viewer.NonExistentClass");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNull(fields);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        Arrays.stream(methods).forEach(System.out::println);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        Arrays.stream(methods).forEach(System.out::println);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        Arrays.stream(interfaces).forEach(System.out::println);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String tempStr = "toString";
        String[] methods = classInfo.srchMethods(tempStr);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public synchronized java.lang.String toString()", methods[0]);
    }

    @Test
    public void testSrchMethodsWithIndex() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String tempStr = "toString";
        String[] index = new String[]{"public"};
        String[] methods = classInfo.srchMethods(tempStr, index);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public synchronized java.lang.String toString()", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String tempStr = "toString";
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("toString", methodData.getMethName());
        assertEquals("java.lang.String", methodData.getMethClass());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodWrongIndex() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(1);
    }
}