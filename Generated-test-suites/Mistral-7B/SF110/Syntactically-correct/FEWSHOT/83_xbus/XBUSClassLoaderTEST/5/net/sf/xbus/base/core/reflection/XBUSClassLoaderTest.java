package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Trace; // Assuming Trace is a static logging utility class

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ XBUSClassLoader.class })
public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithValidHomeDirectory() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        String xbusHome = "testXBUSHomeDirectory"; // Set an appropriate value for XBUS_HOME
        System.setProperty("XBUS_HOME", xbusHome);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
        Vector urls = new Vector();
        classLoader.addUrls(urls, xbusHome + "/lib");
        classLoader.addUrls(urls, xbusHome + "/lib/runtime");
        classLoader.addUrls(urls, xbusHome + "/plugin/lib");
        classLoader.addUrls(urls, xbusHome + "/test/lib");
        assertTrue(urls.size() == 4);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateClassLoaderWithNullHomeDirectory() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader.createClassLoader(parent); // Should throw an IllegalStateException since XBUS_HOME is null
    }

    @Test
    public void testAddUrls() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        String xbusHome = "testXBUSHomeDirectory"; // Set an appropriate value for XBUS_HOME
        System.setProperty("XBUS_HOME", xbusHome);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        Vector urls = new Vector();
        File libPath = new File(xbusHome + "/lib");
        File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                URL url = jars[i].toURL();
                classLoader.addUrls(urls, xbusHome + "/lib");
                assertTrue(urls.size() == i + 1);
                assertTrue(urls.elementAt(i).toString().startsWith(xbusHome + "/lib/"));
            }
        }
    }
}