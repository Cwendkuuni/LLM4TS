import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component component;
    private SimpleKeyListener listener;

    @Before
    public void setUp() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(component);
        helper.addListener(listener);
    }

    @Test
    public void testKeyPressed_EnterKey() {
        KeyEvent enterKeyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0);
        helper.keyPressed(enterKeyEvent);
        verify(listener).enter(component);
    }
}