package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testInstance() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testGetInstanceWithNullParent() {
        assertThrows(NullPointerException.class, () -> XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testAddUrls() {
        String dirName = "test/resources/lib";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        for (Object urlObj : urls) {
            URL url = (URL) urlObj;
            assertNotNull(url);
        }
    }

    @Test
    public void testAddUrlsWithNullDirName() {
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, null);
        assertFalse(urls.isEmpty());
        assertNull(urls.elementAt(0));
    }

    @Test
    public void testAddUrlsWithEmptyDirName() {
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "");
        assertFalse(urls.isEmpty());
        assertNull(urls.elementAt(0));
    }

    @Test
    public void testAddUrlsWithInvalidDirName() {
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "invalidDirName");
        assertFalse(urls.isEmpty());
        assertNull(urls.elementAt(0));
    }

    @Test
    public void testAddUrlsWithInvalidFile() {
        String dirName = "test/resources";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        for (Object urlObj : urls) {
            URL url = (URL) urlObj;
            assertNotNull(url);
            File file = new File(url.getPath());
            assertFalse(file.exists());
        }
    }

    @Test
    public void testAddUrlsWithInvalidJarOrZip() {
        String dirName = "test/resources";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        for (Object urlObj : urls) {
            URL url = (URL) urlObj;
            assertNotNull(url);
            File file = new File(url.getPath());
            assertFalse(file.exists());
            String fileName = file.getName();
            assertFalse(fileName.endsWith(".jar"));
            assertFalse(fileName.endsWith(".zip"));
        }
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        assertThrows(NullPointerException.class, () -> XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testCreateClassLoaderWithEmptyUrls() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        Vector urls = new Vector<>();
        XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithNullUrlArray() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = new XBUSClassLoader(null, parent);
        assertNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithEmptyParent() {
        ClassLoader parent = null;
        assertThrows(NullPointerException.class, () -> new XBUSClassLoader(new URL[0], parent));
    }

    @Test
    public void testCreateClassLoaderWithNullUrl() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = new XBUSClassLoader(new URL[]{null}, parent);
        assertNull(classLoader);
    }
}