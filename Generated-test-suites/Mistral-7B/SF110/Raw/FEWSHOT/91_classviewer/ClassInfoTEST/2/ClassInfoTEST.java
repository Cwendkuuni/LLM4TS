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
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Collection");
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
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Object");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNull(fields);

        classInfo = new ClassInfo("java.util.ArrayList");
        String[] fields2 = classInfo.printFields();
        assertNotNull(fields2);
        assertEquals(11, fields2.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNull(methods);

        classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods2 = classInfo.printMethods();
        assertNotNull(methods2);
        assertEquals(85, methods2.length);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods(0);
        assertNotNull(methods);
        assertEquals(5, methods.length);
        assertTrue(Arrays.asList(methods).contains("public int size()"));
        assertTrue(Arrays.asList(methods).contains("public boolean isEmpty()"));
        assertTrue(Arrays.asList(methods).contains("public boolean contains(Object o)"));
        assertTrue(Arrays.asList(methods).contains("public int indexOf(Object o)"));
        assertTrue(Arrays.asList(methods).contains("public Object get(int index)"));

        methods = classInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(16, methods.length);
        assertTrue(Arrays.asList(methods).contains("public ArrayList<E> add(int index, E element)"));
        assertTrue(Arrays.asList(methods).contains("public E get(int index)"));
        assertTrue(Arrays.asList(methods).contains("public E set(int index, E element)"));
        assertTrue(Arrays.asList(methods).contains("public void add(int index, Object elemen)t"));
        assertTrue(Arrays.asList(methods).contains("public boolean add(E e)"));
        assertTrue(Arrays.asList(methods).contains("public boolean remove(int index)"));
        assertTrue(Arrays.asList(methods).contains("public boolean remove(Object o)"));
        assertTrue(Arrays.asList(methods).contains("public void clear()"));
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);

        classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors2 = classInfo.printConstructors();
        assertNotNull(constructors2);
        assertEquals(7, constructors2.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces2 = classInfo.printInterfaces();
        assertNotNull(interfaces2);
        assertEquals(1, interfaces2.length);
        assertEquals("java.util.List", interfaces2[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.srchMethods("toString");
        assertNull(methods);

        methods = classInfo.srchMethods("toString", new String[]{"public"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public String toString()", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);

        String[] methods = classInfo.srchMethods("add");
        methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("public boolean add(E e)", methodData.getMethName());
    }
}