package net.sf.xbus.base.core.reflection;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classUnderTest;
 private static URL[] urls;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 classUnderTest = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());

 // Create a mock XBUS_HOME directory for testing
 File xbusHome = new File("target/test-classes/xbus-home");
 xbusHome.mkdir();

 // Add test jar files to the XBUS_HOME directory
 File libDirectory = new File(xbusHome, "lib");
 libDirectory.mkdir();
 File runtimeJar = new File(libDirectory, "test-runtime.jar");
 File runtimeJarFile = new File("src/test/resources/test-runtime.jar");
 Files.copy(runtimeJarFile.toPath(), runtimeJar.toPath());

 File pluginLibDirectory = new File(xbusHome, "plugin/lib");
 pluginLibDirectory.mkdir();
 File pluginJar = new File(pluginLibDirectory, "test-plugin.jar");
 File pluginJarFile = new File("src/test/resources/test-plugin.jar");
 Files.copy(pluginJarFile.toPath(), pluginJar.toPath());

 File testLibDirectory = new File(xbusHome, "test/lib");
 testLibDirectory.mkdir();
 File testJar = new File(testLibDirectory, "test-library.jar");
 File testJarFile = new File("src/test/resources/test-library.jar");
 Files.copy(testJarFile.toPath(), testJar.toPath());

 Vector mockedUrls = new Vector();
 XBUSClassLoader.addUrls(mockedUrls, xbusHome.getAbsolutePath());
 urls = mockedUrls.toArray(new URL[mockedUrls.size()]);
 }

 @Test
 public void testGetInstance() {
 XBUSClassLoader instance1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 XBUSClassLoader instance2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
 assertSame("getInstance should return the same instance", instance1, instance2);
 }

 @Test
 public void testCreateClassLoader() {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
 assertNotNull("createClassLoader should not return null", classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urlsVector = new Vector();
 XBUSClassLoader.addUrls(urlsVector, "test-dir");
 URL[] expectedUrls = new URL[] { new File("test-dir/test-1.jar").toURL(),
 new File("test-dir/test-2.jar").toURL() };
 assertArrayEquals("addUrls should add the correct URLs", expectedUrls, urlsVector.toArray(new URL[urlsVector.size()]));
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateClassLoaderFailure() {
 XBUSClassLoader.createClassLoader(null);
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException {
 String className = "TestClass";
 Class<?> expectedClass = Class.forName("net.sf.xbus.base.core.reflection.TestClass");
 Class<?> loadedClass = classUnderTest.loadClass(className);
 assertEquals("loadClass should load the correct class", expectedClass, loadedClass);
 }
}