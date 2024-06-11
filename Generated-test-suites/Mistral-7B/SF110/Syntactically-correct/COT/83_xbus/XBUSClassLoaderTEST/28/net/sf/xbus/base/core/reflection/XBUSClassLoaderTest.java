package net.sf.xbus.base.core.reflection.test;

import org.junit.Assert;
import org.junit.Test;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;
import net.sf.xbus.base.core.trace.Trace;
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home";

 @Test
 public void testGetInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 Assert.assertEquals(4, urls.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullDirName() {
 XBUSClassLoader.addUrls(null, XBUS_HOME + "/lib");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithInvalidDirName() {
 XBUSClassLoader.addUrls(new Vector<>(), "invalid_dir_name");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidJarFile() {
 Vector urls = new Vector<>();
 File invalidJarFile = new File("path/to/invalid/jar");
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 urls.add(invalidJarFile.toURL());
 }

 @Test(expected = MalformedURLException.class)
 public void testGetInstanceWithNullParent() {
 ClassLoader nullParent = null;
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(nullParent);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWithNullParent() {
 ClassLoader nullParent = null;
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(nullParent);
 }

 @Test
 public void testInstanceIsSingleton() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
 Assert.assertSame(classLoader1, classLoader2);
 }
}