package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.WindowConstants;

public class ClassInfoTest {

    static ClassInfo classInfo;

    @BeforeClass
    public static void setup() throws Exception {
        classInfo = new ClassInfo("javax.swing.JFrame");
    }

    

    // Test isAbstract()
    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsAbstractWhenClassIsAbstract() throws Exception {
        classInfo = new ClassInfo("java.lang.AbstractClass");
        assertTrue(classInfo.isAbstract());
    }

    // Test isInterface()
    @Test
    public void testIsInterface() {
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testIsInterfaceWhenClassIsInterface() {
        classInfo = new ClassInfo("java.util.List");
        assertTrue(classInfo.isInterface());
    }

    // Test getClassName()
    @Test
    public void testGetClassName() {
        assertEquals("javax.swing.JFrame", classInfo.getClassName());
    }

    // Test getClassPackage()
    @Test
    public void testGetClassPackage() {
        assertEquals("javax.swing", classInfo.getClassPackage());
    }

    // Test getSuperClassName()
    @Test
    public void testGetSuperClassName() {
        assertEquals(Object.class.getName(), classInfo.getSuperClassName());
    }

    @Test
    public void testGetSuperClassNameWhenClassHasSuperClass() throws Exception {
        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    // Test constructor with no arguments
    @Test
    public void testConstructorNoArguments() throws Exception {
        classInfo = new ClassInfo();
        assertEquals(WindowConstants.class.getName(), classInfo.getClassName());
    }

    // Test constructor with arguments
    @Test
    public void testConstructorWithArguments() throws Exception {
        ClassInfo classInfoWithLocManager = new ClassInfo("javax.swing.JFrame", new DirManager());
        assertEquals("javax.swing.JFrame", classInfoWithLocManager.getClassName());
    }

    // Test printFields()
    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);
    }

    // Test printMethods()
    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    // Test printMethods with parameter
    @Test
    public void testPrintMethodsWithParameter() {
        String[] methods = classInfo.printMethods("toString");
        assertNotNull(methods);
        assertEquals(1, methods.length);
    }

    // Test printConstructors()
    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);
    }

    // Test printInterfaces()
    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);
    }

    // Test getData() with Members
    @Test
    public void testGetDataWithMembers() {
        Class<?> clazz = Class.forName("java.lang.String");
        Method[] methods = clazz.getMethods();
        String[] data = ClassInfo.getData(methods);
        assertNotNull(data);
    }

    // Test getData() with Classes
    @Test
    public void testGetDataWithClasses() {
        Class<?> clazz = Class.forName("java.lang.String");
        Class<?>[] classes = { clazz, Object.class };
        String[] data = ClassInfo.getData(classes);
        assertNotNull(data);
        assertEquals(1, data.length);
        assertEquals(clazz.getName(), data[0]);
    }

    // Test srchMethods() with empty String
    @Test
    public void testSrchMethodsWithEmptyString() {
        String[] methods = classInfo.srchMethods(null);
        assertNull(methods);
    }

    // Test srchMethods() with String and empty index array
    @Test
    public void testSrchMethodsWithStringAndEmptyIndexArray() {
        String tempStr = "toString";
        String[] methods = classInfo.srchMethods(tempStr, new String[0]);
        assertNull(methods);
    }

    // Test srchMethods() with String and non-empty index array
    @Test
    public void testSrchMethodsWithStringAndNonEmptyIndexArray() {
        String tempStr = "toString";
        String[] index = {"toString"};
        String[] methods = classInfo.srchMethods(tempStr, index);
        assertNotNull(methods);
        assertEquals(1, methods.length);
    }

    // Test getFoundMethod() with valid index
    @Test
    public void testGetFoundMethodWithValidIndex() {
        String[] methods = classInfo.srchMethods("toString");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertEquals(methods[0], methodData.getMethName());
    }

    // Test getFoundMethod() with invalid index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndex() {
        String[] methods = classInfo.srchMethods("toString");
        classInfo.getFoundMethod(methods.length);
    }

    // Test tryClass() with valid class name
    @Test
    public void testTryClassWithValidClassName() throws Exception {
        String className = "java.lang.String";
        Class tempClass = ClassInfo.tryClass(className);
        assertNotNull(tempClass);
    }

    // Test tryClass() with invalid class name
    @Test(expected = ClassNotFoundException.class)
    public void testTryClassWithInvalidClassName() throws Exception {
        String className = "InvalidClassName";
        Class tempClass = ClassInfo.tryClass(className);
        assertNull(tempClass);
    }

    // Test tryClass() with package and class name
    @Test
    public void testTryClassWithPackageAndClassName() throws Exception {
        String packageName = "java.lang.";
        String className = "StringBuilder";
        Class tempClass = ClassInfo.tryClass(packageName + className);
        assertNotNull(tempClass);
    }

    
}