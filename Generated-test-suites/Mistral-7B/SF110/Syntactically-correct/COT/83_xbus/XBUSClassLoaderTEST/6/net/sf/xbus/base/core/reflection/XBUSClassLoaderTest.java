package net.sf.xbus.base.core.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "testDir"; // replace with a valid directory path
        XBUSClassLoader.addUrls(urls, dirName);
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = (URL) urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader( urlArray, getClass().getClassLoader());
        Assert.assertTrue(urls.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullParent() {
        XBUSClassLoader.getInstance(null);
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsInvalidURL() {
        Vector urls = new Vector();
        String dirName = "testDir"; // replace with a valid directory path
        XBUSClassLoader.addUrls(urls, dirName);
        urls.add("invalidURL"); // replace with an invalid URL
        try {
            XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
        } catch (MalformedURLException e) {
            Assert.assertEquals("Invalid URL", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsNullDirName() {
        Vector urls = new Vector();
        String dirName = null;
        XBUSClassLoader.addUrls(urls, dirName);
        Trace.enableTrace(); // Uncomment this line to see the trace output
        XBUSClassLoader.getInstance(getClass().getClassLoader());
        Assert.fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateClassLoaderNullParent() {
        XBUSClassLoader classLoader = new XBUSClassLoader(null, getClass().getClassLoader());
        Assert.fail("NullPointerException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderEmptyUrls() {
        ClassLoader parent = getClass().getClassLoader();
        Vector urls = new Vector();
        XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        Assert.fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateClassLoaderMalformedURLs() {
        ClassLoader parent = getClass().getClassLoader();
        Vector urls = new Vector();
        urls.add("invalidURL"); // replace with an invalid URL
        XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        Assert.fail("MalformedURLException expected but not thrown");
    }

    @Test
    public void testStaticInitialization() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testTraceEnabled() {
        XBUSClassLoader.getInstance(getClass().getClassLoader());
        Trace.isTraceEnabled(); // Uncomment this line to enable trace output and check the console
        Assert.assertTrue(Trace.isTraceEnabled());
    }

    @Test
    public void testTraceDisabled() {
        XBUSClassLoader.getInstance(getClass().getClassLoader());
        Trace.setTraceEnabled(false);
        Trace.isTraceEnabled();
        Assert.assertFalse(Trace.isTraceEnabled());
    }
}