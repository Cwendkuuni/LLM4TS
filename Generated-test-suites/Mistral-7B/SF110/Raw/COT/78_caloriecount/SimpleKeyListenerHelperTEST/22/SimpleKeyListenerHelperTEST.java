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

    // Testing constructor and component attachment
    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(mockComponent, simpleKeyListenerHelper.getComponent());
    }

    // Testing detach method
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    // Testing enter case in notifyListener method
    @Test
    public void testEnter() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 10, null);
        verify(mockSimpleKeyListener).enter(mockComponent);
    }

    // Testing insert case in notifyListener method
    @Test
    public void testInsert() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 155, null);
        verify(mockSimpleKeyListener).insert(mockComponent);
    }

    // Testing delete case in notifyListener method
    @Test
    public void testDelete() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 127, null);
        verify(mockSimpleKeyListener).delete(mockComponent);
    }

    // Testing tab case in notifyListener method
    @Test
    public void testTab() {
        simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 9, null);
        verify(mockSimpleKeyListener).tab(mockComponent);
    }

    // Testing keyPressed method with valid key codes
    @Test
    public void testKeyPressed_ValidKeyCodes() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 9, true, false));
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 10, true, false));
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 127, true, false));
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 155, true, false));
        for (int i = 0; i < 4; i++) {
            simpleKeyListenerHelper.fire(i);
            verify(mockSimpleKeyListener, times(1)).keyPressed(eq(i));
        }
    }

    // Testing keyPressed method with invalid key code
    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressed_InvalidKeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 500, true, false));
    }

    // Testing keyReleased method
    @Test
    public void testKeyReleased() {
        simpleKeyListenerHelper.keyReleased(new KeyEvent(mockComponent, 1, true, false));
        // No verification needed as this method is empty in the given class
    }

    // Testing keyTyped method
    @Test
    public void testKeyTyped() {
        simpleKeyListenerHelper.keyTyped(new KeyEvent(mockComponent, 1, true, false));
        // No verification needed as this method is empty in the given class
    }
}