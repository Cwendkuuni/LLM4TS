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
    public void setUp() {
        mockSimpleKeyListener = mock(SimpleKeyListener.class);
        mockComponent = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    // Test constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(mockComponent, simpleKeyListenerHelper.component);
    }

    // Test detach method
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    // Test notifyListener method
    @Test
    public void testNotifyListenerEnter() {
        simpleKeyListenerHelper.fire(10);
        verify(mockSimpleKeyListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        simpleKeyListenerHelper.fire(155);
        verify(mockSimpleKeyListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        simpleKeyListenerHelper.fire(127);
        verify(mockSimpleKeyListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        simpleKeyListenerHelper.fire(9);
        verify(mockSimpleKeyListener).tab(mockComponent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerInvalidKeyCode() {
        simpleKeyListenerHelper.fire(128); // KeyEvent.VK_BACK_SPACE + 1
    }

    // Test keyPressed method
    @Test
    public void testKeyPressedEnter() {
        KeyEvent enterEvent = new KeyEvent(mockComponent, 1, false, 10);
        simpleKeyListenerHelper.keyPressed(enterEvent);
        simpleKeyListenerHelper.fire(10);
        verifyNoMoreInteractions(mockSimpleKeyListener);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent tabEvent = new KeyEvent(mockComponent, 1, false, 9);
        simpleKeyListenerHelper.keyPressed(tabEvent);
        simpleKeyListenerHelper.fire(9);
        verifyNoMoreInteractions(mockSimpleKeyListener);
    }

    @Test
    public void testKeyPressedInvalidKeyCode() {
        KeyEvent invalidKeyCodeEvent = new KeyEvent(mockComponent, 1, false, 128); // KeyEvent.VK_BACK_SPACE + 1
        simpleKeyListenerHelper.keyPressed(invalidKeyCodeEvent);
    }

    // Test keyReleased method
    @Test
    public void testKeyReleased() {
        simpleKeyListenerHelper.keyReleased(new KeyEvent(mockComponent, 1, false, 0));
    }

    // Test keyTyped method
    @Test
    public void testKeyTyped() {
        simpleKeyListenerHelper.keyTyped(new KeyEvent(mockComponent, 1, false, ' '));
    }

    // Test fire method with null listener
    @Test(expected = NullPointerException.class)
    public void testFireNullListener() {
        simpleKeyListenerHelper.fire(10, null);
    }
}