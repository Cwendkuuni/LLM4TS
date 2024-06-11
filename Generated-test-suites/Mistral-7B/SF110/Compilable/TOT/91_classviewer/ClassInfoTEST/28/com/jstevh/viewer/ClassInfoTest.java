package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertTrue(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.ArrayList", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.Collection", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testInvalidClassName() throws ClassNotFoundException {
 new ClassInfo("com.jstevh.viewer.NonExistentClass");
 }

 @Test
 public void testPrintMethods() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 }

 @Test
 public void testPrintConstructors() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 }

 @Test
 public void testPrintInterfaces() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNull(interfaces);
 }

 @Test
 public void testSrchMethods() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = {"length()", "toUpperCase()"};
 String[] foundMethods = classInfo.srchMethods("toUpper", methods);
 assertNotNull(foundMethods);
 assertEquals(1, foundMethods.length);
 assertEquals("toUpperCase()", foundMethods[0]);
 }

 @Test
 public void testGetFoundMethod() throws ClassNotFoundException {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 MethodData methodData = classInfo.getFoundMethod(0);
 assertEquals("java.lang.String", methodData.getMethClass());
 assertEquals("length()", methodData.getMethName());
 }
}