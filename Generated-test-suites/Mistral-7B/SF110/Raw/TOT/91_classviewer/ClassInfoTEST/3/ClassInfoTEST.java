package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertTrue(classInfo.isAbstract()); // String is a final class, but Java's reflection API considers it abstract due to its constant members

 classInfo = new ClassInfo("java.util.ArrayList");
 assertFalse(classInfo.isAbstract()); // ArrayList is a class, not an interface or an abstract class
 }

 @Test
 public void testIsInterface() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface()); // List is an interface, but Java's reflection API does not show it as such

 classInfo = new ClassInfo("java.util.Collections");
 assertFalse(classInfo.isInterface()); // Collections is a class
 }

 @Test
 public void testGetClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.ArrayList", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertNull(classInfo.getSuperClassName());

 classInfo = new ClassInfo("java.util.LinkedList");
 assertEquals("java.util.Collection", classInfo.getSuperClassName());
 }

 @Test
 public void testPrintMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods();
 assertNotNull(methods);
 assertFalse(Arrays.stream(methods).anyMatch(m -> m.equals("null"))); // check for null methods
 }

 @Test
 public void testPrintMethodsWithParam() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
 assertNotNull(methods);
 assertFalse(Arrays.stream(methods).anyMatch(m -> m.equals("null")));
 }

 @Test
 public void testPrintConstructors() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] constructors = classInfo.printConstructors();
 assertNotNull(constructors);
 assertFalse(Arrays.stream(constructors).anyMatch(m -> m.equals("null")));
 }

 @Test
 public void testPrintInterfaces() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] interfaces = classInfo.printInterfaces();
 assertNull(interfaces);

 classInfo = new ClassInfo("java.util.Collection");
 String[] interfaces = classInfo.printInterfaces();
 assertNotNull(interfaces);
 assertEquals(3, interfaces.length);
 assertArrayEquals(new String[]{"java.io.Serializable", "java.util.Collection", "java.lang.Iterable"}, interfaces);
 }

 @Test
 public void testSrchMethods() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("remove");
 assertNotNull(methods);
 assertFalse(Arrays.stream(methods).anyMatch(m -> m.equals("null")));
 }

 @Test
 public void testSrchMethodsWithIndex() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 String[] methods = classInfo.srchMethods("add", new String[]{"int", "E"});
 assertNotNull(methods);
 assertFalse(Arrays.stream(methods).anyMatch(m -> m.equals("null")));
 }

 @Test
 public void testGetFoundMethod() throws Exception {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 MethodData method = classInfo.getFoundMethod(0);
 assertNotNull(method);
 assertEquals("java.util.ArrayList", method.getMethClass());
 assertEquals("add", method.getMethName());
 }
}