import org.junit.Test;
import net.sf.xbus.base.core.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = this.getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
        // Check if loaded classes
        Class<?> clazz1 = classLoader.loadClass("net.sf.xbus.base.core.trace.Trace");
        Class<?> clazz2 = classLoader.loadClass("java.lang.String");
        assertNotNull(clazz1);
        assertNotNull(clazz2);
    }
    
}