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

    private static final String XBUS_HOME = "path/to/xbus/home";

    @BeforeClass
    public static void setupClass() {
        System.setProperty("XBUS_HOME", XBUS_HOME);
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(URLClassLoader.class);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(URLClassLoader.class);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "path/to/xbus/home/lib");
        assertEquals(4, urls.size());
        assertNull(urls.elementAt(0)); // null check for empty element at index 0
        URL expectedUrl1 = new URL("file:path/to/xbus/home/lib/jar1.jar");
        URL expectedUrl2 = new URL("file:path/to/xbus/home/lib/jar2.jar");
        URL expectedUrl3 = new URL("file:path/to/xbus/home/lib/jar3.jar");
        URL expectedUrl4 = new URL("file:path/to/xbus/home/lib/jar4.jar");
        for (int i = 1; i < urls.size(); ++i) {
            URL url = (URL) urls.elementAt(i);
            if (url.equals(expectedUrl1)) {
                assertTrue(url.getFile().endsWith("/lib/jar1.jar"));
                break;
            }
            if (url.equals(expectedUrl2)) {
                assertTrue(url.getFile().endsWith("/lib/jar2.jar"));
                break;
            }
            if (url.equals(expectedUrl3)) {
                assertTrue(url.getFile().endsWith("/lib/jar3.jar"));
                break;
            }
            if (url.equals(expectedUrl4)) {
                assertTrue(url.getFile().endsWith("/lib/jar4.jar"));
                break;
            }
        }
    }

    @Test
    public void testCreateClassLoaderWithNullXBUSHome() {
        System.clearProperty("XBUS_HOME");
        try {
            XBUSClassLoader.createClassLoader(URLClassLoader.class);
            fail("Expected IllegalStateException when XBUS_HOME is not set.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testCreateClassLoaderWithEmptyXBUSHome() {
        System.setProperty("XBUS_HOME", "");
        try {
            XBUSClassLoader.createClassLoader(URLClassLoader.class);
            fail("Expected IllegalStateException when XBUS_HOME is empty.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        try {
            XBUSClassLoader.createClassLoader(null);
            fail("Expected NullPointerException when parent is null.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCreateClassLoaderWithNonExistentXBUSHome() throws IOException {
        System.setProperty("XBUS_HOME", "non/existent/path");
        try {
            XBUSClassLoader.createClassLoader(URLClassLoader.class);
            fail("Expected IOException when XBUS_HOME does not exist.");
        } catch (IOException e) {
            // expected
        }
    }
}