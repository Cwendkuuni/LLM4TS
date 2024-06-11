import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // any Component will do
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(component, simpleKeyListenerHelper.getComponent());
    }
}