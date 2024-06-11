package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // Assuming JFrame is a component type
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(sklHelper);
        assertNotNull(sklHelper.component);
        assertEquals(component, sklHelper.component);
    }

    @Test
    public void testDetach() {
        Component component = new JFrame();
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        sklHelper.detach();
        assertNull(sklHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        sklHelper.addListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 10, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 'enter');
        sklHelper.keyPressed(keyEvent);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        sklHelper.addListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 155, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT, 'insert');
        sklHelper.keyPressed(keyEvent);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        sklHelper.addListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 127, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, 'delete');
        sklHelper.keyPressed(keyEvent);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        sklHelper.addListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 9, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, 'tab');
        sklHelper.keyPressed(keyEvent);
        verify(listener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testFireWithNullComponent() {
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(null);
        sklHelper.fire(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFireWithInvalidKeyCode() {
        Component component = new JFrame();
        SimpleKeyListenerHelper sklHelper = new SimpleKeyListenerHelper(component);
        sklHelper.fire(-1);
    }
}