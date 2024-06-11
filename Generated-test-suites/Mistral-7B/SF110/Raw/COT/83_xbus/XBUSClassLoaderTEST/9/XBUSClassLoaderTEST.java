package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;

 @Test
 public void testGetInstance() {
 classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, ".");
 assertEquals(3, urls.size());
 assertTrue(urls.contains(new File(".").toURI().toURL()));
 assertTrue(urls.contains(new File("lib").toURI().toURL()));
 assertTrue(urls.contains(new File("lib/runtime").toURI().toURL()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsNullDirName() {
 XBUSClassLoader.addUrls(null, ".");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsNullJars() {
 File libPath = new File(".");
 File[] jars = null;
 XBUSClassLoader.addUrls(null, libPath.getAbsolutePath());
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsInvalidUrl() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "invalid_url");
 fail("MalformedURLException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceNullParent() {
 XBUSClassLoader.getInstance(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderNullParent() {
 XBUSClassLoader.createClassLoader(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderNullUrlArray() {
 XBUSClassLoader.createClassLoader(null, ClassLoader.getSystemClassLoader());
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException, MalformedURLException, IOException {
 String className = "net.sf.xbus.base.core.XBusFactory";
 Class<?> loadedClass = classLoader.loadClass(className);
 assertNotNull(loadedClass);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadClassUnknownClass() throws ClassNotFoundException {
 String className = "UnknownClass";
 classLoader.loadClass(className);
 fail("ClassNotFoundException expected but not thrown");
 }

 @Test(expected = MalformedURLException.class)
 public void testLoadClassMalformedURL() throws ClassNotFoundException, MalformedURLException {
 String className = "java.lang.String";
 String urlString = "invalid_url";
 classLoader.loadClass(className, new URL(urlString));
 fail("MalformedURLException expected but not thrown");
 }

 @Test(expected = IOException.class)
 public void testLoadClassIOError() throws ClassNotFoundException, IOException {
 String className = "java.lang.String";
 InputStream is = new ByteArrayInputStream("invalid_class_bytes".getBytes());
 classLoader.loadClass(className, is, null);
 fail("IOException expected but not thrown");
 }

 @Test(expected = LinkageError.class)
 public void testLoadClassCircularDependency() throws ClassNotFoundException, MalformedURLException {
 String class1Name = "net.sf.xbus.base.core.XBusFactory";
 String class2Name = "net.sf.xbus.base.core.XBusFactory$XBusFactoryLoader";
 Class<?> class1 = classLoader.loadClass(class1Name);
 Class<?> class2 = classLoader.loadClass(class2Name);
 assertNotNull(class1);
 assertNotNull(class2);
 try {
 class1.getMethod("loadXBus");
 class2.getMethod("loadXBus");
 fail("LinkageError expected but not thrown");
 } catch (NoSuchMethodException e) {
 // Expected
 }
 }

 @Test
 public void testLoadClassWithResourcePath() throws ClassNotFoundException, MalformedURLException, IOException {
 String className = "net.sf.xbus.base.core.XBusFactory";
 Class<?> loadedClass = classLoader.loadClass(className, XBUSClassLoader.class.getResource("/"));
 assertNotNull(loadedClass);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadClassWithResourcePathUnknownClass() throws ClassNotFoundException {
 String className = "UnknownClass";
 classLoader.loadClass(className, XBUSClassLoader.class.getResource("/"));
 fail("ClassNotFoundException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testLoadClassWithResourcePathNullResource() {
 String className = "net.sf.xbus.base.core.XBusFactory";
 classLoader.loadClass(className, null);
 fail("NullPointerException expected but not thrown");
 }

}