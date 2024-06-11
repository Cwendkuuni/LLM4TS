import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testKeyPressed() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()) {
            @Override
            public void fire(int keyCode) {
                listener.keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, keyCode, KeyEvent.KEY_PRESSED));
            }
        };

        helper.keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 9, KeyEvent.KEY_PRESSED)); // Tab key
        helper.keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 10, KeyEvent.KEY_PRESSED)); // Enter key
        helper.keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 127, KeyEvent.KEY_PRESSED)); // Delete key
        helper.keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 155, KeyEvent.KEY_PRESSED)); // Insert key

        verify(listener, times(4)).keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 9, KeyEvent.KEY_PRESSED)); // Tab key
        verify(listener, times(1)).keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 10, KeyEvent.KEY_PRESSED)); // Enter key
        verify(listener, times(1)).keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 127, KeyEvent.KEY_PRESSED)); // Delete key
        verify(listener, times(1)).keyPressed(new KeyEvent(new Component(){}, 0, 0, 0, 155, KeyEvent.KEY_PRESSED)); // Insert key
    }
    
}