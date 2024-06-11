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

    private static final String XBUS_HOME = System.getProperty("user.dir");

    private XBUSClassLoader classLoader;
    private Vector<URL> urls;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        urls = new Vector<>();
        when(Trace.error(String.class)).thenCallRealMethod();
    }

    @Test
    public void testInstanceWithNullParent() {
        assertNull(XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testInstanceWithValidParent() {
        ClassLoader parent = mock(ClassLoader.class);
        classLoader = XBUSClassLoader.getInstance(parent);
        assertEquals(classLoader.getParent(), parent);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateClassLoaderWithNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test
    public void testCreateClassLoaderWithValidParent() {
        ClassLoader parent = mock(ClassLoader.class);
        classLoader = XBUSClassLoader.createClassLoader(parent);
        assertEquals(classLoader.getParent(), parent);

        URL[] urlArray = classLoader.getURLs();
        assertNotNull(urlArray);
        assertEquals(urlArray.length, 4);

        for (URL url : urlArray) {
            String path = url.getFile();
            assertTrue(path.startsWith(XBUS_HOME));
            assertTrue(path.contains("/lib") || path.contains("/plugin/lib") || path.contains("/test/lib"));
        }
    }

    @Test
    public void testAddUrls() throws IOException {
        String dirName = XBUS_HOME;
        Vector<URL> urlVector = new Vector<>();

        XBUSClassLoader.addUrls(urlVector, dirName);

        int urlCount = urlVector.size();
        for (int i = 0; i < urlCount; ++i) {
            URL url = urlVector.elementAt(i);
            File file = new File(url.getFile());
            assertTrue(file.exists());
            assertTrue(file.isDirectory());
            String[] files = file.list();
            assertNotNull(files);
            for (String fileName : files) {
                File jarFile = new File(file, fileName);
                if (jarFile.isFile()) {
                    String extension = jarFile.getName().substring(jarFile.getName().lastIndexOf("."));
                    assertTrue(extension.equals(".jar") || extension.equals(".zip"));
                }
            }
        }
    }

    @Test
    public void testStaticInitialization() {
        ClassLoader parent = mock(ClassLoader.class);
        when(Constants.XBUS_HOME).thenReturn(XBUS_HOME);

        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);

        URL[] urlArray = classLoader.getURLs();
        assertNotNull(urlArray);
        assertEquals(urlArray.length, 4);

        for (URL url : urlArray) {
            String path = url.getFile();
            assertTrue(path.startsWith(XBUS_HOME));
            assertTrue(path.contains("/lib") || path.contains("/plugin/lib") || path.contains("/test/lib"));
        }

        verify(Trace, times(0)).error(String.class);
    }
}