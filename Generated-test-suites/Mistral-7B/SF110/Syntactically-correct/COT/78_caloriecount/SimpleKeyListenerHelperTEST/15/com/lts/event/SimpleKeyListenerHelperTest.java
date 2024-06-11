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

    // Test constructor method
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

    // Test notifyListener method - enter key
    @Test
    public void testNotifyListenerEnter() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 10, null);
        verify(mockSimpleKeyListener).enter(mockComponent);
    }

    // Test notifyListener method - insert key
    @Test
    public void testNotifyListenerInsert() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 155, null);
        verify(mockSimpleKeyListener).insert(mockComponent);
    }

    // Test notifyListener method - delete key
    @Test
    public void testNotifyListenerDelete() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 127, null);
        verify(mockSimpleKeyListener).delete(mockComponent);
    }

    // Test notifyListener method - tab key
    @Test
    public void testNotifyListenerTab() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 9, null);
        verify(mockSimpleKeyListener).tab(mockComponent);
    }

    // Test keyPressed method - enter key
    @Test
    public void testKeyPressedEnter() {
        KeyEvent enterEvent = new KeyEvent(mockComponent, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, ' ');
        simpleKeyListenerHelper.keyPressed(enterEvent);
        simpleKeyListenerHelper.fire(enterEvent.getKeyCode());
        verify(mockSimpleKeyListener).enter(mockComponent);
    }

    // Test keyPressed method - insert key
    @Test
    public void testKeyPressedInsert() {
        KeyEvent insertEvent = new KeyEvent(mockComponent, 155, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, ' ');
        simpleKeyListenerHelper.keyPressed(insertEvent);
        simpleKeyListenerHelper.fire(insertEvent.getKeyCode());
        verify(mockSimpleKeyListener).insert(mockComponent);
    }

    // Test keyPressed method - delete key
    @Test
    public void testKeyPressedDelete() {
        KeyEvent deleteEvent = new KeyEvent(mockComponent, 127, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, ' ');
        simpleKeyListenerHelper.keyPressed(deleteEvent);
        simpleKeyListenerHelper.fire(deleteEvent.getKeyCode());
        verify(mockSimpleKeyListener).delete(mockComponent);
    }

    // Test keyPressed method - tab key
    @Test
    public void testKeyPressedTab() {
        KeyEvent tabEvent = new KeyEvent(mockComponent, 9, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, ' ');
        simpleKeyListenerHelper.keyPressed(tabEvent);
        simpleKeyListenerHelper.fire(tabEvent.getKeyCode());
        verify(mockSimpleKeyListener).tab(mockComponent);
    }

    // Test keyPressed method - other keys
    @Test
    public void testKeyPressedOtherKeys() {
        KeyEvent otherKeyEvent = new KeyEvent(mockComponent, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, ' ');
        simpleKeyListenerHelper.keyPressed(otherKeyEvent);
        simpleKeyListenerHelper.fire(otherKeyEvent.getKeyCode());
        // No action expected for other keys
        verifyNoMoreInteractions(mockSimpleKeyListener);
    }

    // Test keyReleased method
    @Test
    public void testKeyReleased() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_RELEASED, ' ');
        simpleKeyListenerHelper.keyReleased(keyEvent);
        // No action expected for keyReleased
        verifyNoMoreInteractions(mockSimpleKeyListener);
    }

    // Test keyTyped method
    @Test
    public void testKeyTyped() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_TYPED, ' ');
        simpleKeyListenerHelper.keyTyped(keyEvent);
        // No action expected for keyTyped
        verifyNoMoreInteractions(mockSimpleKeyListener);
    }

    // Test fire method - enter key
    @Test(expected = IllegalArgumentException.class)
    public void testFireEnterInvalidListener() {
        SimpleKeyListener invalidListener = null;
        simpleKeyListenerHelper.fire(10);
    }

    // Test fire method - enter key
    @Test
    public void testFireEnterValidListener() {
        simpleKeyListenerHelper.fire(10);
        verify(mockSimpleKeyListener).enter(mockComponent);
    }

    // Test fire method - insert key
    @Test
    public void testFireInsertValidListener() {
        simpleKeyListenerHelper.fire(155);
        verify(mockSimpleKeyListener).insert(mockComponent);
    }

    // Test fire method - delete key
    @Test
    public void testFireDeleteValidListener() {
        simpleKeyListenerHelper.fire(127);
        verify(mockSimpleKeyListener).delete(mockComponent);
    }

    // Test fire method - tab key
    @Test
    public void testFireTabValidListener() {
        simpleKeyListenerHelper.fire(9);
        verify(mockSimpleKeyListener).tab(mockComponent);
    }

    // Test fire method - other keys
    @Test(expected = IllegalArgumentException.class)
    public void testFireOtherKeysInvalidListener() {
        SimpleKeyListener invalidListener = null;
        simpleKeyListenerHelper.fire(1);
    }
}