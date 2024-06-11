package net.sf.xbus.base.core.reflection;

import net.sf.xbus.base.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullParent() {
        XBUSClassLoader.getInstance(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateClassLoaderNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertEquals(4, urls.size());
        for (int i = 0; i < urls.size(); ++i) {
            URL url = urls.elementAt(i);
            String expectedUrl = "testDir/testFile" + (i + 1) + ".jar";
            assertTrue(url.toString().startsWith(expectedUrl));
        }
    }

    @Test
    public void testCreateClassLoaderWithEmptyDir() {
        ClassLoader parent = getClass().getClassLoader();
        Vector urls = new Vector();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        XBUSClassLoader.addUrls(urls, "emptyDir");
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        classLoader = new XBUSClassLoader(urlArray, parent);
        assertNotNull(classLoader);
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsMalformedURL() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "badURL");
    }

    @Test
    public void testGetInstanceTwice() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
        assertSame(classLoader, classLoader2);
    }

}