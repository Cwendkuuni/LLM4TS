import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;
import java.net.*;
import java.util.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

 private static XBUSClassLoader classLoader;
 private static ClassLoader parent;

 @BeforeClass
 public static void initClass() {
 parent = XBUSClassLoader.class.getClassLoader();
 classLoader = XBUSClassLoader.getInstance(parent);
 }

 @Test
 public void testCreateClassLoader() {
 assertNotNull(XBUSClassLoader.mClassLoader);
 }

 @Test
 public void testAddUrls() {
 List<URL> urls = new ArrayList<>();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 assertTrue(urls.size() > 0);
 }

 @Test
 public void testGetInstance() {
 assertNotNull(classLoader);
 }

 @Test
 public void testLoadClass() throws ClassNotFoundException {
 Class<?> clazz = classLoader.loadClass("net.sf.xbus.base.core.reflection.XBUSClassLoader");
 assertNotNull(clazz);
 }

 @Test
 public void testLoadClassFromParent() throws ClassNotFoundException {
 Class<?> clazz = parent.loadClass("java.lang.String");
 assertNotNull(clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testLoadMissingClass() throws ClassNotFoundException {
 classLoader.loadClass("net.sf.xbus.unknown.UnknownClass");
 fail("ClassNotFoundException not thrown");
 }

 @AfterClass
 public static void tearDownClass() {
 // No need to do anything since we don't modify any state.
 }
}