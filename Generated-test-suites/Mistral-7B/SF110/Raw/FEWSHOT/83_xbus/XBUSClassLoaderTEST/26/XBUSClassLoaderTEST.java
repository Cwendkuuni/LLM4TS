package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XBUSClassLoaderTest {

    private static ClassLoader parent;
    private static XBUSClassLoader classUnderTest;

    @BeforeClass
    public static void setUpClass() {
        MockitoAnnotations.initMocks(XBUSClassLoaderTest.class);
        parent = mock(ClassLoader.class);
    }

    @Test
    public void testInstance() {
        classUnderTest = XBUSClassLoader.getInstance(parent);
        assertNotNull(classUnderTest);
    }

    @Test(expected = IllegalStateException.class)
    public void testInstanceWithNullParent() {
        classUnderTest = XBUSClassLoader.getInstance(null);
    }

    @Test
    public void testCreateClassLoader() {
        when(Constants.XBUS_HOME).thenReturn(".");

        classUnderTest = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classUnderTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderWithNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test
    public void testAddUrls() throws IOException {
        String xbusHome = "test/resources/xbus";
        File libPath = new File(xbusHome + "/lib");

        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, xbusHome + "/lib");

        assertEquals(3, urls.size());
        URL[] expectedUrls = new URL[] { new File(xbusHome + "/lib/commons-cli-1.4.jar").toURL(),
                new File(xbusHome + "/lib/xbus-api-1.0.jar").toURL(),
                new File(xbusHome + "/lib/xbus-core-1.0.jar").toURL() };

        for (int i = 0; i < urls.size(); ++i) {
            URL url = urls.elementAt(i);
            assertEquals(expectedUrls[i].toString(), url.toString());
        }
    }

    @Test
    public void testGetInstanceWithDefaultParent() {
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        classUnderTest = XBUSClassLoader.getInstance(parent);
        assertNotNull(classUnderTest);
    }
}