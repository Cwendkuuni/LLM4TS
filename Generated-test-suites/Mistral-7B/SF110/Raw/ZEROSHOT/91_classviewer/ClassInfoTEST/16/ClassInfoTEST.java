package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.StringList;

import org.junit.Test;

import com.jstevh.tools.StringTools;
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
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.List");
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

        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    @Test
    public void testGetMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(25, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(6, constructors.length);
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.Hashtable");
        fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(11, fields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(25, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(6, methods.length);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.List", interfaces[0]);

        classInfo = new ClassInfo("java.util.HashMap");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(2, interfaces.length);
        assertEquals("java.io.Serializable", interfaces[0]);
        assertEquals("java.util.Map", interfaces[1]);
    }

    @Test
    public void testSearchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(3);
        assertNotNull(methodData);
        assertEquals("public static java.lang.String valueOf(java.lang.String)", methodData.getMethName());

        String[] methods = classInfo.srchMethods("valueOf");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public static java.lang.String valueOf(java.lang.String)", methods[0]);

        methods = classInfo.srchMethods("valueOf", new String[]{"StringBuilder"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public static java.lang.String valueOf(java.lang.StringBuilder)", methods[0]);
    }
}

class MethodData {
    String tempStr;
    int begin;
    int end;

    public String getMethPackage() {
        return null;
    }

    public String getMethClass() {
        return null;
    }

    public String getMethName() {
        return null;
    }
}