package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import org.junit.Test;

public class ClassInfoTest {

 @Test
 public void testIsAbstractMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertFalse(classInfo.isAbstract());

 ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(interfaceClassInfo.isAbstract());

 ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractList");
 assertTrue(abstractClassInfo.isAbstract());
 }

 @Test
 public void testIsInterfaceMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertFalse(classInfo.isInterface());

 ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
 assertTrue(interfaceClassInfo.isInterface());
 }

 @Test
 public void testGetClassNameMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.String", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackageMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassNameMethod() {
 ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
 assertEquals("java.util.AbstractList", classInfo.getSuperClassName());

 ClassInfo classInfoForObject = new ClassInfo("java.lang.Object");
 assertNull(classInfoForObject.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testInvalidClassNameConstructor() {
 new ClassInfo("java.non.existent.class");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testInvalidFileNameConstructor() {
 final DirManager locManager = new DirManager();
 new ClassInfo("non.existent.class", locManager);
 }
}