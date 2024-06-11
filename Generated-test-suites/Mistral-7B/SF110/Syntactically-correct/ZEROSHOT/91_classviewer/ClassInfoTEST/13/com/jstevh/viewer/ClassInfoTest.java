package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.util.List");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Map");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.HashMap");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Collection");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.concurrent.ConcurrentHashMap");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.concurrent.locks.Lock");
        assertTrue(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getClassPackage());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Boolean");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Number");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.AbstractMap");
        assertEquals("java.util.Map", classInfo.getSuperClassName());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(33, methods.length);

        classInfo = new ClassInfo("java.lang.Boolean");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(13, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(10, methods.length);

        classInfo = new ClassInfo("java.util.HashMap");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(11, methods.length);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertNotNull(methods);
        assertEquals(26, methods.length);

        classInfo = new ClassInfo("java.lang.Boolean");
        methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertNotNull(methods);
        assertEquals(11, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertNotNull(methods);
        assertEquals(6, methods.length);

        classInfo = new ClassInfo("java.util.HashMap");
        methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertNotNull(methods);
        assertEquals(5, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);

        classInfo = new ClassInfo("java.lang.Boolean");
        constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(2, constructors.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(5, constructors.length);

        classInfo = new ClassInfo("java.util.HashMap");
        constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(6, constructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.lang.Boolean");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.util.HashMap");
        interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.util.Map");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Map", interfaces[0]);

        classInfo = new ClassInfo("java.util.concurrent.Map");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Map", interfaces[0]);
    }
}