package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import org.junit.Test;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract() == false);
 ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(interfaceClassInfo.isAbstract() == true);
 }

 @Test
 public void testIsInterface() {
 ClassInfo runnableClassInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(runnableClassInfo.isInterface() == true);
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertTrue(stringClassInfo.isInterface() == false);
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String className = classInfo.getClassName();
 assertEquals("java.lang.String", className);
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String packageName = classInfo.getClassPackage();
 assertEquals("java.lang", packageName);
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String superClassName = classInfo.getSuperClassName();
 assertEquals("java.util.AbstractList", superClassName);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidConstructor() {
 ClassInfo classInfo = new ClassInfo();
 }

 @Test
 public void testPrintFields() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
 assertNotNull(methods);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = {"public native int codePointAt(java.lang.String,int)","public native int codePointBefore(java.lang.String,int)","public native int codePointCount(java.lang.String,int,int)","public native java.lang.String concat(java.lang.String)"};
 String tempStr = "codePoint";
 String[] foundMethods = classInfo.srchMethods(tempStr);
 assertArrayEquals(methods, foundMethods);
 }

 @Test
 public void testSrchMethodsWithIndex() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String tempStr = "codePointAt";
 String[] index = {"int"};
 String[] foundMethods = classInfo.srchMethods(tempStr, index);
 assertArrayEquals(new String[]{"public native int codePointAt(java.lang.String,int)"}, foundMethods);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = {"public native int codePointAt(java.lang.String,int)","public native int codePointBefore(java.lang.String,int)","public native int codePointCount(java.lang.String,int,int)","public native java.lang.String concat(java.lang.String)"};
 int pos = 0;
 MethodData foundMethod = classInfo.getFoundMethod(pos);
 assertEquals("java.lang.String", foundMethod.getMethPackage());
 assertEquals("java.lang.String", foundMethod.getMethClass());
 assertEquals("codePointAt", foundMethod.getMethName());
 }
}