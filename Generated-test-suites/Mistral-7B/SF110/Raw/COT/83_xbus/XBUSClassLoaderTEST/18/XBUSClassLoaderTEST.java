package net.sf.xbus.base.core.reflection.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.XBUSHome;
import net.sf.xbus.base.core.trace.Trace;

import net.sf.xbus.base.core.reflection.XBUSClassLoader;

public class XBUSClassLoaderTest {

 private static ClassLoader parent;
 private static XBUSClassLoader xbusClassLoader;

 @BeforeClass
 public static void setup() {
 System.setProperty("XBUS_HOME", System.getProperty("user.dir") + File.separator + "testData");
 parent = XBUSClassLoader.class.getClassLoader();
 }

 // Test for getInstance method
 @Test
 public void testGetInstance() {
 XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader xbusClassLoader2 = XBUSClassLoader.getInstance(parent);
 assertEquals(xbusClassLoader1, xbusClassLoader2);
 }

 // Test for createClassLoader method
 @Test
 public void testCreateClassLoader() {
 xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(xbusClassLoader);
 }

 // Test for addUrls method with valid directory
 @Test
 public void testAddUrlsValidDirectory() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "testData/lib");
 assertEquals(3, urls.size());
 }

 // Test for addUrls method with invalid directory
 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsInvalidDirectory() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "testData/test");
 }

 // Test for addUrls method with null directory
 @Test(expected = NullPointerException.class)
 public void testAddUrlsNullDirectory() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, null);
 }

 // Test for addUrls method with invalid file
 @Test(expected = MalformedURLException.class)
 public void testAddUrlsInvalidFile() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, new File("testData/testFile").toURL());
 }

 // Test for addUrls method with empty directory
 @Test
 public void testAddUrlsEmptyDirectory() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "testData/emptyDir");
 assertEquals(0, urls.size());
 }

 // Test for constructor with null urlArray and valid parent
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullUrlArray() {
 XBUSClassLoader xbusClassLoader = new XBUSClassLoader(null, parent);
 }

 // Test for constructor with null urlArray and null parent
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullUrlArrayAndParent() {
 XBUSClassLoader xbusClassLoader = new XBUSClassLoader(null, null);
 }

 // Test for constructor with valid urlArray and null parent
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorValidUrlArrayAndNullParent() {
 URL[] urlArray = new URL[1];
 urlArray[0] = new File("testData/testFile").toURL();
 XBUSClassLoader xbusClassLoader = new XBUSClassLoader(urlArray, null);
 }

 // Test for constructor with valid urlArray and valid parent
 @Test
 public void testConstructorValidUrlArrayAndParent() {
 URL[] urlArray = new URL[1];
 urlArray[0] = new File("testData/testFile").toURL();
 XBUSClassLoader xbusClassLoader = new XBUSClassLoader(urlArray, parent);
 assertNotNull(xbusClassLoader);
 }
}