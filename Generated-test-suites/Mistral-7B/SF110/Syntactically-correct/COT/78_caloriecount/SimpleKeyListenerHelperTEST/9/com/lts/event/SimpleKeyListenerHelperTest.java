package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockListener;

    @Before
    public void setup() {
        component = new JFrame();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        mockListener = mock(SimpleKeyListener.class);
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testAddKeyListener() {
        assertTrue(component.getKeyListeners().length > 0);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertFalse(component.getKeyListeners().length > 0);
    }

    @Test
    public void testNotifyListenerEnter() {
        when(mockListener.enter(component)).thenReturn(true);
        simpleKeyListenerHelper.notifyListener(mockListener, 10, null);
        verify(mockListener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        when(mockListener.insert(component)).thenReturn(true);
        simpleKeyListenerHelper.notifyListener(mockListener, 155, null);
        verify(mockListener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        when(mockListener.delete(component)).thenReturn(true);
        simpleKeyListenerHelper.notifyListener(mockListener, 127, null);
        verify(mockListener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        when(mockListener.tab(component)).thenReturn(true);
        simpleKeyListenerHelper.notifyListener(mockListener, 9, null);
        verify(mockListener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListenerNullListener() {
        simpleKeyListenerHelper.notifyListener(null, 10, null);
    }

    @Test
    public void testKeyPressedEnter() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        verifyNoMoreInteractions(mockListener);
        assertTrue(component.getKeyEventQueue().peek() instanceof KeyEvent);
        assertEquals(KeyEvent.KEY_PRESSED, ((KeyEvent) component.getKeyEventQueue().peek()).getID());
        assertEquals(KeyEvent.VK_ENTER, ((KeyEvent) component.getKeyEventQueue().peek()).getKeyCode());
        component.getToolkit().getSystemEventQueue().postEvent(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
        assertEquals(0, component.getKeyEventQueue().size());
    }

    @Test
    public void testKeyPressedTab() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ""));
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verifyNoMoreInteractions(mockListener);
        assertTrue(component.getKeyEventQueue().peek() instanceof KeyEvent);
        assertEquals(KeyEvent.KEY_PRESSED, ((KeyEvent) component.getKeyEventQueue().peek()).getID());
        assertEquals(KeyEvent.VK_TAB, ((KeyEvent) component.getKeyEventQueue().peek()).getKeyCode());
        component.getToolkit().getSystemEventQueue().postEvent(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ""));
        assertEquals(0, component.getKeyEventQueue().size());
    }

    @Test
    public void testKeyPressedBackspace() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, ""));
        simpleKeyListenerHelper.fire(KeyEvent.VK_BACK_SPACE);
        verifyNoMoreInteractions(mockListener);
        assertTrue(component.getKeyEventQueue().peek() instanceof KeyEvent);
        assertEquals(KeyEvent.KEY_PRESSED, ((KeyEvent) component.getKeyEventQueue().peek()).getID());
        assertEquals(KeyEvent.VK_BACK_SPACE, ((KeyEvent) component.getKeyEventQueue().peek()).getKeyCode());
        component.getToolkit().getSystemEventQueue().postEvent(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, ""));
        assertEquals(0, component.getKeyEventQueue().size());
    }

    @Test
    public void testKeyPressedUnknownKey() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 0, ""));
        simpleKeyListenerHelper.fire(0);
        verifyNoMoreInteractions(mockListener);
        assertTrue(component.getKeyEventQueue().peek() instanceof KeyEvent);
        assertEquals(KeyEvent.KEY_PRESSED, ((KeyEvent) component.getKeyEventQueue().peek()).getID());
        assertEquals(0, ((KeyEvent) component.getKeyEventQueue().peek()).getKeyCode());
        component.getToolkit().getSystemEventQueue().postEvent(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, 0, ""));
        assertEquals(0, component.getKeyEventQueue().size());
    }

    @Test
    public void testFireNullEvent() {
        simpleKeyListenerHelper.fire(null);
        verifyNoMoreInteractions(mockListener);
    }
}