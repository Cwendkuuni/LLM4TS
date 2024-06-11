package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private Component testComponent;
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener testListener;

    @Before
    public void setUp() {
        testComponent = new Button();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        testListener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                // implementation not needed for testing
            }

            @Override
            public void insert(Component component) {
                // implementation not needed for testing
            }

            @Override
            public void delete(Component component) {
                // implementation not needed for testing
            }

            @Override
            public void tab(Component component) {
                // implementation not needed for testing
            }
        };
        simpleKeyListenerHelper.addListener(testListener);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testAddListener() {
        assertNotNull(simpleKeyListenerHelper.getListeners().get(0));
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getListeners());
    }

    @Test
    public void testNotifyListener_EnterKey_ValidComponent() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 10, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 10, keyEvent);
        assertTrue(testListener.isEnterCalled());
    }

    @Test
    public void testNotifyListener_EnterKey_NullComponent() {
        simpleKeyListenerHelper.component = null;
        KeyEvent keyEvent = new KeyEvent(null, 10, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 10, keyEvent);
        assertFalse(testListener.isEnterCalled());
    }

    @Test
    public void testNotifyListener_InsertKey_ValidComponent() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 155, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 155, keyEvent);
        assertTrue(testListener.isInsertCalled());
    }

    @Test
    public void testNotifyListener_InsertKey_NullComponent() {
        simpleKeyListenerHelper.component = null;
        KeyEvent keyEvent = new KeyEvent(null, 155, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 155, keyEvent);
        assertFalse(testListener.isInsertCalled());
    }

    @Test
    public void testNotifyListener_DeleteKey_ValidComponent() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 127, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 127, keyEvent);
        assertTrue(testListener.isDeleteCalled());
    }

    @Test
    public void testNotifyListener_DeleteKey_NullComponent() {
        simpleKeyListenerHelper.component = null;
        KeyEvent keyEvent = new KeyEvent(null, 127, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 127, keyEvent);
        assertFalse(testListener.isDeleteCalled());
    }

    @Test
    public void testNotifyListener_TabKey_ValidComponent() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 9, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 9, keyEvent);
        assertTrue(testListener.isTabCalled());
    }

    @Test
    public void testNotifyListener_TabKey_NullComponent() {
        simpleKeyListenerHelper.component = null;
        KeyEvent keyEvent = new KeyEvent(null, 9, true, false, ' ');
        simpleKeyListenerHelper.notifyListener(testListener, 9, keyEvent);
        assertFalse(testListener.isTabCalled());
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListener_NullListener() {
        simpleKeyListenerHelper.notifyListener(null, 10, new KeyEvent(testComponent, 10, true, false, ' '));
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListener_NullComponent() {
        simpleKeyListenerHelper.component = null;
        simpleKeyListenerHelper.notifyListener(testListener, 10, new KeyEvent(null, 10, true, false, ' '));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListener_InvalidType() {
        simpleKeyListenerHelper.notifyListener(testListener, 20, new KeyEvent(testComponent, 10, true, false, ' '));
    }

    @Test
    public void testKeyPressed_EnterKey() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 10, true, false, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        assertTrue(testListener.isEnterCalled());
    }

    @Test
    public void testKeyPressed_InsertKey() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 155, true, false, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        assertTrue(testListener.isInsertCalled());
    }

    @Test
    public void testKeyPressed_DeleteKey() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 127, true, false, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        assertTrue(testListener.isDeleteCalled());
    }

    @Test
    public void testKeyPressed_TabKey() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 9, true, false, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        assertTrue(testListener.isTabCalled());
    }

    @Test
    public void testKeyPressed_OtherKey() {
        KeyEvent keyEvent = new KeyEvent(testComponent, 20, true, false, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        assertFalse(testListener.isEnterCalled());
        assertFalse(testListener.isInsertCalled());
        assertFalse(testListener.isDeleteCalled());
        assertFalse(testListener.isTabCalled());
    }
}