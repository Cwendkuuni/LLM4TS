package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class ClassInfoTest {

    private static final String CLASS_NAME = "com.jstevh.viewer.ClassInfo";
    private static final String PACKAGE_NAME = "com.jstevh.viewer";
    private static final String DIR_PATH = "path/to/your/test/directory";
    private DirManager dirMan;
    private ClassInfo classInfo;

    @Before
    public void setUp() throws Exception {
        dirMan = new DirManager(new File(DIR_PATH));
        classInfo = new ClassInfo(CLASS_NAME, dirMan);
    }

    @Test
    public void testIsAbstract() throws Exception {
        assertFalse(classInfo.isAbstract());
        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractMethodError", dirMan);
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        assertFalse(classInfo.isInterface());
        ClassInfo interfaceClassInfo = new ClassInfo("java.util.Iterator", dirMan);
        assertTrue(interfaceClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        assertEquals(CLASS_NAME, classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        String packageName = PACKAGE_NAME;
        assertEquals(packageName, classInfo.getClassPackage());
        ClassInfo nestedClassInfo = new ClassInfo("com.jstevh.viewer.NestedClass", dirMan);
        String nestedPackageName = "com.jstevh.viewer.NestedClass";
        assertEquals(nestedPackageName, nestedClassInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        assertNull(classInfo.getSuperClassName());
        ClassInfo superClassInfo = new ClassInfo("java.lang.Object", dirMan);
        assertEquals("java.lang.Object", superClassInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructor_InvalidClassName() throws Exception {
        new ClassInfo("java.axx.InvalidClassName", dirMan);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructor_NoSuchFile() throws Exception {
        DirManager noFileDirMan = new DirManager(new File("non-existent-directory"));
        new ClassInfo(CLASS_NAME, noFileDirMan);
    }

    @Test
    public void testPrintMethods() throws Exception {
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        // Add more assertions based on the expected methods in ClassInfo class
    }

    @Test
    public void testPrintMethods_Parameter() throws Exception {
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        // Add more assertions based on the expected methods in ClassInfo class
    }

    @Test
    public void testPrintConstructors() throws Exception {
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        // Add more assertions based on the expected constructors in ClassInfo class
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        // Add more assertions based on the expected interfaces in ClassInfo class
    }
}