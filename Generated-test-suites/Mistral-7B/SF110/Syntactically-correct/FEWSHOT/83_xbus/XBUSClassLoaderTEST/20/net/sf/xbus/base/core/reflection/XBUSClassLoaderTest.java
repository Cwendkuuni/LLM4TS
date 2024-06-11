package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;

import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);

        XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(parent);
        assertEquals(classLoader, classLoader2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test
    public void testAddUrls() {
        String xbusHome = System.getProperty("XBUS_HOME");
        File libPath = new File(xbusHome + "/lib");
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, xbusHome + "/lib");
        assertEquals(urls.size(), 5);

        urls.clear();
        XBUSClassLoader.addUrls(urls, "nonexistent");
        assertEquals(urls.size(), 0);
    }

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
        assertEquals(classLoader, classLoader2);
    }

    @Test
    public void testGetInstanceDifferentParents() {
        ClassLoader parent1 = getClass().getClassLoader();
        ClassLoader parent2 = ClassLoader.getSystemClassLoader();
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent1);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent2);
        assertNotNull(classLoader1);
        assertNotNull(classLoader2);
        assertNotSame(classLoader1, classLoader2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLoadClass() throws ClassNotFoundException {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        String className = "net.sf.xbus.base.core.trace.Trace";
        Class<?> clazz = classLoader.loadClass(className);
        assertNotNull(clazz);
        assertEquals(Trace.class, clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testLoadClassNotFound() throws ClassNotFoundException {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        String className = "net.sf.xbus.base.core.nonexistent.Trace";
        Class<?> clazz = classLoader.loadClass(className);
        fail("ClassNotFoundException expected but not thrown");
    }

    @Test
    public void testLoadClassFromUrl() throws ClassNotFoundException, MalformedURLException {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        String fileName = "net/sf/xbus/base/core/trace/Trace.class";
        URL url = new File("src/test/resources/" + fileName).toURL();
        Class<?> clazz = classLoader.loadClassFromURL(url);
        assertNotNull(clazz);
        assertEquals(Trace.class, clazz);
    }

    @Test(expected = MalformedURLException.class)
    public void testLoadClassFromUrlInvalid() throws MalformedURLException {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        String fileName = "net/sf/xbus/base/core/trace/Trace.class/invalid";
        URL url = new File("src/test/resources/" + fileName).toURL();
        classLoader.loadClassFromURL(url);
        fail("MalformedURLException expected but not thrown");
    }

    @Test
    public void testUrls() throws IOException, ClassNotFoundException {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        URL[] urls = classLoader.getURLs();
        assertEquals(urls.length, 4);

        String xbusHome = System.getProperty("XBUS_HOME");
        for (URL url : urls) {
            File file = new File(url.getFile());
            assertTrue(file.isDirectory() || file.isFile() && file.getName().endsWith(".jar") || file.getName().endsWith(".zip"));
            assertTrue(new File(xbusHome, file.getPath().substring(0, file.getPath().lastIndexOf('/'))).isDirectory());
        }
    }

    @Test
    public void testGetClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertEquals(parent, classLoader.getParent());
    }
}