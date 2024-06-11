package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XBUSClassLoaderTest {

    private static final String XBUS_HOME = "test/resources/xbus-home";
    private static final ClassLoader MOCK_PARENT_CLASSLOADER = mock(ClassLoader.class);
    private static XBUSClassLoader mClassLoader;
    private static final Object classLock = XBUSClassLoader.class;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("XBUS_HOME", XBUS_HOME);
        mClassLoader = XBUSClassLoader.getInstance(MOCK_PARENT_CLASSLOADER);
    }

    @Test
    public void testInstance() {
        assertEquals(XBUSClassLoader.class, mClassLoader.getClass());
        assertNotNull(mClassLoader.getParent());
        assertEquals(MOCK_PARENT_CLASSLOADER, mClassLoader.getParent());
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance1 = XBUSClassLoader.getInstance(MOCK_PARENT_CLASSLOADER);
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(MOCK_PARENT_CLASSLOADER);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        assertThrows(IllegalArgumentException.class, () -> XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testCreateClassLoader() {
        URL[] urlArray = XBUSClassLoader.createClassLoaderURLs();
        verify(MOCK_PARENT_CLASSLOADER, times(1)).getResources("/");
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(MOCK_PARENT_CLASSLOADER);
        assertEquals(urlArray.length, classLoader.getURLs().length);
        for (int i = 0; i < urlArray.length; ++i) {
            URL url = urlArray[i];
            URL urlInClassLoader = classLoader.getURL(url.toString());
            assertEquals(url, urlInClassLoader);
        }
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
        assertEquals(4, urls.size());
        URL[] expectedURLs = new URL[]{
                new File(XBUS_HOME + "/lib/xbus-core.jar").toURL(),
                new File(XBUS_HOME + "/lib/xbus-core-runtime.jar").toURL(),
                new File(XBUS_HOME + "/lib/xbus-plugin-api.jar").toURL(),
                new File(XBUS_HOME + "/lib/xbus-test-lib.jar").toURL()
        };
        for (int i = 0; i < urls.size(); ++i) {
            assertEquals(expectedURLs[i], urls.elementAt(i));
        }
    }

    @Test
    public void testStaticInitializer() {
        assertNull(XBUSClassLoader.mClassLoader);
        verify(MOCK_PARENT_CLASSLOADER, never()).getResources("/");
    }

    @AfterAll
    public static void tearDownAfterAll() {
        System.clearProperty("XBUS_HOME");
    }
}