 package net.sf.xbus.base.core.reflection;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.util.*;
import java.io.*;
import java.net.*;
import org.junit.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @BeforeClass
    public static void setUpClass() {
        // Initialize XBUSClassLoader before running tests
        XBUSClassLoader.getInstance(null);
    }

    @AfterClass
    public static void tearDownClass() {
        // Release any resources or reset any state
    }

    @Test
    public void testInstance() {
        ClassLoader parent = this.getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(classLoader);
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceWithNullParent() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
    }

    @Test
    public void testGetInstance() {
        ClassLoader parent = this.getClass().getClassLoader();
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);

        Assert.assertSame(classLoader1, classLoader2);
    }

    @Test
    public void testGetInstanceDifferentParents() {
        ClassLoader parent1 = this.getClass().getClassLoader();
        ClassLoader parent2 = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent1);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent2);

        Assert.assertNotSame(classLoader1, classLoader2);
    }

    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector urls = new Vector();
        String dirName = "test/lib";
        XBUSClassLoader.addUrls(urls, dirName);

        // Test with an existing directory
        URL[] expectedUrls = {new File("test/lib/test-1.jar").toURL(), new File("test/lib/test-2.jar").toURL()};
        URL[] actualUrls = urls.toArray(new URL[0]);
        Assert.assertArrayEquals(expectedUrls, actualUrls);

        // Test with a non-existent directory
        dirName = "test/nonexistent";
        urls = new Vector();
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertEquals(0, urls.size());

        // Test with an empty directory
        dirName = "test/empty";
        urls = new Vector();
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertEquals(0, urls.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddUrlsWithNullDirectory() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, null);
    }
}