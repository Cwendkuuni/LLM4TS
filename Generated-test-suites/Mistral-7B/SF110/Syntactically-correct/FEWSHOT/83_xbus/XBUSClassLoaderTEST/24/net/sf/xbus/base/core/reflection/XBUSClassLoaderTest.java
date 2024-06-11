package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.Trace;

import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNullParent() {
        XBUSClassLoader.getInstance(null);
    }

    @Test
    public void testCreateClassLoader() {
        assertNull(XBUSClassLoader.mClassLoader);
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "testDir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        for (Object urlObj : urls) {
            URL url = (URL) urlObj;
            assertTrue(url.getFile().endsWith(".jar"));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAddUrlsWithNullDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, null);
    }

    @Test
    public void testCreateClassLoaderWithValidXBUSHome() {
        System.setProperty(Constants.XBUS_HOME, "testDir");
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderWithNullXBUSHome() {
        XBUSClassLoader.createClassLoader(getClass().getClassLoader());
    }

    @Test
    public void testCreateClassLoaderWithEmptyXBUSHome() {
        System.setProperty(Constants.XBUS_HOME, "");
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNull(classLoader);
    }

    @Test
    public void testCreateClassLoaderWithInvalidXBUSHome() {
        System.setProperty(Constants.XBUS_HOME, "testDir/invalidPath");
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNull(classLoader);
    }

    @Test
    public void testGetClassNameFromResource() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        String className = "net.sf.xbus.base.core.reflection.Constants";
        Class<?> clazz = classLoader.findClass(className);
        assertNotNull(clazz);
        assertEquals(Constants.class, clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassNameFromNonExistentResource() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        String className = "net.sf.xbus.base.core.reflection.NonExistentClass";
        classLoader.findClass(className);
    }

    @Test
    public void testGetClassNameFromExistingFile() throws IOException, ClassNotFoundException {
        File classFile = new File("testClassFile.class");
        ClassLoader fileClassLoader = new URLClassLoader(new FileObjectURL(classFile), getClass().getClassLoader());
        String className = "test.TestClass";
        Class<?> clazz = fileClassLoader.findClass(className);
        assertNotNull(clazz);
        assertEquals(Class.forName("test.TestClass"), clazz);
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithInvalidUrl() {
        Vector urls = new Vector();
        String invalidUrl = "invalid:/url";
        XBUSClassLoader.addUrls(urls, invalidUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUrlsWithNullUrlsVector() {
        XBUSClassLoader.addUrls(null, "testDir");
    }

    @Test
    public void testFindClassFromURL() throws Exception {
        File classFile = new File("testClassFile.class");
        URL url = classFile.toURL();
        ClassLoader urlClassLoader = new URLClassLoader(new URL[] { url }, getClass().getClassLoader());
        String className = "test.TestClass";
        Class<?> clazz = urlClassLoader.findClass(className);
        assertNotNull(clazz);
        assertEquals(Class.forName("test.TestClass"), clazz);
    }
}