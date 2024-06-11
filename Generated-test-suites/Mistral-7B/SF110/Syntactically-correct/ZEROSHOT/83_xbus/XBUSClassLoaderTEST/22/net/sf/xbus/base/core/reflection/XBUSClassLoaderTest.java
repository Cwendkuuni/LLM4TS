package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader xbusClassLoader;
 private static final String XBUS_HOME = "path/to/xbus/home";

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 xbusClassLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testInstance() {
 XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertEquals(xbusClassLoader, xbusClassLoader1);
 }

 @Test
 public void testCreateClassLoader() throws Exception {
 XBUSClassLoader xbusClassLoader2 = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 assertEquals(xbusClassLoader, xbusClassLoader2);
 }

 @Test
 public void testGetClass() throws Exception {
 Class<?> clazz = xbusClassLoader.findClass("net.sf.xbus.base.core.trace.Trace");
 assertNotNull(clazz);
 assertEquals(Trace.class, clazz.asSubclassOf(Object.class));
 }

 @Test
 public void testAddUrls() throws Exception {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 XBUSClassLoader xbusClassLoader3 = new XBUSClassLoader(urlArray, XBUSClassLoader.class.getClassLoader());
 Class<?> clazz2 = xbusClassLoader3.findClass("net.sf.xbus.base.core.trace.Trace");
 assertNotNull(clazz2);
 assertEquals(Trace.class, clazz2.asSubclassOf(Object.class));
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedURLException() throws Exception {
 XBUSClassLoader.addUrls(null, XBUS_HOME + "/lib");
 }

 @Test
 public void testClassLoader() {
 assertNotNull(XBUSClassLoader.mClassLoader);
 }
}