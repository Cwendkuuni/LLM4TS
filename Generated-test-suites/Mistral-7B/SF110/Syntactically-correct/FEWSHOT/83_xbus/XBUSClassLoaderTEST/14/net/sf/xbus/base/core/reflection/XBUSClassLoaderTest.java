package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import org.junit.Test;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home"; // Set the XBUS_HOME system property before running the test

 @Test
 public void testInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() throws IOException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 assertTrue(urls.size() > 0);
 for (Object url : urls) {
 assertTrue(url instanceof URL);
 }
 }

 @Test
 public void testCreateClassLoaderWithInvalidDir() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 assertThrows(IllegalArgumentException.class, () -> XBUSClassLoader.createClassLoader(parent, "invalid_dir"));
 }

 @Test
 public void testCreateClassLoaderWithNullParent() {
 assertThrows(NullPointerException.class, () -> XBUSClassLoader.createClassLoader(null));
 }

 @Test
 public void testGetInstanceWithNullParent() {
 assertThrows(NullPointerException.class, () -> XBUSClassLoader.getInstance(null));
 }

 @Test
 public void testGetInstanceTwice() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
 assertEquals(classLoader1.hashCode(), classLoader2.hashCode());
 }
}