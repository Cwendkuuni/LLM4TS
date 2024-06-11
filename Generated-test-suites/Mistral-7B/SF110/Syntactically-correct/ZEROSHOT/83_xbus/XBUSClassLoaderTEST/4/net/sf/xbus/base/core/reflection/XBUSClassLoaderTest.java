package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static final String XBUS_HOME = "path/to/xbus/home";

 @BeforeClass
 public static void setup() {
 Constants.setXBUS_HOME(XBUS_HOME);
 classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 }

 @Test
 public void testInstance() {
 XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertEquals(classLoader1, classLoader2);
 }

 @Test
 public void testCreateClassLoader() {
 Class<?> parentClass = XBUSClassLoader.class.getClassLoader().getClass();
 XBUSClassLoader classLoader1 = XBUSClassLoader.createClassLoader(parentClass);
 XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(parentClass);
 assertEquals(classLoader1, classLoader2);
 }

 @Test
 public void testGetInstanceWithNullXBUS_HOME() {
 Constants.setXBUS_HOME(null);
 assertNull(XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader()));
 }

 @Test(expected = RuntimeException.class)
 public void testCreateClassLoaderWithNullParent() {
 XBUSClassLoader classLoader = null;
 try {
 classLoader = XBUSClassLoader.createClassLoader(null);
 } catch (NullPointerException e) {
 // Expected null pointer exception since parent is null
 } finally {
 assertNull(classLoader);
 }
 }

 @Test
 public void testAddUrls() throws MalformedURLException {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 List<URL> urlList = new ArrayList<>(urls);
 List<String> expectedUrls = Arrays.asList(
 new String[]{"file:path/to/xbus/home/lib/", "file:path/to/xbus/home/lib/runtime/", "file:path/to/xbus/home/plugin/lib/", "file:path/to/xbus/home/test/lib/"});
 assertEquals(expectedUrls, urlList.stream().map(URL::toExternalForm).collect(Collectors.toList()));
 }

 @Test
 public void testStaticInitialization() {
 assertNull(XBUSClassLoader.mClassLoader);
 }

}