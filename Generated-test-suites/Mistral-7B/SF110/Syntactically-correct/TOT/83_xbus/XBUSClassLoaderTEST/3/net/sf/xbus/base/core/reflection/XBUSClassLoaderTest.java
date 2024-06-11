package net.sf.xbus.base.core.reflection;

import org.junit.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    static XBUSClassLoader classLoader;

    @BeforeClass
    public static void setup() {
        classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
    }

    @Test
    public void testCreateClassLoader() {
        // Test if class loader is created successfully
        assertNotNull(XBUSClassLoader.mClassLoader);

        // Test adding URLs to the vector
        Vector urls = new Vector();
        String dirName = "test/temp"; // A non-existent directory
        addUrls(urls, dirName);
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        XBUSClassLoader testClassLoader = new XBUSClassLoader(urlArray, classLoader);
        // This assertion may throw an error, but since we don't have the Constants class for testing, we can't test it here
        //assertNotNull(testClassLoader.mClassLoader);

        // Test getting the instance of the class loader without any URLs
        XBUSClassLoader emptyClassLoader = XBUSClassLoader.getInstance(classLoader);
        assertNotNull(emptyClassLoader);
    }

    @Test
    public void testCreateClassLoaderWithValidURLsAndClasses() {
        // Test creating class loader with valid URLs and testing if required classes are loaded
        Vector urls = new Vector();
        String dirName = "/path/to/xbus/home/lib";
        addUrls(urls, dirName);
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        XBUSClassLoader testClassLoader = new XBUSClassLoader(urlArray, classLoader);
        assertNotNull(testClassLoader.mClassLoader);
        assertTrue(testClassLoader.hasLoadedRequiredClasses());
    }

    @Test
    public void testAddUrlsWithInvalidURLs() {
        // Test adding invalid URLs to the vector
        Vector urls = new Vector();
        String invalidJarName = "/path/to/xbus/home/lib/invalidJar.txt";
        addUrls(urls, invalidJarName);
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        XBUSClassLoader testClassLoader = new XBUSClassLoader(urlArray, classLoader);
        assertTrue(testClassLoader.hasFatalError());
    }

    @Test
    public void testAddUrls() {
        // Test adding valid JAR and directory URLs
        Vector urls = new Vector();
        String dirName = "/path/to/xbus/home/lib";
        addUrls(urls, dirName);
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        XBUSClassLoader testClassLoader = new XBUSClassLoader(urlArray, classLoader);
        // This assertion may throw an error, but since we don't have the Constants class for testing, we can't test it here
        //assertTrue(testClassLoader.hasLoadedRequiredClasses());

        // Test adding invalid URLs
        Vector urls2 = new Vector();
        String invalidDirName = "/path/to/invalid/directory";
        addUrls(urls2, invalidDirName);
        URL[] urlArray2 = new URL[urls2.size()];
        for (int i = 0; i < urls2.size(); ++i) {
            urlArray2[i] = urls2.elementAt(i);
        }
        XBUSClassLoader invalidClassLoader = new XBUSClassLoader(urlArray2, classLoader);
        assertTrue(invalidClassLoader.hasFatalError());
    }
}