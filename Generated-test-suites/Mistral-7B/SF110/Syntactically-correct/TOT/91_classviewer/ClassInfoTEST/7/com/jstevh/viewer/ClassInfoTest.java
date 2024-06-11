package com.jstevh.viewer.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.ClassInfo.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassInfoTest {

 @Test
 public void testNewInstance() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 assertNull(classInfo.c);
 assertSame(WindowConstants.class, classInfo.c);
 }

 @Test
 public void testNewInstanceWithName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertSame(String.class, classInfo.c);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testNewInstanceWithWrongName() throws Exception {
 new ClassInfo("com.jstevh.viewer.NonExistentClass");
 }

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertFalse(classInfo.isAbstract());

 ClassInfo classInfoAbstract = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfoAbstract.isAbstract());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNull(classInfo.getClassPackage());

 ClassInfo classInfoWithPackage = new ClassInfo("com.jstevh.viewer.PackageTest");
 assertEquals("com.jstevh.viewer", classInfoWithPackage.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfoObject = new ClassInfo();
 assertNull(classInfoObject.getSuperClassName());

 ClassInfo classInfoString = new ClassInfo("java.lang.String");
 assertNull(classInfoString.getSuperClassName());

 ClassInfo classInfoArrayList = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", classInfoArrayList.getSuperClassName());
 }

 @Test
 public void testClassInfoConstructorWithDirManager() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String", new DirManager("."));
 assertSame(String.class, classInfo.c);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods();
 assertArrayEquals(new String[]{"length()", "charAt(int)", "substring(int, int)", "equals(Object)", "toString()"}, methods);
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.printMethods(NO_INHERITED_METHODS);
 assertArrayEquals(new String[]{"length()"}, methods);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertArrayEquals(new String[]{"public java.util.ArrayList()", "public java.util.ArrayList(int)", "public java.util.ArrayList(Collection)", "public java.util.ArrayList(int, Collection)"}, constructors);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertArrayEquals(new String[]{"java.io.Serializable"}, interfaces);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("toString");
 assertArrayEquals(new String[]{"public java.lang.String toString()"}, methods);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 String[] methods = classInfo.srchMethods("equals", new String[]{"Object"});
 assertArrayEquals(new String[]{"public boolean equals(Object)"}, methods);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 Method method = classInfo.getFoundMethod(0);
 assertEquals("public int size()", method.getMethName());
 assertEquals("java.util.ArrayList", method.getMethClass());
 assertEquals("java.util.List", method.getMethPackage());
 }

 @Test
 public void testGetFoundMethodWithInvalidIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 Method method = classInfo.getFoundMethod(100);
 assertNull(method);
 }

 @Test
 public void testGetClass() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 Class clazz1 = classInfo.getClass("java.lang.String");
 assertSame(String.class, clazz1);

 Class clazz2 = classInfo.getClass("java.util.ArrayList");
 assertSame(ArrayList.class, clazz2);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetClassWithNonExistentClass() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 classInfo.getClass("com.jstevh.viewer.NonExistentClass");
 }

 @Test
 public void testGetClassWithPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 Class clazz = classInfo.getClass("java.util.ArrayList");
 assertSame(ArrayList.class, clazz);

 Class clazzWithPackage = classInfo.getClass("java.util.ArrayList", new DirManager("."));
 assertSame(ArrayList.class, clazzWithPackage);
 }

 @Test(expected = NoClassDefFoundError.class)
 public void testGetClassWithNonExistentPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 classInfo.getClass("com.jstevh.viewer.NonExistentPackage.NonExistentClass");
 }

 @Test
 public void testConstructorWithNoArguments() throws Exception {
 Constructor constructor = ArrayList.class.getConstructor();
 ClassInfo classInfo = new ClassInfo(ArrayList.class);
 MethodData methodData = classInfo.getFoundMethod(0);
 assertEquals(constructor, methodData.getMethod());
 }

 @Test
 public void testConstructorWithArguments() throws Exception {
 Constructor constructor = ArrayList.class.getConstructor(int.class);
 ClassInfo classInfo = new ClassInfo(ArrayList.class);
 MethodData methodData = classInfo.getFoundMethod(1);
 assertEquals(constructor, methodData.getMethod());
 }

 @Test
 public void testConstructorWithAccessToPrivateConstructor() throws Exception {
 Class<List> listClass = List.class;
 Constructor privateConstructor = listClass.getDeclaredConstructor();
 privateConstructor.setAccessible(true);
 ClassInfo classInfo = new ClassInfo(listClass);
 MethodData methodData = classInfo.getFoundMethod(2);
 assertEquals(privateConstructor, methodData.getMethod());
 }
}