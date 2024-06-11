package net.sf.xbus.base.core.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus_home";

 @Test
 public void testGetInstance() {
 ClassLoader parent = this.getClass().getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = this.getClass().getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 Assert.assertEquals(3, urls.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithNullDirName() {
 XBUSClassLoader.addUrls(null, XBUS_HOME + "/lib");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidJarFile() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib/invalid_file.txt");
 }

 @Test
 public void testGetInstanceWithNullParent() {
 Assert.assertNull(XBUSClassLoader.getInstance(null));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWithNullParent() {
 XBUSClassLoader.createClassLoader(null);
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullUrls() {
 XBUSClassLoader.addUrls(null, XBUS_HOME + "/lib");
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullDirNameAndNullUrls() {
 XBUSClassLoader.addUrls(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInstanceWithInvalidXBUS_HOME() {
 System.setProperty(Constants.XBUS_HOME, "invalid_path");
 ClassLoader parent = this.getClass().getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNull(classLoader);
 }
}