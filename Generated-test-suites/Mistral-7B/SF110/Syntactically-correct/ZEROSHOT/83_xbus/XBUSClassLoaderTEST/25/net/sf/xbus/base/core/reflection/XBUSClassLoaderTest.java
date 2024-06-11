package net.sf.xbus.base.core.reflection;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classUnderTest;
 private static final String XBUS_HOME = "path/to/xbus/home"; // Set your XBUS_HOME here

 @BeforeClass
 public static void setup() {
 classUnderTest = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testInstance() {
 Class<?> clazz = classUnderTest.loadClass("net.sf.xbus.base.core.reflection.XBUSClassLoader");
 assertNotNull(clazz);
 }

 @Test
 public void testGetInstance() {
 Class<?> clazz = classUnderTest.getClass();
 XBUSClassLoader loader1 = (XBUSClassLoader) clazz.getDeclaredConstructor().newInstance();
 XBUSClassLoader loader2 = XBUSClassLoader.getInstance(classUnderTest);
 assertEquals(loader1, loader2);
 }

 @Test
 public void testCreateClassLoader() {
 Class<?> clazz = classUnderTest.getClass();
 XBUSClassLoader loader1 = (XBUSClassLoader) clazz.getDeclaredConstructor().newInstance();
 XBUSClassLoader loader2 = XBUSClassLoader.createClassLoader(classUnderTest);
 assertEquals(loader1, loader2);
 }

 @Test
 public void testAddUrls() {
 Class<?> clazz = classUnderTest.getClass();
 try {
 Method addUrlsMethod = clazz.getDeclaredMethod("addUrls", Vector.class, String.class);
 addUrlsMethod.setAccessible(true);

 Vector urls = new Vector();
 String dirName = XBUS_HOME + "/lib";
 addUrlsMethod.invoke(classUnderTest, urls, dirName);

 int initialSize = urls.size();
 addUrls(urls, XBUS_HOME + "/plugin/lib");
 int finalSize = urls.size();

 assertEquals("Adding URLs to the vector should have increased its size.", initialSize + 1, finalSize);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unable to invoke addUrls method.");
 }
 }

 @Test
 public void testLoadClass() {
 try {
 Class<?> clazz = classUnderTest.loadClass("java.lang.String");
 assertNotNull(clazz);
 } catch (ClassNotFoundException e) {
 fail("Class 'java.lang.String' should be found.");
 }
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedURLException() {
 try {
 Vector urls = new Vector();
 File libPath = new File("nonExistentDirectory");
 File[] jars = libPath.listFiles();
 urls.add(jars[0].toURL());
 XBUSClassLoader loader = new XBUSClassLoader(urls.toArray(new URL[0]), classUnderTest);
 } catch (MalformedURLException e) {
 // Expected
 }
 }
}