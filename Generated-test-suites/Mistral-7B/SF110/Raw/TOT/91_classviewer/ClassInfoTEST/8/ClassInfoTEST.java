package com.jstevh.viewer.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

 @Test
 public void testIsAbstract() {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo classInfo = new ClassInfo("java.util.List");
 assertFalse(classInfo.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang.StringBuilder", classInfo.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
 assertEquals("java.lang", classInfo.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo classInfo = new ClassInfo("java.lang.Object");
 assertNull(classInfo.getSuperClassName());
 }

 @Test
 public void testGetSuperClassNameForClassWithSuperClass() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertEquals("java.lang.Object", classInfo.getSuperClassName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() {
 new ClassInfo("com.nonExistentPackage.NonExistentClass");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 new ClassInfo(null);
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccessException() {
 final ClassInfo classInfo = new ClassInfo("java.lang.String");
 final String[] fields = classInfo.printFields();
 assertArrayEquals(new String[]{"length=6", "value=null"}, fields);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForNullDirManager() {
 new ClassInfo("java.lang.String", null);
 }
}