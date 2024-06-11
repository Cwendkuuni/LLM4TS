package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 boolean isAbstract = classInfo.isAbstract();
 assertFalse(isAbstract);

 classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 isAbstract = classInfo.isAbstract();
 assertTrue(isAbstract);
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertFalse(classInfo.isInterface());

 classInfo = new ClassInfo("java.util.List");
 assertTrue(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
 String className = classInfo.getClassName();
 assertEquals("com.jstevh.viewer.ClassInfo", className);
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
 String classPackage = classInfo.getClassPackage();
 assertNotNull(classPackage);
 assertEquals("com.jstevh.viewer", classPackage);
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.lang.String");
 String superClassName = classInfo.getSuperClassName();
 assertEquals("java.lang.Object", superClassName);
 }

 @Test
 public void testConstructor() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
 assertNotNull(classInfo);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorNotFound() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.NonExistentClass");
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertTrue(methods.length > 0);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
 assertNotNull(methods);
 assertTrue(methods.length > 0);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertTrue(constructors.length > 0);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertTrue(interfaces.length > 0);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String testString = "contains";
 String[] methods = classInfo.srchMethods(testString);
 assertNotNull(methods);
 assertTrue(methods.length > 0);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String testString = "contains";
 String[] testIndex = {"String"};
 String[] methods = classInfo.srchMethods(testString, testIndex);
 assertNotNull(methods);
 assertTrue(methods.length > 0);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 int index = 0;
 MethodData foundMethod = classInfo.getFoundMethod(index);
 assertNotNull(foundMethod);
 }
}