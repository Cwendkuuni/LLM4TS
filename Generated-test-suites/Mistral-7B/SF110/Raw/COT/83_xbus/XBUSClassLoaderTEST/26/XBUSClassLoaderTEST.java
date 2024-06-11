package net.sf.xbus.base.core.reflection.test;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.reflection.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;

 @BeforeClass
 public static void setup() {
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 // Test for constructor
 @Test
 public void testXBUSClassLoader() {
 XBUSClassLoader xbusClassLoader = new XBUSClassLoader(new URL[]{}, null);
 assertNotNull(xbusClassLoader);
 }

 // Test for getInstance method
 @Test
 public void testGetInstance() {
 assertNotNull(classLoader);
 }

 // Test for createClassLoader method (edge case: null parent)
 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderNullParent() {
 XBUSClassLoader.createClassLoader(null);
 }

 // Test for addUrls method (edge case: null urls, null dirName)
 @Test(expected = NullPointerException.class)
 public void testAddUrlsNullParams() {
 XBUSClassLoader.addUrls(null, null);
 }

 // Test for addUrls method (edge case: null jars, null dirName)
 @Test(expected = NullPointerException.class)
 public void testAddUrlsNullJars() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, null);
 }

 // Test for addUrls method (edge case: null jars, empty dirName)
 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsEmptyDirName() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "");
 }

 // Test for addUrls method (edge case: non-directory)
 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsNonDirectory() {
 Vector<URL> urls = new Vector<>();
 File libPath = new File("non_existing_file.txt");
 XBUSClassLoader.addUrls(urls, libPath.getAbsolutePath());
 }

 // Test for addUrls method (edge case: invalid jar file)
 @Test(expected = MalformedURLException.class)
 public void testAddUrlsInvalidJarFile() {
 Vector<URL> urls = new Vector<>();
 File libPath = new File("non_existing_file");
 URL url = libPath.toURL();
 XBUSClassLoader.addUrls(urls, libPath.getAbsolutePath());
 urls.add(url);
 }

 // Test for addUrls method (edge case: malformed url in the vector)
 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsMalformedUrl() {
 Vector<URL> urls = new Vector<>();
 URL malformedUrl = new URL("malformed://example.com");
 urls.add(malformedUrl);
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 }

 // Test for createClassLoader method
 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader newInstance = XBUSClassLoader.createClassLoader(classLoader);
 assertNotNull(newInstance);
 assertNotSame(classLoader, newInstance);
 }
}