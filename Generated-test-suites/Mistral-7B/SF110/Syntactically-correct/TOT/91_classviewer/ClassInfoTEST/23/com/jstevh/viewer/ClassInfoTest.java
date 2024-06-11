package com.jstevh.viewer;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*; // for assert* methods

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface());

 ClassInfo listInterface = new ClassInfo("java.util.List");
 listInterface.setDebug(true);
 listInterface.printClassInfo();
 classInfo = new ClassInfo("java.util.List<String>");
 assertTrue(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.ArrayList", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNull(classInfo.getClassPackage());

 ClassInfo stringArrayList = new ClassInfo("java.util.ArrayList<String>");
 assertEquals("java.util", stringArrayList.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNull(classInfo.getSuperClassName());

 ClassInfo stringArrayList = new ClassInfo("java.util.ArrayList<String>");
 assertEquals("java.util.Collection", stringArrayList.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFound() throws Exception {
 new ClassInfo("java.abcd.NonExistentClass");
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(15, methods.length);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(1, interfaces.length);
 assertEquals("java.util.Collection", interfaces[0]);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = {"add(E)", "remove(int)"};
 String[] foundMethods = classInfo.srchMethods(methods);
 assertNotNull(foundMethods);
 assertEquals(2, foundMethods.length);
 assertEquals("add(E)", foundMethods[0]);
 assertEquals("remove(int)", foundMethods[1]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = {"add(int)"};
 String[] foundMethods = classInfo.srchMethods(methods, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = null;
 String[] foundMethods = classInfo.srchMethods(methods);
 }
}