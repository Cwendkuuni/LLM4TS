package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

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

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfo.getClassPackage());
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
    public void testConstructorWithClassNotFoundException() {
        new ClassInfo("com.jstevh.viewer.NonExistentClass");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(5, fields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.lang.Object");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(4, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(22, methods.length);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public int size()", methods[0]);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
        assertNotNull(methods);
        assertEquals(10, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);
        assertEquals("public ArrayList()", constructors[0]);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.List", interfaces[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.srchMethods("add");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean add(E e)", methods[0]);

        methods = classInfo.srchMethods("add", new String[]{"E"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean add(E e)", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("public boolean add(E e)", methodData.getMethName());
        assertEquals("java.util.ArrayList", methodData.getMethClass().getName());
    }

    @Test
    public void testMain() {
        String[] args = {"new", "java.util.ArrayList"};
        ClassInfo.main(args);
    }
}