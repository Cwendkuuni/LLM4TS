package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import net.sf.xbus.base.core.trace.Trace;

import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private static XBUSClassLoader mClassLoader;
    private static final String XBUS_HOME = "path/to/xbus/home";

    @BeforeClass
    public static void init() {
        System.setProperty("XBUS_HOME", XBUS_HOME);
    }

    @Test
    public void testInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(URLClassLoader.class);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(URLClassLoader.class);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
        assertEquals(3, urls.size());
        File libPath = new File(XBUS_HOME + "/lib");
        File[] jars = libPath.listFiles();
        for (int i = 0; i < jars.length; ++i) {
            if (jars[i].isFile()) {
                String name = jars[i].getName();
                assertTrue(name.endsWith("jar") || name.endsWith("zip"));
                URL url = urls.elementAt(i);
                assertEquals(jars[i].toURI().toURL(), url);
            }
        }
    }

    @Test
    public void testGetInstanceNullXBUS_HOME() {
        System.clearProperty("XBUS_HOME");
        try {
            XBUSClassLoader.getInstance(URLClassLoader.class);
            fail("Expected XBUS_HOME has not been set!");
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testGetInstanceInvalidXBUS_HOME() {
        System.setProperty("XBUS_HOME", "");
        try {
            XBUSClassLoader.getInstance(URLClassLoader.class);
            fail("Expected XBUS_HOME has not been set!");
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testGetInstanceNoLibDirectory() {
        System.setProperty("XBUS_HOME", "path/to/xbus/home/without/lib");
        try {
            XBUSClassLoader.getInstance(URLClassLoader.class);
            fail("Expected XBUS_HOME does not contain lib directory!");
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testGetInstanceEmptyLibDirectory() {
        System.setProperty("XBUS_HOME", "path/to/xbus/home/lib");
        try {
            XBUSClassLoader classLoader = XBUSClassLoader.getInstance(URLClassLoader.class);
            assertNotNull(classLoader);
            assertFalse(classLoader.getParent() instanceof XBUSClassLoader);
            Vector urls = new Vector();
            Enumeration<URL> enumeration = classLoader.getResources("/");
            for (; enumeration.hasMoreElements(); ) {
                URL url = enumeration.nextElement();
                urls.add(url);
            }
            assertEquals(3, urls.size());
            for (int i = 0; i < urls.size(); ++i) {
                URL url = urls.elementAt(i);
                assertNotNull(url);
                File file = new File(url.getPath());
                assertTrue(file.exists());
                assertTrue(file.isFile());
                assertTrue(file.getName().endsWith("jar") || file.getName().endsWith("zip"));
            }
        } catch (IOException e) {
            Trace.error(e);
            System.exit(1);
        }
    }

    @Test
    public void testGetInstanceWithParent() {
        ClassLoader parent = new URLClassLoader(new URL[] { new File("path/to/parent.jar").toURL() }, null);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent.getClass());
        assertNotNull(classLoader);
        assertFalse(classLoader.getParent() instanceof XBUSClassLoader);
        assertNull(classLoader.getParent().getParent());
        assertNotNull(classLoader.getParent());
        assertEquals(parent.getClass(), classLoader.getParent().getClass());
    }

    @Test
    public void testGetInstanceWithXBUSHomeParent() {
        ClassLoader parent = XBUSClassLoader.getInstance(URLClassLoader.class);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent.getClass());
        assertNotNull(classLoader);
        assertFalse(classLoader.getParent() instanceof XBUSClassLoader);
        assertNotNull(classLoader.getParent());
        assertEquals(parent, classLoader.getParent());
    }
}