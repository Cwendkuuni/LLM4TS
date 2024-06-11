package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListener helper;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        helper = new SimpleKeyKeyListenerHelper(component);
    }

    @Test
    public void testConstruction() {
        assertNotNull(helper);
        assertNotNull(helper.component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);

        verify(component).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.notifyListener(listener, 10, null);

        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.notifyListener(listener, 155, null);

        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.notifyListener(listener, 127, null);

        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.notifyListener(listener, 9, null);

        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "Enter");

        helper.keyPressed(event);
        helper.fire(event.getKeyCode());

        verify(listener).enter(component);
    }

    @Test
    public void testKeyPressedTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, "Tab");

        helper.keyPressed(event);
        helper.fire(event.getKeyCode());

        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, 127, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, "Delete");

        helper.keyPressed(event);
        helper.fire(event.getKeyCode());

        verify(listener).delete(component);
    }

    @Test
    public void testKeyPressedInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, 155, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, "Insert");

        helper.keyPressed(event);
        helper.fire(event.getKeyCode());

        verify(listener).insert(component);
    }

    @Test
    public void testKeyPressedOther() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, 1, System.currentTimeMillis(), 0, KeyEvent.VK_A, "A");

        helper.keyPressed(event);
        helper.fire(event.getKeyCode());

        // No action for other keys
        verify(listener, never()).enter(component);
        verify(listener, never()).delete(component);
        verify(listener, never()).insert(component);
        verify(listener, never()).tab(component);
    }
}