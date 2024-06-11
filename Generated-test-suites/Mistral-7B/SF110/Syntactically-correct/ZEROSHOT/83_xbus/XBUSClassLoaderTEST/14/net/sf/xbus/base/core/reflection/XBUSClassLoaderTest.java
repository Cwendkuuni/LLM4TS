package net.sf.xbus.base.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.Trace;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import java.util.jar.JarFile;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader mClassLoader;
 private static Class<?> testClass;

 @BeforeClass
 public static void setUpBeforeClass() {
 mClassLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 try {
 testClass = mClassLoader.loadClass("net.sf.xbus.base.core.reflection.XBUSClassLoaderTest");
 } catch (ClassNotFoundException e) {
 Trace.error("Failed to load test class: " + e.getMessage());
 System.exit(1);
 }
 }

 @AfterClass
 public static void tearDownAfterClass() {
 mClassLoader = null;
 }

 @Test
 public void testInstance() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertSame(classLoader1, classLoader2);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 String xbusHome = System.getProperty("XBUS_HOME");
 assertTrue(xbusHome != null);

 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, xbusHome + "/lib");
 assertEquals(3, urls.size());

 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }

 File libPath = new File(xbusHome + "/lib");
 File[] jars = libPath.listFiles();
 for (int i = 0; jars != null && i < jars.length; ++i) {
 if (jars[i].isFile()) {
 if (!jars[i].getName().endsWith("jar")) {
 continue;
 }
 try {
 URL url = jars[i].toURL();
 URL expectedUrl = urlArray[i];
 assertEquals(expectedUrl.toString(), url.toString());
 } catch (MalformedURLException e) {
 Trace.error("Failed to convert file to URL: " + e.getMessage());
 System.exit(1);
 }
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateClassloaderWhenXBUSHomeIsNull() {
 Constants.XBUS_HOME = null;
 XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 fail("IllegalStateException expected but not thrown");
 }
}