package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

import net.sf.xbus.base.core.Constants;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader mClassLoader;

 @Test
 public void testGetInstance() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());

 assertSame(classLoader1, classLoader2);
 }

 @Test
 public void testCreateClassLoader() {
 mClassLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderNullParent() {
 XBUSClassLoader.createClassLoader(null);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 String dirName = System.getProperty("user.dir");
 XBUSClassLoader.addUrls(urls, dirName);

 URL url = new File(dirName, "test.jar").toURL();
 urls.add(url);

 assertNotNull(urls);
 assertEquals(1, urls.size());
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsMalformedURL() {
 Vector urls = new Vector();
 String dirName = System.getProperty("user.dir");
 XBUSClassLoader.addUrls(urls, dirName + "/test");
 }

 @Test
 public void testGetInstanceWithNullParent() {
 try {
 XBUSClassLoader.getInstance(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGetInstanceWithEmptyParent() {
 ClassLoader emptyParent = null;
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(emptyParent);
 assertNull(classLoader);
 }

 @Test
 public void testGetInstanceTwice() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertSame(classLoader1, classLoader2);
 }

 @Test
 public void testGetInstanceDifferentParents() {
 ClassLoader parent1 = XBUSClassLoader.class.getClassLoader();
 ClassLoader parent2 = ClassLoader.getSystemClassLoader();
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent1);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent2);

 assertNotSame(classLoader1, classLoader2);
 }

 @Test
 public void testGetInstanceTwiceDifferentParents() {
 ClassLoader parent1 = XBUSClassLoader.class.getClassLoader();
 ClassLoader parent2 = ClassLoader.getSystemClassLoader();
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent1);
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent2);
 XBUSClassLoader classLoader3 = XBUSClassLoader.getInstance(parent1);

 assertSame(classLoader1, classLoader3);
 assertNotSame(classLoader1, classLoader2);
 }

 @Test
 public void testGetInstanceWithXBUS_HOMESet() {
 System.setProperty(Constants.XBUS_HOME, System.getProperty("user.dir"));
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());

 assertNotNull(classLoader);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInstanceWithXBUS_HOMENull() {
 System.setProperty(Constants.XBUS_HOME, null);
 XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testGetInstanceWithXBUS_HOMEEmpty() {
 System.setProperty(Constants.XBUS_HOME, "");
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertNull(classLoader);
 }

 @Test
 public void testGetInstanceWithXBUS_HOMENotSet() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertNull(classLoader);
 }

 @Test
 public void testGetInstanceWithXBUS_HOMESetAndParentNull() {
 System.setProperty(Constants.XBUS_HOME, System.getProperty("user.dir"));
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
 assertNull(classLoader);
 }

 @Test
 public void testGetInstanceWithXBUS_HOMESetAndParentEmpty() {
 System.setProperty(Constants.XBUS_HOME, System.getProperty("user.dir"));
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(new ClassLoader("") {});
 assertNull(classLoader);
 }

 @Test
 public void testGetInstanceWithXBUS_HOMESetAndParentNotInstanceOfURLClassLoader() {
 System.setProperty(Constants.XBUS_HOME, System.getProperty("user.dir"));
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(new Object() {});
 assertNull(classLoader);
 }

 @Test
 public void testGetInstanceWithXBUS_HOMESetAndParentInstanceOfURLClassLoader() {
 System.setProperty(Constants.XBUS_HOME, System.getProperty("user.dir"));
 URL[] urls = new URL[1];
 urls[0] = new File("test.jar").toURL();
 ClassLoader parent = new URLClassLoader(urls, null);
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

}