package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private static final String XBUS_HOME = "path/to/xbus/home";
    private static XBUSClassLoader mClassLoader;

    @Before
    public void setUp() {
        System.setProperty("XBUS_HOME", XBUS_HOME);
        assumeTrue("XBUS_HOME is set", Constants.XBUS_HOME != null);
        mClassLoader = XBUSClassLoader.getInstance(URLClassLoader.class.getClassLoader());
    }

    @After
    public void tearDown() {
        XBUSClassLoader.mClassLoader = null;
    }

    @Test
    public void testInstance() {
        assertNotNull("XBUSClassLoader instance should not be null", XBUSClassLoader.getInstance(URLClassLoader.class.getClassLoader()));
    }

    @Test
    public void testCreateClassLoader() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, URLClassLoader.class.getClassLoader());
        assertNotNull("XBUSClassLoader instance should not be null", classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
        assertTrue("At least one URL should be added", urls.size() > 0);

        File libPath = new File(Constants.XBUS_HOME + "/lib");
        File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                URL url = jars[i].toURL();
                assertTrue("URL for " + jars[i].getName() + " should be present", urls.contains(url));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateClassLoaderNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderEmptyVector() {
        Vector urls = new Vector();
        XBUSClassLoader.createClassLoader(urls.toArray(new URL[0]));
    }
}