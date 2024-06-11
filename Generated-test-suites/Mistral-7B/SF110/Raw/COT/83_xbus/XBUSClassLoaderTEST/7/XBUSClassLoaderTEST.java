import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

 private static final String XBUS_HOME = "path/to/xbus/home"; // Set this to the actual XBUS_HOME value

 @Test
 public void testGetInstance() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector<>();
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
 assertFalse(urls.isEmpty());
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib/runtime");
 assertFalse(urls.isEmpty());
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/plugin/lib");
 assertFalse(urls.isEmpty());
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/test/lib");
 assertFalse(urls.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWhenXBUS_HOMEIsNull() {
 Constants.XBUS_HOME = null;
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
 fail("XBUSClassLoader.getInstance() should have thrown a NullPointerException when XBUS_HOME is null");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateClassLoaderWhenXBUS_HOMEIsNull() {
 Constants.XBUS_HOME = null;
 ClassLoader parent = XBUSClassLoader.class.getClassLoader();
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
 fail("XBUSClassLoader.createClassLoader() should have thrown a NullPointerException when XBUS_HOME is null");
 }

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidUrl() {
 Vector urls = new Vector<>();
 String invalidUrl = "invalid://url";
 XBUSClassLoader.addUrls(urls, XBUS_HOME + "/" + invalidUrl);
 fail("XBUSClassLoader.addUrls() should have thrown a MalformedURLException when an invalid URL is provided");
 }

 @Test
 public void testGetMClassLoader() {
 ClassLoader mClassLoader = XBUSClassLoader.mClassLoader;
 assertNotNull(mClassLoader);
 }

}