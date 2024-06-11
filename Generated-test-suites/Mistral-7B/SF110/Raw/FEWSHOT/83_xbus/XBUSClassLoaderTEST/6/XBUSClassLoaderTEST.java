package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "test/resources/xbus-home";
 private static final String XBUS_CLASSLOADER_URL_0 = "file:test/resources/xbus-home/lib/SomeClass-1.0.jar";
 private static final String XBUS_CLASSLOADER_URL_1 = "file:test/resources/xbus-home/lib/SomeClass-2.0.jar";
 private static final String XBUS_CLASSLOADER_URL_2 = "file:test/resources/xbus-home/plugin/lib/SomeClass-3.0.jar";
 private static final String XBUS_CLASSLOADER_URL_3 = "file:test/resources/xbus-home/test/lib/SomeClass-4.0.jar";

 private List<URL> urls;
 private XBUSClassLoader classLoader;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 urls = new ArrayList<>();
 addUrls(urls, XBUS_HOME);
 classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 }

 @Test
 public void testInstance() {
 assertEquals(getClass().getClassLoader(), classLoader.getParent());
 assertEquals(urls.toArray(new URL[0]), classLoader.getURLs());
 }

 @Test
 public void testCreateClassLoader() {
 verify(Constants.class, times(1)).getXBUS_HOME();
 List<URL> urls = new ArrayList<>();
 addUrls(urls, XBUS_HOME);
 XBUSClassLoader instance = createClassLoader(getClass().getClassLoader(), urls.toArray(new URL[0]));
 assertEquals(getClass().getClassLoader(), instance.getParent());
 assertEquals(urls.toArray(new URL[0]), instance.getURLs());
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertEquals(getClass().getClassLoader(), instance.getParent());
 assertEquals(urls.toArray(new URL[0]), instance.getURLs());
 }

 @Test
 public void testAddUrls() {
 File libPath = new File(XBUS_HOME);
 File[] jars = libPath.listFiles();
 int expectedSize = 4;
 int actualSize = 0;
 for (File jar : jars) {
 if (jar != null) {
 String name = jar.getName();
 if (name.endsWith("jar") || name.endsWith("zip")) {
 try {
 urls.add(jar.toURL());
 actualSize++;
 } catch (MalformedURLException e) {
 fail("Unexpected MalformedURLException: " + e.getMessage());
 }
 }
 }
 }
 assertEquals(expectedSize, urls.size());
 assertEquals(expectedSize, actualSize);
 }

 private static XBUSClassLoader createClassLoader(ClassLoader parent, URL[] urls) {
 return new XBUSClassLoader(urls, parent);
 }
}