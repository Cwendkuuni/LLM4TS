package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListener;
    private JFrame frame;

    @Before
    public void setUp() {
        simpleKeyListener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                // implementation not relevant for testing
            }

            @Override
            public void insert(Component component) {
                // implementation not relevant for testing
            }

            @Override
            public void delete(Component component) {
                // implementation not relevant for testing
            }

            @Override
            public void tab(Component component) {
                // implementation not relevant for testing
            }
        };

        frame = new JFrame();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testComponentAddKeyListener() {
        assertNotNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListener() {
        simpleKeyListenerHelper.fire(10);
        verify(simpleKeyListener).enter(simpleKeyListenerHelper.component);

        simpleKeyListenerHelper.fire(155);
        verify(simpleKeyListener).insert(simpleKeyListenerHelper.component);

        simpleKeyListenerHelper.fire(127);
        verify(simpleKeyListener).delete(simpleKeyListenerHelper.component);

        simpleKeyListenerHelper.fire(9);
        verify(simpleKeyListener).tab(simpleKeyListenerHelper.component);
    }

    @Test(expected = NullPointerException.class)
    public void testKeyPressedNullComponent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
        helper.keyPressed(new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, 0, 0, 'a', 0));
    }

    @Test
    public void testKeyPressedValidKeyCodes() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.component, KeyEvent.KEY_PRESSED, 0, 0, 'a', KeyEvent.VK_TAB));
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(simpleKeyListener).tab(simpleKeyListenerHelper.component);

        simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.component, KeyEvent.KEY_PRESSED, 0, 0, 'a', KeyEvent.VK_ENTER));
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        verify(simpleKeyListener).enter(simpleKeyListenerHelper.component);

        simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.component, KeyEvent.KEY_PRESSED, 0, 0, 'a', KeyEvent.VK_BACK_SPACE));
        simpleKeyListenerHelper.fire(KeyEvent.VK_BACK_SPACE);
        verify(simpleKeyListener).delete(simpleKeyListenerHelper.component);

        simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.component, KeyEvent.KEY_PRESSED, 0, 0, 'a', KeyEvent.VK_INSERT));
        simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
        verify(simpleKeyListener).insert(simpleKeyListenerHelper.component);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFireInvalidKeyCode() {
        simpleKeyListenerHelper.fire(128);
    }
}