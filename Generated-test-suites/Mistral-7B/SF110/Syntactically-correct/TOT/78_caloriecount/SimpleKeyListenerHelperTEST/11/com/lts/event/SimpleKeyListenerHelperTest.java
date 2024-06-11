package com.lts.event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private KeyListener mockListener;

    @Before
    public void setUp() {
        mockListener = mock(KeyListener.class);
        helper = new SimpleKeyListenerHelper(new JFrame()) {
            @Override
            public void fire(int eventCode) {
                mockListener.enter(null); // assuming enter is a valid event for JFrame
            }
        };
        helper.addKeyListener(mockListener);
    }

    @Test
    public void testKeyPressed_EnterKey() {
        KeyEvent enterEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, 1, KeyEvent.VK_ENTER);
        helper.keyPressed(enterEvent);
        verify(mockListener).enter(helper.component);
    }

    @Test
    public void testKeyPressed_OtherKey() {
        KeyEvent otherEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, 1, KeyEvent.VK_A);
        helper.keyPressed(otherEvent);
        // No verification expected as this method does not call the listener for other keys
    }

    @After
    public void tearDown() {
        helper.detach();
    }
    
    
}