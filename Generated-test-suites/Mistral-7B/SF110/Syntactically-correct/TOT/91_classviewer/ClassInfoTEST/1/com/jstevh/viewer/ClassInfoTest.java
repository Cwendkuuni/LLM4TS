package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.junit.Test;

public class ClassInfoTest {

 @Test
 public void testConstructor() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IOException {
 Class<ClassInfo> classInfoClass = Class.forName("com.jstevh.viewer.ClassInfo");
 Constructor<ClassInfo> constructor = classInfoClass.getDeclaredConstructor(String.class, DirManager.class);
 constructor.setAccessible(true);

 // Test with valid arguments
 File dir = new File(".");
 ClassInfo classInfo = constructor.newInstance("java.lang.String", dir);
 assertTrue(classInfo.isInstanceOf(String.class));
 assertTrue(classInfo.isInstanceOf(DirManager.class));

 // Test with null arguments
 assertNull(constructor.newInstance((Object[]) null));
 assertNull(constructor.newInstance(null, dir));
 assertNull(constructor.newInstance(dir, null));

 // Test with invalid argument type
 try {
 constructor.newInstance("java.lang.Integer", dir);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertFalse(classInfo.isAbstract());

 ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
 assertTrue(interfaceClassInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertFalse(stringClassInfo.isInterface());

 ClassInfo listClassInfo = new ClassInfo("java.util.List");
 assertTrue(listClassInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", stringClassInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang", stringClassInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 assertNull(stringClassInfo.getSuperClassName());

 ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
 assertEquals("java.lang.Object", stringBufferClassInfo.getSuperClassName());
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] methods = stringClassInfo.printMethods();
 assertNotNull(methods);
 assertEquals(14, methods.length);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] methods = stringClassInfo.printMethods(1);
 assertNotNull(methods);
 assertEquals(4, methods.length);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
 String[] interfaces = stringClassInfo.printInterfaces();
 assertNull(interfaces);

 ClassInfo listClassInfo = new ClassInfo("java.util.List");
 String[] interfaces = listClassInfo.printInterfaces();
 assertEquals(1, interfaces.length);
 assertEquals("java.lang.Iterator", interfaces[0]);
 }
}