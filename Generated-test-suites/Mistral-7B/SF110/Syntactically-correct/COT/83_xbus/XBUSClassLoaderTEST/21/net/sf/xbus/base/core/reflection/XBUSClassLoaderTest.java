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

 private static final String XBUS_HOME = "path/to/xbus/home"; // replace with valid XBUS_HOME
 private static final ClassLoader parent = XBUSClassLoader.class.getClassLoader();

 @Test
 public void testGetInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 Assert.assertEquals(3, urls.size()); // assuming 3 jars in lib folder
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWithNullParent() {
 XBUSClassLoader.createClassLoader(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderWithEmptyUrlVector() {
 XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], parent);
 Assert.assertNull(classLoader);
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidUrl() {
 Vector urls = new Vector<>();
 Assert.assertThrows(MalformedURLException.class, () -> XBUSClassLoader.addUrls(urls, "invalid_url"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullDirName() {
 Vector urls = new Vector<>();
 Assert.assertThrows(NullPointerException.class, () -> XBUSClassLoader.addUrls(urls, null));
 }

 @Test
 public void testgetInstanceWithValidXBUS_HOME() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader anotherInstance = XBUSClassLoader.getInstance(parent);
 Assert.assertSame(classLoader, anotherInstance);
 }

 @Test
 public void testgetInstanceWithInvalidXBUS_HOME() {
 Constants.XBUS_HOME = null;
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNull(classLoader);
 }

}