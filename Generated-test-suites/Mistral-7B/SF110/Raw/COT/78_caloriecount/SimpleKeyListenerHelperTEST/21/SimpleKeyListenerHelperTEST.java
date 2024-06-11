package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component testComponent;
    private SimpleKeyListener testSimpleKeyListener;

    @Before
    public void setUp() {
        testComponent = new Frame();
        testSimpleKeyListener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                // Implement this method as per your requirement
            }

            @Override
            public void insert(Component component) {
                // Implement this method as per your requirement
            }

            @Override
            public void delete(Component component) {
                // Implement this method as per your requirement
            }

            @Override
            public void tab(Component component) {
                // Implement this method as per your requirement
            }
        };
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent enterEvent = new KeyEvent(testComponent, 10, 0, 'A');
        simpleKeyListenerHelper.notifyListener(testSimpleKeyListener, 10, enterEvent);
        assertTrue(testSimpleKeyListener.isEnterEventFired());
    }

    @Test
    public void testNotifyListener_Insert() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent insertEvent = new KeyEvent(testComponent, 155, 0, 'A');
        simpleKeyListenerHelper.notifyListener(testSimpleKeyListener, 155, insertEvent);
        assertTrue(testSimpleKeyListener.isInsertEventFired());
    }

    @Test
    public void testNotifyListener_Delete() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent deleteEvent = new KeyEvent(testComponent, 127, 0, 'A');
        simpleKeyListenerHelper.notifyListener(testSimpleKeyListener, 127, deleteEvent);
        assertTrue(testSimpleKeyListener.isDeleteEventFired());
    }

    @Test
    public void testNotifyListener_Tab() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent tabEvent = new KeyEvent(testComponent, 9, 0, 'A');
        simpleKeyListenerHelper.notifyListener(testSimpleKeyListener, 9, tabEvent);
        assertTrue(testSimpleKeyListener.isTabEventFired());
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListener_NullListener() {
        simpleKeyListenerHelper.notifyListener(null, 10, new KeyEvent(testComponent, 10, 0, 'A'));
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListener_NullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        simpleKeyListenerHelper.notifyListener(testSimpleKeyListener, 10, new KeyEvent(testComponent, 10, 0, 'A'));
    }

    @Test
    public void testKeyPressed_Enter() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent enterEvent = new KeyEvent(testComponent, 10, 0, 'A');
        simpleKeyListenerHelper.keyPressed(enterEvent);
        assertTrue(testSimpleKeyListener.isEnterEventFired());
    }

    @Test
    public void testKeyPressed_Insert() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent insertEvent = new KeyEvent(testComponent, 155, 0, 'A');
        simpleKeyListenerHelper.keyPressed(insertEvent);
        assertTrue(testSimpleKeyListener.isInsertEventFired());
    }

    @Test
    public void testKeyPressed_Delete() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent deleteEvent = new KeyEvent(testComponent, 127, 0, 'A');
        simpleKeyListenerHelper.keyPressed(deleteEvent);
        assertTrue(testSimpleKeyListener.isDeleteEventFired());
    }

    @Test
    public void testKeyPressed_Tab() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent tabEvent = new KeyEvent(testComponent, 9, 0, 'A');
        simpleKeyListenerHelper.keyPressed(tabEvent);
        assertTrue(testSimpleKeyListener.isTabEventFired());
    }

    @Test
    public void testKeyPressed_OtherKeycodes() {
        simpleKeyListenerHelper.addListener(testSimpleKeyListener);
        KeyEvent otherKeyEvent = new KeyEvent(testComponent, 1, 0, 'A');
        simpleKeyListenerHelper.keyPressed(otherKeyEvent);
        assertFalse(testSimpleKeyListener.isEnterEventFired());
        assertFalse(testSimpleKeyListener.isInsertEventFired());
        assertFalse(testSimpleKeyListener.isDeleteEventFired());
        assertFalse(testSimpleKeyListener.isTabEventFired());
    }
}