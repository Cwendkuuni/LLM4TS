package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

import net.sf.xbus.base.core.Constants;

public class XBUSClassLoaderTest {

 private static ClassLoader parent = XBUSClassLoader.class.getClassLoader();

 @Test
 public void testGetInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 assertEquals(4, urls.size());
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/plugin/lib");
 assertEquals(6, urls.size());
 }

 @Test
 public void testGetURLs() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 URL[] urls = classLoader.getURLs();
 assertNotNull(urls);
 assertEquals(6, urls.length);
 }

 @Test
 public void testFindClass() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.trace.Trace";
 Class<?> clazz = XBUSClassLoader.getInstance(parent).findClass(className);
 assertNotNull(clazz);
 }

 @Test
 public void testFindClasses() throws ClassNotFoundException {
 String packageName = "net.sf.xbus.base.core.reflection";
 Class<?>[] classes = XBUSClassLoader.getInstance(parent).findClasses(packageName);
 assertNotNull(classes);
 assertEquals(15, classes.length);
 }

 @Test
 public void testLoadClass() throws MalformedURLException, ClassNotFoundException {
 String className = "java.lang.String";
 Class<?> clazz = XBUSClassLoader.createClassLoader(parent).loadClass(className);
 assertNotNull(clazz);
 assertEquals(String.class, clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadClassNotFound() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 classLoader.loadClass("net.sf.xbus.base.core.nonexistent.ClassName");
 fail("ClassNotFoundException expected");
 }
}