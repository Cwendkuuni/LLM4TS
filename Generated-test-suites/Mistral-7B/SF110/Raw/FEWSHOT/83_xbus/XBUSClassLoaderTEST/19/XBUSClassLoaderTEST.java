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

import net.sf.xbus.base.core.XBUSClassLoader;
import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace; // Assuming Trace is a static class

public class XBUSClassLoaderTest {

    @Test
    public void testgetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test
    public void testGetInstanceAndCreateClassLoader() {
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(null);
        XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(classLoader1);
        assertNotNull(classLoader2);
        assertSame(classLoader1, classLoader2.getParent());
    }

    @Test
    public void testAddUrls() {
        String dirName = "testDir";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);

        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();
        int expectedFilesCount = jars != null ? jars.length : 0;

        assertEquals(expectedFilesCount, urls.size());

        for (Object urlObj : urls) {
            URL url = (URL) urlObj;
            assertNotNull(url);
        }
    }

    @Test
    public void testCreateClassLoaderWithEmptyDir() {
        String dirName = "testEmptyDir";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);

        File libPath = new File(dirName);
        assertFalse(libPath.exists());

        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
        assertNull(classLoader.findLoadedClass("NonExistentClass"));
    }

    @Test
    public void testCreateClassLoaderWithInvalidUrls() {
        String dirName = "testDir";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);

        File libPath = new File(dirName);
        File invalidFile = new File(libPath, "invalidFile.txt");
        invalidFile.createNewFile();

        try {
            URL invalidUrl = invalidFile.toURL();
            urls.add(invalidUrl);
            XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
            assertNotNull(classLoader);
            assertNull(classLoader.findLoadedClass("NonExistentClass"));
            invalidFile.delete();
        } catch (MalformedURLException e) {
            Trace.error(e); // Assuming Trace is a static class and has an error method
            System.exit(1);
        }
    }

    @Test
    public void testCreateClassLoaderWithJarFiles() throws MalformedURLException {
        String dirName = "testDir";
        Vector urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);

        File libPath = new File(dirName);
        File jarFile = new File(libPath, "test.jar");
        jarFile.createNewFile();
        URL jarUrl = jarFile.toURL();
        urls.add(jarUrl);

        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
        Class<?> testClass = classLoader.findLoadedClass("test.TestClass");
        assertNotNull(testClass);

        jarFile.delete();
    }

    static class TestClass {
        // Add methods and fields here if needed
    }
}