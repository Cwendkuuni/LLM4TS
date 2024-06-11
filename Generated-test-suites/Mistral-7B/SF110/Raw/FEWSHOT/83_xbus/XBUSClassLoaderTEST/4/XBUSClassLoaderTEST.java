package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XBUSClassLoaderTest {

    private static final String XBUS_HOME = "test/xbus_home";

    private XBUSClassLoader xbusClassLoader;
    private ClassLoader parent;
    private Vector urls;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parent = mock(URLClassLoader.class);
        urls = new Vector<>();
    }

    @Test
    public void testInstanceNullParent() {
        assertNull(XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testInstanceWithParent() {
        xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertEquals(xbusClassLoader, XBUSClassLoader.getInstance(parent));
        verify(parent, times(1)).getResources(".");
    }

    @Test
    public void testCreateClassLoaderNullParent() {
        assertNull(XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testCreateClassLoaderWithParent() {
        when(parent.getResources(".")).thenReturn(getClass().getResource("/test"));

        xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertEquals(xbusClassLoader, XBUSClassLoader.getInstance(parent));
        verify(parent, times(1)).getResources(".");
    }

    @Test
    public void testAddUrlsEmptyDirName() {
        XBUSClassLoader.addUrls(urls, "");
        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrlsNonExistentDirName() {
        File libPath = new File("non_existent_dir");
        XBUSClassLoader.addUrls(urls, libPath.getAbsolutePath());
        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrlsExistentDirWithJarFile() throws IOException {
        File libPath = new File(XBUS_HOME);
        File jarFile = new File(libPath, "xbus.jar");
        URL url = jarFile.toURL();

        XBUSClassLoader.addUrls(urls, XBUS_HOME);
        assertEquals(1, urls.size());
        URL urlAtIndex = urls.elementAt(0);
        assertEquals(url, urlAtIndex);
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsExistentDirWithInvalidFileName() throws IOException {
        File libPath = new File(XBUS_HOME);
        File invalidFile = new File(libPath, "invalid_file");

        XBUSClassLoader.addUrls(urls, XBUS_HOME);
        XBUSClassLoader.addUrls(urls, invalidFile.getAbsolutePath());
        fail("Should throw MalformedURLException");
    }

    @Test
    public void testConstructorWithUrlArrayAndParent() {
        URL[] urlArray = new URL[]{new URL("file:/test/test.jar")};
        xbusClassLoader = new XBUSClassLoader(urlArray, parent);
        assertEquals(1, xbusClassLoader.getParent().getParent().getURLs().length);
    }

    @Test
    public void testGetInstanceWithXBUS_HOMESet() {
        System.setProperty(Constants.XBUS_HOME, XBUS_HOME);
        xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertEquals(xbusClassLoader, XBUSClassLoader.getInstance(parent));
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceWithoutXBUS_HOMESet() {
        System.setProperty(Constants.XBUS_HOME, null);
        XBUSClassLoader.getInstance(parent);
        fail("Should throw RuntimeException");
    }

    @Test
    public void testCreateClassLoaderWithXBUS_HOMESet() {
        System.setProperty(Constants.XBUS_HOME, XBUS_HOME);
        xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertEquals(xbusClassLoader, XBUSClassLoader.getInstance(parent));
    }

    @Test
    public void testCreateClassLoaderWithoutXBUS_HOMESet() {
        System.setProperty(Constants.XBUS_HOME, null);
        XBUSClassLoader.createClassLoader(parent);
        assertNull(XBUSClassLoader.mClassLoader);
    }
}