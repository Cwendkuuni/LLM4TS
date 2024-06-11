package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Before
 public void setUp() {
 try {
 classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 fail("Failed to instantiate ClassInfo class");
 }
 }

 @Test
 public void testIsAbstract() {
 assertTrue(classInfo.isAbstract() == false);
 }

 @Test
 public void testIsInterface() {
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 assertEquals("ClassInfo", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 assertEquals(null, classInfo.getSuperClassName());
 }

 @Test
 public void testPrintFields() {
 assertNotNull(classInfo.printFields());
 }

 @Test
 public void testPrintMethods() {
 assertNotNull(classInfo.printMethods());
 }

 @Test
 public void testPrintMethodsWithParam() {
 assertNotNull(classInfo.printMethods(1));
 }

 @Test
 public void testPrintConstructors() {
 assertNotNull(classInfo.printConstructors());
 }

 @Test
 public void testPrintInterfaces() {
 assertNotNull(classInfo.printInterfaces());
 }

 @Test
 public void testSrchMethods() {
 String[] methods = classInfo.srchMethods("printFields");
 assertEquals(1, methods.length);
 assertEquals("void printFields()", methods[0]);
 }

 @Test
 public void testSrchMethodsWithIndex() {
 String[] methods = classInfo.srchMethods(null, new String[]{"void", "print"});
 assertEquals(1, methods.length);
 assertEquals("void printFields()", methods[0]);
 }

 @Test
 public void testGetFoundMethod() {
 Method foundMethod = classInfo.getFoundMethod(0);
 assertEquals("void printFields()", foundMethod.getMethName());
 assertEquals("com.jstevh.viewer.ClassInfo", foundMethod.getMethClass().getName());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetFoundMethodWithInvalidIndex() {
 classInfo.getFoundMethod(10);
 }

 @Test
 public void testTryClass() {
 Class tempClass = ClassInfo.tryClass("java.lang.String");
 assertNotNull(tempClass);
 assertEquals("java.lang.String", tempClass.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testTryClassWithNullName() {
 Class tempClass = ClassInfo.tryClass(null);
 fail("Expected NullPointerException but received null instead");
 }
}