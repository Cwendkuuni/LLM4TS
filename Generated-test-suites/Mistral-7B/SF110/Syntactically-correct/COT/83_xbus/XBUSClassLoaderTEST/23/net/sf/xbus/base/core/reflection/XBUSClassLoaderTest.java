package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import static net.sf.xbus.base.core.Constants.*;
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;

 @Test
 public void testGetInstance() {
 classLoader = XBUSClassLoader.getInstance(null);
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 classLoader = XBUSClassLoader.createClassLoader(null);
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 assertTrue(urls.size() > 0);
 for (int i = 0; i < urls.size(); ++i) {
 URL url = urls.elementAt(i);
 assertTrue(url.toString().startsWith(XBUS_HOME + "/lib"));
 }
 }

 @Test
 public void testGetInstanceWithParent() {
 ClassLoader parent = new URLClassLoader(new URL[]{}, null);
 classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullParent() {
 classLoader = XBUSClassLoader.getInstance(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderWithNullParent() {
 classLoader = XBUSClassLoader.createClassLoader(null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreateClassLoaderEmptyUrls() {
 Vector urls = new Vector();
 classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), null);
 assertNotNull(classLoader);
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsInvalidUrl() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib/invalidUrl");
 fail("Expected MalformedURLException");
 }

 @Test
 public void testCreateClassLoaderWithEmptyHome() {
 Constants.XBUS_HOME = "";
 classLoader = XBUSClassLoader.createClassLoader(null);
 assertNull(classLoader);
 }

 @Test
 public void testCreateClassLoaderWithNullHome() {
 Constants.XBUS_HOME = null;
 classLoader = XBUSClassLoader.createClassLoader(null);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testCreateClassLoaderWithInvalidHome() {
 Constants.XBUS_HOME = "invalidHome";
 try {
 classLoader = XBUSClassLoader.createClassLoader(null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }
}