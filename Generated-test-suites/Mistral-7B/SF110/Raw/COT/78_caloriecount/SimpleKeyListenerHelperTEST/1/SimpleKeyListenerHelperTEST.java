package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockListener;
    private Component mockComponent;

    @Before
    public void setup() {
        mockListener = mock(SimpleKeyListener.class);
        mockComponent = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(mockComponent, simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testNotifyListener_enter() {
        simpleKeyListenerHelper.notifyListener(mockListener, 10, null);
        verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListener_insert() {
        simpleKeyListenerHelper.notifyListener(mockListener, 155, null);
        verify(mockListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListener_delete() {
        simpleKeyListenerHelper.notifyListener(mockListener, 127, null);
        verify(mockListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListener_tab() {
        simpleKeyListenerHelper.notifyListener(mockListener, 9, null);
        verify(mockListener).tab(mockComponent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListener_invalidType() {
        simpleKeyListenerHelper.notifyListener(mockListener, 4, null);
    }

    @Test
    public void testKeyPressed_validKeys() {
        int[] validKeys = {9, 10, 127, 155};
        for (int keyCode : validKeys) {
            KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, ' ');
            simpleKeyListenerHelper.keyPressed(event);
            simpleKeyListenerHelper.fire(keyCode);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressed_invalidKeyCode() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 4, ' ');
        simpleKeyListenerHelper.keyPressed(event);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, 10, ' ');
        simpleKeyListenerHelper.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 10, ' ');
        simpleKeyListenerHelper.keyTyped(event);
    }
}