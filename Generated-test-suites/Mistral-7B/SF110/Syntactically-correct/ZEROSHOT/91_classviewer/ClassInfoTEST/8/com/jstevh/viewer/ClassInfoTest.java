package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract() == false);

 classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract() == true);
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertFalse(classInfo.isInterface() == true);

 classInfo = new ClassInfo("java.util.List");
 assertTrue(classInfo.isInterface() == false);

 classInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(classInfo.isInterface() == false);

 classInfo = new ClassInfo("java.util.Collection");
 assertTrue(classInfo.isInterface() == true);
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
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

 classInfo = new ClassInfo("java.lang.Object");
 assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.lang.Exception");
 assertEquals("java.lang.Throwable", classInfo.getSuperClassName());
 }

 @Test
 public void testConstructorWithNoArgs() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 assertNotNull(classInfo.c);

 classInfo = new ClassInfo("java.lang.String");
 assertNotNull(classInfo.c);
 }

 @Test
 public void testConstructorWithArgs() throws Exception {
 ClassInfo dirManager = new DirManager();
 ClassInfo classInfo = new ClassInfo("java.lang.String", dirManager);
 assertNotNull(classInfo.c);

 classInfo = new ClassInfo("java.io.File", dirManager);
 assertNotNull(classInfo.c);
 }

 @Test
 public void testPrintFields() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] fields = classInfo.printFields();
 assertEquals(0, fields.length);

 classInfo = new ClassInfo("java.lang.StringBuilder");
 fields = classInfo.printFields();
 assertNotNull(fields);
 assertNotSame(0, fields.length);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertEquals(0, methods.length);

 classInfo = new ClassInfo("java.lang.StringBuilder");
 methods = classInfo.printMethods();
 assertNotNull(methods);
 assertNotSame(0, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
 assertEquals(0, methods.length);

 classInfo = new ClassInfo("java.lang.StringBuilder");
 methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
 assertNotNull(methods);
 assertNotSame(0, methods.length);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] constructors = classInfo.printConstructors();
 assertEquals(0, constructors.length);

 classInfo = new ClassInfo("java.lang.StringBuilder");
 constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertNotSame(0, constructors.length);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] interfaces = classInfo.printInterfaces();
 assertNull(interfaces);

 classInfo = new ClassInfo("java.lang.RuntimeException");
 interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertNotSame(0, interfaces.length);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = {"public java.lang.String valueOf(int)", "public java.lang.String substring(int, int)", "public java.lang.String concat(java.lang.String)"};
 String[] foundMethods = classInfo.srchMethods(null, methods);
 assertNull(foundMethods);

 classInfo = new ClassInfo("java.lang.StringBuilder");
 foundMethods = classInfo.srchMethods(null, methods);
 assertNotNull(foundMethods);
 assertNotSame(0, foundMethods.length);

 foundMethods = classInfo.srchMethods("concat", new String[]{"java.lang.StringBuilder", "java.lang.StringBuilder", "java.lang.StringBuilder"});
 assertNotNull(foundMethods);
 assertNotSame(0, foundMethods.length);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
 MethodData methodData = classInfo.getFoundMethod(0);
 assertEquals("public java.lang.StringBuilder append(char)", methodData.getMethName());
 }
}