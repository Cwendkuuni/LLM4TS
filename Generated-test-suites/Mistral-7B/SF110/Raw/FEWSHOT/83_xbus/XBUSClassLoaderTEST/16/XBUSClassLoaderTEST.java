package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;

import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private static XBUSClassLoader mClassLoader;

    @BeforeClass
    public static void setupClass() {
        mClassLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
    }

    @Test
    public void testGetInstance() {
        assertNotNull(mClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        assertNotNull(XBUSClassLoader.mClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        String dirName = xbusHome + "/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(4, urls.size());
        urls.clear();
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(4, urls.size());
    }

    @Test
    public void testGetInstanceWithXBUS_HOME_Set() {
        String xbusHome = System.getProperty("XBUS_HOME");
        assertNull(xbusHome);
        XBUSClassLoader mClassLoader1 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertNull(mClassLoader1);
        System.setProperty("XBUS_HOME", "/tmp/xbus");
        XBUSClassLoader mClassLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertNotNull(mClassLoader2);
        System.clearProperty("XBUS_HOME");
    }

    @Test
    public void testGetInstanceWithXBUS_HOME_Not_Set() {
        String xbusHome = System.getProperty("XBUS_HOME");
        assertNull(xbusHome);
        XBUSClassLoader mClassLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertNull(mClassLoader);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateClassLoaderWithNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateClassLoaderWithEmptyUrlArray() {
        XBUSClassLoader.createClassLoader(new URL[0], null);
    }

    @Test
    public void testCreateClassLoaderWithUrls() {
        URL[] urls = new URL[3];
        urls[0] = new File("test1.jar").toURL();
        urls[1] = new File("test2.jar").toURL();
        urls[2] = new File("test3.jar").toURL();
        XBUSClassLoader mClassLoader = XBUSClassLoader.createClassLoader(urls, null);
        assertNotNull(mClassLoader);
    }

    @Test
    public void testGetParent() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());
    }

    @Test
    public void testFindClass() {
        try {
            Class<?> clazz = mClassLoader.findClass("net.sf.xbus.TestClass");
            assertNotNull(clazz);
        } catch (ClassNotFoundException e) {
            fail("Class not found: " + e.getMessage());
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testFindClassNotFound() {
        mClassLoader.findClass("net.sf.xbus.UnknownClass");
    }
}