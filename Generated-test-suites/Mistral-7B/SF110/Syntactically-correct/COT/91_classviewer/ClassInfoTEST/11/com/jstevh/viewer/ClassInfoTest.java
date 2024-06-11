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
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());
        
        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());
        
        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNameThrowsClassNotFoundException() {
        new ClassInfo("NonExistentClass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidLocationManagerThrowsIllegalArgumentException() {
        new ClassInfo("java.lang.String", null);
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.util.Date");
        String[] expected = {
            "private static final long serialVersionUID = -2440934546133251113L",
            "private transient long time",
            "private int len",
            "private static final int leapYearCutoff = 1900",
            "private int year",
            "private int month",
            "private int day",
            "private int hour",
            "private int minute",
            "private int second",
            "private int millisecond",
            "private int timezoneOffsetHours",
            "private static final int timezoneOffsetMinutes = 480",
            "private static final boolean useLeapDays = true",
            "private static final boolean HenricsClock = true",
            "private boolean lenient",
            "private static final long GMT_OFFSET = 0L",
            "private static final long DST_OFFSET = 3600000L",
            "private static final long JAN_1_1970 = 0L",
            "private long time",
            "private int zoneOffset",
            "private boolean DSTSavingsApplied",
            "private java.util.Calendar calendar"};
        String[] actual = classInfo.printFields();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.Date");
        String[] expected = {
            "public java.lang.String toString()",
            "public boolean equals(java.lang.Object)",
            "public long getTime()",
            "public int getYear()",
            "public int getMonth()",
            "public int getDate()",
            "public void setTime(long time)",
            "public java.util.Calendar getCalendar()",
            "public boolean after(java.util.Date date)",
            "public boolean before(java.util.Date date)",
            "public boolean equals(java.util.Date date)",
            "public int compareTo(java.util.Date date)",
            "public long getTimeInMillis()",
            "public static java.util.Date valueOf(long millis)"};
        String[] actual = classInfo.printMethods();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.util.Date");
        String[] expected = {
            "public static java.util.Date valueOf(long millis)"};
        String[] actual = classInfo.printMethods("valueOf");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.util.Date");
        String[] expected = {
            "public java.util.Date()",
            "public java.util.Date(long millis)",
            "public java.util.Date(int year, int month, int date)",
            "public java.util.Date(int year, int month, int date, int hours, int minutes, int seconds, int milliseconds)"};
        String[] actual = classInfo.printConstructors();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.List");
        String[] expected = {
            "java.io.Serializable",
            "java.util.Collection",
            "java.util.RandomAccess",
            "java.lang.Cloneable"};
        String[] actual = classInfo.printInterfaces();
        assertArrayEquals(expected, actual);
    }
}