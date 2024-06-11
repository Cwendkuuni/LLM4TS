package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static final String XBUS_HOME = "path/to/xbus/home";

 @BeforeClass
 public static void setUpBeforeClass() {
 Constants.setXBUS_HOME(XBUS_HOME);
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Reset XBUS_HOME to its original value
 Constants.setXBUS_HOME(null);
 }

 @Test
 public void testInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
 assertNotNull(instance);
 assertEquals(parent, instance.getParent());
 }

 @Test
 public void testCreateClassLoaderUrls() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib/runtime");
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/plugin/lib");
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/test/lib");
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, XBusClassLoader.class.getClassLoader());
 assertNotNull(classLoader);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderNullXBUS_HOME() {
 Constants.setXBUS_HOME(null);
 try {
 XBUSClassLoader.createClassLoader(XBusClassLoader.class.getClassLoader());
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateClassLoaderTwice() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(XBusClassLoader.class.getClassLoader());
 XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(XBusClassLoader.class.getClassLoader());
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.trace.Trace";
 Class<?> clazz = classLoader.loadClass(className);
 assertNotNull(clazz);
 assertEquals(className, clazz.getName());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadClassNotFound() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$NotAClass";
 Class<?> clazz = classLoader.loadClass(className);
 fail("Should not be able to load non-existent class");
 }

 @Test
 public void testGetParent() {
 ClassLoader parent = XBusClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertEquals(parent, classLoader.getParent());
 }
}