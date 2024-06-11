package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home"; // Set XBUS_HOME environment variable or replace with the correct path

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testGetInstanceWithNullParent() {
 try {
 XBUSClassLoader.getInstance(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetInstanceWithValidParent() {
 ClassLoader parent = new URLClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader());
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoaderWithNullParent() {
 try {
 XBUSClassLoader.createClassLoader(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testCreateClassLoaderWithValidParent() {
 ClassLoader parent = ClassLoader.getSystemClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() throws IOException, MalformedURLException {
 Vector urls = new Vector<>();
 String dirName = XBUS_HOME + "/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 assertTrue(urls.size() > 0);
 for (int i = 0; i < urls.size(); ++i) {
 File jarFile = new File(dirName, "library" + i + ".jar");
 if (jarFile.exists()) {
 assertTrue(urls.elementAt(i) instanceof URL);
 URL url = (URL) urls.elementAt(i);
 File file = new File(url.getFile());
 assertTrue(file.getAbsolutePath().endsWith("library" + i + ".jar"));
 } else {
 fail("File " + jarFile.getAbsolutePath() + " does not exist.");
 }
 }
 }

 @Test
 public void testCreateClassLoaderWithWrongDirName() {
 Vector urls = new Vector<>();
 String dirName = XBUS_HOME + "/non-existing-dir";
 XBUSClassLoader.addUrls(urls, dirName);
 assertTrue(urls.size() == 0);
 }

 @Test
 public void testCreateClassLoaderWithEmptyDir() throws IOException {
 Vector urls = new Vector<>();
 String dirName = XBUS_HOME + "/empty-dir";
 XBUSClassLoader.addUrls(urls, dirName);
 assertTrue(urls.size() == 0);
 }

 @Test
 public void testCreateClassLoaderWithInvalidFile() throws IOException, MalformedURLException {
 Vector urls = new Vector<>();
 String dirName = XBUS_HOME + "/lib";
 String invalidFileName = "invalid-file";
 File invalidFile = new File(dirName, invalidFileName);
 invalidFile.createNewFile();
 try {
 XBUSClassLoader.addUrls(urls, dirName);
 fail("Should throw MalformedURLException");
 } catch (MalformedURLException e) {
 // Expected
 }
 finally {
 invalidFile.delete();
 }
 assertTrue(urls.size() == 0);
 }

 @Test
 public void testCreateClassLoaderWithValidJars() throws IOException, MalformedURLException {
 Vector urls = new Vector<>();
 String dirName = XBUS_HOME + "/lib";
 XBUSClassLoader.addUrls(urls, dirName);
 int numJars = 3;
 for (int i = 0; i < numJars; ++i) {
 File jarFile = new File(dirName, "library" + i + ".jar");
 jarFile.createNewFile();
 urls.add(jarFile.toURL());
 }
 XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), ClassLoader.getSystemClassLoader());
 assertNotNull(classLoader);
 for (int i = 0; i < numJars; ++i) {
 File jarFile = new File(dirName, "library" + i + ".jar");
 jarFile.delete();
 }
 }
}