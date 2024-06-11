package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URL;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testGetInstanceWithNullParent() {
        assertNull(XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testGetInstanceWithSameParent() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader xbusClassLoader2 = XBUSClassLoader.getInstance(parent);
        assertEquals(xbusClassLoader1.hashCode(), xbusClassLoader2.hashCode());
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        assertNull(XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testCreateClassLoaderWithSameParent() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader1 = XBUSClassLoader.createClassLoader(parent);
        XBUSClassLoader xbusClassLoader2 = XBUSClassLoader.createClassLoader(parent);
        assertEquals(xbusClassLoader1.hashCode(), xbusClassLoader2.hashCode());
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "test/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        URL expectedUrl = new File("test/lib/test.jar").toURL();
        URL actualUrl = urls.firstElement();
        assertEquals(expectedUrl.toString(), actualUrl.toString());
    }

    @Test
    public void testAddUrlsWithNullUrls() {
        String dirName = "test/lib";
        Vector urls = null;
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithNullDirName() {
        Vector urls = new Vector();
        String dirName = null;
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithNonExistentDirectory() {
        Vector urls = new Vector();
        String dirName = "non_existent_directory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithNonJarOrZipFile() {
        Vector urls = new Vector();
        String dirName = "test/lib";
        File file = new File(dirName, "test.txt");
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithJarFile() {
        Vector urls = new Vector();
        String dirName = "test/lib";
        File file = new File(dirName, "test.jar");
        XBUSClassLoader.addUrls(urls, dirName);
        URL expectedUrl = file.toURL();
        URL actualUrl = urls.firstElement();
        assertEquals(expectedUrl.toString(), actualUrl.toString());
    }
}