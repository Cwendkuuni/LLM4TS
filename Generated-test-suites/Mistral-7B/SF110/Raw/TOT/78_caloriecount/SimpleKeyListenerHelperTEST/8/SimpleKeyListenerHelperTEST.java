package com.lts.event;

import org.junit.*;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {
    private static SimpleKeyListener listener;
    private static SimpleKeyListenerHelper helper;

    @BeforeClass
    public static void beforeClass() {
        listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                Assert.assertEquals(component, helper.component);
            }

            @Override
            public void keyPressed(KeyEvent event) {
                Assert.assertEquals(event.getKeyCode(), helper.eventKeyCode);
            }
        };
    }

    @AfterClass
    public static void afterClass() {
        helper = null;
    }

    @Before
    public void before() {
        helper = new SimpleKeyListenerHelper(new JFrame()) {
            @Override
            public void fire(int keyCode) {
                super.fire(new KeyEvent(helper.component, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
                eventKeyCode = keyCode;
            }
        };
        helper.addListener(listener);
    }

    @After
    public void after() {
        if (helper != null) {
            helper.detach();
        }
    }

    @Test
    public void testConstructor() {
        assertEquals(helper.getClass(), SimpleKeyListenerHelper.class);
        assertNotNull(helper.component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        helper.component.dispatchEvent(new KeyEvent(helper.component, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertTrue(listener.isEnterEventFired);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerInsert() {
        // Add test case for insert key event here
    }

    @Test
    public void testNotifyListenerDelete() {
        // Add test case for delete key event here
    }

    @Test
    public void testNotifyListenerTab() {
        // Add test case for tab key event here
    }

    @Test
    public void testKeyPressed() {
        helper.component.dispatchEvent(new KeyEvent(helper.component, 9, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertEquals(9, helper.eventKeyCode);
        helper.component.dispatchEvent(new KeyEvent(helper.component, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertEquals(10, helper.eventKeyCode);
        helper.component.dispatchEvent(new KeyEvent(helper.component, 127, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertEquals(127, helper.eventKeyCode);
        helper.component.dispatchEvent(new KeyEvent(helper.component, 155, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertEquals(155, helper.eventKeyCode);
    }

    @Test
    public void testKeyPressedInvalidKeyCode() {
        helper.component.dispatchEvent(new KeyEvent(helper.component, 128, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertFalse(listener.isEnterEventFired);
    }

    @Test
    public void testDetachAndReattachListener() {
        helper.component.dispatchEvent(new KeyEvent(helper.component, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertTrue(listener.isEnterEventFired);
        helper.detach();
        assertNull(helper.component);
        helper = new SimpleKeyListenerHelper(new JFrame()) {
            @Override
            public void fire(int keyCode) {
                super.fire(new KeyEvent(helper.component, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
                eventKeyCode = keyCode;
            }
        };
        helper.addListener(listener);
        helper.component.dispatchEvent(new KeyEvent(helper.component, 10, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, new Character(' ').charValue()));
        assertTrue(listener.isEnterEventFired);
    }

    @Test
    public void testKeyReleased() {
        // Add test case for keyReleased event here
    }

    @Test
    public void testKeyTyped() {
        // Add test case for keyTyped event here
    }

    private int eventKeyCode;
}