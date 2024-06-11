package net.sf.xbus.base.core. reflection;

import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

 @Test
 public void testInstance() {
 ClassLoader parent = this.getClass().getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = this.getClass().getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = "src/test/resources/XBUSClassLoaderTestData";
 XBUSClassLoader.addUrls(urls, dirName);
 assertEquals(5, urls.size());
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedURLException() {
 Vector urls = new Vector();
 String invalidDirName = "src/test/resources/XBUSClassLoaderTestData/invalid";
 XBUSClassLoader.addUrls(urls, invalidDirName);
 fail("MalformedURLException should have been thrown");
 }

 @Test
 public void testGetInstance() {
 ClassLoader parent = this.getClass().getClassLoader();
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
 assertEquals(classLoader1, classLoader2);
 }

 @Test
 public void testGetParent() {
 ClassLoader classLoader = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
 assertEquals(this.getClass().getClassLoader(), classLoader.getParent());
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClass";
 Class<?> expectedClass = this.getClass().getClassLoader().loadClass(className);
 Class<?> loadedClass = XBUSClassLoader.getInstance(this.getClass().getClassLoader()).loadClass(className);
 assertEquals(expectedClass, loadedClass);
 }

 @Test
 public void testLoadClassNotFound() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClassNotFound";
 assertFalse(XBUSClassLoader.getInstance(this.getClass().getClassLoader()).loadClass(className) != null);
 }

 @Test
 public void testFindClass() throws ClassNotFoundException {
 String className = "java.lang.String";
 Class<?> expectedClass = this.getClass().getClassLoader().loadClass(className);
 Class<?> loadedClass = XBUSClassLoader.getInstance(this.getClass().getClassLoader()).findLoadedClass(null, className);
 assertEquals(expectedClass, loadedClass);
 }

 @Test
 public void testFindClassNotFound() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClassNotFound";
 assertNull(XBUSClassLoader.getInstance(this.getClass().getClassLoader()).findLoadedClass(null, className));
 }

}