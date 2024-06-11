package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassInfoTest {

    private static final String CLASS_NAME = "com.jstevh.viewer.ClassInfo";
    private static final String PACKAGE_NAME = "com.jstevh.viewer";
    private static final String TEST_DIRECTORY = "src/test/java";

    private ClassInfo classInfo;

    @Before
    public void setUp() throws Exception {
        classInfo = new ClassInfo(CLASS_NAME);
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
        ClassInfo abstractClassInfo = new ClassInfo(AbstractClass.class.getName());
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        assertFalse(classInfo.isInterface());
        ClassInfo interfaceClassInfo = new ClassInfo(InterfaceClass.class.getName());
        assertTrue(interfaceClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        String className = classInfo.getClassName();
        assertEquals(CLASS_NAME, className);
    }

    @Test
    public void testGetClassPackage() throws Exception {
        String packageName = classInfo.getClassPackage();
        assertNull(packageName);
        ClassInfo packageClassInfo = new ClassInfo(PackageClass.class.getName());
        assertEquals(PACKAGE_NAME, packageClassInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        String superClassName = classInfo.getSuperClassName();
        assertNull(superClassName);
        ClassInfo superClassInfo = new ClassInfo(SuperClass.class.getName());
        assertEquals(SuperClass.class.getSuperclass().getName(), superClassInfo.getSuperClassName());
    }

    @Test
    public void testConstructorWithEmptyArgumentList() throws Exception {
        ClassInfo classInfo = new ClassInfo();
        assertNotNull(classInfo);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() throws Exception {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testConstructorWithLocManager() throws Exception {
        File testFile = new File(TEST_DIRECTORY, "testPackage");
        DirManager dirManager = new DirManager(testFile.getAbsolutePath());
        ClassInfo classInfo = new ClassInfo("TestClass", dirManager);
        assertNotNull(classInfo);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidPackageNameAndClassName() throws Exception {
        File testFile = new File(TEST_DIRECTORY, "testPackage");
        DirManager dirManager = new DirManager(testFile.getAbsolutePath());
        new ClassInfo("InvalidClassName", dirManager);
    }

    @Test
    public void testPrintFields() throws Exception {
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);
    }

    @Test
    public void testPrintMethods() throws Exception {
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        String[] methods = classInfo.printMethods(0);
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);
    }

    @Test
    public void testSrchMethodsWithEmptyString() throws Exception {
        String[] methods = classInfo.srchMethods(null);
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testSrchMethodsWithValidString() throws Exception {
        String[] methods = classInfo.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(1, methods.length);
    }

    @Test
    public void testSrchMethodsWithValidStringAndIndex() throws Exception {
        String[] methods = classInfo.srchMethods("toString", new String[]{"getClass"});
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFoundMethodWithInvalidIndex() throws Exception {
        Method method = classInfo.getFoundMethod(-1);
        assertNull(method);
    }

    @Test
    public void testGetFoundMethodWithValidIndex() throws Exception {
        Method method = classInfo.getFoundMethod(0);
        assertNotNull(method);
    }

    private static class AbstractClass {
        public abstract void abstractMethod();
    }

    private static interface InterfaceClass {
        void interfaceMethod();
    }

    private static class PackageClass {
    }

    private static class SuperClass extends Object {
        public SuperClass() {}
    }
}