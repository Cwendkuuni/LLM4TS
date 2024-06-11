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
    public void setUp() {
        mockComponent = mock(Component.class);
        mockListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testAddKeyListener() {
        verify(mockComponent).addKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListener_ENTER_KeyCode_10() {
        simpleKeyListenerHelper.fire(10);
        verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListener_INSERT_KeyCode_155() {
        simpleKeyListenerHelper.fire(155);
        verify(mockListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListener_DELETE_KeyCode_127() {
        simpleKeyListenerHelper.fire(127);
        verify(mockListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListener_TAB_KeyCode_9() {
        simpleKeyListenerHelper.fire(9);
        verify(mockListener).tab(mockComponent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFire_Invalid_KeyCode() {
        simpleKeyListenerHelper.fire(123);
    }

    @Test
    public void testKeyPressed_ENTER_KeyCode_10() {
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(10);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListenerHelper.fire(10);
        verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testKeyPressed_TAB_KeyCode_9() {
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(9);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListenerHelper.fire(9);
        verify(mockListener).tab(mockComponent);
    }

    @Test
    public void testKeyPressed_Other_KeyCode() {
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(123);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListenerHelper.fire(123);
        // No verification expected, as this method doesn't call mockListener
    }
}