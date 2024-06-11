package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

import net.sf.xbus.base.core.Constants;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader getClassLoader() {
 return XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector<URL> urls = new Vector<URL>();
 String xbusHome = System.getProperty("XBUS_HOME");
 XBUSClassLoader.addUrls(urls, xbusHome + "/lib");
 assertTrue(urls.size() > 0);
 XBUSClassLoader.addUrls(urls, xbusHome + "/lib/runtime");
 assertTrue(urls.size() > 0);
 XBUSClassLoader.addUrls(urls, xbusHome + "/plugin/lib");
 assertTrue(urls.size() > 0);
 XBUSClassLoader.addUrls(urls, xbusHome + "/test/lib");
 assertTrue(urls.size() > 0);
 }

 @Test
 public void testGetUrls() {
 XBUSClassLoader classLoader = getClassLoader();
 URL[] urls = classLoader.getURLs();
 assertNotNull(urls);
 assertTrue(urls.length > 0);
 }

 @Test
 public void testFindClass() throws ClassNotFoundException {
 Class<?> clazz = classLoader.findClass("net.sf.xbus.base.core.XBusRuntime");
 assertNotNull(clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testFindClassNotFound() throws ClassNotFoundException {
 Class<?> clazz = classLoader.findClass("net.sf.xbus.base.core.nonExistentClass");
 fail("Class should not be found");
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException {
 Class<?> clazz = classLoader.loadClass("java.util.Date");
 assertNotNull(clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadClassNotFound() throws ClassNotFoundException {
 Class<?> clazz = classLoader.loadClass("net.sf.xbus.base.core.nonExistentClass");
 fail("Class should not be found");
 }

}