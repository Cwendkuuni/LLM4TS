package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstruction() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        assertNotNull(listenerHelper);
    }

    @Test
    public void testDetach() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.detach();
        assertNull(listenerHelper.component);
        verify(mockComponent).removeKeyListener(listenerHelper);
    }

    @Test
    public void testNotifyListenerEnter() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.fire(10);
        verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.fire(155);
        verify(mockListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.fire(127);
        verify(mockListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.fire(9);
        verify(mockListener).tab(mockComponent);
    }

    @Test
    public void testKeyPressedEnterKey() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.addListener(mockListener);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(10);
        listenerHelper.keyPressed(mockEvent);
        verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testKeyPressedTabKey() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.addListener(mockListener);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(9);
        listenerHelper.keyPressed(mockEvent);
        verify(mockListener).tab(mockComponent);
    }

    @Test
    public void testKeyPressedBackspaceKey() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.addListener(mockListener);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(127);
        listenerHelper.keyPressed(mockEvent);
        verify(mockListener).delete(mockComponent);
    }

    @Test
    public void testKeyPressedOtherKey() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockComponent);
        listenerHelper.addListener(mockListener);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(1);
        listenerHelper.keyPressed(mockEvent);
        verify(mockListener, never()).enter(mockComponent);
        verify(mockListener, never()).insert(mockComponent);
        verify(mockListener, never()).delete(mockComponent);
        verify(mockListener, never()).tab(mockComponent);
    }
}