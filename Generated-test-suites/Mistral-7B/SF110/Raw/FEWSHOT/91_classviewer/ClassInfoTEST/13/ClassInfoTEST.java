package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.StringJoiner;

import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertTrue(classInfo.isAbstract() == false);

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract() == true);
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.List");
        assertTrue(classInfo.isInterface() == false);

        classInfo = new ClassInfo("java.util.Map");
        assertTrue(classInfo.isInterface() == false);

        classInfo = new ClassInfo("java.util.Collection");
        assertTrue(classInfo.isInterface() == false);

        classInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.HashMap");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.LinkedList");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Hashtable");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Properties");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Enumeration");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Iterator");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Spliterator");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.function.Function");
        assertTrue(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.function.Supplier");
        assertTrue(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.function.Consumer");
        assertTrue(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.function.Predicate");
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

        classInfo = new ClassInfo("java.lang.StringBuilder");
        assertEquals("java.lang.StringBuilder", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.AbstractCollection");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.Collection");
        assertEquals("java.lang.Iterable", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Iterable");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(classInfo);
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        String methodsString = joiner.merge(methods);
        System.out.println(methodsString);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(classInfo);
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        String constructorsString = joiner.merge(constructors);
        System.out.println(constructorsString);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(classInfo);
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        String interfacesString = joiner.merge(interfaces);
        System.out.println(interfacesString);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = null;

        try {
            classInfo = new ClassInfo("java.util.ArrayList");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(classInfo);
        Method method = null;
        try {
            method = classInfo.getClass().getMethod("add", int.class, Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assertNotNull(method);
        int pos = indexOf(classInfo.cMethods, method.toString());
        MethodData methodData = classInfo.getFoundMethod(pos);
        assertNotNull(methodData);
        assertEquals(classInfo.c.getName(), methodData.getMethPackage());
        assertEquals(classInfo.c.getName(), methodData.getMethClass());
        assertEquals("add", methodData.getMethName());
    }

    private int indexOf(String[] array, String str) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }
}