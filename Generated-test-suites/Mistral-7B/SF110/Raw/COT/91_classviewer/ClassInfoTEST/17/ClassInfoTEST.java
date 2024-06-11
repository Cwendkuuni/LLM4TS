package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;
import com.jstevh.viewer.ClassInfo;
import java.lang.reflect.Method;

public class ClassInfoTest {

    private ClassInfo classInfo;

    @Before
    public void setUp() {
        classInfo = new ClassInfo("java.lang.String");
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        assertNotNull(classInfo.getClassPackage());
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassInfoConstructorThrowException() {
        new ClassInfo("nonexistentclass");
    }

    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertTrue(fields.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertTrue(methods.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testPrintMethodsParam() {
        String[] methods = classInfo.printMethods("java.lang.String");
        assertNotNull(methods);
        assertTrue(methods.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertTrue(constructors.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertTrue(interfaces.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testSrchMethods() {
        String searchTerm = "toString";
        String[] methods = classInfo.srchMethods(searchTerm);
        assertNotNull(methods);
        assertTrue(methods.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testSrchMethodsWithIndex() {
        String searchTerm = "equals";
        String[] index = {"java.lang.Object", "java.lang.String"};
        String[] methods = classInfo.srchMethods(searchTerm, index);
        assertNotNull(methods);
        assertTrue(methods.length > 0);
        // Add more assertions if needed
    }

    @Test
    public void testGetFoundMethod() {
        int position = 0;
        MethodData methodData = classInfo.getFoundMethod(position);
        assertNotNull(methodData);
        // Add more assertions if needed
    }

    @Test
    public void testPrintArray() {
        Object[] array = new Object[10];
        classInfo.printArray(array);
        // Add more assertions if needed
    }

    @Test
    public void testgetClass() {
        String className = "java.util.ArrayList";
        Class actualClass = classInfo.getClass(className);
        assertNotNull(actualClass);
        assertEquals(ArrayList.class, actualClass);
    }
}