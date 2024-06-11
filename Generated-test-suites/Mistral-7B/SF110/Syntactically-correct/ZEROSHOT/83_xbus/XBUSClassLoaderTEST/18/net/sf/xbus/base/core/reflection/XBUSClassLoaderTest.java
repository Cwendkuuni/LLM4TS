package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * XBUSClassLoaderTest class file which contains all JUnit 4 test methods to
 * comprehensively test the XBUSClassLoader class.
 */
public class XBUSClassLoaderTest {

    private static XBUSClassLoader mClassLoader;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void init() {
        Constants.setXBUS_HOME(new File("src/test/resources/xbus-home").getAbsolutePath());
    }

    @Test
    public void testGetInstance() {
        mClassLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(mClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        mClassLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(mClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "src/test/resources/xbus-home/lib");
        assertEquals(4, urls.size());
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            urlArray[i] = urls.elementAt(i);
        }
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, null);
        assertNotNull(classLoader);
    }

    @Test
    public void testGetInstanceWithParent() {
        XBUSClassLoader parent = XBUSClassLoader.createClassLoader(null);
        mClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(mClassLoader);
    }

    @Test
    public void testFailIfXBUS_HOME_Is_Null() {
        Constants.setXBUS_HOME(null);
        expectedException.expect(RuntimeException.class);
        XBUSClassLoader.createClassLoader(null);
    }

    @Test
    public void testLoadClass() {
        Class<?> clazz = null;
        try {
            clazz = mClassLoader.loadClass("net.sf.xbus.base.core.XBUSLogger");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException should not be thrown.");
        }
        assertNotNull(clazz);
    }

    @Test
    public void testLoadClassWithPackage() {
        Class<?> clazz = null;
        try {
            clazz = mClassLoader.loadClass("net.sf.xbus.base.core.trace.XBUSLogger");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException should not be thrown.");
        }
        assertNotNull(clazz);
    }

    @Test
    public void testLoadClassWithInvalidExtension() {
        expectedException.expect(MalformedURLException.class);
        XBUSClassLoader.addUrls(new Vector<URL>(), "src/test/resources/xbus-home/lib/invalidFile");
        XBUSClassLoader loader = new XBUSClassLoader(new URL[0], null);
        loader.loadClass("net.sf.xbus.base.core.XBUSLogger");
    }

    @Test
    public void testLoadClassWithInvalidFileName() {
        expectedException.expect(MalformedURLException.class);
        XBUSClassLoader.addUrls(new Vector<URL>(), "src/test/resources/xbus-home/lib/invalidFileName.jar");
        XBUSClassLoader loader = new XBUSClassLoader(new URL[0], null);
        loader.loadClass("net.sf.xbus.base.core.XBUSLogger");
    }
}