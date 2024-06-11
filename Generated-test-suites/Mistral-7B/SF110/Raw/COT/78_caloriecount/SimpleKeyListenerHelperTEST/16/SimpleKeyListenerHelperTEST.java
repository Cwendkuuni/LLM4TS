package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component testComponent;
    private SimpleKeyListener testListener;

    @Before
    public void setUp() {
        testComponent = new JFrame();
        testListener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                // Implementation not required for testing
            }

            @Override
            public void insert(Component component) {
                // Implementation not required for testing
            }

            @Override
            public void delete(Component component) {
                // Implementation not required for testing
            }

            @Override
            public void tab(Component component) {
                // Implementation not required for testing
            }
        };
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        assertNotNull(simpleKeyListenerHelper);
        assertSame(testComponent, simpleKeyListenerHelper.component);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.fire(10);
        testListener.enter(testComponent);
        assertTrue(simpleKeyListenerHelper.isListenerNotified(testListener, 10));
    }

    @Test
    public void testNotifyListenerInsert() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.fire(155);
        testListener.insert(testComponent);
        assertTrue(simpleKeyListenerHelper.isListenerNotified(testListener, 155));
    }

    @Test
    public void testNotifyListenerDelete() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.fire(127);
        testListener.delete(testComponent);
        assertTrue(simpleKeyListenerHelper.isListenerNotified(testListener, 127));
    }

    @Test
    public void testNotifyListenerTab() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.fire(9);
        testListener.tab(testComponent);
        assertTrue(simpleKeyListenerHelper.isListenerNotified(testListener, 9));
    }

    @Test(expected = NullPointerException.class)
    public void testKeyPressedNullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        KeyEvent keyEvent = new KeyEvent(null, 1, true, 'A');
        simpleKeyListenerHelper.keyPressed(keyEvent);
    }

    @Test
    public void testKeyPressedNonSupportedKeyCode() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        KeyEvent keyEvent = new KeyEvent(testComponent, 1, true, 'A');
        simpleKeyListenerHelper.keyPressed(keyEvent);
    }

    @Test
    public void testKeyPressedSupportedKeyCode() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        KeyEvent keyEvent = new KeyEvent(testComponent, 10, true, 'A');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        simpleKeyListenerHelper.fire(10);
        assertTrue(simpleKeyListenerHelper.isListenerNotified(testListener, 10));
    }
}