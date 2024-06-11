package net.sf.xbus.base.core.reflection.test;

import org.junit.Assert;
import org.junit.Test;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.reflection.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "C:/XBusHome"; // Assuming a valid XBUS_HOME for testing purpose

 @Test
 public void testGetInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() throws MalformedURLException {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotNull(classLoader);

 Vector urls = new Vector();
 urls.add(new File(XBUS_HOME + "/lib").toURL());
 urls.add(new File(XBUS_HOME + "/lib/runtime").toURL());
 urls.add(new File(XBUS_HOME + "/plugin/lib").toURL());
 urls.add(new File(XBUS_HOME + "/test/lib").toURL());

 XBUSClassLoader expectedClassLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);

 Assert.assertEquals(expectedClassLoader.getURLs().length, classLoader.getURLs().length);
 for (int i = 0; i < expectedClassLoader.getURLs().length; i++) {
 Assert.assertEquals(expectedClassLoader.getURLs()[i], classLoader.getURLs()[i]);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderNullParent() {
 ClassLoader parent = null;
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderEmptyUrls() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], parent);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = XBUS_HOME;
 XBUSClassLoader.addUrls(urls, dirName);

 File libPath = new File(dirName);
 File[] jars = libPath.listFiles();
 int expectedJarsCount = libPath.listFiles().length;

 for (URL url : urls) {
 String urlString = url.toString();
 int index = urlString.lastIndexOf('/');
 String jarName = urlString.substring(index + 1);
 int count = 0;
 for (File jarFile : jars) {
 if (jarFile.getName().equals(jarName)) {
 count++;
 break;
 }
 }
 Assert.assertEquals(expectedJarsCount, urls.size());
 Assert.assertEquals(1, count);
 }
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsInvalidUrl() {
 Vector urls = new Vector();
 String dirName = XBUS_HOME;
 XBUSClassLoader.addUrls(urls, dirName);

 URL invalidUrl = new URL("invalid://example.com");
 urls.add(invalidUrl);
 }

 @Test
 public void testStaticInitializer() {
 XBUSClassLoader classLoader = null;
 assertNull(XBUSClassLoader.mClassLoader);
 }

}