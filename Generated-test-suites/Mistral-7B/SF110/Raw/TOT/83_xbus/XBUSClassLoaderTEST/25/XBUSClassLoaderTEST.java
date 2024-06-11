package net.sf.xbus.base.core.reflection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.io.File;
import java.net.URL;

@RunWith(Suite.class)
@Suite.SuiteClasses({MyTest1.class, MyTest2.class, MyTest3.class})
public class XBUSClassLoaderTest {
    static {
        XBUSClassLoader.getInstance(null); // Testing null parent
    }

    @BeforeClass
    public static void setupClass() {
        Constants.setXBUS_HOME("test/resources");
    }
}

@RunWith(Parameterized.class)
class MyTest1 {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {{"lib"}, {"lib/runtime"}, {"plugin/lib"}, {"test/lib"}});
    }

    private String dirName;

    public MyTest1(String dirName) {
        this.dirName = dirName;
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/" + this.dirName);

        assertEquals(4, urls.size());
        for (Object urlObj : urls) {
            URL url = (URL) urlObj;
            assertTrue(url.toString().startsWith(Constants.XBUS_HOME + "/"));
        }
    }
}

class MyTest2 {
    @Test
    public void testGetInstanceWithParent() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        ClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);

        URL[] urls = classLoader.getURLs();
        for (URL url : urls) {
            System.out.println(url.toString());
        }

        try {
            Class<?> clazz = classLoader.loadClass("net.sf.xbus.test.DummyClass");
            assertNotNull(clazz);
        } catch (ClassNotFoundException e) {
            fail("Class not found");
        }
    }
}

class MyTest3 {
    @Test
    public void testCreateClassLoaderWithNullParent() {
        try {
            XBUSClassLoader.createClassLoader(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

class MyTest4 {
    @Test
    public void testGetInstanceWithoutInitializingXBUS_HOME() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        ClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNull(classLoader);
    }
}

class MyTest5 {
    @Test
    public void testCreateClassLoaderWithEmptyVector() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        Vector urls = new Vector();
        XBUSClassLoader classLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        assertNotNull(classLoader);

        URL[] urlsActual = classLoader.getURLs();
        assertEquals(0, urlsActual.length);
    }
}

class MyTest6 {
    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderWithNullURLArray() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = new XBUSClassLoader(null, parent);
        fail("Expected IllegalArgumentException");
    }
}

class MyTest7 {
    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassLoaderWithNullParent() {
        ClassLoader parent = null;
        XBUSClassLoader classLoader = new XBUSClassLoader(new URL[]{new File("lib/runtime/test.jar").toURL()}, parent);
        fail("Expected IllegalArgumentException");
    }
}

class MyTest8 {
    @Test
    public void testGetInstanceWithNoInitialization() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        ClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);

        URL[] urls = classLoader.getURLs();
        assertEquals(0, urls.length);
    }
}

class MyTest9 {
    @Test
    public void testLoadClassWithInvalidClassName() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        ClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        try {
            Class<?> clazz = classLoader.loadClass("net.sf.xbus.test.NonExistentClass");
            fail("Expected ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }
}

class MyTest10 {
    @Test
    public void testLoadClassWithFullyQualifiedName() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        ClassLoader classLoader = XBUSClassLoader.getInstance(parent);

        try {
            Class<?> clazz = classLoader.loadClass("java.lang.String");
            assertNotNull(clazz);
        } catch (ClassNotFoundException e) {
            fail("Expected the class to be found");
        }
    }
}