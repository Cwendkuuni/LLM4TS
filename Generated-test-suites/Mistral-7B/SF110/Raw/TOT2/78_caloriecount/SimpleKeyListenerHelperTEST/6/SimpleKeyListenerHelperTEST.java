
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;

    @Before
    public void setUp() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(simpleKeyListenerHelper.getClass(), SimpleKeyListenerHelper.class);
    }

    @Test
    public void testDetachWithNullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testDetachTwice() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
    }

    @Test
    public void testNotifyListenerWithInvalidEventType() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 13, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressedWithInvalidKeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(new JFrame(), 100, 1, 0, KeyEvent.KEY_PRESSED, ' '));
    }

    @Test
    public void testKeyPressed() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent keyEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, 1, 0, KeyEvent.VK_ENTER, '');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(listener).enter(simpleKeyListenerHelper.component);
    }

    @Test
    public void testKeyReleased() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.keyReleased(new KeyEvent(new JFrame(), KeyEvent.KEY_RELEASED, 1, 0, KeyEvent.VK_ENTER, ''));
        verify(listener, never()).enter(simpleKeyListenerHelper.component);
    }

    @Test
    public void testKeyTyped() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.keyTyped(new KeyEvent(new JFrame(), KeyEvent.KEY_TYPED, 1, 0, KeyEvent.VK_ENTER, ''));
        verify(listener, never()).enter(simpleKeyListenerHelper.component);
    }
}