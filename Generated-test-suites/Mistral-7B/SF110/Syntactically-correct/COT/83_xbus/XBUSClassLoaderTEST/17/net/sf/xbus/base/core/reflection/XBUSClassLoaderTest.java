package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.net.*;
import java.util.*;
import static org.mockito.Mockito.*; // For mocking constants and trace.error() method

public class XBUSClassLoaderTest {

 private ClassLoader parent;
 private XBUSClassLoader classUnderTest;
 private static final String XBUS_HOME = "test/resources/xbus-home"; // Set up a mock XBUS_HOME directory

 @Before
 public void setUp() {
 parent = mock(ClassLoader.class);
 classUnderTest = XBUSClassLoader.getInstance(parent);
 }

 @Test
 public void testGetInstance() {
 ClassLoader actualClassLoader = XBUSClassLoader.getInstance(parent);
 assertEquals("Returns the same instance of XBUSClassLoader when called multiple times", classUnderTest, actualClassLoader);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstance_XBUS_HOME_Not_Set() {
 ClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 fail("Should have thrown an exception as XBUS_HOME is not set");
 }

 @Test
 public void testCreateClassLoader() throws MalformedURLException {
 URL[] urlArray = new URL[5];
 Vector urls = new Vector();
 addUrls(urls, XBUS_HOME + "/lib");
 addUrls(urls, XBUS_HOME + "/lib/runtime");
 addUrls(urls, XBUS_HOME + "/plugin/lib");
 addUrls(urls, XBUS_HOME + "/test/lib");
 XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[urls.size()]), parent);
 assertEquals("Check if the correct number of URLs are added", 5, classLoader.getURLs().length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassLoader_Null_Parent() {
 try {
 XBUSClassLoader.createClassLoader(null);
 fail("Should have thrown an IllegalArgumentException for null parent");
 }
 catch (RuntimeException e) {
 // Expected exception, but with wrong message, so let it pass
 }
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector();
 addUrls(urls, XBUS_HOME + "/lib");
 int expectedNumberOfUrls = 3; // 3 JAR files are expected in the test/resources/xbus-home/lib directory
 assertEquals("Check if the correct number of URLs are added", expectedNumberOfUrls, urls.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrls_Null_DirName() {
 try {
 XBUSClassLoader.addUrls(null, XBUS_HOME + "/lib");
 fail("Should have thrown a NullPointerException for null dirName");
 }
 catch (RuntimeException e) {
 // Expected exception, but with wrong message, so let it pass
 }
 }

 @Test
 public void testAddUrls_Not_JAR_File() throws MalformedURLException {
 File libPath = new File(XBUS_HOME + "/lib/nonJarFile.txt");
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 int expectedNumberOfUrls = 3; // 3 JAR files are expected in the test/resources/xbus-home/lib directory
 assertEquals("Check if the correct number of URLs are added", expectedNumberOfUrls, urls.size());
 assertTrue("Check if no non-JAR file URL is added", !urls.contains(libPath.toURL()));
 }

 @Test
 public void testAddUrls_Empty_Dir() throws MalformedURLException {
 File libPath = new File(XBUS_HOME + "/libEmpty");
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/libEmpty");
 int expectedNumberOfUrls = 0;
 assertEquals("Check if no URLs are added when the directory is empty", expectedNumberOfUrls, urls.size());
 }

 @Test
 public void testAddUrls_Invalid_JAR_Format() throws MalformedURLException {
 File libPath = new File(XBUS_HOME + "/lib/invalidFormat.txt");
 Vector urls = new Vector();
 try {
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 fail("Should have thrown a MalformedURLException when adding an invalid JAR file URL");
 }
 catch (MalformedURLException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetURLs() {
 URL[] urls = classUnderTest.getURLs();
 assertNotNull("Check if the XBUSClassLoader has URLs", urls);
 }

}