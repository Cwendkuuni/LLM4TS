package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import org.junit.Test;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 assertFalse(instance.isAbstract());

 instance = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(instance.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo instance = new ClassInfo("java.util.List");
 assertFalse(instance.isInterface());

 instance = new ClassInfo("java.util.ArrayList");
 assertFalse(instance.isInterface());

 instance = new ClassInfo("java.util.Map");
 assertFalse(instance.isInterface());

 instance = new ClassInfo("java.util.HashMap");
 assertFalse(instance.isInterface());

 instance = new ClassInfo("java.util.Collection");
 assertFalse(instance.isInterface());

 instance = new ClassInfo("java.util.ArrayList");
 assertFalse(instance.isInterface());

 instance = new ClassInfo("java.util.Map.Entry");
 assertTrue(instance.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", instance.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 assertNull(instance.getClassPackage());

 instance = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", instance.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 assertNull(instance.getSuperClassName());

 instance = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", instance.getSuperClassName());
 }

 @Test
 public void testConstructorWithNoArgs() throws Exception {
 ClassInfo instance = new ClassInfo();
 assertNotNull(instance.c);
 assertEquals(WindowConstants.class, instance.c);
 }

 @Test
 public void testConstructorWithName() throws Exception {
 ClassInfo instance = new ClassInfo("java.lang.String");
 assertNotNull(instance.c);
 assertEquals("java.lang.String", instance.c.getName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorWithInvalidName() throws Exception {
 new ClassInfo("java.nonExistingClass");
 }

 @Test
 public void testConstructorWithNameAndDirManager() throws Exception {
 String testPath = System.getProperty("user.dir") + "/src/test/java/com/jstevh/viewer";
 DirManager dirManager = new DirManager(testPath);
 ClassInfo instance = new ClassInfo("MyTestClass", dirManager);
 assertNotNull(instance.c);
 }

 @Test
 public void testPrintFields() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 String[] fields = instance.printFields();
 assertNotNull(fields);
 assertEquals(0, fields.length);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 String[] methods = instance.printMethods();
 assertEquals(0, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 String[] methods = instance.printMethods(ClassInfo.NO_OBJECT_METHODS);
 assertEquals(0, methods.length);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo instance = new ClassInfo("java.util.ArrayList");
 String[] constructors = instance.printConstructors();
 assertEquals(1, constructors.length);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo instance = new ClassInfo("java.util.ArrayList");
 String[] interfaces = instance.printInterfaces();
 assertEquals(0, interfaces.length);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 String[] methods = instance.srchMethods("toString");
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public synchronized java.lang.String toString()", methods[0]);
 }

 @Test
 public void testSrchMethodsWithIndex() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 String[] methods = instance.srchMethods("toString", new String[]{"java.lang.Object"});
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public synchronized java.lang.String toString()", methods[0]);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo instance = new ClassInfo("java.lang.String");
 MethodData methodData = instance.getFoundMethod(0);
 assertNotNull(methodData);
 assertNotNull(methodData.getMethPackage());
 assertNotNull(methodData.getMethClass());
 assertNotNull(methodData.getMethName());
 }
}