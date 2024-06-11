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
 listInterface.setDebug(true);
 listInterface.printClassInfo();

 ClassInfo hashSetInterface = new ClassInfo("java.util.HashSet");
 hashSetInterface.setDebug(true);
 hashSetInterface.printClassInfo();

 ClassInfo mapInterface = new ClassInfo("java.util.Map");
 mapInterface.setDebug(true);
 mapInterface.printClassInfo();

 assertFalse(listInterface.isInterface());
 assertFalse(hashSetInterface.isInterface());
 assertTrue(mapInterface.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.ArrayList", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNotNull(classInfo.getClassPackage());
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNull(classInfo.getSuperClassName());

 ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
 assertNotNull(stringBufferClassInfo.getSuperClassName());
 assertEquals("java.lang.Object", stringBufferClassInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.jstevh.NonExistentClass");
 }

 @Test
 public void testPrintFields() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 classInfo.setDebug(true);
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertFalse(fields.length == 0);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 classInfo.setDebug(true);
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(1);
 assertNotNull(methods);
 assertEquals("add(E e)", methods[0]);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 classInfo.setDebug(true);
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertFalse(constructors.length == 0);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 classInfo.setDebug(true);
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(interfaces.length == 0);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("toString");
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 assertEquals("public synchronized string toString()", methods[0]);
 }

 @Test
 public void testSrchMethodsWithParam() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("add", new String[]{"java.lang.Object"} );
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 assertEquals("public boolean add(E e)", methods[0]);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 MethodData methodData = classInfo.getFoundMethod(0);
 assertNotNull(methodData);
 assertEquals("public boolean add(E e)", methodData.getMethName());
 }
}