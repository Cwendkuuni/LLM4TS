package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.*;
import java.lang.reflect.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractString");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.lang.Runnable");
        assertTrue(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.AbstractCollection");
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(29, methods.length);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.List");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    @Test
    public void testSrchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("equals");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean java.lang.String.equals(java.lang.Object)", methods[0]);

        methods = classInfo.srchMethods("toString", new String[]{"Object"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public native java.lang.String java.lang.Object.toString()", methods[0]);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("public boolean java.lang.String.equals(java.lang.Object)", methodData.getMethPackage());
        assertEquals("java.lang.String", methodData.getMethClass());
        assertEquals("equals", methodData.getMethName());
    }
}