package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/**
 * TypeHandlerTest class to test all methods in TypeHandler class.
 *
 * @author [Your Name]
 */
public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "test";
 Object obj = new Object();
 Object expected = TypeHandler.createValue(str, obj.getClass());
 Object actual = TypeHandler.createValue(str, obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateValue_StringClass() {
 String str = "test";
 Class clazz = String.class;
 Object expected = str;
 Object actual = TypeHandler.createValue(str, clazz);
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateValue_StringClassNotHandled() {
 String str = "test";
 Class clazz = Integer.class;
 Object expected = null;
 Object actual = TypeHandler.createValue(str, clazz);
 assertNull(actual);
 }

 @Test
 public void testCreateObject() {
 String className = "java.lang.String";
 Object expected = "Test";
 Object actual = TypeHandler.createObject(className);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(expected, actual);
 }

 @Test(expected = Exception.class)
 public void testCreateObject_InvalidClassName() {
 String className = "InvalidClassName";
 Object expected = null;
 Object actual = TypeHandler.createObject(className);
 assertNull(actual);
 }

 @Test
 public void testCreateNumber() {
 String numberString = "123";
 Number expected = 123;
 Number actual = (Number) TypeHandler.createValue(numberString, Number.class);
 assertEquals(expected.longValue(), actual.longValue());

 String numberStringWithDecimal = "123.45";
 Double expectedDouble = 123.45;
 Double actualDouble = (Double) TypeHandler.createValue(numberStringWithDecimal, Double.class);
 assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testCreateDate() {
 String dateString = "2022-12-31";
 Date expected = new Date(2022, 11, 31);
 Date actual = (Date) TypeHandler.createValue(dateString, Date.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateURL() {
 String urlString = "http://www.example.com";
 URL expected = new URL(urlString);
 URL actual = (URL) TypeHandler.createValue(urlString, URL.class);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testCreateFile() {
 String filePath = "/tmp/test.txt";
 File expected = new File(filePath);
 File actual = (File) TypeHandler.createValue(filePath, File.class);
 assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
 }

 @Test
 public void testCreateFiles() {
 String filePaths = "/tmp/test1.txt,/tmp/test2.txt";
 File[] expected = new File[] { new File("/tmp/test1.txt"), new File("/tmp/test2.txt") };
 File[] actual = (File[]) TypeHandler.createValue(filePaths, File[].class);
 assertArrayEquals(expected, actual);
 }
}