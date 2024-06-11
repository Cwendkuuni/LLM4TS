package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home";

 @Test
 public void testInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

 @Test(expected = NullPointerException.class)
 public void testInstanceNullParent() {
 ClassLoader parent = null;
 assertNull(XBUSClassLoader.getInstance(parent));
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateClassLoaderNullParent() {
 ClassLoader parent = null;
 XBUSClassLoader.createClassLoader(parent);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = XBUS_HOME + "/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 assertTrue(urls.size() > 0);
 }

 @Test
 public void testGetInstanceUrls() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 assertNotNull(classLoader.getURLs());
 }

 @Test
 public void testGetInstanceLoadedClasses() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 assertNotNull(classLoader.getLoadedClasses());
 }

 @Test
 public void testGetClass() throws ClassNotFoundException {
 String className = "com.test.TestClass";
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 Class<?> clazz = classLoader.findClass(className);
 assertNotNull(clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetClassNotFound() throws ClassNotFoundException {
 String className = "com.test.NonExistentClass";
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 classLoader.findClass(className);
 }

 @Test
 public void testFindResource() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 String resourceName = "net/sf/xbus/base/core/reflection/XBUSClassLoader.class";
 assertNotNull(classLoader.getResource(resourceName));
 }

 @Test(expected = FileNotFoundException.class)
 public void testFindResourceNotFound() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 String resourceName = "nonExistentResource.txt";
 classLoader.getResource(resourceName);
 }

 @Test
 public void testFindResourceWithFile() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 String resourceName = "net/sf/xbus/base/core/reflection/XBUSClassLoader.class";
 File file = new File(resourceName);
 URL url = classLoader.getResource(resourceName);
 assertNotNull(url);
 assertTrue(url.getFile().equals(file.getAbsolutePath()));
 }

 @Test(expected = MalformedURLException.class)
 public void testFindResourceMalformedURL() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 String resourceName = "nonExistentResource::/";
 classLoader.getResource(resourceName);
 }
}