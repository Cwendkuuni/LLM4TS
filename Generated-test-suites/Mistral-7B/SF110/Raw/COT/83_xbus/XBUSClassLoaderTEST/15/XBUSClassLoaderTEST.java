package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

 private XBUSClassLoader classLoader;

 @Test
 public void testGetInstance() {
 ClassLoader parent = getClass().getClassLoader();
 XBUSClassLoader loadedClassLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(loadedClassLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = getClass().getClassLoader();
 XBUSClassLoader loadedClassLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(loadedClassLoader);
 }

 @Test
 public void testAddUrls() {
 List<URL> urls = new ArrayList<>();
 String xbusHome = Constants.XBUS_HOME;
 String dirName = xbusHome + "/lib";
 XBUSClassLoader.addUrls(urls, dirName);

 assertEquals(3, urls.size());

 File libPath = new File(dirName);
 File[] jars = libPath.listFiles();
 for (File jar : jars) {
 if (jar != null) {
 String name = jar.getName();
 assertTrue(name.endsWith("jar") || name.endsWith("zip"));
 try {
 urls.add(jar.toURL());
 } catch (MalformedURLException e) {
 fail(e.getMessage());
 }
 }
 }
}

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWhenXBUS_HOMEIsNull() {
 ClassLoader parent = getClass().getClassLoader();
 Constants.XBUS_HOME = null;
 XBUSClassLoader loadedClassLoader = XBUSClassLoader.getInstance(parent);
 fail("Expected NullPointerException due to XBUS_HOME being null.");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWhenXBUS_HOMEIsNull() {
 ClassLoader parent = getClass().getClassLoader();
 Constants.XBUS_HOME = null;
 XBUSClassLoader loadedClassLoader = XBUSClassLoader.createClassLoader(parent);
 fail("Expected NullPointerException due to XBUS_HOME being null.");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWhenInvalidURLIsEncountered() {
 List<URL> urls = new ArrayList<>();
 String xbusHome = Constants.XBUS_HOME;
 String dirName = xbusHome + "/not_a_valid_directory";
 XBUSClassLoader.addUrls(urls, dirName);
 fail("Expected MalformedURLException when encountering an invalid directory.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWhenDirectoryDoesNotExist() {
 List<URL> urls = new ArrayList<>();
 String xbusHome = Constants.XBUS_HOME;
 String dirName = xbusHome + "/nonexistent_directory";
 XBUSClassLoader.addUrls(urls, dirName);
 fail("Expected IllegalArgumentException when directory does not exist.");
 }

 @Test
 public void testGetMClassLoader() {
 ClassLoader parent = getClass().getClassLoader();
 XBUSClassLoader loadedClassLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader loadedClassLoader2 = XBUSClassLoader.getInstance(parent);
 assertEquals(loadedClassLoader1, loadedClassLoader2);
 }
}