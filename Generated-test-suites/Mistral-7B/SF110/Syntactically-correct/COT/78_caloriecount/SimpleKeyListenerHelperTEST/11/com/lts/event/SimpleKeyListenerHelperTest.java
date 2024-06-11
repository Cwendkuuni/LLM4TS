package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
    }

    // Test constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(simpleKeyListenerHelper.getComponent());
    }

    // Test detach method
    @Test
    public void testDetach() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.detach();
        verify(component, times(1)).removeKeyListener(simpleKeyListenerHelper);
    }

    // Test notifyListener method with valid key codes
    @Test
    public void testNotifyListenerWithValidKeyCodes() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(9); // Tab key
        verify(simpleKeyListener, times(1)).tab(component);

        simpleKeylistenerHelper.fire(10); // Enter key
        verify(simpleKeyListener, times(1)).enter(component);

        simpleKeylistenerHelper.fire(127); // Delete key
        verify(simpleKeyListener, times(1)).delete(component);

        simpleKeylistenerHelper.fire(155); // Insert key
        verify(simpleKeyListener, times(1)).insert(component);
    }

    // Test notifyListener method with invalid key code
    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerWithInvalidKeyCode() {
        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.fire(128); // Backspace key is not a valid key code for SimpleKeyListenerHelper
    }

    // Test keyPressed method with valid key codes
    @Test
    public void testKeyPressedWithValidKeyCodes() {
        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_DOWN, System.currentTimeMillis(), 0, 9, isKeyEvent(true)));
        verify(simpleKeylistener, times(1)).tab(component);

        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_DOWN, System.currentTimeMillis(), 0, 10, isKeyEvent(true)));
        verify(simpleKeylistener, times(1)).enter(component);

        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_DOWN, System.currentTimeMillis(), 0, 127, isKeyEvent(true)));
        verify(simpleKeylistener, times(1)).delete(component);

        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_DOWN, System.currentTimeMillis(), 0, 155, isKeyEvent(true)));
        verify(simpleKeylistener, times(1)).insert(component);
    }

    // Test keyPressed method with invalid key code
    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressedWithInvalidKeyCode() {
        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_DOWN, System.currentTimeMillis(), 0, 128, isKeyEvent(true)));
    }

    // Test keyReleased method
    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyReleased(new KeyEvent(component, KeyEvent.KEY_UP, System.currentTimeMillis(), 0, 1, isKeyEvent(true)));
        // No verification required as keyReleased method doesn't call any listener methods
    }

    // Test keyTyped method
    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeylistenerHelper.keyTyped(new KeyEvent(component, KeyEvent.KEY_TYPED, System.currenttimeMillis(), 0, 'A', isKeyEvent(true)));
        // No verification required as keyTyped method doesn't call any listener methods
    }

    // Test getComponent method
    @Test
    public void testGetComponent() {
        simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
        Component returnedComponent = simpleKeylistenerHelper.getComponent();
        assertEquals(component, returnedComponent);
    }

    // Test isKeyEvent method
    private boolean isKeyEvent(boolean isConsumed) {
        KeyEvent dummyKeyEvent = mock(KeyEvent.class);
        when(dummyKeyEvent.isConsumed()).thenReturn(isConsumed);
        return dummyKeyEvent;
    }

    // Test fire method null component
    @Test(expected = NullPointerException.class)
    public void testFireWithNullComponent() {
        SimpleKeyListenerHelper simpleKeylistenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeylistenerHelper.fire(10);
    }
}