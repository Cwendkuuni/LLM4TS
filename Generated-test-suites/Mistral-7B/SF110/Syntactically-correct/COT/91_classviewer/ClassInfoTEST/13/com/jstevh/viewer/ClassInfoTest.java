package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    private ClassInfo classInfo;

    @Before
    public void setUp() {
        classInfo = new ClassInfo("java.lang.String");
    }

    

    // Test isAbstract()
    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsAbstractForInterface() {
        ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
        assertTrue(interfaceInfo.isAbstract());
    }

    // Test isInterface()
    @Test
    public void testIsInterface() {
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testIsInterfaceForInterface() {
        assertTrue(classInfo = new ClassInfo("java.lang.Runnable").isInterface());
    }

    // Test getClassName()
    @Test
    public void testGetClassName() {
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    // Test getClassPackage()
    @Test
    public void testGetClassPackage() {
        assertNull(classInfo.getClassPackage());
    }

    @Test
    public void testGetClassPackageForClassInPackage() {
        ClassInfo classInfoForPackage = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfoForPackage.getClassPackage());
    }

    // Test getSuperClassName()
    @Test
    public void testGetSuperClassNameForObject() {
        assertNull(classInfo.getSuperClassName());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSuperClassNameForNullClass() {
        new ClassInfo(null);
    }

    @Test
    public void testGetSuperClassNameForClassWithSuperclass() {
        ClassInfo classInfoWithSuperclass = new ClassInfo("java.lang.Object");
        assertEquals("java.lang.Object", classInfoWithSuperclass.getSuperClassName());
    }

    // Test constructor with no arguments
    @Test
    public void testConstructorWithNoArguments() {
        ClassInfo classInfo = new ClassInfo();
        assertNotNull(classInfo.c);
        assertEquals(ClassInfo.class, classInfo.c.getClass());
    }

    // Test constructor with name
    @Test
    public void testConstructorWithName() throws ClassNotFoundException {
        assertEquals("java.lang.String", classInfo.getClassName());
        ClassInfo classInfoWithName = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.ArrayList", classInfoWithName.getClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNonExistentClassName() throws ClassNotFoundException {
        new ClassInfo("nonExistentClass");
    }

    // Test constructor with name and DirManager
    @Test
    public void testConstructorWithNameAndDirManager() throws ClassNotFoundException {
        ClassInfo classInfoWithNameAndDirManager = new ClassInfo("java.util.ArrayList", new DirManager());
        assertEquals("java.util.ArrayList", classInfoWithNameAndDirManager.getClassName());
    }

    // Test printFields()
    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        assertArrayEquals(new String[0], fields);
    }

    // Test printMethods()
    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintMethodsForClassWithMethods() {
        String[] methods = classInfo = new ClassInfo("java.lang.String").printMethods();
        assertNotNull(methods);
        assertEquals(31, methods.length);
    }

    // Test printMethods(int param)
    @Test
    public void testPrintMethodsForClassWithMethodsAndFilter() {
        String[] methods = classInfo = new ClassInfo("java.lang.String").printMethods(1);
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintMethodsForClassWithMethodsAndFilterForMethod() {
        String[] methods = classInfo = new ClassInfo("java.lang.String").printMethods(11);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("valueOf(char)", methods[0]);
    }

    // Test printConstructors()
    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertArrayEquals(new String[0], constructors);
    }

    // Test printInterfaces()
    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertArrayEquals(new String[0], interfaces);
    }

    @Test
    public void testPrintInterfacesForClassWithInterfaces() {
        String[] interfaces = classInfo = new ClassInfo("java.lang.Runnable").printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.lang.Runnable", interfaces[0]);
    }

    
}