package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.StringTools;
import com.jstevh.tools.DirManager;

public class ClassInfoTest {

 private ClassInfo classInfo;
 private String className;
 private DirManager dirManager;

 @Before
 public void setUp() throws Exception {
 className = "com.jstevh.viewer.ClassInfo";
 dirManager = new DirManager(".");
 classInfo = new ClassInfo(className, dirManager);
 }

 @Test
 public void testIsAbstract() throws Exception {
 Class InfoAbstractClass = Class.forName("java.lang.AbstractMethodError");
 Class InfoConcreteClass = Class.forName("java.lang.String");
 ClassInfo abstractClassInfo = new ClassInfo(InfoAbstractClass.getName());
 ClassInfo concreteClassInfo = new ClassInfo(InfoConcreteClass.getName());

 assertTrue(abstractClassInfo.isAbstract());
 assertFalse(concreteClassInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 Class InfoInterface = Class.forName("java.util.List");
 ClassInfo interfaceClassInfo = new ClassInfo(InfoInterface.getName());
 Class InfoNonInterface = Class.forName("java.lang.String");
 ClassInfo nonInterfaceClassInfo = new ClassInfo(InfoNonInterface.getName());

 assertTrue(interfaceClassInfo.isInterface());
 assertFalse(nonInterfaceClassInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 assertEquals(className, classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 String packageName = "com.jstevh.viewer";
 assertEquals(packageName, classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo(Object.class.getName());
 assertNull(classInfo.getSuperClassName());

 ClassInfo StringClassInfo = new ClassInfo(String.class.getName());
 assertEquals("java.lang.Object", StringClassInfo.getSuperClassName());
 }

 @Test
 public void testGetMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 Method[] methods = classInfo.c.getMethods();
 String[] methodsNames = classInfo.cMethods;

 assertEquals(methods.length, methodsNames.length);
 for (int i = 0; i < methods.length; i++) {
 assertEquals(methods[i].getName(), methodsNames[i]);
 }
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFound() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.nonexistent.Class");
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testPrintFields() throws Exception {
 ClassInfo classInfo = new ClassInfo(String.class.getName());
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 String methodName = "getClassName";
 int index = Arrays.asList(classInfo.cMethods).indexOf(methodName);
 Method method = classInfo.c.getMethod(methodName, new Class[0]);
 MethodData methodData = classInfo.getFoundMethod(index);

 assertEquals(method.getReturnType().getName(), methodData.getMethClass());
 assertEquals(methodName, methodData.getMethName());
 assertEquals(method.getParameterTypes().length, 0);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 String methodName = "getClassName";
 String[] methods = classInfo.srchMethods(methodName);
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals(methodName, methods[0]);
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 String methodName = "getClassName";
 String[] index = { "ClassInfo" };
 String[] methods = classInfo.srchMethods(methodName, index);
 assertNotNull(methods);
 assertEquals(1, methods.length);
 assertEquals(methodName, methods[0]);
 }

 @Test
 public void testgetData() throws Exception {
 Method[] methods = classInfo.c.getMethods();
 String[] data = ClassInfo.getData(methods);
 assertNotNull(data);
 }

 @Test
 public void testgetDataForClasses() throws Exception {
 Class[] classes = { String.class };
 String[] data = ClassInfo.getData(classes);
 assertNotNull(data);
 }

 @Test
 public void testSearchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 String methodName = "getClassName";
 List<Method> methods = Arrays.asList(classInfo.c.getMethods());
 Method method = Arrays.stream(methods.toArray(new Method[0])).filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);
 String[] methodsData = classInfo.srchMethods(methodName);
 String expected = method.toString();

 assertEquals(expected, methodsData[0]);
 }

 @Test
 public void testPrintArray() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 Object[] array = new Object[10];
 classInfo.printArray(array);
 }

 @Test
 public void testGetClass() throws Exception {
 String name = "java.util.ArrayList";
 Class classObject = classInfo.getClass(name);
 assertEquals(ArrayList.class, classObject);
 }

 @Test
 public void testGetClassWithPackage() throws Exception {
 String name = "com.jstevh.viewer.ClassInfo";
 Class classObject = classInfo.getClass(name);
 assertEquals(ClassInfo.class, classObject);
 }

 @Test(expected = NoClassDefFoundError.class)
 public void testNoClassDefFoundError() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.nonexistent.Class");
 fail("Expected NoClassDefFoundError");
 }

 @Test
 public void testPrintClassInfo() throws Exception {
 ClassInfo classInfo = new ClassInfo(className);
 classInfo.printClassInfo();
 }

 @Test
 public void testMain() throws Exception {
 String[] args = new String[]{"new", className};
 ClassInfo.main(args);
 }

 @Test
 public void testMainWithArgumentsNotFound() throws Exception {
 String[] args = new String[0];
 ClassInfo.main(args);
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testMainWithInvalidArgument() throws Exception {
 String[] args = new String[]{"invalid"};
 ClassInfo.main(args);
 fail("Expected ClassNotFoundException");
 }
}