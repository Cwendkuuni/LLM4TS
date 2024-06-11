package net.sf.xbus.base.core.reflection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static ClassLoader parent;

 @BeforeClass
 public static void setUpBeforeClass() {
 parent = XBUSClassLoader.class.getClassLoader();
 classLoader = XBUSClassLoader.getInstance(parent);
 }

 @Test
 public void testInstance() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
 Assert.assertNotSame(classLoader, classLoader1);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.createClassLoader(parent);
 XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotSame(classLoader1, classLoader2);
 }

 @Test
 public void testAddUrls() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "testPath");

 int expectedSize = 3; // Assuming there are 3 JAR files in the testPath directory
 int actualSize = urls.size();

 Assert.assertEquals("Number of URLs added should be correct.", expectedSize, actualSize);
 }

 @Test
 public void testGetInstanceWithNullXBUS_HOME() {
 XBUSClassLoader classLoader = null;
 try {
 classLoader = XBUSClassLoader.getInstance(parent);
 } catch (Exception e) {
 // Expected
 }

 Assert.assertNull("XBUSClassLoader should be null if XBUS_HOME is not set.", classLoader);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithNullDirName() {
 XBUSClassLoader.addUrls(null, "testPath");
 }
}