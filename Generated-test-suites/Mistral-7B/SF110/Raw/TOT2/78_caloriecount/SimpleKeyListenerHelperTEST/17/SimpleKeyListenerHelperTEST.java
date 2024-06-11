package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListener listener;

    @Before
    public void setUp() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertEquals(component, helper.getComponent());
        assertNotNull(helper.getComponent());
        verify(component).addKeyListener(helper);
    }

    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.getComponent());
        verify(component).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListener() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener simpleKeyListener = mock(SimpleKeyListener.class);
        helper.attach(simpleKeyListener);
        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 1, KeyEvent.VK_ENTER, ' ');
        helper.notifyListener(simpleKeyListener, KeyEvent.KEY_EVENT_ID, keyEvent);
        verify(simpleKeyListener).enter(component);
    }

    @Test
    public void testKeyEvents() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener simpleKeyListener = mock(SimpleKeyListener.class);
        helper.attach(simpleKeyListener);
        for (int i = 0; i < 4; i++) {
            KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 1, i, ' ');
            helper.notifyListener(simpleKeyListener, KeyEvent.KEY_EVENT_ID, keyEvent);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetachWithNullComponent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
        helper.detach();
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testNotifyListenerWithNullListener() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 1, 0, ' ');
        helper.notifyListener(null, KeyEvent.KEY_EVENT_ID, keyEvent);
    }

    @Test
    public void testNotifyListenerWithUnknownKeyCode() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener simpleKeyListener = mock(SimpleKeyListener.class);
        helper.attach(simpleKeyListener);
        KeyEvent keyEvent = new KeyEvent(component, 999, 0, 1, 0, ' ');
        helper.notifyListener(simpleKeyListener, KeyEvent.KEY_EVENT_ID, keyEvent);
    }
}