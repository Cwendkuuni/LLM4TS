package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract());

 ClassInfo classInfo2 = new ClassInfo("java.lang.StringBuffer");
 assertFalse(classInfo2.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertTrue(classInfo.isInterface());

 ClassInfo classInfo2 = new ClassInfo("java.lang.String");
 assertFalse(classInfo2.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNotNull(classInfo.getClassPackage());
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", classInfo.getSuperClassName());

 ClassInfo classInfo2 = new ClassInfo(String.class.getName());
 assertNull(classInfo2.getSuperClassName());
 }

 @Test
 public void testConstructorWithName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 Class<?> clazz = classInfo.c;
 assertEquals(ArrayList.class, clazz);
 }

 @Test
 public void testConstructorWithNameAndDirManager() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo", new DirManager());
 Class<?> clazz = classInfo.c;
 assertEquals(ClassInfo.class, clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testInvalidClassName() throws Exception {
 new ClassInfo("InvalidClassName");
 fail("Expected ClassNotFoundException not thrown.");
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(Arrays.asList(methods).isEmpty());
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
 assertNotNull(methods);
 assertFalse(Arrays.asList(methods).isEmpty());
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertFalse(Arrays.asList(constructors).isEmpty());
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(Arrays.asList(interfaces).isEmpty());
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("toString");
 assertNotNull(methods);
 assertFalse(Arrays.asList(methods).isEmpty());
 assertEquals(1, methods.length);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("equals", new String[]{"Object"});
 assertNotNull(methods);
 assertFalse(Arrays.asList(methods).isEmpty());
 assertEquals(1, methods.length);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 Method method = classInfo.getFoundMethod(Arrays.asList(methods).indexOf("add")).getMethod();
 assertEquals("add", method.getName());
 }

 @Test
 public void testGetPackageList() throws Exception {
 File dir = new File(".");
 DirManager dirManager = new DirManager(dir);
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList", dirManager);
 String[] packages = classInfo.knownPackages;
 assertNotNull(packages);
 assertFalse(Arrays.asList(packages).isEmpty());
 assertTrue(Collections.containsAll(Arrays.asList(packages), new String[]{"."}));
 }
}