package net.sf.xbus.base.core.reflection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import net.sf.xbus.base.core.Constants;

public class XBUSClassLoaderTest {
    private static XBUSClassLoader classLoader;
    private static Vector urls;

    @BeforeClass
    public static void setup() {
        classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        urls = new Vector();
    }

    @Test
    public void testInstance() {
        Assert.assertNotNull("XBUSClassLoader instance should not be null", classLoader);
        Assert.assertNotNull("Parent class loader should not be null", classLoader.getParent());
        Assert.assertEquals("Class loader URLs should match", Constants.XBUS_HOME, classLoader.getURLs()[0].getPath());
    }

    @Test
    public void testCreateClassLoader() {
        Assert.assertNotNull("XBUSClassLoader instance should not be null", classLoader);
        Assert.assertNotNull("Parent class loader should not be null", classLoader.getParent());

        URL[] urls = classLoader.getURLs();
        for (URL url : urls) {
            Assert.assertNotNull("URLs should not be null", url);
            Assert.assertTrue("URLs should start with XBUS_HOME", url.getPath().startsWith(Constants.XBUS_HOME));
        }
    }

    @Test
    public void testAddUrls() {
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
        Assert.assertEquals("Number of URLs should be greater than 0", 1, urls.size());
        Assert.assertNotNull("URL should not be null", urls.elementAt(0));
        Assert.assertTrue("URL path should start with XBUS_HOME", urls.elementAt(0).toString().startsWith(Constants.XBUS_HOME + "/lib"));

        File libPath = new File(Constants.XBUS_HOME + "/plugin/lib");
        Assert.assertTrue("Plugin library path should exist", libPath.exists());
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/plugin/lib");
        Assert.assertEquals("Number of URLs should be greater than 1", 2, urls.size());
    }

    @Before
    public void setUp() {
    }

    @AfterClass
    public static void teardown() {
        if (classLoader != null) {
            classLoader = null;
        }
        if (urls != null) {
            urls = null;
        }
    }

    @Test
    public void testGetInstanceWithNullParent() {
        Assert.assertThrows(IllegalArgumentException.class, () -> XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testGetInstanceWithEmptyURLArray() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new XBUSClassLoader(new URL[0], null));
    }

    @Test
    public void testGetInstanceWithNullURLArray() {
        Assert.assertThrows(NullPointerException.class, () -> new XBUSClassLoader(null, null));
    }

    @Test
    public void testGetInstanceWithInvalidURL() {
        Assert.assertThrows(MalformedURLException.class, () -> new XBUSClassLoader(new URL[]{"invalid_url"}, null));
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        Assert.assertThrows(IllegalArgumentException.class, () -> XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testCreateClassLoaderWithEmptyURLArray() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new XBUSClassLoader(new URL[0], null));
    }

    @Test
    public void testCreateClassLoaderWithNullURLArray() {
        Assert.assertThrows(NullPointerException.class, () -> new XBUSClassLoader(null, null));
    }

    @Test
    public void testCreateClassLoaderWithInvalidURLs() {
        Assert.assertThrows(MalformedURLException.class, () -> new XBUSClassLoader(new URL[]{"invalid_url"}, null));
    }

    @Test
    public void testGetParent() {
        Assert.assertNotNull("Parent class loader should not be null", classLoader.getParent());
    }

    @Test
    public void testGetURLs() {
        URL[] urls = classLoader.getURLs();
        Assert.assertNotNull("URLs should not be null", urls);
        Assert.assertEquals("Number of URLs should be greater than 0", 4, urls.length);
    }
}