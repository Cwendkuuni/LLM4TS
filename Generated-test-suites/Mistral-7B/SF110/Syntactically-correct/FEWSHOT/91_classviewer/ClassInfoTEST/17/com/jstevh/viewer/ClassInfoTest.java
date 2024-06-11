package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.StringList;

import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        assertFalse(ci.isAbstract());

        ci = new ClassInfo("java.lang.AbstractString");
        assertTrue(ci.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        assertFalse(ci.isInterface());

        ci = new ClassInfo("java.util.List");
        assertTrue(ci.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", ci.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        assertNull(ci.getClassPackage());

        ci = new ClassInfo("java.util.Collection");
        assertEquals("java.util", ci.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        assertNull(ci.getSuperClassName());

        ci = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", ci.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("org.nonExistentPackage.NonExistentClass");
    }

    @Test
    public void testPrintFields() {
        String[] fields = new ClassInfo("java.util.Date").printFields();
        assertNotNull(fields);

        for (String field : fields) {
            String[] parts = field.split("=");
            assertEquals(parts[0].trim(), parts[1].trim());
        }
    }

    @Test
    public void testPrintMethods() {
        String[] methods = new ClassInfo("java.lang.Math").printMethods();
        assertNotNull(methods);

        for (String method : methods) {
            String[] parts = method.split(" ");
            assertEquals(parts[parts.length - 1], parts[0].substring(parts[0].lastIndexOf(".") + 1));
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        String[] methods = new ClassInfo("java.lang.String").printMethods(1);
        assertNull(methods);

        methods = new ClassInfo("java.util.ArrayList").printMethods(0);
        assertNotNull(methods);

        assertEquals(methods[0].substring(methods[0].lastIndexOf(".")), "add");
    }

    @Test
    public void testPrintConstructors() {
        String[] constructors = new ClassInfo("java.util.Date").printConstructors();
        assertNotNull(constructors);

        for (String constructor : constructors) {
            String[] parts = constructor.split(" ");
            assertEquals(parts[parts.length - 1], "Date");
        }
    }

    @Test
    public void testPrintInterfaces() {
        String[] interfaces = new ClassInfo("java.util.ArrayList").printInterfaces();
        assertNull(interfaces);

        interfaces = new ClassInfo("java.util.Collection").printInterfaces();
        assertNotNull(interfaces);

        assertEquals(interfaces[0], "java.util.Collection");
    }

    @Test
    public void testSearchMethods() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        String[] methods = ci.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(methods[0], "public synchronized java.lang.String toString()");

        methods = ci.srchMethods("toString", new String[]{"Object"});
        assertNotNull(methods);
        assertEquals(methods[0], "public synchronized java.lang.String toString(java.lang.Object) throws java.lang.IllegalArgumentException");
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo ci = new ClassInfo("java.lang.String");
        MethodData md = ci.getFoundMethod(0);
        assertNotNull(md);

        assertEquals(md.getMethPackage(), null);
        assertEquals(md.getMethClass(), "java.lang.String");
        assertEquals(md.getMethName(), "toString");
    }

    @Test
    public void testPrintArray() {
        String[] array = new ClassInfo(String[].class).printMethods();
        assertNotNull(array);

        for (String method : array) {
            String[] parts = method.split(" ");
            String name = parts[parts.length - 1];
            Method m = null;
            try {
                m = String[].class.getMethod(name);
            } catch (NoSuchMethodException e) {
                fail("Method '" + name + "' not found");
            }
            assertEquals(m.getParameterTypes().length, 0);
        }
    }
}