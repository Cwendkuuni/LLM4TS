package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.Class.forName;

public class XBUSClassLoaderTest {

 private XBUSClassLoader classLoader;

 @Test
 public void testGetInstance() {
 classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "testPath");
 assertFalse(urls.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWithNullParent() {
 XBUSClassLoader.createClassLoader(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithNullDirName() {
 XBUSClassLoader.addUrls(null, "testPath");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithEmptyDirName() {
 XBUSClassLoader.addUrls(new Vector(), "");
 }

 @Test
 public void testFindClass() throws ClassNotFoundException {
 String className = "com.test.TestClass";
 Class testClass = classLoader.findClass(className);
 assertNotNull(testClass);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testFindNonExistentClass() throws ClassNotFoundException {
 String nonExistentClassName = "com.test.NonExistentClass";
 classLoader.findClass(nonExistentClassName);
 fail("ClassNotFoundException expected but not thrown");
 }

 @Test(expected = IOException.class)
 public void testAddUrlsWithInvalidPath() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "testPath/invalidPath");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithMalformedURL() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "testPath:/invalidURL");
 }

 @Test(expected = NullPointerException.class)
 public void testFindClassWithNullClassName() {
 classLoader.findClass(null);
 }

 @Test(expected = ClassFormatError.class)
 public void testFindClassWithInvalidClassFile() throws ClassNotFoundException {
 XBUSClassLoader urlClassLoader = new XBUSClassLoader(new URL[]{}, getClass().getClassLoader());
 Class invalidClass = urlClassLoader.findClass("java.lang.Class$Test");
 fail("ClassFormatError expected but not thrown");
 }
}