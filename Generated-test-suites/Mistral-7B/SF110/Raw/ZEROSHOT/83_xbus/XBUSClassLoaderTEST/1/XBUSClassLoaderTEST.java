package net.sf.xbus.base.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static final String XBUS_HOME = "path/to/xbus/home";

 @BeforeClass
 public static void setUpBeforeClass() {
 Constants.XBUS_HOME = XBUS_HOME;
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @AfterClass
 public static void tearDownAfterClass() {
 XBUSClassLoader.mClassLoader = null;
 }

 @Test
 public void testInstance() {
 Class<?> clazz = classLoader.getClass();
 assertEquals("XBUSClassLoader", clazz.getName());
 }

 @Test
 public void testCreateClassLoader() {
 Class<?> parentClass = ClassLoader.class;
 Class<?> clazz = null;
 try {
 clazz = classLoader.loadClass("java.lang.Integer", parentClass);
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 fail("Class not found exception should not be thrown.");
 }
 assertEquals("java.lang.Integer", clazz.getName());
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader loader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader loader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertTrue(loader1 == loader2);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = XBUS_HOME + "/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 assertEquals(4, urls.size());
 }

 @Test
 public void testAddUrlsWithInvalidDir() {
 Vector urls = new Vector();
 String dirName = XBUS_HOME + "/non_existent_dir";
 XBUSClassLoader.addUrls(urls, dirName);
 assertEquals(0, urls.size());
 }

 @Test
 public void testAddUrlsWithInvalidFile() {
 Vector urls = new Vector();
 String dirName = XBUS_HOME + "/lib";
 File libPath = new File(dirName);
 File[] jars = libPath.listFiles();
 for (File jar : jars) {
 if (jar.getName().equals("invalid_file.txt")) {
 continue;
 }
 URL url = jar.toURL();
 try {
 urls.add(url);
 } catch (MalformedURLException e) {
 fail("MalformedURLException should not be thrown.");
 }
 }
 assertEquals(3, urls.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullDirName() {
 Vector urls = new Vector();
 String dirName = null;
 XBUSClassLoader.addUrls(urls, dirName);
 fail("NullPointerException should be thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithEmptyDirName() {
 Vector urls = new Vector();
 String dirName = "";
 XBUSClassLoader.addUrls(urls, dirName);
 fail("IllegalArgumentException should be thrown.");
 }
}