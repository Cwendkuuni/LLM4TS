package net.sf.xbus.base.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    private static XBUSClassLoader classLoader;
    private static ClassLoader parent;

    @BeforeClass
    public static void setup() {
        parent = XBUSClassLoader.class.getClassLoader();
        classLoader = XBUSClassLoader.getInstance(parent);
    }

    @AfterClass
    public static void teardown() {
        classLoader = null;
    }

    @Test
    public void testInstance() {
        assertNotNull(classLoader);
        assertInstanceOf(XBUSClassLoader.class, classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader1 = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader1);
        assertInstanceOf(XBUSClassLoader.class, classLoader1);

        XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(parent);
        assertEquals(classLoader1, classLoader2);
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
        assertEquals(classLoader1, classLoader2);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        File libPath = new File(xbusHome + "/lib");
        XBUSClassLoader.addUrls(urls, libPath.getAbsolutePath());

        assertEquals(7, urls.size());
        for (int i = 0; i < urls.size(); ++i) {
            URL url = (URL) urls.elementAt(i);
            assertTrue(url.toString().startsWith(xbusHome));
        }
    }
}