package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInfoTest {

    private static ClassInfo classInfo;
    private static String className;

    @BeforeClass
    public static void setUpClass() throws Exception {
        className = "com.jstevh.viewer.ClassInfo";
        classInfo = new ClassInfo(); // Test constructor with no arguments
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNullNameThrowsException() throws Exception {
        new ClassInfo(null);
    }

    @Test
    public void testConstructorWithValidName() throws Exception {
        Class<?> clazz = Class.forName(className);
        classInfo = new ClassInfo(className);
        assertEquals(className, classInfo.getClassName());
        assertEquals(clazz.getPackage().getName(), classInfo.getClassPackage());
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testConstructorWithValidNameAndDirManager() throws Exception {
        DirManager dirManager = new DirManager();
        classInfo = new ClassInfo(className, dirManager);
        assertEquals(className, classInfo.getClassName());
        assertEquals(clazz.getPackage().getName(), classInfo.getClassPackage());
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        classInfo = new ClassInfo(className);
        String[] fields = classInfo.printFields();
        // Add some assertions based on the expected fields of the ClassInfo class
        assertNotNull(fields);
        assertTrue(fields.length > 0);
        // ...
    }

    @Test
    public void testPrintMethods() {
        classInfo = new ClassInfo(className);
        String[] methods = classInfo.printMethods();
        // Add some assertions based on the expected methods of the ClassInfo class
        assertNotNull(methods);
        assertTrue(methods.length > 0);
        // ...
    }

    @Test
    public void testPrintMethodsWithParameter() {
        classInfo = new ClassInfo(className);
        String[] methods = classInfo.printMethods(1);
        // Add some assertions based on the expected method with index 1 of the ClassInfo class
        assertNotNull(methods);
        if (methods != null && methods.length > 0) {
            assertEquals("someMethodName", methods[0]);
        }
        // ...
    }

    @Test
    public void testPrintConstructors() {
        classInfo = new ClassInfo(className);
        String[] constructors = classInfo.printConstructors();
        // Add some assertions based on the expected constructors of the ClassInfo class
        assertNotNull(constructors);
        assertTrue(constructors.length > 0);
        // ...
    }

    @Test
    public void testPrintInterfaces() {
        classInfo = new ClassInfo(className);
        String[] interfaces = classInfo.printInterfaces();
        // Add some assertions based on the expected interfaces of the ClassInfo class
        assertNotNull(interfaces);
        assertTrue(interfaces.length > 0);
        // ...
    }

    @Test(expected = NullPointerException.class)
    public void testPrintMethodsWithNullMethodsThrowsException() {
        classInfo = new ClassInfo(className);
        classInfo.cMethods = null;
        classInfo.printMethods();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidPositionThrowsException() {
        classInfo = new ClassInfo(className);
        String[] methods = classInfo.cMethods;
        assertNull(classInfo.getFoundMethod(-1));
        assertNull(classInfo.getFoundMethod(methods.length));
    }

    @Test
    public void testGetFoundMethodWithValidPosition() throws Exception {
        String expectedClassName = "com.jstevh.viewer.ClassInfoTest";
        String expectedMethodName = "testPrintMethods";
        String expectedMethodSignature = "()V";

        classInfo = new ClassInfo(className);
        Method testPrintMethodsMethod = ClassInfoTest.class.getDeclaredMethod(expectedMethodName);
        Method foundMethod = classInfo.getFoundMethod(Arrays.asList(className, expectedMethodName, expectedMethodSignature).toString().hashCode());

        assertNotNull(foundMethod);
        assertEquals(expectedClassName, foundMethod.getMethClass());
        assertEquals(expectedMethodName, foundMethod.getMethName());
        assertEquals(expectedMethodSignature, foundMethod.getMethSignature());
    }
}