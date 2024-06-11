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

 private static XBUSClassLoader classLoader;

 @BeforeClass
 public static void setup() {
 Constants.setXBUS_HOME("target/test-classes/resources");
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @AfterClass
 public static void teardown() {
 XBUSClassLoader.mClassLoader = null;
 }

 @Test
 public void testGetInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
 assertSame(classLoader1, classLoader2);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotSame(getClass().getClassLoader(), classLoader);
 assertSame(parent, classLoader.getParent());
 }

 @Test
 public void testAddUrls() throws IOException {
 Vector urls = new Vector<>();
 String dirName = "src/test/resources/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 Class<?> clazz = classLoader.loadClass("net.sf.xbus.test.TestClass", urlArray[0]);
 assertNotNull(clazz);
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidDir() {
 Vector urls = new Vector<>();
 String dirName = "src/test/resources/libWithInvalidDir";
 XBUSClassLoader.addUrls(urls, dirName);
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullDir() {
 Vector urls = new Vector<>();
 String dirName = null;
 XBUSClassLoader.addUrls(urls, dirName);
 }

 @Test
 public void testStaticInitialization() {
 assertNull(XBUSClassLoader.mClassLoader);
 }
}