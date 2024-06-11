import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    private Component component;
    private SimpleKeyListenerHelper helper;

    @Before
    public void setup() {
        component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testConstructor() {
        assertNotNull(helper);
    }
}