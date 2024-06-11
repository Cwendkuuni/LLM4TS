package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 static {
 try {
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 } catch (Exception e) {
 Trace.error(e);
 }
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader loadedClassLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(loadedClassLoader);
 assertEquals(parent, loadedClassLoader.getParent());
 }

 @Test
 public void testGetInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader loadedClassLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(loadedClassLoader);
 assertEquals(parent, loadedClassLoader.getParent());
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String xbusHome = System.getProperty("XBUS_HOME");
 String dirName = xbusHome + "/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 assertFalse(urls.isEmpty());
 URL url = urls.get(0);
 assertTrue(url.toString().startsWith(xbusHome + "/lib/"));
 }

 @Test
 public void testCreateClassLoaderWithNullXBUSHome() {
 assertNull(Constants.XBUS_HOME);
 try {
 XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testCreateClassLoaderWithEmptyXBUSHome() {
 Constants.XBUS_HOME = "";
 try {
 XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testCreateClassLoaderWithInvalidDirName() {
 Constants.XBUS_HOME = "C:/XBus/home";
 try {
 XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testCreateClassLoaderWithInvalidJarFile() {
 Constants.XBUS_HOME = "C:/XBus/home";
 File libPath = new File(Constants.XBUS_HOME + "/lib");
 File invalidJarFile = new File(libPath, "invalid.txt");
 File[] jars = libPath.listFiles();
 assertTrue(jars.length > 0);
 try {
 XBUSClassLoader.addUrls(jars, invalidJarFile.getAbsolutePath());
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testCreateClassLoaderWithInvalidJarFileExtension() {
 Constants.XBUS_HOME = "C:/XBus/home";
 File libPath = new File(Constants.XBUS_HOME + "/lib");
 File invalidJarFile = new File(libPath, "invalid.txt");
 File[] jars = libPath.listFiles();
 assertTrue(jars.length > 0);
 try {
 XBUSClassLoader.addUrls(jars, invalidJarFile.getAbsolutePath());
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testCreateClassLoaderWithMismatchedJarFile() {
 Constants.XBUS_HOME = "C:/XBus/home";
 File libPath = new File(Constants.XBUS_HOME + "/lib");
 File invalidJarFile = new File(libPath, "invalid.jarx");
 File[] jars = libPath.listFiles();
 assertTrue(jars.length > 0);
 try {
 XBUSClassLoader.addUrls(jars, invalidJarFile.getAbsolutePath());
 fail();
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testCreateClassLoaderWithMalformedURL() {
 Constants.XBUS_HOME = "C:/XBus/home";
 File libPath = new File(Constants.XBUS_HOME + "/lib");
 File jarFile = new File(libPath, "xbus.jar");
 File[] jars = libPath.listFiles();
 assertTrue(jars.length > 0);
 try {
 XBUSClassLoader.addUrls(jars, "invalid:jar");
 fail();
 } catch (MalformedURLException e) {
 // expected
 }
 }
}