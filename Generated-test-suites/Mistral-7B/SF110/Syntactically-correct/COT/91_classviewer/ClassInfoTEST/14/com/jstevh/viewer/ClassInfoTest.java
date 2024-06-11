package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.StringList;

import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertTrue(classInfo.isAbstract());
        classInfo = new ClassInfo("java.lang.String"); // already initialized, statusAbstract should be true
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.util.AbstractList");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.lang.Runnable");
        assertTrue(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.ArrayList", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getClassPackage());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws Exception {
        new ClassInfo("java.notavalidclass");
    }

    @Test
    public void testPrintFields() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(2, fields.length);
        assertEquals("capacity=10", fields[0]);
        assertEquals("size=0", fields[1]);
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(20, methods.length);
        assertEquals("add(E)", methods[0]);
        assertEquals("clone()", methods[1]);
        assertEquals("get(int)", methods[18]);
        assertEquals("set(int, E)", methods[19]);
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("add(E)", methods[0]);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);
        assertEquals("ArrayList()", constructors[0]);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertArrayEquals(new String[0], interfaces);

        classInfo = new ClassInfo("java.util.ArrayList");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);

        classInfo = new ClassInfo("java.util.AbstractList");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.List", interfaces[0]);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    @Test
    public void testGetData() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] data = ClassInfo.getData(classInfo.c.getDeclaredMethods());
        assertNotNull(data);
        assertEquals(0, data.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        data = ClassInfo.getData(classInfo.c.getDeclaredConstructors());
        assertNotNull(data);
        assertEquals(1, data.length);
        assertEquals("ArrayList(int)", data[0]);
    }

    @Test
    public void testSrchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.srchMethods("toString");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public synchronized void toString()", methods[0]);

        methods = classInfo.srchMethods("toString", new String[]{"int"});
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        classInfo.fndMethods = new String[0];
        String[] methods = classInfo.getFoundMethod(1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testArrayIndexOutOfBoundsException() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.cMethods;
        Method method = classInfo.c.getMethod("get", int.class);
        methods[0] = method.toString();
        String[] foundMethods = classInfo.fndMethods;
        MethodData methodData = classInfo.getFoundMethod(1);
        methodData.getMethPackage();
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethodException() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        classInfo.fndMethods = new String[0];
        String[] methods = classInfo.cMethods;
        Method method = classInfo.c.getMethod("get", double.class);
        methods[0] = method.toString();
        String[] foundMethods = classInfo.fndMethods;
        MethodData methodData = classInfo.getFoundMethod(1);
    }

    @Test
    public void testMain() throws Exception {
        String[] args = new String[1];
        args[0] = "new java.util.ArrayList";
        ClassInfo.main(args);
    }
}