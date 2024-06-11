package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader mClassLoader;

 @BeforeClass
 public static void setUpBeforeClass() {
 mClassLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
 }

 @AfterClass
 public static void tearDownAfterClass() {
 mClassLoader = null;
 }

 @Test
 public void testInstance() {
 XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
 assertNotNull(xbusClassLoader);
 assertEquals(XBUSClassLoader.class, xbusClassLoader.getClass());
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = ClassLoader.getSystemClassLoader();
 XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(xbusClassLoader);
 assertEquals(XBUSClassLoader.class, xbusClassLoader.getClass());
 }

 @Test
 public void testGetInstanceAlreadyInitialized() {
 ClassLoader parent = ClassLoader.getSystemClassLoader();
 XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader xbusClassLoader2 = XBUSClassLoader.getInstance(parent);
 assertSame(xbusClassLoader1, xbusClassLoader2);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 String xbusHome = System.getProperty("xbus.home");
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, xbusHome + "/lib");
 List<URL> urlList = new ArrayList<>(urls);
 List<String> expectedUrls = Arrays.asList(
 new String[]{
 xbusHome + "/lib/",
 xbusHome + "/lib/runtime/",
 xbusHome + "/plugin/lib/",
 xbusHome + "/test/lib/"
 });
 for (int i = 0; i < urlList.size(); ++i) {
 String urlString = urlList.get(i).toString();
 assertTrue(expectedUrls.contains(urlString.substring(0, urlString.length() - 1)));
 }
 }

 @Test
 public void testCreateClassLoaderWithNullXBUS_HOME() {
 Constants.XBUS_HOME = null;
 try {
 XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
 fail("Expected an error to be thrown when XBUS_HOME is not set.");
 } catch (RuntimeException e) {
 // Expected exception
 }
 Constants.XBUS_HOME = "";
 try {
 XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
 fail("Expected an error to be thrown when XBUS_HOME is an empty string.");
 } catch (RuntimeException e) {
 // Expected exception
 }
 Constants.XBUS_HOME = ".";
 try {
 XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
 fail("Expected an error to be thrown when XBUS_HOME is a relative path.");
 } catch (RuntimeException e) {
 // Expected exception
 }
 }
}