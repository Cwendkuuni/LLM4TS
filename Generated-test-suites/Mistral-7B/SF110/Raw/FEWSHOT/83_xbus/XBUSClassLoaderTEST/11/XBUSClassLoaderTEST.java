package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

 private static URL[] urlArray;
 private static ClassLoader parent;
 private static XBUSClassLoader classLoader;

 @BeforeClass
 public static void setup() {
 Constants stubConstants = mock(Constants.class);
 when(stubConstants.XBUS_HOME).thenReturn("/path/to/xbus/home");
 parent = mock(ClassLoader.class);
 urlArray = new URL[]{new File("/path/to/xbus/home/lib").toURL(),
 new File("/path/to/xbus/home/lib/runtime").toURL(),
 new File("/path/to/xbus/home/plugin/lib").toURL(),
 new File("/path/to/xbus/home/test/lib").toURL()};
 }

 @Test
 public void testInstance() {
 classLoader = XBUSClassLoader.getInstance(parent);
 assertEquals("XBUSClassLoader instance should not be null", classLoader, XBUSClassLoader.getInstance(parent));
 }

 @Test
 public void testCreateClassLoader() {
 classLoader = XBUSClassLoader.createClassLoader(parent);
 assertEquals("XBUSClassLoader instance should not be null", classLoader, XBUSClassLoader.getInstance(parent));
 }

 @Test
 public void testGetInstanceWithNullXBUS_HOME() {
 Constants stubConstantsNullXBUS_HOME = mock(Constants.class);
 when(stubConstantsNullXBUS_HOME.XBUS_HOME).thenReturn(null);
 assertNull("XBUSClassLoader instance should be null when XBUS_HOME is not set", XBUSClassLoader.getInstance(parent));
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "/path/to/lib");
 assertEquals("Number of URLs added should be 4", 4, urls.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullVector() {
 XBUSClassLoader.addUrls(null, "/path/to/lib");
 }

 @Test
 public void testGetInstanceWithNullParent() {
 assertNull("XBUSClassLoader instance should be null when parent is null", XBUSClassLoader.getInstance(null));
 }

 @Test
 public void testGetInstanceWithNonExistentXBUS_HOME() {
 Constants stubConstantsNonExistentXBUS_HOME = mock(Constants.class);
 when(stubConstantsNonExistentXBUS_HOME.XBUS_HOME).thenReturn("");
 assertNull("XBUSClassLoader instance should be null when XBUS_HOME does not exist", XBUSClassLoader.getInstance(parent));
}

 @Test
 public void testGetClass() throws Exception {
 String className = "net.sf.xbus.test.TestClass";
 Class<?> testClass = classLoader.loadClass(className);
 assertNotNull("Class should not be null", testClass);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetClassWithNonExistentClassName() throws Exception {
 String className = "net.sf.xbus.test.NonExistentClass";
 classLoader.loadClass(className);
 fail("ClassNotFoundException should be thrown");
 }

 @Test
 public void testGetParent() {
 assertEquals("Parent class loader should be the one passed in", parent, classLoader.getParent());
 }
}