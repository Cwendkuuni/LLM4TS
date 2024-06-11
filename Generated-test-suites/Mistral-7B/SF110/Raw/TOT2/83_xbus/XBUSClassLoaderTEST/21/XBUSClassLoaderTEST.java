import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testInstance() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    // Edge case: testing with null parent
    @Test(expected = NullPointerException.class)
    public void testInstanceNullParent() {
        XBUSClassLoader.getInstance(null);
    }
}