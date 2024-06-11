package com.jstevh.viewer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class ClassInfoTest {

    

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        Assert.assertFalse(classInfo.isAbstract());

        ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
        Assert.assertTrue(interfaceInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo stringInfo = new ClassInfo("java.lang.String");
        Assert.assertFalse(stringInfo.isInterface());

        ClassInfo runnableInfo = new ClassInfo("java.lang.Runnable");
        Assert.assertTrue(runnableInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String className = classInfo.getClassName();
        Assert.assertEquals("java.lang.String", className);
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String packageName = classInfo.getClassPackage();
        Assert.assertNotNull(packageName);
        Assert.assertEquals("java.lang", packageName);
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String superClassName = classInfo.getSuperClassName();
        Assert.assertNull(superClassName);

        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
        superClassName = stringClassInfo.getSuperClassName();
        Assert.assertEquals("java.lang.Object", superClassName);
    }

    @Test
    public void testConstructorWithNoArguments() throws Exception {
        ClassInfo classInfo = new ClassInfo();
        Class<?> clazz = classInfo.c;
        Assert.assertNotNull(clazz);
        Assert.assertEquals(WindowConstants.class, clazz);
    }

    @Test
    public void testConstructorWithArguments() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.io.File");
        Class<?> clazz = classInfo.c;
        Assert.assertNotNull(clazz);
        Assert.assertEquals(File.class, clazz);
    }

    @Test
    public void testConstructorWithDirManager() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.io.File", new DirManager(new File(".")));
        Class<?> clazz = classInfo.c;
        Assert.assertNotNull(clazz);
        Assert.assertEquals(File.class, clazz);
    }

    @Test
    public void testPrintFields() throws Exception {
        // Create a ClassInfo object for a class with public fields
        ClassInfo stringInfo = new ClassInfo("java.lang.String");
        String[] fields = stringInfo.printFields();

        // Check if the returned array is not null and has some elements
        Assert.assertNotNull(fields);
        Assert.assertFalse(Arrays.equals(new String[0], fields));
    }

    @Test
    public void testPrintMethods() throws Exception {
        // Create a ClassInfo object for a class with public methods
        ClassInfo stringInfo = new ClassInfo("java.lang.String");
        String[] methods = stringInfo.printMethods();

        // Check if the returned array is not null and has some elements
        Assert.assertNotNull(methods);
        Assert.assertFalse(Arrays.equals(new String[0], methods));
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        // Create a ClassInfo object for a class with public methods
        ClassInfo stringInfo = new ClassInfo("java.lang.String");
        String[] methods = stringInfo.printMethods(1);

        // Check if the returned array is not null and has some elements
        Assert.assertNotNull(methods);
        Assert.assertFalse(Arrays.equals(new String[0], methods));
    }

    @Test
    public void testPrintConstructors() throws Exception {
        // Create a ClassInfo object for a class with public constructors
        ClassInfo stringInfo = new ClassInfo("java.lang.String");
        String[] constructors = stringInfo.printConstructors();

        // Check if the returned array is not null and has some elements
        Assert.assertNotNull(constructors);
        Assert.assertFalse(Arrays.equals(new String[0], constructors));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws Exception {
        new ClassInfo("nonExistentClass");
    }

    
}