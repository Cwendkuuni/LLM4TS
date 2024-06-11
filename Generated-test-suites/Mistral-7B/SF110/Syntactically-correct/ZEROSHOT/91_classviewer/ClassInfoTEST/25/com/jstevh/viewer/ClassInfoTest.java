package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.List");
        assertTrue(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
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

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(38, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(78, methods.length);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(8, constructors.length);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.util.ArrayList");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    @Test
    public void testSrchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("equals");
        assertNotNull(methods);
        assertEquals(2, methods.length);

        methods = classInfo.srchMethods("toString", new String[]{"Object", "int"});
        assertNotNull(methods);
        assertEquals(1, methods.length);

        methods = classInfo.srchMethods("hashCode", new String[]{"Object"});
        assertNotNull(methods);
        assertEquals(1, methods.length);

        methods = classInfo.srchMethods("hashCode");
        assertNull(methods);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);

        String expectedPackage = "java.lang";
        String expectedClassName = "String";
        String expectedMethodName = "equals";
        String expectedMethodSignature = "(" + "Ljava/lang/Object;)Z";

        assertEquals(expectedPackage, methodData.getMethPackage());
        assertEquals(expectedClassName, methodData.getMethClass());
        assertEquals(expectedMethodName, methodData.getMethName());
        assertEquals(expectedMethodSignature, methodData.getMethodSignature());
    }
}