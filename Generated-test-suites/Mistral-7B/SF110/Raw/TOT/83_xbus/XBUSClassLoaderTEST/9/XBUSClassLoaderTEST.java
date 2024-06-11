import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(classLoader1.getParent());
        XBUSClassLoader classLoader3 = XBUSClassLoader.getInstance(classLoader1.getParent());

        assertEquals(classLoader1, classLoader2);
        assertEquals(classLoader1, classLoader3);
    }
}