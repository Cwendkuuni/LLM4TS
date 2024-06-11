package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractClass");
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
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassNameThrowsClassNotFoundException() {
        new ClassInfo("InvalidClassName");
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNullNameThrowsClassNotFoundException() {
        new ClassInfo(null);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(28, methods.length);
        for (int i = 0; i < methods.length; i++) {
            System.out.println(methods[i]);
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(5, constructors.length);
        for (int i = 0; i < constructors.length; i++) {
            System.out.println(constructors[i]);
        }
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public synchronized java.lang.String toString()", methods[0]);

        methods = classInfo.srchMethods("equals", new String[]{"Object"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean equals(java.lang.Object)", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("java.util.Collection", methodData.getMethPackage());
        assertEquals("java.util.ArrayList", methodData.getMethClass());
        assertEquals("add", methodData.getMethName());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndexThrowsArrayIndexOutOfBoundsException() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        MethodData methodData = classInfo.getFoundMethod(10);
    }

    @Test
    public void testPrintArray() {
        String[] array = new String[5];
        ClassInfo classInfo = new ClassInfo();
        classInfo.printArray(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    @Test
    public void testGetClass() {
        ClassInfo classInfo = new ClassInfo();
        Class clazz = classInfo.getClass("java.util.ArrayList");
        assertNotNull(clazz);
        assertEquals(ArrayList.class, clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassWithInvalidClassNameThrowsClassNotFoundException() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.getClass("InvalidClassName");
    }

    @Test
    public void testTryClassWithValidClassName() {
        Class clazz = ClassInfo.tryClass("java.util.ArrayList");
        assertNotNull(clazz);
        assertEquals(ArrayList.class, clazz);
    }

    @Test(expected = NoClassDefFoundError.class)
    public void testTryClassWithInvalidClassNameThrowsNoClassDefFoundError() {
        Class clazz = ClassInfo.tryClass("InvalidClassName");
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = {"new", "java.util.ArrayList"};
        ClassInfo.main(args);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMainWithInvalidArgumentsThrowsArrayIndexOutOfBoundsException() {
        String[] args = {"new", "java.util.ArrayList", "1"};
        ClassInfo.main(args);
    }
}