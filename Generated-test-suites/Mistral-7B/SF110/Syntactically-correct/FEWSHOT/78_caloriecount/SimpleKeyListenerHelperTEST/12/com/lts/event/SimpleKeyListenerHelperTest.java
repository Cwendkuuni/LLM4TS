package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
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
        component = mock(Component.class);
        simpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        if (simpleKeyListenerHelper != null) {
            simpleKeyListenerHelper.detach();
        }
    }

    @Test
    public void testSimpleKeyListenerHelperConstruction() {
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testSimpleKeyListenerHelperAddKeyListener() {
        assertEquals(simpleKeyListenerHelper.getComponent(), component);
    }

    @Test
    public void testSimpleKeyListenerHelperDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testNotifyListenerEnter() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);
        verify(simpleKeyListener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, null);
        verify(simpleKeyListener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, null);
        verify(simpleKeyListener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, null);
        verify(simpleKeyListener).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.KEY_PRESSED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListener.enter(component);
        verify(simpleKeyListener).enter(component);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent keyEvent = new KeyEvent(component, 9, 0, 0, KeyEvent.KEY_PRESSED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListener.tab(component);
        verify(simpleKeyListener).tab(component);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent keyEvent = new KeyEvent(component, 127, 0, 0, KeyEvent.KEY_PRESSED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListener.delete(component);
        verify(simpleKeyListener).delete(component);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent keyEvent = new KeyEvent(component, 155, 0, 0, KeyEvent.KEY_PRESSED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListener.insert(component);
        verify(simpleKeyListener).insert(component);
    }
}