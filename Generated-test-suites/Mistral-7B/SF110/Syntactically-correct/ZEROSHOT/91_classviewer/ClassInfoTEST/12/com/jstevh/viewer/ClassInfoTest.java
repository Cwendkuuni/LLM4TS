package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract() == false);

 classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract() == true);
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertFalse(classInfo.isInterface() == true);

 classInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(classInfo.isInterface() == true);
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertEquals(null, classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.Object", classInfo.getSuperClassName());
 }

 @Test
 public void testConstructorWithName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNotNull(classInfo.cMethods);
 assertEquals(2, classInfo.cMethods.length);
 }

 @Test
 public void testConstructorWithNameAndDirManager() throws Exception {
 ClassInfo dirManager = new ClassInfo();
 DirManager dirMan = new DirManager();
 ClassInfo classInfo = new ClassInfo("java.lang.String", dirMan);
 assertNotNull(classInfo.cMethods);
 assertEquals(2, classInfo.cMethods.length);
 }

 @Test
 public void testPrintFields() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertEquals(0, fields.length);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(52, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("substring(int, int)", methods[0]);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(0, constructors.length);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(0, interfaces.length);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString");
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals("public synchronized java.lang.String toString()", methods[0]);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods(new String[]{"public"}, "value");
 assertNotNull(methods);
 assertEquals(0, methods.length);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 Method method = classInfo.getClass().getMethod("valueOf", new Class[] {String.class});
 ClassInfo.MethodData methodData = classInfo.getFoundMethod(Arrays.asList(classInfo.cMethods).indexOf(method.toString()));
 assertNotNull(methodData);
 assertEquals("java.lang.String", methodData.getMethClass());
 assertEquals("valueOf", methodData.getMethName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrintInterfacesWithInvalidClass() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] interfaces = classInfo.printInterfaces();
 classInfo.printArray(interfaces);
 }

 @Test
 public void testMain() throws Exception {
 String[] args = new String[]{"java.lang.String"};
 ClassInfo.main(args);
 }
}