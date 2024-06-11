package net.sf.xbus.base.core.reflection.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.reflection.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home";

 @Test
 public void testGetInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 assertEquals(4, urls.size());
 for (int i = 0; i < urls.size(); ++i) {
 URL url = urls.elementAt(i);
 assertTrue(url.toString().startsWith(XBUS_HOME + "/lib/"));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullXBUS_HOME() {
 Constants.XBUS_HOME = null;
 XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyXBUS_HOME() {
 Constants.XBUS_HOME = "";
 XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidURL() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, "invalid_url");
 assertEquals(0, urls.size());
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedURL() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib/invalid_file");
 }

 @Test
 public void testGetMClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 XBUSClassLoader anotherClassLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertEquals(classLoader, anotherClassLoader);
 }
}