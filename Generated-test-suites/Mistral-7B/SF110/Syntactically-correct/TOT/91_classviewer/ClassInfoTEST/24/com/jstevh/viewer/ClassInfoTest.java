package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import org.junit.Test;

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

 ClassInfo listInterface = new ClassInfo("java.util.List");
 listInterface.isInterface = true; // simulate a mock interface
 assertTrue(listInterface.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.StringBuilder", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertNotNull(classInfo.getClassPackage());
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.Object", classInfo.getSuperClassName());

 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertEquals(null, stringClassInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.non.existent.package.NonExistentClass");
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintFields() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertFalse(fields.length == 0);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(interfaces.length == 0);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.srchMethods("append");
 assertNotNull(methods);
 assertFalse(methods.length == 0);

 assertEquals(2, methods.length); // test for multiple occurrences
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.printMethods();
 MethodData methodData = classInfo.getFoundMethod(0);

 assertEquals("append", methodData.getMethName());
 assertEquals("java.lang.StringBuilder", methodData.getMethClass());
 assertNotNull(methodData.getMethPackage());
 }
}