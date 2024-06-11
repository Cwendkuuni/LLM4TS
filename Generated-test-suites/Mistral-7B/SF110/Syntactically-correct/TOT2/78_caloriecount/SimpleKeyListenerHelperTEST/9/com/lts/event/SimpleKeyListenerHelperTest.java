import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {

    SimpleKeyListenerHelper simpleKeyListenerHelper;
    Component component;

    @Before
    public void setUp() {
        component = new JFrame();
    }

    // Test SimpleKeyListenerHelper constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        // Test valid component
        SimpleKeyListenerHelper simpleKeyListenerHelper1 = new SimpleKeyListenerHelper(component);

        // Test null component
        SimpleKeyListenerHelper simpleKeyListenerHelper2 = new SimpleKeyListenerHelper(null);
    }

    // Test detach()
    @Test
    public void testDetach() {
        // Test detaching null component
        SimpleKeyListenerHelper simpleKeyListenerHelper1 = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper1.detach();

        // Test detaching component without key listener
        SimpleKeyListenerHelper simpleKeyListenerHelper2 = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper2.detach();

        // Test detaching component with existing key listener
        SimpleKeyListenerHelper simpleKeyListenerHelper3 = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper3.addListener(new DummySimpleKeyListener());
        simpleKeyListenerHelper3.detach();
    }

    // Test notifyListener(...)
    @Test
    public void testNotifyListener() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
        SimpleKeyListener validListener = new DummySimpleKeyListener();
        simpleKeyListenerHelper.addListener(validListener);

        // Test enter event
        KeyEvent enterEvent = new KeyEvent(new JFrame(), 10, false, ' ');
        simpleKeyListenerHelper.notifyListener(validListener, 10, null);

        // Test insert event
        KeyEvent insertEvent = new KeyEvent(new JFrame(), 155, false, ' ');
        simpleKeyListenerHelper.notifyListener(validListener, 155, null);

        // Test delete event
        KeyEvent deleteEvent = new KeyEvent(new JFrame(), 127, false, ' ');
        simpleKeyListenerHelper.notifyListener(validListener, 127, null);

        // Test tab event
        KeyEvent tabEvent = new KeyEvent(new JFrame(), 9, false, ' ');
        simpleKeyListenerHelper.notifyListener(validListener, 9, null);
    }

    // Test keyPressed(...)
    @Test
    public void testKeyPressed() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());

        // Test key pressed events for valid key codes
        for (int validKeyCode : new int[] { 9, 10, 127, 155 }) {
            KeyEvent keyEvent = new KeyEvent(new JFrame(), 0, false, validKeyCode);
            simpleKeyListenerHelper.keyPressed(keyEvent);
        }

        // Test key pressed event for invalid key code
        KeyEvent invalidKeyEvent = new KeyEvent(new JFrame(), 0, false, -1);
        simpleKeyListenerHelper.keyPressed(invalidKeyEvent);
    }
}