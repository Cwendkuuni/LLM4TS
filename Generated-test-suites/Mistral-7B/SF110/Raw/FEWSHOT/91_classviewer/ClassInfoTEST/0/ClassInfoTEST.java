package com.jstevh.viewer.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.StringTools;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(abstractClassInfo.isAbstract());

        ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
        assertTrue(interfaceClassInfo.isInterface());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertFalse(classInfo.isInterface());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertFalse(abstractClassInfo.isInterface());

        ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
        assertTrue(interfaceClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertEquals(String.class.getName(), classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.getClassPackage());

        ClassInfo classInfoWithPackage = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfoWithPackage.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.getSuperClassName());

        ClassInfo classInfoWithSuperclass = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfoWithSuperclass.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("non.existent.class.name");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.printFields());

        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        String[] fields = stringBuilderInfo.printFields();
        assertNotNull(fields);
        assertEquals(11, fields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], classInfo.printMethods());

        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        String[] methods = stringBuilderInfo.printMethods();
        assertNotNull(methods);
        assertEquals(33, methods.length);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], classInfo.printMethods(1));

        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        String[] methods = stringBuilderInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("append(char)", methods[0]);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], classInfo.printConstructors());

        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        String[] constructors = stringBuilderInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(10, constructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], classInfo.printInterfaces());

        ClassInfo arrayListInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = arrayListInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.RandomAccess", interfaces[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        String[] methods = classInfo.srchMethods("toString");
        assertArrayEquals(new String[0], methods);

        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        methods = stringBuilderInfo.srchMethods("append");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("append(char)", methods[0]);

        methods = stringBuilderInfo.srchMethods("append", new String[]{"java.lang.String"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("append(String)", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        MethodData methodData = stringBuilderInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("append", methodData.getMethName());
        assertEquals("java.lang.StringBuilder", methodData.getMethClass());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndex() {
        ClassInfo stringBuilderInfo = new ClassInfo("java.lang.StringBuilder");
        MethodData methodData = stringBuilderInfo.getFoundMethod(10);
    }

    @Test
    public void testPrintArray() {
        String[] array = {"foo", "bar", "baz"};
        ClassInfo classInfo = new ClassInfo();
        classInfo.printArray(array);

        String[] expected = {"foo", "bar", "baz"};
        ClassInfo classInfo2 = new ClassInfo();
        classInfo2.printArray(expected);

        assertArrayEquals(expected, array);
    }

    @Test
    public void testGetClass() {
        ClassInfo classInfo = new ClassInfo();
        Class stringClass = classInfo.getClass("java.lang.String");
        assertEquals(String.class, stringClass);

        try {
            classInfo.getClass("non.existent.class.name");
            fail();
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testTryClass() {
        Class stringClass = Class.forName("java.lang.String");
        ClassInfo classInfo = new ClassInfo();
        Class result = classInfo.tryClass("java.lang.String");
        assertEquals(stringClass, result);

        result = classInfo.tryClass("non.existent.class.name");
        assertNull(result);
    }

    @Test
    public void testPrintClassInfo() throws Exception {
        ClassInfo classInfo = new ClassInfo();
        classInfo.main(new String[]{"new", "java.lang.StringBuilder"});
    }
}