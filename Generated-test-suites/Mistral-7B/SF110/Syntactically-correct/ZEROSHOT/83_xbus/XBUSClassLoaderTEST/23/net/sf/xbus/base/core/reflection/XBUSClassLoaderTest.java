package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import org.junit.Assert;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static ClassLoader parentClassLoader = XBUSClassLoader.class.getClassLoader();

 @Test
 public void testGetInstance() {
 classLoader = XBUSClassLoader.getInstance(parentClassLoader);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 classLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "src/test/resources/XBUSClassLoaderTest");
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 System.out.println(urlArray[i].toString());
 }
 Assert.assertEquals(8, urls.size()); // Assuming there are 8 test files in the test directory
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException, IOException {
 String className = "net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClass";
 Class<?> loadedClass = classLoader.loadClass(className);
 Assert.assertNotNull(loadedClass);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadClassNotFound() throws ClassNotFoundException {
 String className = "net.sf.xbus.base.core.reflection.XBUSClassLoaderTest$TestClassNotFound";
 classLoader.loadClass(className);
 }

}