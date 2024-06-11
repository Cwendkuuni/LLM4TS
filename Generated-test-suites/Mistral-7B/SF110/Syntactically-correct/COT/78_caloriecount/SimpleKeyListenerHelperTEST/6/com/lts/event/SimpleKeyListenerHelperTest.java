package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper listenerHelper;
    private SimpleKeyListener simpleKeyListenerSpy;

    @Before
    public void setUp() {
        component = new JFrame();
        simpleKeyListenerSpy = new SimpleKeyListenerSpy();
    }

    // Test constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        assertTrue(listenerHelper instanceof SimpleKeyListenerHelper);
    }

    // Test detach method
    @Test
    public void testDetach() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.detach();
        assertNull(listenerHelper.component);
    }

    // Test notifyListener method with enter event
    @Test
    public void testNotifyListenerEnterEvent() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(simpleKeyListenerSpy);
        KeyEvent enterEvent = new KeyEvent(component, 10, 1, KeyEvent.VK_ENTER, 1);
        listenerHelper.notifyListener(simpleKeyListenerSpy, 10, enterEvent);
        simpleKeyListenerSpy.assertEnterCalled(component);
    }

    // Test notifyListener method with insert event
    @Test
    public void testNotifyListenerInsertEvent() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(simpleKeyListenerSpy);
        KeyEvent insertEvent = new KeyEvent(component, 155, 1, KeyEvent.VK_INSERT, 1);
        listenerHelper.notifyListener(simpleKeyListenerSpy, 155, insertEvent);
        simpleKeyListenerSpy.assertInsertCalled(component);
    }

    // Test notifyListener method with delete event
    @Test
    public void testNotifyListenerDeleteEvent() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(simpleKeyListenerSpy);
        KeyEvent deleteEvent = new KeyEvent(component, 127, 1, KeyEvent.VK_DELETE, 1);
        listenerHelper.notifyListener(simpleKeyListenerSpy, 127, deleteEvent);
        simpleKeyListenerSpy.assertDeleteCalled(component);
    }

    // Test notifyListener method with tab event
    @Test
    public void testNotifyListenerTabEvent() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(simpleKeyListenerSpy);
        KeyEvent tabEvent = new KeyEvent(component, 9, 1, KeyEvent.VK_TAB, 1);
        listenerHelper.notifyListener(simpleKeyListenerSpy, 9, tabEvent);
        simpleKeyListenerSpy.assertTabCalled(component);
    }

    // Test keyPressed method with enter, insert, delete, and tab keys
    @Test
    public void testKeyPressed() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(simpleKeyListenerSpy);

        KeyEvent enterEvent = new KeyEvent(component, 10, 1, KeyEvent.VK_ENTER, 1);
        listenerHelper.keyPressed(enterEvent);
        simpleKeyListenerSpy.assertEnterCalled(component);

        KeyEvent insertEvent = new KeyEvent(component, 155, 1, KeyEvent.VK_INSERT, 1);
        listenerHelper.keyPressed(insertEvent);
        simpleKeyListenerSpy.assertInsertCalled(component);

        KeyEvent deleteEvent = new KeyEvent(component, 127, 1, KeyEvent.VK_DELETE, 1);
        listenerHelper.keyPressed(deleteEvent);
        simpleKeyListenerSpy.assertDeleteCalled(component);

        KeyEvent tabEvent = new KeyEvent(component, 9, 1, KeyEvent.VK_TAB, 1);
        listenerHelper.keyPressed(tabEvent);
        simpleKeyListenerSpy.assertTabCalled(component);
    }

    // Test keyPressed method with other keys
    @Test(expected = IllegalStateException.class)
    public void testKeyPressedOtherKeys() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(simpleKeyListenerSpy);

        KeyEvent otherKeyEvent = new KeyEvent(component, 1, 1, KeyEvent.VK_F1, 1);
        listenerHelper.keyPressed(otherKeyEvent);
    }

    // Test keyReleased method
    @Test
    public void testKeyReleased() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(component, 1, 1, KeyEvent.VK_A, 1);
        listenerHelper.keyReleased(keyEvent);
    }

    // Test keyTyped method
    @Test
    public void testKeyTyped() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(component, 1, 1, KeyEvent.VK_A, 1);
        listenerHelper.keyTyped(keyEvent);
    }

    // Test attach method with null component
    @Test(expected = NullPointerException.class)
    public void testAttachNullComponent() {
        listenerHelper = new SimpleKeyListenerHelper(null);
        listenerHelper.attach(simpleKeyListenerSpy);
    }

    // Test attach method with null listener
    @Test(expected = NullPointerException.class)
    public void testAttachNullListener() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        listenerHelper.attach(null);
    }

    // Test SimpleKeyListenerHelper with multiple listeners
    @Test
    public void testMultipleListeners() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener1 = new SimpleKeyListenerSpy();
        SimpleKeyListener listener2 = new SimpleKeyListenerSpy();

        listenerHelper.attach(listener1);
        listenerHelper.attach(listener2);

        KeyEvent deleteEvent = new KeyEvent(component, 127, 1, KeyEvent.VK_DELETE, 1);
        listenerHelper.notifyListener(simpleKeyListenerSpy, 127, deleteEvent);

        simpleKeyListenerSpy.assertDeleteCalled(component);
        listener2.assertDeleteCalled(component);
    }

    // Test SimpleKeyListenerHelper with detached listener
    @Test
    public void testDetachedListener() {
        listenerHelper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListenerSpy();
        listenerHelper.attach(listener);
        listenerHelper.detach();

        KeyEvent deleteEvent = new KeyEvent(component, 127, 1, KeyEvent.VK_DELETE, 1);
        listenerHelper.notifyListener(listener, 127, deleteEvent);

        assertNull(listener.getInvocationCount());
    }
}