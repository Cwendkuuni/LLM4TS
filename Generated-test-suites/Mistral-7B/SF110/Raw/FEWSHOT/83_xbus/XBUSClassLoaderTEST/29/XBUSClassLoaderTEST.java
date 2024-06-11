package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import org.junit.Test;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderAddUrls() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        Vector urls = new Vector();
        String xbusHome = System.getProperty("XBUS_HOME");
        addUrls(urls, xbusHome + "/lib");
        addUrls(urls, xbusHome + "/lib/runtime");
        addUrls(urls, xbusHome + "/plugin/lib");
        addUrls(urls, xbusHome + "/test/lib");

        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }

        XBUSClassLoader expectedClassLoader = new XBUSClassLoader(urlArray, parent);
        assertEquals(expectedClassLoader.getURLs(), classLoader.getURLs());
    }

    @Test
    public void testAddUrls() {
        String dirName = "test";
        Vector urls = new Vector();
        File libPath = new File(dirName);
        addUrls(urls, dirName);

        URL[] expectedUrls = new URL[] { new File("test/lib1.jar").toURL(), new File("test/lib2.jar").toURL() };
        assertEquals(expectedUrls.length, urls.size());
        for (int i = 0; i < expectedUrls.length; ++i) {
            URL url = urls.elementAt(i);
            assertEquals(expectedUrls[i].toString(), url.toString());
        }
    }

    @Test
    public void testGetInstanceWithNullXBUS_HOME() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        assertThrows(RuntimeException.class, () -> XBUSClassLoader.getInstance(parent));
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        assertThrows(NullPointerException.class, () -> new XBUSClassLoader(null, null));
    }

    private void addUrls(Vector urls, String dirName) {
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();
        for (File jar : jars) {
            if (jar != null && (jar.isFile() || jar.isDirectory())) {
                if (jar.isFile() && (jar.getName().endsWith("jar") || jar.getName().endsWith("zip"))) {
                    try {
                        urls.add(jar.toURL());
                    } catch (MalformedURLException e) {
                        fail("Failed to create URL from file: " + jar.getAbsolutePath());
                    }
                } else if (jar.isDirectory()) {
                    File[] files = jar.listFiles();
                    for (File file : files) {
                        addUrls(urls, file.getAbsolutePath());
                    }
                }
            }
        }
    }
}