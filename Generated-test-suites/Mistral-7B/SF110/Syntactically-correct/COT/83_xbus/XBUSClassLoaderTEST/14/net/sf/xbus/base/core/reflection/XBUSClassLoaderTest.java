package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.reflection.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home"; // Set this to your XBUS_HOME directory

 @Test
 public void testGetInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = getClass().getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 assertTrue(urls.size() >= 3); // Assuming at least 3 URLs in the lib directory
 }

 @Test
 public void testGetInstanceWithNullXBUS_HOME() {
 assertNull(XBUSClassLoader.getInstance(getClass().getClassLoader()));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWithNullParent() {
 XBUSClassLoader.createClassLoader(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderWithEmptyUrlArray() {
 URL[] urlArray = new URL[0];
 assertThrows(IllegalArgumentException.class, () -> new XBUSClassLoader(urlArray, getClass().getClassLoader()));
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidUrl() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "invalid_url");
 fail("Expected MalformedURLException not thrown");
 }

 @Test
 public void testGetMClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 XBUSClassLoader mClassLoader = XBUSClassLoader.mClassLoader;
 assertEquals(classLoader, mClassLoader);
 }

}