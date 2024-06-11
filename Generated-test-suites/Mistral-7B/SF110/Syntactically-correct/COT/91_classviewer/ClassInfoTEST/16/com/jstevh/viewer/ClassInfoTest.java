package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassInfoTest {

    private static final String PACKAGE_NAME = "com.jstevh.viewer.";
    private static final String CLASS_NAME = "ClassInfo";
    private static final String INTERFACE_NAME = "java.util.List";
    private static final boolean DEBUG = true;

    @Before
    public void setUp() {
        System.setProperty("debug", Boolean.toString(DEBUG));
    }

    // Test for isAbstract() method
    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo(AbstractClass.class.getName());
        assertTrue(classInfo.isAbstract());

        ClassInfo classInfo2 = new ClassInfo(ConcreteClass.class.getName());
        assertFalse(classInfo2.isAbstract());
    }

    // Test for isInterface() method
    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertFalse(classInfo.isInterface());

        ClassInfo classInfo2 = new ClassInfo(List.class.getName());
        assertTrue(classInfo2.isInterface());
    }

    // Test for getClassName() method
    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertEquals("java.lang.String", classInfo.getClassName());

        ClassInfo classInfo2 = new ClassInfo(List.class.getName());
        assertEquals("java.util.List", classInfo2.getClassName());
    }

    // Test for getClassPackage() method
    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.getClassPackage());

        ClassInfo classInfo2 = new ClassInfo(List.class.getName());
        assertEquals("java.util", classInfo2.getClassPackage());
    }

    // Test for getSuperClassName() method
    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.getSuperClassName());

        ClassInfo classInfo2 = new ClassInfo(ArrayList.class.getName());
        assertEquals("java.util.AbstractList", classInfo2.getSuperClassName());
    }

    // Test for constructor with no arguments
    @Test
    public void testClassInfoConstructorNoArgs() throws Exception {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(WindowConstants.class.getName(), classInfo.getClassName());
        assertNull(classInfo.cMethods);
        assertNull(classInfo.fndMethods);
    }

    // Test for constructor with a String argument
    @Test
    public void testClassInfoConstructorString() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        assertEquals(className, classInfo.getClassName());
        assertNotNull(classInfo.cMethods);
        assertNotNull(classInfo.fndMethods);

        // Test for empty string
        ClassInfo classInfoEmpty = new ClassInfo("");
        assertNull(classInfoEmpty.getClassName());
        assertNull(classInfoEmpty.cMethods);
        assertNull(classInfoEmpty.fndMethods);
    }

    // Test for constructor with a String and DirManager argument
    @Test
    public void testClassInfoConstructorStringDirManager() throws Exception {
        String className = List.class.getName();
        DirManager dirManager = new DirManager();
        ClassInfo classInfo = new ClassInfo(className, dirManager);
        assertEquals(className, classInfo.getClassName());
        assertNotNull(classInfo.cMethods);
        assertNotNull(classInfo.fndMethods);
    }

    // Test for printFields() method
    @Test
    public void testPrintFields() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        String[] expectedFields = {"public static final char UNDEFINED=-1;",
                "public final native int hashCode();",
                "public final native String toString();",
                "public abstract int compareTo(String anotherString);",
                "public final boolean equals(Object anObject);",
                "public final int length();",
                "public char charAt(int index);",
                "public final int codePointAt(int index);",
                "public int codePointBefore(int index);",
                "public int codePointCount(int beginIndex, int endIndex);",
                "public int indexOf(int ch, int fromIndex);",
                "public int lastIndexOf(int ch, int fromIndex);",
                "public String substring(int beginIndex, int endIndex);",
                "public String toLowerCase();",
                "public String toUpperCase();",
                "public String trim();"};
        String[] actualFields = classInfo.printFields();
        assertArrayEquals(expectedFields, actualFields);
    }

    // Test for printMethods() method
    @Test
    public void testPrintMethods() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        String[] expectedMethods = {"public static void main(java.lang.String[]);",
                "public native int compareTo(java.lang.String);",
                "public native int compareToIgnoreCase(java.lang.String);",
                "public boolean equals(java.lang.Object);",
                "public static boolean matches(java.lang.String regex);",
                "public static boolean matches(java.lang.String regex, java.lang.char[] text);",
                "public static boolean matches(java.lang.String regex, char[] text, int offset, int length);",
                "public static boolean matches(java.lang.String regex, java.lang.String text);",
                "public static boolean matches(java.lang.String regex, java.lang.String text, int offset, int length);",
                "public int codePointAt(int index);",
                "public int codePointBefore(int index);",
                "public int codePointCount(int beginIndex, int endIndex);",
                "public int compareTo(java.lang.Object);",
                "public static int compareToIgnoreCase(java.lang.String s1, java.lang.String s2);",
                "public static int compareToIgnoreCase(java.lang.String s1, java.lang.String s2, int len1, int len2);",
                "public static boolean equals(java.lang.String s1, java.lang.String s2);",
                "public static boolean matches(java.lang.String regex, java.lang.String text, int offset, int length);",
                "public static boolean regionMatches(java.lang.String buffer, int offset, java.lang.String regex);",
                "public static boolean regionMatches(java.lang.String buffer, int sourceOffset, java.lang.String regex, int offset, int length);",
                "public static boolean regionMatches(java.lang.String buffer, int sourceOffset, java.lang.String regex, int offset, int length, Matcher matcher);",
                "public static boolean regionMatches(java.lang.String buffer, int sourceOffset, java.lang.String regex, int offset, int length, int flags);",
                "public static boolean regionMatches(java.lang.String text, int offset, java.lang.String regex);",
                "public static boolean regionMatches(java.lang.String text, int offset, java.lang.String regex, int length);",
                "public static boolean regionMatches(java.lang.String text, int offset, java.lang.String regex, int length, Matcher matcher);",
                "public static boolean regionMatches(java.lang.String text, int offset, java.lang.String regex, int length, int flags);",
                "public int indexOf(int ch);",
                "public int lastIndexOf(int ch);",
                "public java.lang.String substring(int beginIndex);",
                "public java.lang.String substring(int beginIndex, int endIndex);",
                "public java.lang.String toLowerCase();",
                "public java.lang.String toUpperCase();",
                "public java.lang.String trim();"};
        String[] actualMethods = classInfo.printMethods();
        assertArrayEquals(expectedMethods, actualMethods);
    }

    // Test for printMethods(int param) method
    @Test
    public void testPrintMethodsWithParam() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        String[] expectedMethods = {"public static void main(java.lang.String[]);",
                "public native int compareTo(java.lang.String);",
                "public native java.lang.String concat(java.lang.String str);"};
        String[] actualMethods = classInfo.printMethods(1);
        assertArrayEquals(expectedMethods, actualMethods);
    }

    // Test for printInterfaces() method
    @Test
    public void testPrintInterfaces() throws Exception {
        String className = AbstractList.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        String[] expectedInterfaces = {"java.util.List", "java.util.RandomAccess", "java.io.Serializable"};
        String[] actualInterfaces = classInfo.printInterfaces();
        assertArrayEquals(expectedInterfaces, actualInterfaces);

        String className2 = Map.class.getName();
        ClassInfo classInfo2 = new ClassInfo(className2);
        String[] expectedInterfaces2 = {"java.util.Map", "java.io.Serializable"};
        String[] actualInterfaces2 = classInfo2.printInterfaces();
        assertArrayEquals(expectedInterfaces2, actualInterfaces2);
    }

    // Test getData(Member[]) method
    @Test
    public void testGetDataMemberArray() throws Exception {
        Class<String> stringClass = String.class;
        Method[] methods = stringClass.getMethods();
        String[] expectedMethods = getData(methods);
        ClassInfo classInfo = new ClassInfo();
        String[] actualMethods = classInfo.getData(methods);
        assertArrayEquals(expectedMethods, actualMethods);
    }

    // Test getData(Class[]) method
    @Test
    public void testGetDataClassArray() throws Exception {
        Class<String> stringClass = String.class;
        Class[] classes = new Class[]{stringClass, Integer.class};
        String[] expectedClasses = getData(classes);
        ClassInfo classInfo = new ClassInfo();
        String[] actualClasses = classInfo.getData(classes);
        assertArrayEquals(expectedClasses, actualClasses);
    }

    // Test srchMethods() method with null input
    @Test(expected = IllegalArgumentException.class)
    public void testSrchMethodsNullInput() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        classInfo.srchMethods((String) null);
    }

    // Test srchMethods() method with valid input
    @Test
    public void testSrchMethodsValidInput() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        String[] expectedMethods = {"public native int codePointAt(int);",
                "public native int codePointBefore(int);",
                "public native int codePointCount(int, int);",
                "public int compareTo(java.lang.Object);",
                "public static boolean equals(java.lang.String, java.lang.String);",
                "public int indexOf(int);",
                "public int lastIndexOf(int);",
                "public java.lang.String substring(int);",
                "public java.lang.String substring(int, int);",
                "public java.lang.String toLowerCase();",
                "public java.lang.String toUpperCase();",
                "public java.lang.String trim();"};
        String[] input = {"codePointAt", "codePointBefore", "codePointCount"};
        MethodData[] actualMethods = classInfo.getFoundMethods(0);
        String[] actualMethodNames = new String[actualMethods.length];
        for (int i = 0; i < actualMethods.length; i++) {
            actualMethodNames[i] = actualMethods[i].getMethName();
        }
        assertArrayEquals(expectedMethods, actualMethodNames);
    }

    // Test srchMethods() method with String[] input
    @Test
    public void testSrchMethodsStringArrayInput() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        String[] expectedMethods = {"public native int codePointAt(int);",
                "public native int codePointBefore(int);",
                "public native int codePointCount(int, int);",
                "public int compareTo(java.lang.Object);",
                "public static boolean equals(java.lang.String, java.lang.String);",
                "public int indexOf(int);",
                "public int lastIndexOf(int);",
                "public java.lang.String substring(int);",
                "public java.lang.String substring(int, int);",
                "public java.lang.String toLowerCase();",
                "public java.lang.String toUpperCase();",
                "public java.lang.String trim();"};
        String[] input = {"codePointAt", "codePointBefore", "codePointCount"};
        MethodData[] actualMethods = classInfo.getFoundMethods(input, new int[input.length]);
        String[] actualMethodNames = new String[actualMethods.length];
        for (int i = 0; i < actualMethods.length; i++) {
            actualMethodNames[i] = actualMethods[i].getMethName();
        }
        assertArrayEquals(expectedMethods, actualMethodNames);
    }

    // Test getting a method with invalid index
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodInvalidIndex() throws Exception {
        String className = String.class.getName();
        ClassInfo classInfo = new ClassInfo(className);
        MethodData methodData = classInfo.getFoundMethod(-1);
    }

    private static String[] getData(Member[] members) {
        ClassInfo classInfo = new ClassInfo();
        return classInfo.getData(members);
    }

    private static String[] getData(Class[] classes) {
        ClassInfo classInfo = new ClassInfo();
        return classInfo.getData(classes);
    }
}

class MethodData {
    String tempStr;
    int begin;
    int end;

    public String getMethPackage() {
        return "";
    }

    public String getMethClass() {
        return "";
    }

    public String getMethName() {
        return "";
    }
}

class AbstractClass {}
class ConcreteClass {}