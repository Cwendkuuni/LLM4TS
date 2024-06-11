package com.jstevh.viewer.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertTrue(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.Object", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFound() throws Exception {
 ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.NonExistentClass");
 fail("ClassNotFoundException expected but not thrown.");
 }

 @Test
 public void testPrintFields() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.HashMap");
 String[] fields = classInfo.printFields();
 assertNotNull(fields);
 assertEquals(11, fields.length);
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertEquals(87, methods.length);
 }

 @Test
 public void testPrintMethodsWithParamFilter() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(1);
 assertNotNull(methods);
 assertEquals(17, methods.length);
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertEquals(1, constructors.length);
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(0, interfaces.length);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("contains");
 assertNotNull(methods);
 assertEquals(1, methods.length);
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 Method method = classInfo.getFoundMethod(1);
 assertNotNull(method);
 assertEquals("public boolean add(E e)", method.getName());
 }

 @Test
 public void testData() throws Exception {
 String[] data = ClassInfo.getData(ClassInfo.class.getMethods());
 assertNotNull(data);
 assertEquals(110, data.length);
 }

 @Test
 public void testGetDataWithClass() throws Exception {
 Class[] classes = new Class[] { String[].class, Integer[].class };
 String[] data = ClassInfo.getData(classes);
 assertNotNull(data);
 assertEquals(2, data.length);
 }

 @Test
 public void testMain() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 classInfo.main(new String[] { "new", "java.util.ArrayList" });
 }

 @Test
 public void testMainWithWrongArgument() throws Exception {
 ClassInfo classInfo = new ClassInfo();
 classInfo.main(new String[] { "wrongArgument" });
 }

}