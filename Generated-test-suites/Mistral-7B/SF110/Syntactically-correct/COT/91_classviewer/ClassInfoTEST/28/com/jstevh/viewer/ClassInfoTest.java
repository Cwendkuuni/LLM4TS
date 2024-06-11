package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.StringTools;
import org.junit.Test;

import java.io.File;
import java.lang.ClassCastException;
import java.lang.Exception;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertTrue(classInfo.isAbstract());

        ClassInfo interfaceClassInfo = new ClassInfo(List.class.getName());
        assertFalse(interfaceClassInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractCollection");
        assertTrue(abstractClassInfo.isAbstract());

        ClassInfo nonAbstractClassInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(nonAbstractClassInfo.isAbstract());

        ClassInfo nullClassInfo = new ClassInfo(null);
        assertFalse(nullClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertFalse(classInfo.isInterface());

        ClassInfo interfaceClassInfo = new ClassInfo(List.class.getName());
        assertTrue(interfaceClassInfo.isInterface());

        ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractCollection");
        assertFalse(abstractClassInfo.isInterface());

        ClassInfo nullClassInfo = new ClassInfo(null);
        assertFalse(nullClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertEquals("java.lang.String", classInfo.getClassName());

        try {
            new ClassInfo(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertEquals("java.lang", classInfo.getClassPackage());

        ClassInfo interfaceClassInfo = new ClassInfo(List.class.getName());
        assertNull(interfaceClassInfo.getClassPackage());

        try {
            new ClassInfo(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo stringClassInfo = new ClassInfo(String.class.getName());
        assertNull(stringClassInfo.getSuperClassName());

        ClassInfo listClassInfo = new ClassInfo(List.class.getName());
        assertEquals("java.util.Collection", listClassInfo.getSuperClassName());

        try {
            new ClassInfo(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithNoArguments() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(WindowConstants.class, classInfo.c);

        try {
            new ClassInfo(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithName() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.ArrayList", classInfo.c.getName());

        try {
            new ClassInfo(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithNameAndDirManager() throws ClassNotFoundException {
        DirManager dirManager = new DirManager();
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList", dirManager);
        assertEquals("java.util.ArrayList", classInfo.c.getName());

        try {
            new ClassInfo(null, dirManager);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new ClassInfo("java.util.ArrayList", null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidName() throws Exception {
        new ClassInfo("java.bad.ClassName");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        fields = stringBuilderClassInfo.printFields();
        assertNotNull(fields);
        assertEquals(11, fields.length);

        try {
            new ClassInfo(null).printFields();
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        methods = stringBuilderClassInfo.printMethods();
        assertNotNull(methods);
        assertEquals(26, methods.length);

        try {
            new ClassInfo(null).printMethods();
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo stringClassInfo = new ClassInfo(String.class.getName());
        String[] methods = stringClassInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(0, methods.length);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        methods = stringBuilderClassInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(2, methods.length);

        try {
            new ClassInfo(null).printMethods(1);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo stringClassInfo = new ClassInfo(String.class.getName());
        String[] constructors = stringClassInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        constructors = stringBuilderClassInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(7, constructors.length);

        try {
            new ClassInfo(null).printConstructors();
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo stringClassInfo = new ClassInfo(String.class.getName());
        String[] interfaces = stringClassInfo.printInterfaces();
        assertNull(interfaces);

        ClassInfo listClassInfo = new ClassInfo(List.class.getName());
        interfaces = listClassInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        interfaces = stringBuilderClassInfo.printInterfaces();
        assertNull(interfaces);

        ClassInfo hashSetClassInfo = new ClassInfo("java.util.HashSet");
        interfaces = hashSetClassInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Set", interfaces[0]);

        try {
            new ClassInfo(null).printInterfaces();
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSrchMethods() {
        ClassInfo stringClassInfo = new ClassInfo(String.class.getName());
        String[] methods = stringClassInfo.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public java.lang.String toString()", methods[0]);

        ClassInfo javaUtilListClassInfo = new ClassInfo("java.util.List");
        methods = javaUtilListClassInfo.srchMethods("size");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public int size()", methods[0]);

        ClassInfo javaLangStringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        methods = javaLangStringBuilderClassInfo.srchMethods("append");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public java.lang.StringBuilder append(java.lang.String)", methods[0]);

        ClassInfo javaLangStringClassInfo = new ClassInfo("java.lang.String");
        methods = javaLangStringClassInfo.srchMethods("substring");
        assertNotNull(methods);
        assertEquals(2, methods.length);
        assertEquals("public java.lang.String substring(int)", methods[0]);
        assertEquals("public java.lang.String substring(int, int)", methods[1]);

        try {
            new ClassInfo(null).srchMethods("toString");
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new ClassInfo("java.bad.ClassName").srchMethods("toString");
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            new ClassInfo("java.util.ArrayList").srchMethods("size", new String[]{"int"});
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo javaLangStringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        MethodData methodData = javaLangStringBuilderClassInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("java.lang.StringBuilder", methodData.getMethClass());
        assertEquals("toString", methodData.getMethName());
        assertEquals(null, methodData.getMethPackage());

        try {
            new ClassInfo("java.bad.ClassName").getFoundMethod(0);
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            new ClassInfo("java.util.ArrayList").getFoundMethod(10);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testPrintArray() {
        String[] array = new String[3];
        ClassInfo classInfo = new ClassInfo();
        classInfo.printArray(array);

        try {
            classInfo.printArray(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetClass() {
        ClassInfo classInfo = new ClassInfo();
        Class<?> stringClass = classInfo.getClass("java.lang.String");
        assertEquals(String.class, stringClass);

        Class<?> listClass = classInfo.getClass("java.util.List");
        assertEquals(List.class, listClass);

        Class<?> nullClass = classInfo.getClass(null);
        assertNull(nullClass);

        try {
            Class<?> unknownClass = classInfo.getClass("java.unknown.ClassName");
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testTryClass() {
        Class<?> stringClass = ClassInfo.tryClass("java.lang.String");
        assertEquals(String.class, stringClass);

        Class<?> listClass = ClassInfo.tryClass("java.util.List");
        assertEquals(List.class, listClass);

        Class<?> nullClass = ClassInfo.tryClass(null);
        assertNull(nullClass);

        Class<?> unknownClass = ClassInfo.tryClass("java.unknown.ClassName");
        assertNull(unknownClass);

        try {
            Class<?> unknownClass2 = ClassInfo.tryClass("java.bad.ClassName");
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }
    }
}