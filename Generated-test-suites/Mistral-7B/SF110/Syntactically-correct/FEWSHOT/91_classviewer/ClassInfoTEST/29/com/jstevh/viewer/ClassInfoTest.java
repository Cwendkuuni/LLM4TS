package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;
import java.lang.reflect.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo info = new ClassInfo("java.lang.String");
        assertFalse(info.isAbstract());

        info = new ClassInfo("java.lang.AbstractString");
        assertTrue(info.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo info = new ClassInfo("java.lang.String");
        assertFalse(info.isInterface());

        info = new ClassInfo("java.util.List");
        assertTrue(info.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo info = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", info.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo info = new ClassInfo("java.lang.String");
        assertNull(info.getClassPackage());

        info = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", info.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo info = new ClassInfo("java.lang.String");
        assertNull(info.getSuperClassName());

        info = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", info.getSuperClassName());
    }

    @Test
    public void testConstructorWithNoArguments() throws Exception {
        ClassInfo info = new ClassInfo();
        assertNotNull(info.c);
        assertEquals(WindowConstants.class, info.c);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() throws Exception {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testPrintFields() {
        ClassInfo info = new ClassInfo("java.lang.String");
        String[] fields = info.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        info = new ClassInfo("java.util.HashMap");
        fields = info.printFields();
        assertNotNull(fields);
        assertNotEquals(0, fields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo info = new ClassInfo("java.lang.String");
        String[] methods = info.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);

        info = new ClassInfo("java.util.ArrayList");
        methods = info.printMethods();
        assertNotNull(methods);
        assertNotEquals(0, methods.length);
    }

    @Test
    public void testPrintMethodsWithParameter() {
        ClassInfo info = new ClassInfo("java.util.ArrayList");
        String[] methods = info.printMethods(1);
        assertNotNull(methods);
        assertNull(methods);

        info = new ClassInfo("java.util.ArrayList");
        methods = info.printMethods(0);
        assertNotNull(methods);
        assertNotEquals(0, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo info = new ClassInfo("java.lang.String");
        String[] constructors = info.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);

        info = new ClassInfo("java.util.ArrayList");
        constructors = info.printConstructors();
        assertNotNull(constructors);
        assertNotEquals(0, constructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo info = new ClassInfo("java.lang.String");
        String[] interfaces = info.printInterfaces();
        assertNull(interfaces);

        info = new ClassInfo("java.util.ArrayList");
        interfaces = info.printInterfaces();
        assertNotNull(interfaces);
        assertNotEquals(0, interfaces.length);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo info = new ClassInfo("java.util.ArrayList");
        String[] methods = info.srchMethods("add");
        assertNotNull(methods);
        assertNotEquals(0, methods.length);

        methods = info.srchMethods("add", new String[]{"int"});
        assertNotNull(methods);
        assertNotEquals(0, methods.length);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo info = new ClassInfo("java.util.ArrayList");
        MethodData method = info.getFoundMethod(0);
        assertNotNull(method);

        String packageName = method.getMethPackage();
        String className = method.getMethClass();
        String methodName = method.getMethName();

        assertEquals("java.util", packageName);
        assertEquals("java.util.ArrayList", className);
        assertEquals("add", methodName);
    }
}