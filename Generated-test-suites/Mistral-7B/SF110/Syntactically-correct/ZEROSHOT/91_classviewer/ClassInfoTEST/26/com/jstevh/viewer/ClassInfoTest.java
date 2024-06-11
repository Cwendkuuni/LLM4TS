package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import org.junit.Test;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertTrue(classInfo.isAbstract() == false);

 classInfo = new ClassInfo("java.util.AbstractList");
 assertTrue(classInfo.isAbstract() == true);
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(classInfo.isInterface() == true);

 classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface() == true);

 classInfo = new ClassInfo("java.util.Collection");
 assertFalse(classInfo.isInterface() == false);

 classInfo = new ClassInfo("java.util.Map");
 assertFalse(classInfo.isInterface() == false);

 classInfo = new ClassInfo("java.util.HashMap");
 assertFalse(classInfo.isInterface() == false);

 classInfo = new ClassInfo("java.util.HashMap$Entry");
 assertTrue(classInfo.isInterface() == true);
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

 classInfo = new ClassInfo("java.util.ArrayList$InnerClass");
 assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.util.AbstractList");
 assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.util.LinkedList");
 assertEquals("java.util.AbstractList", classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.lang.Object");
 assertEquals(null, classInfo.getSuperClassName());
 }

 @Test
 public void testConstructorWithNoArguments() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNotNull(classInfo.cMethods);
 assertEquals(14, classInfo.cMethods.length);
 }

 @Test
 public void testConstructorWithDirManager() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList", new DirManager());
 assertNotNull(classInfo.cMethods);
 assertEquals(14, classInfo.cMethods.length);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(11, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(1);
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public java.util.ArrayList<E> java.util.ArrayList<E>.<init>()", methods[0]);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(1, constructors.length);
 assertEquals("public java.util.ArrayList<E> java.util.ArrayList.<init>()", constructors[0]);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertNull(interfaces.length);

 classInfo = new ClassInfo("java.util.AbstractList");
 interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(1, interfaces.length);
 assertEquals("java.lang.Iterable", interfaces[0]);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("add");
 assertNotNull(methods);
 assertEquals(3, methods.length);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("add", new String[] { "int", "E" });
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public void java.util.ArrayList.add(int,E) throws IndexOutOfBoundsException", methods[0]);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 MethodData methodData = classInfo.getFoundMethod(0);
 assertNotNull(methodData);
 assertEquals("public java.util.ArrayList<E> java.util.ArrayList<E>.<init>()", methodData.getMethName());
 }

 @Test
 public void testGetFoundMethodWithInvalidIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 MethodData methodData = classInfo.getFoundMethod(100);
 assertNull(methodData);
 }
}