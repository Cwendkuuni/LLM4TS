package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // A valid Component is required
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNotNull(helper);
        assertEquals(component, helper.getComponent());
    }

    @Test
    public void testDetach() {
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.getComponent());
    }

    @Test
    public void testNotifyListener_enter() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.setListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 10, 1, 0, KeyEvent.VK_ENTER, 0);
        helper.keyPressed(keyEvent);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_insert() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.setListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 155, 1, 0, KeyEvent.VK_INSERT, 0);
        helper.keyPressed(keyEvent);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_delete() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.setListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 127, 1, 0, KeyEvent.VK_DELETE, 0);
        helper.keyPressed(keyEvent);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_tab() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.setListener(listener);
        KeyEvent keyEvent = new KeyEvent(component, 9, 1, 0, KeyEvent.VK_TAB, 0);
        helper.keyPressed(keyEvent);
        verify(listener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListener_nullListener() {
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.setListener(null);
        KeyEvent keyEvent = new KeyEvent(component, 10, 1, 0, KeyEvent.VK_ENTER, 0);
        helper.keyPressed(keyEvent);
    }

    @Test(expected = NullPointerException.class)
    public void testSimpleKeyListenerHelperConstructor_nullComponent() {
        Component component = null;
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
    }

}