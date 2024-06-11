package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component component;
    private SimpleKeyListener simpleKeyListener;

    @Before
    public void setUp() {
        component = new JFrame();
        simpleKeyListener = Mockito.mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    // Test for constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(component, simpleKeyListenerHelper.getComponent());
    }

    // Test for 'detach' method
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    // Test for 'notifyListener' method with valid key events
    @Test
    public void testNotifyListenerValidKeyEvents() {
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        Mockito.verify(simpleKeyListener).enter(component);

        simpleKeyListenerHelper.fire(KeyEvent.VK_BACK_SPACE);
        Mockito.verify(simpleKeyListener).delete(component);

        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        Mockito.verify(simpleKeyListener).tab(component);

        simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
        Mockito.verify(simpleKeyListener).insert(component);
    }

    // Test for 'notifyListener' method with invalid key event
    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerInvalidKeyEvent() {
        simpleKeyListenerHelper.fire(KeyEvent.VK_ESCAPE);
    }

    // Test for 'keyPressed' method with valid key codes
    @Test
    public void testKeyPressedValidKeyCodes() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER));
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        Mockito.verify(simpleKeyListener).enter(component);

        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE));
        simpleKeyListenerHelper.fire(KeyEvent.VK_BACK_SPACE);
        Mockito.verify(simpleKeyListener).delete(component);

        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB));
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        Mockito.verify(simpleKeyListener).tab(component);

        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT));
        simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
        Mockito.verify(simpleKeyListener).insert(component);
    }

    // Test for 'keyPressed' method with invalid key code
    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressedInvalidKeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_F1));
    }
}