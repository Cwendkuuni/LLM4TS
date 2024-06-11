package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Vector;

import org.junit.Test;
import org.junit.BeforeClass;

public class XBUSClassLoaderTest {

    private static XBUSClassLoader classLoader;

    @BeforeClass
    public static void setupClass() {
        classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
    }

    @Test
    public void testInstance() {
        assertNotNull(XBUSClassLoader.class);
        assertNotNull(classLoader);
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertEquals(instance1, instance2);
    }

    @Test
    public void testCreateClassLoader() {
        assertNull(XBUSClassLoader.mClassLoader);
        XBUSClassLoader newClassLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
        assertNotNull(newClassLoader);
        assertEquals(newClassLoader, XBUSClassLoader.mClassLoader);
    }

    @Test
    public void testAddUrlsVector() {
        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        String xbusHomeDir = null;
        if (xbusHome != null) {
            xbusHomeDir = URLDecoder.decode(xbusHome, "UTF-8");
        }
        assertNotNull(xbusHomeDir);
        addUrls(urls, xbusHomeDir + "/lib");
        addUrls(urls, xbusHomeDir + "/lib/runtime");
        addUrls(urls, xbusHomeDir + "/plugin/lib");
        addUrls(urls, xbusHomeDir + "/test/lib");
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = (URL) urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, XBUSClassLoader.class.getClassLoader());
        assertNotNull(classLoader);
        assertEquals(urls.size(), classLoader.getURLs().length);
    }

    @Test
    public void testAddUrlsNull() {
        Vector urls = new Vector();
        addUrls(urls, null);
        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsEmptyDirectory() {
        Vector urls = new Vector();
        File libPath = new File("");
        addUrls(urls, libPath.getAbsolutePath());
        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsNoJars() {
        Vector urls = new Vector();
        File libPath = new File("doesnotexist");
        addUrls(urls, libPath.getAbsolutePath());
        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsJars() throws IOException {
        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        String xbusHomeDir = null;
        if (xbusHome != null) {
            xbusHomeDir = URLDecoder.decode(xbusHome, "UTF-8");
        }
        assertNotNull(xbusHomeDir);
        File libPath = new File(xbusHomeDir + "/lib");
        File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                URL url = jars[i].toURL();
                urls.add(url);
            }
        }
        assertFalse(urls.isEmpty());
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = (URL) urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, XBUSClassLoader.class.getClassLoader());
        assertNull(classLoader.getParent());
        assertNotNull(classLoader.getURLs());
        assertEquals(urls.size(), classLoader.getURLs().length);
    }

    @Test
    public void testGetURLs() {
        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        String xbusHomeDir = null;
        if (xbusHome != null) {
            xbusHomeDir = URLDecoder.decode(xbusHome, "UTF-8");
        }
        assertNotNull(xbusHomeDir);
        addUrls(urls, xbusHomeDir + "/lib");
        addUrls(urls, xbusHomeDir + "/lib/runtime");
        addUrls(urls, xbusHomeDir + "/plugin/lib");
        addUrls(urls, xbusHomeDir + "/test/lib");
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = (URL) urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, XBUSClassLoader.class.getClassLoader());
        URL[] urlsFromClassLoader = classLoader.getURLs();
        assertNotNull(urlsFromClassLoader);
        assertEquals(urls.size(), urlsFromClassLoader.length);
        for (int i = 0; i < urls.size(); ++i) {
            assertTrue(urlsFromClassLoader[i].equals(urls.elementAt(i)));
        }
    }

    @Test
    public void testGetParent() {
        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        String xbusHomeDir = null;
        if (xbusHome != null) {
            xbusHomeDir = URLDecoder.decode(xbusHome, "UTF-8");
        }
        assertNotNull(xbusHomeDir);
        addUrls(urls, xbusHomeDir + "/lib");
        addUrls(urls, xbusHomeDir + "/lib/runtime");
        addUrls(urls, xbusHomeDir + "/plugin/lib");
        addUrls(urls, xbusHomeDir + "/test/lib");
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = (URL) urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, XBUSClassLoader.class.getClassLoader());
        ClassLoader parent = classLoader.getParent();
        assertNotNull(parent);
        assertFalse(parent.equals(classLoader));
    }
}