package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Category(UnitTests.class)
public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, this.getClass().getClassLoader());
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, this.getClass().getClassLoader());
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/src/main/resources");
        assertEquals(1, urls.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullParent() {
        XBUSClassLoader.getInstance(null);
    }

    @Test
    public void testCreateClassLoaderWithEmptyUrls() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, this.getClass().getClassLoader());
        Vector urls = new Vector();
        XBUSClassLoader xbusClassLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        assertNotNull(xbusClassLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsWithInvalidUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/src/main/resources/invalidUrl");
        assertEquals(0, urls.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddUrlsWithNonExistentDirectory() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/src/main/resources/nonexistentdirectory");
        assertEquals(0, urls.size());
    }

    @Test(expected = IOException.class)
    public void testAddUrlsWithMalformedURLs() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/src/main/resources/malformedurl");
        assertEquals(0, urls.size());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateClassLoaderWithMalformedURLs() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, this.getClass().getClassLoader());
        Vector urls = new Vector();
        urls.add(new URL("malformed://malformedurl.com"));
        XBUSClassLoader xbusClassLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        fail();
    }
}