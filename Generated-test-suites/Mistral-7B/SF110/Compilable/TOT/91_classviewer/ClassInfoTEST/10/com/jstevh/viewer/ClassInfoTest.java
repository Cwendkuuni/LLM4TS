package com.jstevh.viewer;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.AbstractList");
        assertTrue(classInfo.isAbstract());

        ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo2.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.List");
        assertFalse(classInfo.isInterface());

        ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo2.isInterface());

        ClassInfo classInfo3 = new ClassInfo("java.util.Collections");
        assertTrue(classInfo3.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.ArrayList", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertNull(classInfo.getClassPackage());

        ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList", null);
        assertNull(classInfo2.getClassPackage());

        ClassInfo classInfo3 = new ClassInfo("com.jstevh.viewer.ClassInfo", new DirManager());
        assertEquals("com.jstevh.viewer", classInfo3.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertNull(classInfo.getSuperClassName());

        ClassInfo classInfo2 = new ClassInfo("java.awt.Color");
        assertEquals("java.lang.Object", classInfo2.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws Exception {
        new ClassInfo("java.koko.Koko");
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(methods.length, 21);
        Arrays.stream(methods).forEach(System.out::println);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(constructors.length, 6);
        Arrays.stream(constructors).forEach(System.out::println);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        ClassInfo classInfo2 = new ClassInfo("java.util.Observable");
        String[] interfaces2 = classInfo2.printInterfaces();
        assertEquals(interfaces2.length, 1);
        Arrays.stream(interfaces2).forEach(System.out::println);
    }

    @Test
    public void testSrchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(methods.length, 1);
        Arrays.stream(methods).forEach(System.out::println);

        ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList", null);
        String[] methods2 = classInfo2.srchMethods("toString", new String[]{"Object"});
        assertNotNull(methods2);
        assertEquals(methods2.length, 1);
        Arrays.stream(methods2).forEach(System.out::println);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("get", methodData.getMethName());
        assertEquals("java.lang.Object", methodData.getMethClass());
        assertEquals("java.lang.Object get()", methodData.getMethPackage());

        MethodData methodData2 = classInfo.getFoundMethod(1);
        assertNotNull(methodData2);
        assertEquals("clear", methodData2.getMethName());
        assertEquals("java.util.ArrayList", methodData2.getMethClass());
        assertEquals("java.util.ArrayList clear()", methodData2.getMethPackage());
    }
}