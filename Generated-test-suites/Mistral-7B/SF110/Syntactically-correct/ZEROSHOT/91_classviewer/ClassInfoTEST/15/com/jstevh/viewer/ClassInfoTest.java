package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import org.junit.Test;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 private ClassInfo classInfo;
 private String className;

 @Before
 public void setUp() {
 className = "com.jstevh.viewer.ClassInfo";
 try {
 classInfo = new ClassInfo(className);
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 fail("Class not found.");
 }
 }

 @Test
 public void testIsAbstract() {
 assertFalse(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 assertEquals(className, classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 assertNull(classInfo.getSuperClassName());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullName() {
 new ClassInfo(null);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testConstructorInvalidName() {
 new ClassInfo("InvalidClassName");
 }

 @Test
 public void testPrintFields() {
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertFalse(fields.length == 0);
 }

 @Test
 public void testPrintMethods() {
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintMethodsWithParam() {
 String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testPrintConstructors() {
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertFalse(constructors.length == 0);
 }

 @Test
 public void testPrintInterfaces() {
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertFalse(interfaces.length == 0);
 }

 @Test
 public void testSrchMethods() {
 String searchString = "printFields";
 String[] methods = classInfo.srchMethods(searchString);
 assertNotNull(methods);
 assertFalse(methods.length == 0);
 }

 @Test
 public void testGetFoundMethod() {
 int position = 0;
 Method method = classInfo.getFoundMethod(position);
 assertNotNull(method);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetFoundMethodInvalidPosition() {
 int position = classInfo.cMethods.length;
 Method method = classInfo.getFoundMethod(position);
 }
}