package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract() == false);

 classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract() == true);
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertFalse(classInfo.isInterface() == true);

 classInfo = new ClassInfo("java.util.List");
 assertTrue(classInfo.isInterface() == false);
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorWithNameOnlyThrowsException() {
 new ClassInfo("java.NonExistentClass");
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testConstructorWithNameAndDirManager() {
 ClassInfo classInfo = new ClassInfo("java.lang.String", new DirManager());
 assertNotNull(classInfo);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNotNull(classInfo.printMethods());
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNotNull(classInfo.printConstructors());
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNotNull(classInfo.printInterfaces());
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("equals");
 assertEquals(4, methods.length);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 Method method = classInfo.getFoundMethod(0);
 assertEquals("java.lang.Object equals(java.lang.Object)", method.getMethName());
 }

 @Test
 public void testPrintArray() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 }

 @Test
 public void testGetClass() {
 ClassInfo classInfo = new ClassInfo();
 Class clazz = classInfo.getClass("java.lang.String");
 assertEquals("java.lang.String", clazz.getName());
 }

 @Test
 public void testTryClass() {
 ClassInfo classInfo = new ClassInfo();
 Class clazz = classInfo.tryClass("java.lang.String");
 assertEquals("java.lang.String", clazz.getName());
 }

 @Test
 public void testGetData() {
 Class[] constructors = ClassInfo.class.getConstructors();
 String[] data = ClassInfo.getData(constructors);
 assertEquals(0, data.length);

 Method[] methods = ClassInfo.class.getMethods();
 data = ClassInfo.getData(methods);
 assertEquals(29, data.length);
 }

}