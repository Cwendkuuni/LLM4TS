import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;

public class XBUSClassLoaderTest {
    

    @Test
    public void testGetInstance() {
        ClassLoader parent = this.getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    
}