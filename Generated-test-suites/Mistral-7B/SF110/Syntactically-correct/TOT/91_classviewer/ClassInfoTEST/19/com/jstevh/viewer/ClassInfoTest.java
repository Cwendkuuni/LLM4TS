package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.DirManager;
import com.jstevh.viewer.StringTools;

public class ClassInfoTest {

 private static final String CLASS_NAME = "com.jstevh.viewer.ClassInfo";
 private static final String PACKAGE = "com.jstevh.viewer";
 private static final String TEST_DIR = "src/test/resources";
 private static final String TEST_FILE = "ClassInfoTestData.class";
 private static final DirManager testDirManager = new DirManager(TEST_DIR);

 @Test
 public void testClassInfo() throws Exception {
 ClassInfo classInfo = new ClassInfo(CLASS_NAME, testDirManager);

 // Test isAbstract()
 assertFalse(classInfo.isAbstract());

 // Test isInterface()
 assertFalse(classInfo.isInterface());

 // Test getClassName()
 assertEquals(CLASS_NAME, classInfo.getClassName());

 // Test getClassPackage()
 assertEquals(PACKAGE, classInfo.getClassPackage());

 // Test getSuperClassName()
 assertNull(classInfo.getSuperClassName());

 // Test printMethods()
 String[] methods = classInfo.printMethods();
 assertEquals(11, methods.length);
 assertTrue(Arrays.asList(methods).contains("getClassName"));
 assertTrue(Arrays.asList(methods).contains("getClassPackage"));
 assertTrue(Arrays.asList(methods).contains("getSuperClassName"));

 // Test printMethods(int param)
 methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
 assertEquals(0, methods.length);

 // Test printFields()
 String[] fields = classInfo.printFields();
 assertEquals(0, fields.length);

 // Test printInterfaces()
 String[] interfaces = classInfo.printInterfaces();
 assertEquals(0, interfaces.length);

 // Test srchMethods()
 String methodName = "printFields";
 Method method = Class.forName(CLASS_NAME).getMethod(methodName);
 String[] methodsFound = classInfo.srchMethods(method.toString());
 assertEquals(1, methodsFound.length);
 assertEquals(methodName, methodsFound[0]);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() throws Exception {
 new ClassInfo("com.nonExistent.Class");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() throws Exception {
 new ClassInfo("", testDirManager);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException() throws Exception {
 new ClassInfo(null);
 }
}