package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstruction() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(listenerHelper);
        assertEquals(component, listenerHelper.getComponent());
    }

    @Test
    public void testDetach() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.detach();
        verify(component).removeKeyListener(listenerHelper);
        assertNull(listenerHelper.getComponent());
    }

    @Test
    public void testNotifyListener() {
        Component component = mock(Component.class);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);

        when(listener.enter(component)).thenAnswer(invocation -> {
            KeyEvent keyEvent = invocation.getArgument(0);
            listenerHelper.fire(keyEvent.getKeyCode());
        });

        KeyEvent keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_UNKNOWN, false, KeyEvent.CHR_UNDEFINED, "enter");
        listenerHelper.notifyListener(listener, 10, keyEvent);

        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_KeyPressEvent() {
        Component component = mock(Component.class);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);

        when(listener.enter(component)).thenAnswer(invocation -> {});

        KeyEvent keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_UNKNOWN, false, KeyEvent.CHR_UNDEFINED, "enter");
        listenerHelper.notifyListener(listener, 10, keyEvent);

        verifyZeroInteractions(listener);
        verify(component).requestFocus();
    }

    @Test
    public void testNotifyListener_KeyEventCodeNotSupported() {
        Component component = mock(Component.class);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);

        when(listener.enter(component)).thenAnswer(invocation -> {});

        KeyEvent keyEvent = new KeyEvent(component, 128, 0, 0, KeyEvent.VK_BACK_SPACE, KeyEvent.KEY_LOCATION_UNKNOWN, false, KeyEvent.CHR_UNDEFINED, "backspace");
        listenerHelper.notifyListener(listener, 10, keyEvent);

        verifyZeroInteractions(listener);
    }

    @Test
    public void testKeyPressed() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);

        when(listenerHelper.getComponent()).thenReturn(component);

        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.VK_ENTER, 0, 0, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_UNKNOWN, false, KeyEvent.CHR_UNDEFINED, "enter");
        listenerHelper.keyPressed(keyEvent);

        verify(component).requestFocus();
        listenerHelper.fire(keyEvent.getKeyCode());
    }

    @Test
    public void testKeyTyped() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);

        when(listenerHelper.getComponent()).thenReturn(component);

        KeyEvent keyEvent = new KeyEvent(component, 127, 0, 0, KeyEvent.VK_BACK_SPACE, KeyEvent.KEY_LOCATION_UNKNOWN, false, KeyEvent.CHR_BACK_SPACE, "backspace");
        listenerHelper.keyTyped(keyEvent);

        verifyZeroInteractions(component);
        listenerHelper.fire(keyEvent.getKeyCode());
    }

    @Test
    public void testKeyReleased() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(component);

        when(listenerHelper.getComponent()).thenReturn(component);

        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.VK_BACK_SPACE, 0, 0, KeyEvent.VK_BACK_SPACE, KeyEvent.KEY_LOCATION_UNKNOWN, false, KeyEvent.CHR_BACK_SPACE, "backspace");
        listenerHelper.keyReleased(keyEvent);

        verifyZeroInteractions(component);
    }
}