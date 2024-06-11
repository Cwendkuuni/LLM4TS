package net.sf.xbus.base.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static final String XBUS_HOME = "your.xbus.home.path";

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 // Perform any tear-down needed to prepare for the next test.
 }

 @Test
 public void testInstance() {
 Class<?> clazz = classLoader.loadClass("net.sf.xbus.base.core.reflection.XBUSClassLoader");
 assertNotNull(clazz);
 }

 @Test
 public void testCreateClassLoader() {
 Class<?> clazz = classLoader.getClass();
 XBUSClassLoader newClassLoader = null;
 try {
 newClassLoader = (XBUSClassLoader) clazz.newInstance();
 } catch (InstantiationException | IllegalAccessException e) {
 e.printStackTrace();
 fail("Failed to create new instance of XBUSClassLoader");
 }
 assertNotNull(newClassLoader);
 }

 @Test
 public void testGetInstance() {
 Class<?> instanceClass = classLoader.getClass();
 XBUSClassLoader instance = XBUSClassLoader.getInstance(instanceClass.getClassLoader());
 assertSame(classLoader, instance);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 List<URL> urls = new ArrayList<>();
 String dirName = XBUS_HOME;
 XBUSClassLoader.addUrls(urls, dirName);
 assertEquals(4, urls.size());
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsInvalidPath() {
 List<URL> urls = new ArrayList<>();
 String dirName = "invalid.path";
 XBUSClassLoader.addUrls(urls, dirName);
 fail("Should have thrown MalformedURLException");
 }

 @Test
 public void testAddUrlsJarFiles() throws MalformedURLException {
 List<URL> urls = new ArrayList<>();
 String dirName = "path/to/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 for (URL url : urls) {
 assertFalse(url.toString().endsWith("/"));
 assertTrue(url.toString().endsWith(".jar"));
 }
 }

 @Test
 public void testAddUrlsZipFiles() throws MalformedURLException {
 List<URL> urls = new ArrayList<>();
 String dirName = "path/to/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 for (URL url : urls) {
 assertFalse(url.toString().endsWith("/"));
 assertTrue(url.toString().endsWith(".zip"));
 }
 }

 @Test
 public void testStaticInitializer() {
 assertNull(XBUSClassLoader.mClassLoader);
 }
}