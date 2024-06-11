package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        ClassInfo stringBufferInterface = new ClassInfo("java.util.function.BiFunction");
        assertTrue(stringBufferInterface.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getClassPackage());

        ClassInfo javaUtilArrayListClassInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", javaUtilArrayListClassInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
        assertNull(stringClassInfo.getSuperClassName());

        ClassInfo vectorClassInfo = new ClassInfo("java.util.Vector");
        assertEquals("java.util.Collection", vectorClassInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws Exception {
        new ClassInfo("com.jstevh.viewer.NonExistentClass");
    }

    @Test
    public void testPrintMethods() throws Exception {
        String[] expectedMethods = {"public java.lang.String toString()",
                "public native long hashCode()",
                "public native java.lang.String toLowerCase()",
                "public native java.lang.String toUpperCase()",
                "public java.lang.Class<?> getClass()",
                "public static java.lang.String valueOf(java.lang.char[])"};
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
        String[] actualMethods = stringClassInfo.printMethods();
        assertArrayEquals(expectedMethods, actualMethods);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        String[] expectedConstructors = {"public java.lang.String()",
                "public java.lang.String(java.lang.String)",
                "public java.lang.String(java.lang.String, int)"};
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
        String[] actualConstructors = stringClassInfo.printConstructors();
        assertArrayEquals(expectedConstructors, actualConstructors);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        String[] expectedInterfaces = {};
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
        String[] actualInterfaces = stringClassInfo.printInterfaces();
        assertArrayEquals(expectedInterfaces, actualInterfaces);

        String[] expectedInterfacesForMap = {"java.util.Map"};
        ClassInfo hashMapClassInfo = new ClassInfo("java.util.HashMap");
        String[] actualInterfacesForMap = hashMapClassInfo.printInterfaces();
        assertArrayEquals(expectedInterfacesForMap, actualInterfacesForMap);
    }

    @Test
    public void testSrchMethods() throws Exception {
        String[] classMethods = {"public java.lang.String toString()",
                "public native long hashCode()",
                "public native java.lang.String toLowerCase()",
                "public native java.lang.String toUpperCase()",
                "public java.lang.Class<?> getClass()",
                "public static java.lang.String valueOf(java.lang.char[])",
                "public java.lang.String concat(java.lang.String)"};
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");

        String[] foundMethods = stringClassInfo.srchMethods("concat");
        assertArrayEquals(new String[]{"public java.lang.String concat(java.lang.String)"}, foundMethods);

        String[] foundMethodsWithIndex = stringClassInfo.srchMethods("concat", new String[]{"java.lang.String"});
        assertArrayEquals(new String[]{"public java.lang.String concat(java.lang.String)"}, foundMethodsWithIndex);

        String[] emptyMethods = stringClassInfo.srchMethods(null);
        assertArrayEquals(new String[]{}, emptyMethods);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFoundMethodNull() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        classInfo.getFoundMethod(1);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        String[] classMethods = {"public java.lang.String toString()",
                "public native long hashCode()",
                "public native java.lang.String toLowerCase()",
                "public native java.lang.String toUpperCase()",
                "public java.lang.Class<?> getClass()",
                "public static java.lang.String valueOf(java.lang.char[])",
                "public java.lang.String concat(java.lang.String)"};
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");

        MethodData methodData = stringClassInfo.getFoundMethod(0);
        String methodPackage = methodData.getMethPackage();
        String methodClassName = methodData.getMethClass();
        String methodName = methodData.getMethName();
        assertEquals("java.lang", methodPackage);
        assertEquals("java.lang.String", methodClassName);
        assertEquals("toString", methodName);

        methodData = stringClassInfo.getFoundMethod(6);
        methodPackage = methodData.getMethPackage();
        methodClassName = methodData.getMethClass();
        methodName = methodData.getMethName();
        assertEquals("java.lang", methodPackage);
        assertEquals("java.lang.String", methodClassName);
        assertEquals("concat", methodName);
    }
}