package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XBUSClassLoaderTest {

 private static ClassLoader parent;
 private static XBUSClassLoader mockedClassLoader;

 @BeforeClass
 public static void initMocks() {
 MockitoAnnotations.initMocks(XBUSClassLoader.class);
 parent = mock(ClassLoader.class);
 mockedClassLoader = XBUSClassLoader.getInstance(parent);
 }

 @Test
 public void testInstanceWithNullParent() {
 assertNull(XBUSClassLoader.getInstance(null));
 }

 @Test
 public void testInstanceWithValidParent() {
 assertEquals(parent, mockedClassLoader.getParent());
 }

 @Test
 public void testCreateClassLoaderWithNullParent() {
 Vector urls = new Vector();
 when(parent.getResources("/")).thenReturn(mock(Enumeration.class));
 when(parent.getResource("/")).thenReturn(mock(URL.class));
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
 assertEquals(urlArray(), urls.toArray(new URL[0]));
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateClassLoaderWithInvalidParent() {
 XBUSClassLoader.createClassLoader("not-a-class-loader");
 }

 @Test
 public void testAddUrls() {
 File mockLibPath = mock(File.class);
 when(mockLibPath.isDirectory()).thenReturn(true);
 when(mockLibPath.listFiles()).thenReturn(new File[] { new File("jar1.jar"), new File("jar2.jar"), new File("jar3.jar") });

 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "/path/to/lib");
 assertEquals(3, urls.size());
 for (int i = 0; i < 3; ++i) {
 URL url = urls.elementAt(i);
 assertTrue(url.toString().contains("jar1.jar") || url.toString().contains("jar2.jar") || url.toString().contains("jar3.jar"));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testAddUrlsWithNullDirName() {
 XBUSClassLoader.addUrls(null, "/path/to/lib");
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader firstInstance = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader secondInstance = XBUSClassLoader.getInstance(parent);
 assertEquals(firstInstance, secondInstance);
 }

 @Test
 public void testGetInstanceWithDifferentParents() {
 ClassLoader differentParent = mock(ClassLoader.class);
 XBUSClassLoader firstInstance = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader secondInstance = XBUSClassLoader.getInstance(differentParent);
 assertNotEquals(firstInstance, secondInstance);
 }

 @Test
 public void testGetResources() throws IOException {
 URL[] urls = urlArray();
 URL[] expectedUrls = new URL[] { new File("lib/resource1.txt").toURL(), new File("lib/resource2.txt").toURL() };
 when(mockedClassLoader.getResources("/")).thenReturn(mock(Enumeration.class));
 when(mockedClassLoader.getResource("/")).thenReturn(mock(URL.class));
 when(mockedClassLoader.getResources("/lib")).thenReturn(mock(Enumeration.class));

 List<URL> resources = new ArrayList<>();
 try (Enumeration<URL> resourcesEnumeration = mockedClassLoader.getResources("/")) {
 while (resourcesEnumeration.hasMoreElements()) {
 resources.add(resourcesEnumeration.nextElement());
 }
 }
 assertEquals(expectedUrls, resources.toArray(new URL[0]));
 }

 private URL[] urlArray() {
 return new URL[] { new File("lib/jar1.jar").toURL(), new File("lib/jar2.jar").toURL(), new File("lib/jar3.jar").toURL() };
 }
}