package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

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
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Point");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNoName() throws ClassNotFoundException {
        new ClassInfo();
    }

    @Test
    public void testConstructorWithName() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("java.lang.String");

        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(41, methods.length);
        assertArrayEquals(new String[] {"toString", "valueOf", "valueOf(char[])", "equals", "hashCode", "length", "charAt", "regionMatches", "regionMatches(boolean, int, String, int)", "startsWith", "endsWith", "substring", "concat", "compareTo", "compareToIgnoreCase", "equalsIgnoreCase", "indexOf", "lastIndexOf", "toLowerCase", "toUpperCase", "trim", "regionMatches(boolean, int, String, int, int)", "matches", "split", "split(String)", "split(String[], int)", "split(String[], int, int)", "split(String, int)", "split(String, int, int, int)", "replace", "replaceAll", "replaceFirst", "replaceAll(String, String)", "replaceAll(String, String, int)", "replaceFirst(String, String)", "replaceFirst(String, String, int)", "replaceAll(String, String, int, int)", "replaceSubsequence", "codePointAt", "codePointBefore", "codePointCount", "getBytes", "getBytes(int, int)", "getBytes(String encoding)", "getChars", "getChars(int, int)", "getChars(Reader reader, int offset, int length)", "getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin, int length)"}, methods);
    }

    @Test
    public void testConstructorWithNameAndDirManager() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList", new DirManager());

        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(47, methods.length);
        assertArrayEquals(new String[] {"add", "add(int, E)", "addAll", "addAll(Collection<? extends E>)", "clear", "contains", "contains(Object)", "equals", "hashCode", "isEmpty", "iterator", "listIterator", "listIterator(int)", "remove", "remove(int)", "remove(Object)", "removeIf", "size", "toArray", "toArray(T[])", "toString", "trimToSize", "subList", "subList(int, int)", "retainAll(Collection<? extends E>)", "removeAll(Collection<? extends E>)", "retainAll(Collection<? extends E>)", "addAll(Collection<? extends E>)", "getClass", "getSuperclass", "getInterfaces", "getMethods", "getConstructors", "getFields", "getDeclaredMethods", "getDeclaredConstructors", "getDeclaredFields", "getAnnotation", "getAnnotations", "getProtectionDomain", "getModifiers", "getName", "getSimpleName", "getEnclosingMethod", "getEnclosingClass", "getDeclaringClass", "getConstructorsCount", "getMethodsCount", "getFieldsCount", "getSuperclassCount", "getInterfacesCount", "toString", "valueOf", "valueOf(int)", "valueOf(String)"}, methods);
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");

        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.Date");

        fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(10, fields.length);
        assertArrayEquals(new String[] {"time", "timeInMilliseconds", "year", "month", "date", "hours", "minutes", "seconds", "milliseconds", "zoneOffset"}, fields);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");

        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(41, methods.length);
        assertArrayEquals(new String[] {"toString", "valueOf", "valueOf(char[])", "equals", "hashCode", "length", "charAt", "regionMatches", "regionMatches(boolean, int, String, int)", "startsWith", "endsWith", "substring", "concat", "compareTo", "compareToIgnoreCase", "equalsIgnoreCase", "indexOf", "lastIndexOf", "toLowerCase", "toUpperCase", "trim", "regionMatches(boolean, int, String, int, int)", "matches", "split", "split(String)", "split(String[], int)", "split(String[], int, int)", "split(String, int)", "split(String, int, int, int)", "replace", "replaceAll", "replaceFirst", "replaceAll(String, String)", "replaceAll(String, String, int)", "replaceFirst(String, String)", "replaceFirst(String, String, int)", "replaceAll(String, String, int, int)", "replaceSubsequence", "codePointAt", "codePointBefore", "codePointCount", "getBytes", "getBytes(int, int)", "getBytes(String encoding)", "getChars", "getChars(int, int)", "getChars(Reader reader, int offset, int length)", "getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin, int length)"}, methods);
    }

    @Test
    public void testPrintMethodsWithParameter() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");

        String[] methods = classInfo.printMethods("equals");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean java.lang.String.equals(java.lang.Object)", methods[0]);

        methods = classInfo.printMethods("compareToIgnoreCase");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public int java.lang.String.compareToIgnoreCase(java.lang.String)", methods[0]);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");

        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(6, constructors.length);
        assertArrayEquals(new String[] {"public java.util.ArrayList()", "public java.util.ArrayList(int)", "public java.util.ArrayList(Collection)", "public java.util.ArrayList(Collection, boolean)", "public java.util.ArrayList(int, Collection)", "public java.util.ArrayList(int, int, Collection)"}, constructors);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");

        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);

        classInfo = new ClassInfo("java.util.Stack");

        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);

        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(2, interfaces.length);
        assertArrayEquals(new String[] {"java.io.Serializable", "java.util.Collection"}, interfaces);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");

        String[] methods = classInfo.srchMethods("substring");
        assertNotNull(methods);
        assertEquals(2, methods.length);
        assertArrayEquals(new String[] {"public java.lang.String java.lang.String.substring(int)", "public java.lang.String java.lang.String.substring(int, int)"}, methods);

        methods = classInfo.srchMethods("equals", new String[] {"java.lang.Object"});
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean java.lang.String.equals(java.lang.Object)", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");

        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("public java.util.ArrayList()", methodData.getMethClass());
        assertEquals("ArrayList", methodData.getMethPackage());
        assertEquals("<init>", methodData.getMethName());
    }

    @Test
    public void testMain() {
        String[] args = new String[] {"new", "java.util.ArrayList"};
        ClassInfo.main(args);
    }
}