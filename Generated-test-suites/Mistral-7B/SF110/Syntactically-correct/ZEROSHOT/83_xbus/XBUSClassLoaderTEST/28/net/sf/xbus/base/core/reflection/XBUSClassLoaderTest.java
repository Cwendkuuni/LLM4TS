package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace; // Assuming Trace is a static class, if not, remove this import and use a mock or stub

public class XBUSClassLoaderTest {

 private static XBUSClassLoader xbusClassLoader;

 @BeforeClass
 public static void setup() {
 XBUSClassLoader.mClassLoader = null;
 xbusClassLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertNotNull(classLoader);
 assertEquals(XBUSClassLoader.class, classLoader.getClass());
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 assertEquals(XBUSClassLoader.class, classLoader.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderWithNullParent() {
 XBUSClassLoader.createClassLoader(null);
 fail("Expected IllegalArgumentException but got none");
 }

 @Test
 public void testAddUrls() {
 Vector<URL> urls = new Vector<>();
 String xbusHome = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
 XBUSClassLoader.addUrls(urls, xbusHome);
 assertFalse(urls.isEmpty());
 for (URL url : urls) {
 assertNotNull(url);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithNullDirName() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, null);
 fail("Expected IllegalArgumentException but got none");
 }

 @Test
 public void testGetInstanceWithNullParent() {
 try {
 XBUSClassLoader.getInstance(null);
 fail("Expected IllegalStateException but got none");
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}