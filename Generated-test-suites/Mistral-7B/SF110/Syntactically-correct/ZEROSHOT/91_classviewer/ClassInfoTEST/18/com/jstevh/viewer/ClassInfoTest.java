package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    private ClassInfo classInfo;

    @Before
    public void setup() {
        ClassInfo.debug = false;
        try {
            classInfo = new ClassInfo("javax.swing.JFrame");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testGetClassName() {
        assertEquals("javax.swing.JFrame", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        assertEquals("javax.swing", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());
    }

    @Test(expected = NullPointerException.class)
    public void testPrivateConstructor() {
        new ClassInfo();
    }

    @Test(expected = ClassNotFoundException.class)
    public void testInvalidClassName() throws ClassNotFoundException {
        new ClassInfo("Xyz.InvalidClassName");
    }

    @Test(expected = ClassNotFoundException.class)
    public void testInvalidClassNameWithDirManager() throws ClassNotFoundException {
        new ClassInfo("Xyz.InvalidClassName", new DirManager());
    }

    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        assertNotSame(0, fields.length);
        for (String field : fields) {
            System.out.println(field);
        }
    }

    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        assertNotSame(0, methods.length);
        for (String method : methods) {
            System.out.println(method);
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
        assertNotSame(0, methods.length);
        for (String method : methods) {
            System.out.println(method);
        }
    }

    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertNotSame(0, constructors.length);
        for (String constructor : constructors) {
            System.out.println(constructor);
        }
    }

    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertNotSame(0, interfaces.length);
        for (String interfaceName : interfaces) {
            System.out.println(interfaceName);
            try {
                Class interfaceClass = Class.forName(interfaceName);
                Method method = interfaceClass.getMethod("equals");
                assertEquals(method.getParameterTypes().length, 1);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testSrchMethods() {
        String[] methods = classInfo.printMethods();
        String searchString = "set";
        String[] foundMethods = classInfo.srchMethods(searchString);
        assertNotSame(0, foundMethods.length);
        for (String foundMethod : foundMethods) {
            Method method;
            try {
                method = classInfo.c.getMethod(foundMethod);
                assertEquals(method.getParameterTypes().length, 1);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            System.out.println(foundMethod + " -> " + method);
        }

        String[] searchParams = new String[] { "int" };
        String searchString2 = "set";
        String[] foundMethods2 = classInfo.srchMethods(searchString2, searchParams);
        assertNotSame(0, foundMethods2.length);
        for (String foundMethod : foundMethods2) {
            Method method;
            try {
                method = classInfo.c.getMethod(foundMethod, Arrays.asList(searchParams).toArray(new Class[0]));
                assertEquals(method.getParameterTypes().length, 1);
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(foundMethod + " -> " + method);
        }
    }

    @Test
    public void testGetFoundMethod() {
        String[] methods = classInfo.printMethods();
        String searchString = "setTitle";
        int index = Arrays.asList(methods).indexOf(searchString);
        MethodData methodData = classInfo.getFoundMethod(index);
        assertEquals("javax.swing.JFrame", methodData.getMethPackage());
        assertEquals("javax.swing.JFrame", methodData.getMethClass());
        assertEquals("setTitle", methodData.getMethName());
    }

    @Test
    public void testMain() {
        String[] args = new String[] { "new", "javax.swing.JFrame" };
        ClassInfo.main(args);
    }
}