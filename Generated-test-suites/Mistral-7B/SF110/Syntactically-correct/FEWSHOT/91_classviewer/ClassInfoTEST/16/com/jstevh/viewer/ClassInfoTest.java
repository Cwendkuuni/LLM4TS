package com.jstevh.viewer.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertTrue(ci.isInterface() == false);
        assertTrue(ci.isAbstract() == false);

        ClassInfo ciInterface = new ClassInfo(StringBuffer.class.getName());
        assertTrue(ciInterface.isInterface() == true);
        assertTrue(ciInterface.isAbstract() == false);

        ClassInfo ciAbstractClass = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(ciAbstractClass.isInterface() == false);
        assertTrue(ciAbstractClass.isAbstract() == true);
    }

    @Test
    public void testIsInterface() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertTrue(ci.isInterface() == false);

        ClassInfo ciInterface = new ClassInfo(StringBuffer.class.getName());
        assertTrue(ciInterface.isInterface() == true);
    }

    @Test
    public void testGetClassName() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertEquals(String.class.getName(), ci.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertNull(ci.getClassPackage());

        ClassInfo ciWithPackage = new ClassInfo("com.jstevh.viewer.MyClass");
        assertEquals("com.jstevh.viewer", ciWithPackage.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertNull(ci.getSuperClassName());

        ClassInfo ciWithSuperClass = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", ciWithSuperClass.getSuperClassName());
    }

    @Test
    public void testConstructorWithNoLocationManager() {
        ClassInfo ci = new ClassInfo("java.util.ArrayList");
        String[] methods = ci.printMethods();
        assertArrayEquals(new String[]{"public java.util.ArrayList()"}, methods);
    }

    @Test
    public void testConstructorWithLocationManager() {
        ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager() {});
        String[] methods = ci.printMethods();
        assertArrayEquals(new String[]{"public java.util.ArrayList()"}, methods);
    }

    @Test
    public void testPrintFields() {
        try {
            ClassInfo ci = new ClassInfo("java.util.ArrayList");
            String[] fields = ci.printFields();
            assertNull(fields);
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] fields = ci.printFields();
            assertEquals(13, fields.length);
            assertEquals("time", fields[0]);
            assertEquals("timeZoneOffset", fields[1]);
            assertEquals("timeZoneID", fields[2]);
            assertEquals("year", fields[3]);
            assertEquals("month", fields[4]);
            assertEquals("date", fields[5]);
            assertEquals("hours", fields[6]);
            assertEquals("minutes", fields[7]);
            assertEquals("seconds", fields[8]);
            assertEquals("milliseconds", fields[9]);
            assertEquals("zoneOffset", fields[10]);
            assertEquals("zoneID", fields[11]);
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo ci = new ClassInfo("java.util.ArrayList");
            String[] methods = ci.printMethods();
            assertArrayEquals(new String[]{"public int size()"}, methods);
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] methods = ci.printMethods();
            assertEquals(36, methods.length);
            Arrays.sort(methods);
            assertTrue(methods[0].equals("public long getTime()"));
            assertTrue(methods[methods.length - 1].equals("public void setTime(long)"));
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo ci = new ClassInfo("java.lang.String");
            String[] methods = ci.printMethods("equals");
            assertArrayEquals(new String[]{"public boolean equals(java.lang.Object)"}, methods);
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo ci = new ClassInfo("java.util.ArrayList");
            String[] constructors = ci.printConstructors();
            assertNull(constructors);
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] constructors = ci.printConstructors();
            assertEquals(3, constructors.length);
            assertEquals("public java.util.Date()", constructors[0]);
            assertEquals("public java.util.Date(long)", constructors[1]);
            assertEquals("public java.util.Date(long,int)", constructors[2]);
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo ci = new ClassInfo("java.util.ArrayList");
            String[] interfaces = ci.printInterfaces();
            assertArrayEquals(new String[0], interfaces);
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            ClassInfo ci = new ClassInfo("java.util.Date");
            String[] interfaces = ci.printInterfaces();
            assertEquals(3, interfaces.length);
            assertEquals("java.io.Serializable", interfaces[0]);
            assertEquals("java.util.Cloneable", interfaces[1]);
            assertEquals("java.text.DateFormat", interfaces[2]);
        } catch (ClassNotFoundException e) {
            // expected
        }
    }
}