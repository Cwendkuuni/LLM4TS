package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private static ClassLoader parent;
    private static XBUSClassLoader classLoader;
    private static Vector urls;

    @BeforeClass
    public static void setup() {
        parent = XBUSClassLoader.class.getClassLoader();
    }

    @Test
    public void testInstance() {
        classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        urls = new Vector();
        classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);

        assertEquals(4, urls.size());
        for (int i = 0; i < urls.size(); ++i) {
            URL url = (URL) urls.elementAt(i);
            String path = url.getPath();
            assertTrue(path.contains("lib") || path.contains("runtime") || path.contains("plugin/lib") || path.contains("test/lib"));
        }
    }

    @Test
    public void testAddUrls() {
        urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        File libPath = new File(xbusHome + "/lib");
        File[] jars = libPath.listFiles();

        XBUSClassLoader.addUrls(urls, xbusHome + "/lib");
        assertEquals(jars.length, urls.size());

        for (int i = 0; i < urls.size(); ++i) {
            URL url = (URL) urls.elementAt(i);
            File jarFile = new File(url.getFile());
            assertTrue(jarFile.isFile());
            assertTrue(jarFile.getName().endsWith("jar") || jarFile.getName().endsWith("zip"));
        }
    }

    @Test
    public void testStaticInitialization() {
        classLoader = null;
        assertNull(XBUSClassLoader.mClassLoader);

        URL[] urlArray = new URL[4];
        XBUSClassLoader.createClassLoader(parent);

        classLoader = XBUSClassLoader.mClassLoader;
        assertNotNull(classLoader);

        urls = new Vector();
        for (int i = 0; i < urlArray.length; ++i) {
            urls.add(urlArray[i]);
        }

        assertEquals(urls, classLoader.getParent().getURLs());
    }
}