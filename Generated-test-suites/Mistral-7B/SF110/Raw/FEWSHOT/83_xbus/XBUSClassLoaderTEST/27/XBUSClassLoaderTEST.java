package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class XBUSClassLoaderTest {

    private static final String XBUS_HOME = "test/resources/xbus-home";
    private static final String XBUS_HOME_LIB = XBUS_HOME + "/lib";
    private static final String XBUS_HOME_LIB_RUNTIME = XBUS_HOME_LIB + "/runtime";
    private static final String XBUS_HOME_PLUGIN_LIB = XBUS_HOME + "/plugin/lib";
    private static final String XBUS_HOME_TEST_LIB = XBUS_HOME + "/test/lib";

    private ClassLoader parent;
    private XBUSClassLoader classLoader;
    private Vector urls;

    @Before
    public void setUp() {
        parent = mock(ClassLoader.class);
        urls = new Vector();
    }

    @Test
    public void testInstanceWithNullParent() {
        assertNull(XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testInstanceWithValidParent() {
        classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithNullParent() {
        assertNull(XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testCreateClassLoaderWithValidParent() {
        classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testGetInstanceWithNullParent() {
        assertNull(XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testGetInstanceWithValidParent() {
        when(parent.getResource("")).thenReturn(new File("test/resources/xbus-home/").toURL());
        classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithEmptyUrls() {
        classLoader = new XBUSClassLoader(new URL[0], parent);
        assertNotNull(classLoader);
        assertFalse(classLoader.getParent() instanceof XBUSClassLoader);
    }

    @Test
    public void testCreateClassLoaderWithValidUrls() {
        final URL url1 = new URL("file:test/resources/xbus-home/lib/test1.jar");
        final URL url2 = new URL("file:test/resources/xbus-home/lib/test2.jar");
        when(parent.getResource("")).thenReturn(new File("test/resources/xbus-home/").toURL());
        classLoader = new XBUSClassLoader(new URL[] { url1, url2 }, parent);
        assertNotNull(classLoader);
        assertFalse(classLoader.getParent() instanceof XBUSClassLoader);
    }

    @Test
    public void testCreateClassLoaderWithNullUrls() {
        try {
            new XBUSClassLoader(null, parent);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCreateClassLoaderWithEmptyUrlArray() {
        try {
            new XBUSClassLoader(new URL[0], parent);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateClassLoaderWithInvalidUrl() {
        try {
            final URL invalidUrl = new URL("invalid:test");
            new XBUSClassLoader(new URL[] { invalidUrl }, parent);
            fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testCreateClassLoaderWithValidUrls() {
        final File xbusHome = new File(XBUS_HOME);
        final File libPath = new File(XBUS_HOME_LIB);
        final File[] jars = libPath.listFiles();
        when(parent.getResource("")).thenReturn(xbusHome.toURL());
        classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        assertFalse(classLoader.getParent() instanceof XBUSClassLoader);

        final ArgumentCaptor<URL[]> urlsArgumentCaptor = ArgumentCaptor.forClass(URL[].class);
        verify(parent, times(1)).findResources("", urlsArgumentCaptor.capture());
        final URL[] urlsFromParent = urlsArgumentCaptor.getValue();
        assertNotNull(urlsFromParent);
        assertEquals(4, urlsFromParent.length);

        final URL[] urlsFromClassLoader = classLoader.getURLs();
        assertNotNull(urlsFromClassLoader);
        assertEquals(urlsFromParent.length + urls.size(), urlsFromClassLoader.length);

        for (int i = 0; i < urlsFromParent.length; ++i) {
            assertEquals(urlsFromParent[i], urlsFromClassLoader[i]);
        }
    }

    @Test
    public void testAddUrlsToEmptyVector() {
        addUrls(urls, XBUS_HOME_LIB);
        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrlsToVectorWithFiles() {
        addUrls(urls, XBUS_HOME_LIB);
        final File libPath = new File(XBUS_HOME_LIB);
        final File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                urls.add(jars[i].toURL());
            }
        }
        assertEquals(urlsWithSuffix("jar", jars.length), urls);
    }

    @Test
    public void testAddUrlsToVectorWithDirectories() {
        addUrls(urls, XBUS_HOME_LIB);
        final File libPath = new File(XBUS_HOME_LIB);
        final File[] directories = libPath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (int i = 0; directories != null && i < directories.length; ++i) {
            addUrls(urls, directories[i].getPath());
        }
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    @Test
    public void testAddUrlsToVectorWithMixedFilesAndDirectories() {
        addUrls(urls, XBUS_HOME_LIB);
        final File libPath = new File(XBUS_HOME_LIB);
        final File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                urls.add(jars[i].toURL());
            } else {
                addUrls(urls, jars[i].getPath());
            }
        }
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    @Test
    public void testAddUrlsToVectorWithEmptyDirectory() {
        addUrls(urls, XBUS_HOME_LIB);
        final File emptyDir = new File(XBUS_HOME_LIB, "emptyDir");
        addUrls(urls, emptyDir.getPath());
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    @Test
    public void testAddUrlsToVectorWithNestedDirectory() {
        addUrls(urls, XBUS_HOME_LIB);
        final File libPath = new File(XBUS_HOME_LIB);
        final File nestedDir = new File(libPath, "nestedDir");
        addUrls(urls, nestedDir.getPath());
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    @Test
    public void testAddUrlsToVectorWithMultipleLevelsOfNestedDirectories() {
        addUrls(urls, XBUS_HOME_LIB);
        final File libPath = new File(XBUS_HOME_LIB);
        final File nestedLevel1Dir = new File(libPath, "nestedLevel1Dir");
        final File nestedLevel2Dir = new File(nestedLevel1Dir, "nestedLevel2Dir");
        addUrls(urls, nestedLevel2Dir.getPath());
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    @Test
    public void testAddUrlsToVectorWithInvalidDirectory() {
        addUrls(urls, XBUS_HOME_LIB);
        final File invalidDir = new File("invalidDir");
        addUrls(urls, invalidDir.getPath());
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    @Test
    public void testAddUrlsToVectorWithInvalidFile() {
        addUrls(urls, XBUS_HOME_LIB);
        final File invalidFile = new File("invalidFile");
        addUrls(urls, invalidFile.getPath());
        assertEquals(urlsFromResources(XBUS_HOME_LIB), urls);
    }

    private List urlsWithSuffix(final String suffix, final int expectedCount) {
        final List result = new ArrayList();
        for (int i = 0; i < expectedCount; ++i) {
            result.add(new URL("file:test/resources/xbus-home/lib/test" + suffix + i + ".jar"));
        }
        return result;
    }

    private List urlsFromResources(final String directory) {
        final List result = new ArrayList();
        result.add(new URL("file:" + directory + "/"));
        final File libPath = new File(directory);
        final File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                result.add(jars[i].toURL());
            }
        }
        return result;
    }
}