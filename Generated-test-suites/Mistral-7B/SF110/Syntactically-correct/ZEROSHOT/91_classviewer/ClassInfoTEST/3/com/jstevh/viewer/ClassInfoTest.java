package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.getClassPackage());
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("com.jstevh.viewer.NonExistantClass");
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(56, methods.length);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertNotNull(methods);
        assertEquals(21, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);
        assertEquals("(Ljava/lang/String;)V", constructors[0]);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(2, interfaces.length);
        assertEquals("java.io.Serializable", interfaces[0]);
        assertEquals("java.util.List", interfaces[1]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("equals");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean equals(Ljava/lang/Object;)Z", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("valueOf");
        Method method = classInfo.getFoundMethod(0).getMethod();
        assertEquals("public static Ljava/lang/String; valueOf(Ljava/lang/String;)Ljava/lang/String;", method.toGenericString());
    }
}