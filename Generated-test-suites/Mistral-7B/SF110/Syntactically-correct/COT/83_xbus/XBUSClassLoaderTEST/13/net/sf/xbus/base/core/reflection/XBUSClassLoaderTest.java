package net.sf.xbus.base.core.reflection.test;

import org.junit.Assert;
import org.junit.Test;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(XBUSClassLoader.class.getClassLoader());
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
        Assert.assertFalse(urls.isEmpty());

        String dirName = Constants.XBUS_HOME + "/lib";
        File libPath = new File(dirName);
        Assert.assertTrue(libPath.exists());
        Assert.assertFalse(libPath.isFile());
        Assert.assertTrue(libPath.isDirectory());

        File[] jars = libPath.listFiles();
        Assert.assertNotNull(jars);
        for (File jar : jars) {
            if (jar.isFile()) {
                String jarName = jar.getName();
                Assert.assertFalse(jarName.isEmpty());
                if (jarName.endsWith("jar")) {
                    URL url = jar.toURL();
                    Assert.assertEquals(url.getProtocol(), "file");
                    Assert.assertEquals(url.getFile().endsWith(jarName), true);
                    urls.add(url);
                }
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAddUrlsNullParent() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsEmptyDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsNonExistentDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "nonexistentpath");
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsInvalidUrl() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "invalidurl");
    }

    @Test
    public void testGetInstanceWithEmptyXBUS_HOME() {
        Constants.XBUS_HOME = null;
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        Assert.assertNull(classLoader);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetInstanceTwice() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
    }

    @Test
    public void testGetInstanceTraceError() {
        Constants.XBUS_HOME = "nonexistentpath";
        try {
            XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
            Assert.fail();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Assert.assertTrue(errorMessage.contains("XBUS_HOME has not been set!"));
        }
    }

}