package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // Assuming JFrame extends Component
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(component, simpleKeyListenerHelper.component);
    }

    @Test
    public void testDetach() {
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, 10, 1, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);

        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, 155, 1, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);

        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, 127, 1, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);

        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);

        KeyEvent keyEvent = new KeyEvent(component, 9, 1, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);

        verify(listener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testKeyPressedNullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
    }

    @Test
    public void testKeyPressedInvalidKeyCode() {
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(component, 1, 1, ' ');
        keyEvent.setKeyCode(128); // Invalid key code

        simpleKeyListenerHelper.keyPressed(keyEvent);
    }

    @Test
    public void testAddListenerNullListener() {
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = null;

        assertThrows(NullPointerException.class, () -> simpleKeyListenerHelper.addListener(listener));
    }
}