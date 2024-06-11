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
        mockListener = Mockito.mock(SimpleKeyListener.class);
        mockComponent = Mockito.mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    // Test constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertEquals(mockComponent, simpleKeyListenerHelper.component);
    }

    // Test notifyListener method
    @Test
    public void testNotifyListenerEnter() {
        simpleKeyListenerHelper.notifyListener(mockListener, 10, null);
        Mockito.verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        simpleKeyListenerHelper.notifyListener(mockListener, 155, null);
        Mockito.verify(mockListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        simpleKeyListenerHelper.notifyListener(mockListener, 127, null);
        Mockito.verify(mockListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        simpleKeyListenerHelper.notifyListener(mockListener, 9, null);
        Mockito.verify(mockListener).tab(mockComponent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerWithNullListener() {
        simpleKeyListenerHelper.notifyListener(null, 10, null);
    }

    // Test keyPressed method
    @Test
    public void testKeyPressedEnter() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_CODE_ENTER, ""));
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
        Mockito.verify(mockListener).enter(mockComponent);
    }

    @Test
    public void testKeyPressedTab() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_CODE_Tab, ""));
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_Tab);
        Mockito.verify(mockListener).tab(mockComponent);
    }

    @Test
    public void testKeyPressedDelete() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_CODE_DELETE, ""));
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
        Mockito.verify(mockListener).delete(mockComponent);
    }

    @Test
    public void testKeyPressedInsert() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_CODE_INSERT, ""));
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_INSERT);
        Mockito.verify(mockListener).insert(mockComponent);
    }

    @Test
    public void testKeyPressedUnknownKey() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 1, System.currentTimeMillis(), 0, 123, ""));
        simpleKeyListenerHelper.fire(123);
        // No verification expected, as no listener method is called for this key code
    }

    // Test detach method
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    // Test fire method
    @Test
    public void testFireEnter() {
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
        // No verification expected, as no listener method is called in this test
    }

    @Test
    public void testFireTab() {
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_Tab);
        // No verification expected, as no listener method is called in this test
    }

    @Test
    public void testFireDelete() {
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
        // No verification expected, as no listener method is called in this test
    }

    @Test
    public void testFireInsert() {
        simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_INSERT);
        // No verification expected, as no listener method is called in this test
    }

    @Test
    public void testFireUnknownKey() {
        simpleKeyListenerHelper.fire(123);
        // No verification expected, as no listener method is called for this key code
    }

    // Test exception handling
    @Test(expected = IllegalStateException.class)
    public void testDetachAfterComponentRemoved() {
        simpleKeyListenerHelper.component = null;
        simpleKeyListenerHelper.detach();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFireWithNullListener() {
        simpleKeyListenerHelper.fire(null);
    }
}