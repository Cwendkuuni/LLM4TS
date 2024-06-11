package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;
import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractString");
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

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("non.existent.class");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.Date");
        String[] dateFields = classInfo.printFields();
        assertEquals(12, dateFields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.util.Date");
        String[] dateMethods = classInfo.printMethods();
        assertEquals(28, dateMethods.length);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(1);
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.util.Date");
        String[] dateMethods = classInfo.printMethods(2);
        assertEquals(7, dateMethods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertEquals(0, constructors.length);

        classInfo = new ClassInfo("java.util.Date");
        String[] dateConstructors = classInfo.printConstructors();
        assertEquals(2, dateConstructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.util.ArrayList");
        String[] arrayListInterfaces = classInfo.printInterfaces();
        assertEquals(1, arrayListInterfaces.length);
        assertEquals("java.util.List", arrayListInterfaces[0]);

        classInfo = new ClassInfo("java.util.ArrayList", null);
        String[] arrayListInterfaces2 = classInfo.printInterfaces();
        assertEquals(1, arrayListInterfaces2.length);
        assertEquals("java.util.List", arrayListInterfaces2[0]);
        assertEquals("java.io.Serializable", arrayListInterfaces2[1]);
    }
}