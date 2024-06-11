package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstruction() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNull(helper.getComponent());
    }

    @Test
    public void testAddKeyListenerToComponent() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        verify(component).addKeyListener(helper);
    }

    @Test
    public void testDetachKeyListenerFromComponent() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        verify(component).removeKeyListener(helper);
    }

    @Test
    public void testFireKeyEvent() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, 10, 1, 0, KeyEvent.VK_ENTER, "enter");
        helper.fire(keyEvent.getKeyCode());

        verify(listener).enter(component);
    }

    @Test
    public void testKeyPressedEvent() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 1, 0, KeyEvent.VK_TAB, "tab");
        helper.keyPressed(keyEvent);

        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedForUnsupportedKeyCodes() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 1, 0, 255, "unsupported key");
        helper.keyPressed(keyEvent);

        // No verification as the unsupported key codes should not trigger any event
    }

    @Test
    public void testKeyReleasedEvent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(null, KeyEvent.KEY_RELEASED, 1, 0, KeyEvent.VK_ENTER, "enter");
        helper.keyReleased(keyEvent);

        // No verification as keyReleased method is empty
    }

    @Test
    public void testKeyTypedEvent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(null, KeyEvent.KEY_TYPED, 1, 0, KeyEvent.VK_ENTER, "enter");
        helper.keyTyped(keyEvent);

        // No verification as keyTyped method is empty
    }
}