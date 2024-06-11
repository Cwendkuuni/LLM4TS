package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.HashMap");
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertNull(classInfo.getSuperClassName());
 }

 @Test
 public void testGetSuperClassNameForClassWithSuperclass() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.Collection", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() throws Exception {
 new ClassInfo("com.jstevh.viewer.NonExistentClass");
 }

 @Test
 public void testPrintFields() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] fields = classInfo.printFields();
 assertArrayEquals(new String[]{"this.value=<value>", "this.length=0", "this.offset=0", "this.value=<value>", "this.hash=0"}, fields);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertArrayEquals(new String[]{"public native int hashCode()", "public native java.lang.String toString()", "public static java.lang.String valueOf(java.lang.String)", "public static java.lang.String valueOf(byte[])", "public native byte[] getBytes()", "public native int length()", "public boolean equals(java.lang.Object)", "public native char charAt(int)", "public native int codePointAt(int)", "public native int codePointBefore(int)", "public native int codePointCount(int, int)", "public native int indexOf(int)", "public native int lastIndexOf(int)", "public native int length()", "public native int offsetByCodePoints(int, int)", "public native int regionMatches(boolean, int, java.lang.String, int, int)", "public native int regionMatches(boolean, int, java.lang.String, int, int, int)", "public native int regionMatches(boolean, int, java.lang.String, int, int, int, int)", "public native int compareTo(java.lang.String)", "public native int compareToIgnoreCase(java.lang.String)", "public native int indexOf(java.lang.String)", "public native int lastIndexOf(java.lang.String)", "public native int lastIndexOf(java.lang.String, int)", "public native int lastIndexOf(java.lang.String, int, int)", "public native int matches(java.lang.String)", "public native java.lang.String replace(int, int, java.lang.String)", "public native java.lang.String replaceAll(java.lang.regex.Pattern)", "public native java.lang.String replaceAll(java.lang.String)", "public native java.lang.String replaceFirst(java.lang.String)", "public native java.lang.String toLowerCase()", "public native java.lang.String toUpperCase()", "public native java.lang.String trim()", "public native java.lang.String substring(int)", "public native java.lang.String substring(int, int)", "public native java.lang.String concat(java.lang.String)", "public native java.lang.String intern()"}, methods);
 }

 @Test
 public void testPrintMethodsWithPattern() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods("toString");
 assertEquals(1, methods.length);
 assertEquals("public native java.lang.String toString()", methods[0]);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] constructors = classInfo.printConstructors();
 assertArrayEquals(new String[]{"public java.lang.String()"}, constructors);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertArrayEquals(new String[]{"java.util.List", "java.util.RandomAccess", "java.io.Serializable"}, interfaces);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString");
 assertArrayEquals(new String[]{"public native java.lang.String toString()"}, methods);
 }

 @Test
 public void testSrchMethodsWithPattern() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString", new String[]{"public"});
 assertArrayEquals(new String[]{"public native java.lang.String toString()"}, methods);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetFoundMethodOutOfBounds() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 MethodData methodData = classInfo.getFoundMethod(-1);
 assertNull(methodData);
 }
}