package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.net.*;
import java.io.*;
import static java.lang.Class.forName;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader = null;

 @Test
 public void testGetInstance() {
 classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 Assert.assertEquals(3, urls.size());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWithNullParent() {
 classLoader = XBUSClassLoader.createClassLoader(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testFindClass() throws ClassNotFoundException {
 Class<?> clazz = classLoader.findClass("net.sf.xbus.base.core.trace.Trace");
 Assert.assertNotNull(clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testFindNonExistentClass() throws ClassNotFoundException {
 Assert.assertThrows(ClassNotFoundException.class, () -> classLoader.findClass("net.sf.xbus.base.core.nonExistentClass"));
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithMalformedURL() {
 Vector urls = new Vector<>();
 try {
 XBUSClassLoader.addUrls(urls, "http://www.xbus.net");
 fail("Should throw MalformedURLException");
 } catch (MalformedURLException e) {
 // Expected
 }
 }

 @Test
 public void testFindClassWithParent() throws ClassNotFoundException {
 Class<?> parentClass = forName("java.lang.Object");
 Class<?> clazz = classLoader.findClass(parentClass.getName());
 Assert.assertNotNull(clazz);
 Assert.assertEquals(parentClass.getName(), clazz.getName());
 }

 @Test
 public void testFindClassWithCustomClassLoader() throws ClassNotFoundException {
 Class<?> customClass = forName("net.sf.xbus.base.core.test.CustomClass");
 Class<?> clazz = classLoader.findClass(customClass.getName());
 Assert.assertNotNull(clazz);
 Assert.assertEquals(customClass.getName(), clazz.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testFindClassWithDuplicateURLs() throws ClassNotFoundException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), ClassLoader.getSystemClassLoader());
 classLoader.findClass("java.lang.Object");
 fail("Should throw IllegalStateException");
 }

}