package com.jstevh.viewer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.ClassFormatError;
import java.lang.reflect.IllegalAccessException;
import java.lang.reflect.NoSuchMethodException;
import java.util.Arrays;

public class ClassInfoTest {

    private ClassInfo classInfo;

    @Before
    public void setUp() {
        try {
            classInfo = new ClassInfo("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsAbstract() {
        Assert.assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        Assert.assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        Assert.assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        Assert.assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        Assert.assertNull(classInfo.getSuperClassName());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullName() {
        new ClassInfo(null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNonExistentClassName() {
        new ClassInfo("com.jstevh.viewer.NonExistentClass");
    }

    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        Assert.assertNotNull(fields);
        Assert.assertTrue(fields.length > 0);
    }

    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.length > 0);
    }

    @Test
    public void testPrintMethodsWithParam() {
        String[] methods = classInfo.printMethods(1);
        Assert.assertNotNull(methods);
        Assert.assertFalse(methods.length == 0);
    }

    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        Assert.assertNotNull(constructors);
        Assert.assertTrue(constructors.length > 0);
    }

    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        Assert.assertNotNull(interfaces);
        Assert.assertTrue(interfaces.length > 0);
    }

    @Test
    public void testSrchMethods() {
        String[] methods = classInfo.srchMethods("toString");
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.length > 0);

        // Test with empty search string
        methods = classInfo.srchMethods(null);
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.length == 0);
    }

    @Test
    public void testSrchMethodsWithIndex() {
        String[] methods = classInfo.srchMethods("equals", new String[]{"Object"});
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.length > 0);

        // Test with empty search string and index
        methods = classInfo.srchMethods(null, new String[0]);
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.length == 0);
    }

    @Test
    public void testGetFoundMethod() {
        String[] methods = classInfo.printMethods();
        MethodData methodData = classInfo.getFoundMethod(0);

        Assert.assertNotNull(methodData);
        Assert.assertEquals(methods[0], methodData.getMethName());
        Assert.assertEquals("java.lang.String", methodData.getMethPackage());
        Assert.assertEquals("java.lang.String", methodData.getMethClass());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndex() {
        classInfo.getFoundMethod(10);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetMethodWithNonExistentMethod() throws NoSuchMethodException {
        Method method = classInfo.c.getMethod("nonExistentMethod");
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetFieldWithNonExistentField() throws IllegalAccessException {
        Object object = new Object();
        Field field = classInfo.c.getField("nonExistentField");
        field.get(object);
    }

    @Test(expected = ClassFormatError.class)
    public void testConstructorWithMalformedClassName() {
        new ClassInfo("12345.String");
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithMissingPackageClassName() throws ClassNotFoundException {
        new ClassInfo("String");
    }
}