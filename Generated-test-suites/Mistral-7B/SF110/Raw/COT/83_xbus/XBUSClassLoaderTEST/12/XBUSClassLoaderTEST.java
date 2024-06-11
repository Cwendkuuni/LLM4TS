package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static net.sf.xbus.base.core.Constants.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader mClassLoader;

 @Test
 public void testGetInstance() {
 mClassLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
 Assert.assertNotNull(mClassLoader);
 }

 @Test
 public void testCreateClassLoader() {
 mClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
 Assert.assertNotNull(mClassLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 Assert.assertEquals(3, urls.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNull() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUrlsWithEmptyString() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidURL() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "invalid_url");
 }

 @Test
 public void testGetInstanceWithNullParent() {
 try {
 mClassLoader = XBUSClassLoader.getInstance(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoaderWithNullParent() {
 try {
 mClassLoader = XBUSClassLoader.createClassLoader(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testConstructor() {
 mClassLoader = new XBUSClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader());
 Assert.assertNotNull(mClassLoader);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullURLs() {
 mClassLoader = new XBUSClassLoader(null, ClassLoader.getSystemClassLoader());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyURLs() {
 mClassLoader = new XBUSClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullParent() {
 mClassLoader = new XBUSClassLoader(new URL[]{new File("lib").toURL()}, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyParent() {
 mClassLoader = new XBUSClassLoader(new URL[]{new File("lib").toURL()}, new URL[]{});
 }

}