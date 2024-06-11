package com.jstevh.viewer;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 Assert.assertTrue(classInfo.isAbstract());

 classInfo = new ClassInfo("java.lang.String");
 Assert.assertFalse(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 classInfo = new ClassInfo("java.util.List");
 Assert.assertFalse(classInfo.isInterface());

 classInfo = new ClassInfo("java.util.ArrayList");
 Assert.assertFalse(classInfo.isInterface());

 classInfo = new ClassInfo("java.util.Collections");
 Assert.assertTrue(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 classInfo = new ClassInfo("java.lang.String");
 Assert.assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 classInfo = new ClassInfo("java.lang.String");
 Assert.assertNull(classInfo.getClassPackage());

 classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
 Assert.assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 classInfo = new ClassInfo("java.lang.String");
 Assert.assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.lang.Object");
 Assert.assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.lang.Thread");
 Assert.assertEquals("java.lang.Object", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 classInfo = new ClassInfo("java.lang.Xyz");
 }

 @Test
 public void testPrintMethods() {
 classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 Assert.assertNotNull(methods);
 }

 @Test
 public void testPrintMethodsWithParam() {
 classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(1);
 Assert.assertNull(methods);
 }

 @Test
 public void testPrintInterfaces() {
 classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 Assert.assertNull(interfaces);

 classInfo = new ClassInfo("java.util.List");
 interfaces = classInfo.printInterfaces();
 Assert.assertNotNull(interfaces);
 }

 @Test
 public void testSrchMethods() {
 classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString");
 Assert.assertNotNull(methods);
 Assert.assertEquals(1, methods.length);

 methods = classInfo.srchMethods("equals", new String[]{"Object"});
 Assert.assertNotNull(methods);
 Assert.assertEquals(1, methods.length);
 }

 @Test
 public void testGetFoundMethod() {
 classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 MethodData methodData = classInfo.getFoundMethod(0);

 Assert.assertNotNull(methodData);
 Assert.assertEquals("java.lang.String", methodData.getMethPackage());
 Assert.assertEquals("java.lang.String", methodData.getMethClass());
 Assert.assertEquals("toString", methodData.getMethName());
 }
}