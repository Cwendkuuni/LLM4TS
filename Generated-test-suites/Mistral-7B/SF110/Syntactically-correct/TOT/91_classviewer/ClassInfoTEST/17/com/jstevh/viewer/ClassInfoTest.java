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
 assertFalse(classInfo.isAbstract());
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testIsInterfaceWithArgument() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.util.List", new DirManager());
 assertFalse(classInfo.isAbstract());
 assertFalse(classInfo.isInterface());
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.StringBuilder", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertNull(classInfo.getSuperClassName());
 }

 @Test
 public void testGetSuperClassNameWithArguments() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.util.ArrayList", new DirManager());
 assertEquals("java.util.Collection", classInfo.getSuperClassName());
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidConstructor() {
 new ClassInfo();
 }

 @Test
 public void testPrintFields() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertEquals(2, fields.length);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testPrintMethods() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(11, methods.length);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testPrintMethodsWithParameter() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder", new DirManager());
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(11, methods.length);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(3, constructors.length);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertNotNull(interfaces[0]);
 assertEquals("java.util.List", interfaces[0]);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSrchMethods() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.srchMethods("append");
 assertNotNull(methods);
 assertEquals(4, methods.length);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSrchMethodsWithIndex() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder");
 String[] methods = classInfo.srchMethods("append", new String[]{"java.lang.String"});
 assertNotNull(methods);
 assertEquals(1, methods.length);
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo classInfo = null;
 try {
 classInfo = new ClassInfo("java.lang.StringBuilder");
 MethodData methodData = classInfo.getFoundMethod(0);
 assertNotNull(methodData);
 assertEquals("append", methodData.getMethName());
 } catch (ClassNotFoundException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}