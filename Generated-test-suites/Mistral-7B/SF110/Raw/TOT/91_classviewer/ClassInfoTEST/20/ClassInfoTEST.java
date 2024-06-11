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

 ClassInfo listInterface = new ClassInfo("java.util.List");
 listInterface.knownPackages = new String[]{"java.util"};
 assertTrue(listInterface.isInterface());
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

 ClassInfo classInfo2 = new ClassInfo("com.jstevh.viewer.ClassInfo");
 assertNull(classInfo2.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNull(classInfo.getSuperClassName());

 ClassInfo stringClassInfo = new ClassInfo("java.lang.Object");
 assertEquals("java.lang.Object", stringClassInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.jstevh.viewer.NonExistentClass");
 }

 @Test
 public void testPrintFields() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertEquals(0, fields.length);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(1);
 assertNotNull(methods);
 assertTrue(methods.length > 0);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
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
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString");
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testSrchMethodsWithIndex() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods(new String[]{"equals"}, "toString");
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 MethodData methodData = classInfo.getFoundMethod(0);
 assertNotNull(methodData);
 assertEquals("toString", methodData.getMethName());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetFoundMethodOutOfBounds() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 MethodData methodData = classInfo.getFoundMethod(100);
 }
}