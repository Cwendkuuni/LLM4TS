package net.sf.xbus.base.core.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
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
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(classLoader);

        XBUSClassLoader anotherClassLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotSame(classLoader, anotherClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "src/test/resources/testDir"; // Replace with the actual test directory
        XBUSClassLoader.addUrls(urls, dirName);

        Assert.assertEquals(3, urls.size()); // Replace with the expected number of URLs

        for (int i = 0; i < urls.size(); ++i) {
            URL url = (URL) urls.elementAt(i);
            Assert.assertNotNull(url);
        }
    }
}