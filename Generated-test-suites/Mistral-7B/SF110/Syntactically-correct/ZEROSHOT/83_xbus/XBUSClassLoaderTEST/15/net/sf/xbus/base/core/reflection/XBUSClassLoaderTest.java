package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.Trace;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classUnderTest;
 private static ClassLoader parent;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 parent = XBUSClassLoader.class.getClassLoader();
 classUnderTest = XBUSClassLoader.getInstance(parent);
 }

 @Test
 public void testInstance() {
 XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
 assertNotSame(classUnderTest, instance);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader instance = XBUSClassLoader.createClassLoader(parent);
 assertSame(classUnderTest, instance);
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader instance1 = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader instance2 = XBUSClassLoader.getInstance(parent);
 assertSame(instance1, instance2);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = "temp";
 XBUSClassLoader.addUrls(urls, dirName);
 assertFalse(urls.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsNullDirName() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsEmptyDir() {
 Vector urls = new Vector();
 File libPath = new File("");
 XBUSClassLoader.addUrls(urls, libPath.getAbsolutePath());
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetClass() throws ClassNotFoundException {
 String className = "java.lang.String";
 Class<?> expected = String.class;
 Class<?> actual = classUnderTest.loadClass(className);
 assertEquals(expected, actual);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetClassNotFound() throws ClassNotFoundException {
 String className = "net.sf.xbus.NonExistentClass";
 Class<?> actual = classUnderTest.loadClass(className);
 fail("ClassNotFoundException expected but not thrown");
 }
}