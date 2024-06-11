import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;

public class XBUSClassLoaderTest {
    private static ClassLoader parent;
    private static XBUSClassLoader xbusClassLoader;

    @BeforeClass
    public static void setUp() {
        parent = XBUSClassLoader.class.getClassLoader();
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
        assertEquals(parent, XBUSClassLoader.getParent());
    }
}