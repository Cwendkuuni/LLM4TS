package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListener listener;

    @Before
    public void setup() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
    }

    @Test
    public void testConstructor() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNotNull(helper.getComponent());
        assertEquals(component, helper.getComponent());
    }

    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        verify(component).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_ENTER, '');
        helper.keyPressed(event);
        verify(listener).enter(component);
        helper.fire(event.getKeyCode());
    }

    @Test
    public void testKeyPressedTab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 9, 0, 0, KeyEvent.VK_TAB, '');
        helper.keyPressed(event);
        verify(listener).tab(component);
        helper.fire(event.getKeyCode());
    }

    @Test
    public void testKeyPressedBackspace() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 127, 0, 0, KeyEvent.VK_BACK_SPACE, '');
        helper.keyPressed(event);
        verify(listener).delete(component);
        helper.fire(event.getKeyCode());
    }

    @Test
    public void testKeyPressedDelete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 155, 0, 0, KeyEvent.VK_DELETE, '');
        helper.keyPressed(event);
        verify(listener).delete(component);
        helper.fire(event.getKeyCode());
    }
}