package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        assertNotNull(XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testCreateClassLoader() {
        assertNotNull(XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testAddUrls() {
        final String xbusHome = System.getProperty("XBUS_HOME");
        if (xbusHome == null) {
            Trace.error("XBUS_HOME has not been set!");
            return;
        }

        final Vector urlVector = new Vector();
        final String dirName = xbusHome + "/lib";
        XBUSClassLoader.addUrls(urlVector, dirName);

        final int size = urlVector.size();
        for (int i = 0; i < size; ++i) {
            final URL url = urlVector.elementAt(i);
            assertTrue(url.toString().startsWith(dirName));
            assertTrue(url.toString().endsWith("/"));
            assertTrue(new File(new URL(url), "MANIFEST.MF").exists());
        }

        // Test for incorrect file type
        final String invalidFileName = xbusHome + "/lib/invalidFile";
        XBUSClassLoader.addUrls(urlVector, invalidFileName);
        assertTrue(urlVector.size() == size);

        // Test for non-existent directory
        final String nonExistentDirName = xbusHome + "/nonExistentDir";
        XBUSClassLoader.addUrls(urlVector, nonExistentDirName);
        assertTrue(urlVector.size() == size);

        // Test for MalformedURLException
        final String malformedUrlString = xbusHome + ":123";
        try {
            XBUSClassLoader.addUrls(urlVector, malformedUrlString);
            assertTrue(false); // This line should not be executed
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor() {
        final URL[] urlArray = new URL[] { new File("test.jar").toURL() };
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, null);
        assertNotNull(classLoader);
    }
}