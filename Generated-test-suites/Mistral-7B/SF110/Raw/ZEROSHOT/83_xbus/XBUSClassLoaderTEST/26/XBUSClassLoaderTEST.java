package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.Test;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader mClassLoader;

 @Test
 public void testInstance() {
 XBUSClassLoader actual = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertNotNull(actual);
 }

 @Test
 public void testCreateClassLoader() {
 mClassLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 assertNotNull(mClassLoader);
 }

 @Test
 public void testGetInstanceAlreadyInitialized() {
 XBUSClassLoader actual = XBUSClassLoader.getInstance(getClass().getClassLoader());
 XBUSClassLoader expected = mClassLoader;
 assertEquals(expected, actual);
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector();
 String dirName = "src/test/resources/testDir";
 XBUSClassLoader.addUrls(urls, dirName);
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 String[] expectedUrls = { "file:src/test/resources/testDir/test1.jar", "file:src/test/resources/testDir/test2.jar" };
 assertArrayEquals(expectedUrls, toStrings(urlArray));
 }

 @Test
 public void testInitializeClassLoader() {
 assertNull(XBUSClassLoader.mClassLoader);
 XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 assertNotNull(XBUSClassLoader.mClassLoader);
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException, IOException {
 String className = "TestClass";
 Class<?> expected = XBUSClassLoader.loadClass(className);
 Class<?> actual = mClassLoader.loadClass(className);
 assertEquals(expected, actual);
 }

 @Test
 public void testLoadClassNotFound() throws ClassNotFoundException {
 String className = "NonExistentClass";
 assertThrows(ClassNotFoundException.class, () -> mClassLoader.loadClass(className));
 }

 private String[] toStrings(URL[] urls) {
 String[] result = new String[urls.length];
 for (int i = 0; i < urls.length; i++) {
 result[i] = urls[i].toString();
 }
 return result;
}

}