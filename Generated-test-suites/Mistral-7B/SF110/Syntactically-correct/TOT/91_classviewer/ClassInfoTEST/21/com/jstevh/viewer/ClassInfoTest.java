package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass");
 assertTrue(classInfo.isAbstract());

 ClassInfo classInfo2 = new ClassInfo("java.lang.String");
 assertFalse(classInfo2.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface());

 ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList");
 assertFalse(classInfo2.isInterface());

 ClassInfo classInfo3 = new ClassInfo("java.util.Iterator");
 assertTrue(classInfo3.isInterface());
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

 ClassInfo classInfo2 = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", classInfo2.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNull(classInfo.getSuperClassName());

 ClassInfo classInfo2 = new ClassInfo("java.lang.Object");
 assertNull(classInfo2.getSuperClassName());

 ClassInfo classInfo3 = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.Collection", classInfo3.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.jstevh.NonexistentClass");
 }

 @Test
 public void testPrintFields() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
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
 public void testPrintMethodsParam() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(1);
 assertNull(methods);
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
 assertNull(interfaces);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = {"toString", "clone", "equals"};
 String[] foundMethods = classInfo.srchMethods("toString", methods);
 assertNotNull(foundMethods);
 assertEquals(3, foundMethods.length);
 }

 @Test
 public void testSrchMethodsInvalidIndex() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = {"toString", "clone", "equals"};
 String[] foundMethods = classInfo.srchMethods("toString", null);
 assertNull(foundMethods);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.cMethods;
 MethodData foundMethod = classInfo.getFoundMethod(0);
 assertEquals("toString", foundMethod.getMethName());
 assertEquals("java.lang.Object", foundMethod.getMethClass());
 assertEquals("java.lang.String", foundMethod.getMethPackage());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFoundMethodInvalidIndex() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.cMethods;
 MethodData foundMethod = classInfo.getFoundMethod(methods.length);
 }
}