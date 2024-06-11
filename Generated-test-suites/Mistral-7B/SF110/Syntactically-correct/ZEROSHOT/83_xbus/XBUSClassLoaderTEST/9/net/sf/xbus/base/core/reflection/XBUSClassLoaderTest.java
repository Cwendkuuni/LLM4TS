package net.sf.xbus.base.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classUnderTest;
 private static final String XBUS_HOME = "path/to/xbus/home"; // Set this to your XBUS_HOME path

 @BeforeClass
 public static void setUpClass() {
 Constants.XBUS_HOME = XBUS_HOME;
 classUnderTest = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @AfterClass
 public static void tearDownClass() {
 XBUSClassLoader.mClassLoader = null;
 }

 @Test
 public void testInstance() {
 Class<?> actual = classUnderTest.loadClass("java.lang.String");
 assertEquals("java.lang.String", actual.getName());
 }

 @Test
 public void testGetInstance() {
 Class<?> actual = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader()).loadClass("java.lang.String");
 assertEquals("java.lang.String", actual.getName());
 }

 @Test
 public void testCreateClassLoader() {
 Class<?> actual = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader()).loadClass("java.lang.String");
 assertEquals("java.lang.String", actual.getName());
 }

 @Test
 public void testAddUrls() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 assertTrue(urls.size() > 0);
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsMalformedURL() {
 Vector<URL> urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "invalid_url");
 fail("MalformedURLException expected but not thrown");
 }

 @Test
 public void testLoadClassesFromDifferentDirectories() throws ClassNotFoundException {
 Class<?> class1 = classUnderTest.loadClass("net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClass1");
 Class<?> class2 = classUnderTest.loadClass("net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClass2");
 assertNotNull(class1);
 assertNotNull(class2);
 }

 private static class TestClass1 {
 }

 private static class TestClass2 {
 }
}