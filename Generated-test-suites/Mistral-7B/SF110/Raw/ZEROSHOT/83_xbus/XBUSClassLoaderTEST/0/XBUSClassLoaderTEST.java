package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader mClassLoader;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 mClassLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertEquals("The class loaders should be the same", mClassLoader, classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 assertEquals("The class loaders should be the same", mClassLoader, classLoader);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "src/test/resources/XBUSClassLoaderTest");
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, XBUSClassLoader.class.getClassLoader());
 assertTrue("The number of added URLs should be correct", urls.size() >= 4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParent() {
 XBUSClassLoader classLoader = new XBUSClassLoader(null, XBUSClassLoader.class.getClassLoader());
 fail("An IllegalArgumentException should be thrown, but it wasn't");
 }

 @Test(expected = NullPointerException.class)
 public void testNullUrls() {
 XBUSClassLoader classLoader = new XBUSClassLoader(null, XBUSClassLoader.class.getClassLoader());
 fail("A NullPointerException should be thrown, but it wasn't");
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedURL() throws MalformedURLException {
 Vector urls = new Vector();
 urls.add(new URL("http://example.com"));
 XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), XBUSClassLoader.class.getClassLoader());
 fail("A MalformedURLException should be thrown, but it wasn't");
 }

 @Test
 public void testFindClass() throws ClassNotFoundException {
 Class<?> clazz = mClassLoader.findClass("net.sf.xbus.base.core.XBUSEngine");
 assertNotNull("net.sf.xbus.base.core.XBUSEngine should be found", clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testNotFound() throws ClassNotFoundException {
 Class<?> clazz = mClassLoader.findClass("org.junit.Assert");
 fail("ClassNotFoundException should be thrown, but it wasn't");
 }
}