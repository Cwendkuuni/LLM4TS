package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;

import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);

        // Check that classLoader has the expected URLs
        URL[] urls = classLoader.getURLs();
        Vector expectedUrls = new Vector();
        addUrls(expectedUrls, Constants.XBUS_HOME + "/lib");
        addUrls(expectedUrls, Constants.XBUS_HOME + "/lib/runtime");
        addUrls(expectedUrls, Constants.XBUS_HOME + "/plugin/lib");
        addUrls(expectedUrls, Constants.XBUS_HOME + "/test/lib");

        for (int i = 0; i < urls.length; ++i) {
            URL url = urls[i];
            String urlString = url.toString();
            String expectedUrlString = expectedUrls.elementAt(i).toString();
            assertTrue(urlString.startsWith(expectedUrlString));
        }
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        addUrls(urls, "testDir");

        File testDir = new File("testDir");
        File[] files = testDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            if (file.getName().endsWith("jar")) {
                try {
                    URL url = file.toURL();
                    urls.add(url);
                }
                catch (MalformedURLException e) {
                    Trace.error(e);
                    System.exit(1);
                }
            }
        }

        assertEquals(files.length, urls.size());
    }

    @Test
    public void testStaticInitialization() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);

        XBUSClassLoader anotherClassLoader = XBUSClassLoader.getInstance(parent);
        assertSame(classLoader, anotherClassLoader);

        // Test that classLock prevents multiple instance creation
        ClassLoader anotherParent = anotherClassLoader.getParent();
        XBUSClassLoader anotherClassLoader2 = XBUSClassLoader.createClassLoader(anotherParent);
        assertNotNull(anotherClassLoader2);
        assertNotSame(classLoader, anotherClassLoader2);
    }

    @Test
    public void testNullXBUS_HOME() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        try {
            XBUSClassLoader.createClassLoader(parent);
            assertFalse(true);
        }
        catch (Error e) {
            assertEquals("XBUS_HOME has not been set!", e.getMessage());
        }
    }
}