package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.ClassCastException;
import java.lang.ClassFactory;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tests for TypeHandler class.
 */
public class TypeHandlerTest {
 @Test
 public void testCreateValueWithString() {
 String str = "test";
 Object obj = TypeHandler.createValue(str, String.class);
 assertEquals(str, obj);
 }

 @Test
 public void testCreateValueWithValidClassName() {
 String classname = "java.lang.Integer";
 Object obj = TypeHandler.createValue("123", classname);
 assertEquals(123, obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueWithNotFoundClassName() {
 String classname = "java.lang.InvalidClass";
 TypeHandler.createValue("123", classname);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueWithUnableToCreateInstance() {
 String classname = "java.lang.InvalidClass";
 TypeHandler.createValue(classname, classname);
 }

 @Test
 public void testCreateNumberWithValidNumberString() {
 String str = "123";
 Number number = TypeHandler.createNumber(str);
 assertEquals(123, number.intValue());
 }

 @Test(expected = ParseException.class)
 public void testCreateNumberWithInvalidNumberString() {
 String str = "abc";
 TypeHandler.createNumber(str);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateDateWithValidDateString() {
 String str = "2022-03-01";
 TypeHandler.createDate(str);
 }

 @Test(expected = ParseException.class)
 public void testCreateURLWithInvalidURLString() {
 String str = "http://example.com/abc";
 TypeHandler.createURL(str);
 }

 @Test
 public void testCreateFileWithValidFilePath() {
 String str = "/tmp/testfile.txt";
 File file = TypeHandler.createFile(str);
 assertTrue(file.exists());
 }

 @Test
 public void testCreateFileWithNonExistentFilePath() {
 String str = "/tmp/nonexistentfile.txt";
 File file = TypeHandler.createFile(str);
 assertFalse(file.exists());
 }

 @Test
 public void testCreateFilesWithMultipleFilePaths() {
 String str = "/tmp/testfile1.txt,/tmp/testfile2.txt";
 File[] files = TypeHandler.createFiles(str);
 assertEquals(2, files.length);
 for (File file : files) {
 assertTrue(file.exists());
 }
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObjectWithNotFoundClassName() {
 String classname = "java.lang.InvalidClass";
 try {
 Object obj = TypeHandler.createObject(classname);
 fail("Expected ClassNotFoundException");
 } catch (ClassNotFoundException e) {
 // expected
 }
 }

 @Test(expected = InstantiationException.class)
 public void testCreateObjectWithUnableToCreateInstance() {
 String classname = "java.lang.UninstantiableClass";
 try {
 Object obj = TypeHandler.createObject(classname);
 fail("Expected InstantiationException");
 } catch (InstantiationException e) {
 // expected
 }
 }

 @Test(expected = IllegalAccessException.class)
 public void testCreateObjectWithIllegalAccess() {
 String classname = "java.lang.AccessDeniedClass";
 try {
 Class<?> clazz = Class.forName(classname);
 Object obj = clazz.newInstance();
 fail("Expected IllegalAccessException");
 } catch (IllegalAccessException e) {
 // expected
 }
 }

 @Test(expected = ClassCastException.class)
 public void testCreateValueWithMismatchedType() {
 String str = "123";
 Object obj = TypeHandler.createValue(str, int[].class);
 assertNull(obj);
 }

 @Test(expected = InvocationTargetException.class)
 public void testCreateObjectWithExceptionDuringCreation() {
 String classname = "java.lang.RuntimeExceptionClass";
 try {
 Object obj = TypeHandler.createObject(classname);
 fail("Expected InvocationTargetException");
 } catch (InvocationTargetException e) {
 // expected
 }
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURLWithInvalidURLString() {
 String str = "http://example.com/abc";
 TypeHandler.createURL(str);
 }
}