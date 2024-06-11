package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockSimpleKeyListener;
    private Component mockComponent;

    @Before
    public void setup() {
        mockSimpleKeyListener = mock(SimpleKeyListener.class);
        mockComponent = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(mockComponent, simpleKeyListenerHelper.getComponent());
        verify(mockComponent).addKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
        verify(mockComponent).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testNotifyListenerEnter() {
        simpleKeylistenerHelper.fire(KeyEvent.VK_ENTER);
        verify(mockSimpleKeyListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        simpleKeylistenerHelper.fire(KeyEvent.VK_INSERT);
        verify(mockSimpleKeyListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        simpleKeylistenerHelper.fire(KeyEvent.VK_BACK_SPACE);
        verify(mockSimpleKeyListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        simpleKeylistenerHelper.fire(KeyEvent.VK_TAB);
        verify(mockSimpleKeyListener).tab(mockComponent);
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListenerNullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerInvalidKeyCode() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
        simpleKeyListenerHelper.fire(-1);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeylistenerHelper.fire(KeyEvent.VK_ENTER);
        verify(mockSimpleKeyListener, times(2)).enter(mockComponent);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeylistenerHelper.fire(KeyEvent.VK_TAB);
        verify(mockSimpleKeyListener, times(2)).tab(mockComponent);
    }

    @Test
    public void testKeyPressedBackspace() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeylistenerHelper.fire(KeyEvent.VK_BACK_SPACE);
        verify(mockSimpleKeyListener, times(2)).delete(mockComponent);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeylistenerHelper.fire(KeyEvent.VK_INSERT);
        verify(mockSimpleKeyListener, times(2)).insert(mockComponent);
    }
}