package net.sf.xbus.base.core.reflection;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertEquals(5, urls.size());
    }

    @Test
    public void testStaticBlock() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullParent() {
        XBUSClassLoader.getInstance(null);
    }

    @Test
    public void testCreateClassLoaderWithEmptyUrlArray() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsWithNullDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsWithInvalidDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test");
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddUrlsWithInvalidFile() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "/test/invalidFile");
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedURL() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test://invalidURL");
    }

    @Test(expected = ClassCastException.class)
    public void testGetInstanceWithInvalidParent() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(new Object());
    }

    @Test
    public void testGetInstanceWithValidParent() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithValidParent() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrlsWithValidDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "testDir/validDir");
        assertEquals(5, urls.size());
    }

    @Test
    public void testAddUrlsWithJarFile() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "testDir/validJarFile.jar");
        assertEquals(1, urls.size());
    }

    @Test
    public void testAddUrlsWithZipFile() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "testDir/validZipFile.zip");
        assertEquals(1, urls.size());
    }

    @Test
    public void testGetClassWithValidClass() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Class clazz = classLoader.findClass("net.sf.xbus.base.core.reflection.TestClass");
        assertNotNull(clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassWithInvalidClass() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Class clazz = classLoader.findClass("net.sf.xbus.base.core.reflection.InvalidClass");
    }

    @Test
    public void testGetClassesWithValidPackages() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Class[] classes = classLoader.getClasses();
        assertEquals(10, classes.length);
    }

    @Test(expected = IOException.class)
    public void testLoadClassWithInvalidJarFile() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        classLoader.loadClass("invalidJarFile.jar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadClassWithInvalidClassName() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        classLoader.loadClass("invalidClassName");
    }

    @Test
    public void testLoadClassWithValidClassName() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Class clazz = classLoader.loadClass("net.sf.xbus.base.core.reflection.TestClass");
        assertNotNull(clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testFindClassWithInvalidClassName() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Class clazz = classLoader.findClass("invalidClassName");
    }

    @Test
    public void testFindClassesWithValidPackages() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Class[] classes = classLoader.findClasses("net.sf.xbus.base.core.reflection");
        assertEquals(3, classes.length);
    }

    @Test
    public void testGetParent() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader.getParent());
    }

    @Test
    public void testGetURLs() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        URL[] urls = classLoader.getURLs();
        assertEquals(4, urls.length);
    }
}