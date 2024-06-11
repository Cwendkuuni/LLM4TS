package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
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
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() throws Exception {
 new ClassInfo("java.nonExistentClass");
 }

 @Test
 public void testPrintFields() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertEquals(10, fields.length);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(34, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(1);
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public int size()", methods[0]);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(1, constructors.length);
 assertEquals("public java.util.ArrayList()", constructors[0]);
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
 String[] methods = classInfo.srchMethods("add");
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public void add(int index, E element)", methods[0]);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("remove", new String[]{"int"});
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public boolean remove(int index)", methods[0]);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 Method foundMethod = classInfo.getFoundMethod(0);
 assertEquals("java.util.Collection", foundMethod.getMethPackage());
 assertEquals("public boolean add(E e)", foundMethod.getMethClass());
 assertEquals("add", foundMethod.getMethName());
 }

 @Test
 public void testGetFoundMethodWithWrongIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 Method foundMethod = classInfo.getFoundMethod(10);
 assertNull(foundMethod);
 }

 @Test
 public void testConstructorWithDirManager() throws Exception {
 String className = "com.jstevh.MyClass";
 File dir = new File(".");
 DirManager dirManager = new DirManager(dir);
 ClassInfo classInfo = new ClassInfo(className, dirManager);
 assertEquals(className, classInfo.getClassName());
 }
}