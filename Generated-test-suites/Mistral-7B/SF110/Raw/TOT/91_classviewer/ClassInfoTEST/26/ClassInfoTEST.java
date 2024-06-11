package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface());
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
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNull(classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.jstevh.nonExistentClass");
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertFalse(constructors.length == 0);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(interfaces.length == 0);
 }

 @Test
 public void testSearchMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString");
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.cMethods;
 MethodData foundMethod = classInfo.getFoundMethod(ClassInfo.indexOfMethod(methods, "toString"));
 assertEquals("toString", foundMethod.getMethName());
 }
}