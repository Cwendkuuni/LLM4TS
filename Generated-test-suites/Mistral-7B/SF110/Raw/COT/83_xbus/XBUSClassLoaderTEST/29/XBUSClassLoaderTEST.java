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

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidUrl() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/invalid/path");
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullParent() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 }

 @Test
 public void testGetStaticInstance() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 Assert.assertEquals(classLoader1, classLoader2);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInstanceAfterSet() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader.mClassLoader = new XBUSClassLoader(new URL[0], classLoader);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testIsInstance() {
 Class<?> clazz = XBUSClassLoader.class;
 Assert.assertTrue(XBUSClassLoader.isInstance(clazz));
 }

 @Test
 public void testIsInstanceFalse() {
 Class<?> clazz = String.class;
 Assert.assertFalse(XBUSClassLoader.isInstance(clazz));
 }
}