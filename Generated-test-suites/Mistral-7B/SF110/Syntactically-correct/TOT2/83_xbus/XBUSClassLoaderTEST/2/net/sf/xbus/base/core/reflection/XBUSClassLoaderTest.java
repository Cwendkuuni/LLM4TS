import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader());
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        // Test the functionality of getInstance method here
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader());
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        // Test the functionality of createClassLoader method here
    }

    // Test the addUrls method and the static block here in a separate test case
}