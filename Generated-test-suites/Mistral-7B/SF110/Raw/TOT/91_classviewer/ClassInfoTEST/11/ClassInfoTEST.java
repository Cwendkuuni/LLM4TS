package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class ClassInfoTest {

 private ClassInfo classInfo;
 private String className;

 @Before
 public void setUp() throws Exception {
 className = "com.jstevh.viewer.ClassInfo";
 classInfo = new ClassInfo(className);
 }

 @Test
 public void testIsAbstract() {
 ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractList");
 assertTrue(abstractClassInfo.isAbstract());
 ClassInfo nonAbstractClassInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(nonAbstractClassInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
 assertFalse(interfaceClassInfo.isInterface());
 ClassInfo interfaceImplClassInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(interfaceImplClassInfo.isInterface());
 ClassInfo interfaceInfoClassInfo = new ClassInfo("java.util.ListIterator");
 assertTrue(interfaceInfoClassInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 assertEquals("ClassInfo", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 assertNull(classInfo.getClassPackage());
 File file = new File("src/com/jstevh/viewer/ClassInfo.java");
 String packageName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator));
 ClassInfo classInfoWithPackage = new ClassInfo(className, new DirManager(packageName));
 assertEquals(packageName, classInfoWithPackage.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNull(classInfo.getSuperClassName());
 ClassInfo classInfoWithPackage = new ClassInfo("java.util.AbstractList");
 assertEquals("java.util.AbstractCollection", classInfoWithPackage.getSuperClassName());
 }

 @Test
 public void testPrintMethods() throws NoSuchMethodException, SecurityException {
 Method[] methods = classInfo.getClass().getMethods();
 ClassInfo testClassInfo = new ClassInfo("com.jstevh.viewer.ClassInfoTest");
 String[] testMethods = testClassInfo.printMethods();
 String[] classMethods = classInfo.printMethods();
 assertArrayEquals(classMethods, testMethods);
 }

 @Test
 public void testPrintMethodsWithParam() throws NoSuchMethodException, SecurityException {
 Method method = classInfo.getClass().getMethod("printMethods", int.class);
 String[] methodsWithParam = classInfo.printMethods((int) 1);
 String[] expectedMethods = {"printMethods(int)", "main(String[])"};
 assertArrayEquals(expectedMethods, methodsWithParam);
 }

 @Test
 public void testPrintConstructors() throws NoSuchMethodException, SecurityException {
 Method[] constructors = classInfo.getClass().getConstructors();
 String[] classConstructors = classInfo.printConstructors();
 assertArrayEquals(classConstructors, getData(constructors));
 }

 @Test
 public void testPrintInterfaces() throws NoSuchMethodException, SecurityException {
 Method method = classInfo.getClass().getMethod("printInterfaces");
 String[] expectedInterfaces = {"DataTools", "StringTools"};
 String[] interfaces = ((MethodData) method.invoke(classInfo)).getFoundMethod(0).getMethClass();
 String[] classInterfaces = classInfo.printInterfaces();
 assertArrayEquals(expectedInterfaces, classInterfaces);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("java.util.NonExistentClass");
 }
}