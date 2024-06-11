package net.sf.xbus.base.core.reflection;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    private static ClassLoader parent;
    private static XBUSClassLoader classUnderTest;

    @BeforeClass
    public static void setUp() {
        parent = XBUSClassLoader.class.getClassLoader();
    }

    @Test
    public void testGetInstance_WhenXBUS_HOME_IsSet_ThenInstanceIsReturned() {
        classUnderTest = XBUSClassLoader.getInstance(parent);
        assertNotNull(classUnderTest);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetInstance_WhenXBUS_HOME_IsNotSet_ThenErrorIsThrown() {
        System.setProperty("net.sf.xbus.base.core.Constants.XBUS_HOME", null);
        classUnderTest = XBUSClassLoader.getInstance(parent);
        fail("Expected an IllegalStateException, but no exception was thrown.");
    }
}