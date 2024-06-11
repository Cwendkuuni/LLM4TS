package net.sf.xbus.base.core.reflection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XBUSClassLoaderTest {

 private static ClassLoader parentClassLoader;
 private static XBUSClassLoader xbusClassLoader;

 @Before
 public void setUp() {
 System.setProperty("XBUS_HOME", System.getProperty("user.dir") + File.separator + "testData");
 parentClassLoader = getClass().getClassLoader();
 xbusClassLoader = XBUSClassLoader.getInstance(parentClassLoader);
 }

 @After
 public void tearDown() {
 System.clearProperty("XBUS_HOME");
 xbusClassLoader = null;
 parentClassLoader = null;
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.getInstance(parentClassLoader);
 assertNotNull(xbusClassLoader1);
 assertNotSame(xbusClassLoader, xbusClassLoader1);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.createClassLoader(parentClassLoader);
 XBUSClassLoader xbusClassLoader2 = XBUSClassLoader.createClassLoader(parentClassLoader);
 assertNotNull(xbusClassLoader1);
 assertNotNull(xbusClassLoader2);
 assertNotSame(xbusClassLoader, xbusClassLoader1);
 assertNotSame(xbusClassLoader1, xbusClassLoader2);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = System.getProperty("XBUS_HOME") + "/testData/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 for (URL url : urlArray) {
 assertNotNull(url);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullParent() {
 XBUSClassLoader.getInstance(null);
 }

 @Test
 public void testCreateClassLoaderWithNullParent() {
 try {
 XBUSClassLoader.createClassLoader(null);
 } catch (NullPointerException e) {
 // expected
 }
 }
}