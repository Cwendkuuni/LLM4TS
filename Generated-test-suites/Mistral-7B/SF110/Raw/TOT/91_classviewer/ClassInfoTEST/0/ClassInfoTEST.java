package com.jstevh.viewer.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract() == false);

 ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(interfaceClassInfo.isAbstract() == true);
 }

 @Test
 public void testIsInterface() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertFalse(stringClassInfo.isInterface());

 ClassInfo runnableClassInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(runnableClassInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", stringClassInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNotNull(stringClassInfo.getClassPackage());
 assertEquals("java.lang", stringClassInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNull(stringClassInfo.getSuperClassName());

 ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
 assertEquals("java.lang.Object", stringBufferClassInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.jstevh.viewer.NonExistentClass");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 new ClassInfo(null);
 }

 @Test
 public void testPrintFields() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNotNull(stringClassInfo.printFields());
 }

 @Test
 public void testPrintMethods() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNotNull(stringClassInfo.printMethods());
 }

 @Test
 public void testPrintMethodsWithParam() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNotNull(stringClassInfo.printMethods(1));
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNotNull(stringClassInfo.printConstructors());
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNotNull(stringClassInfo.printInterfaces());
 }
}