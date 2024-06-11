package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;

    @Before
    public void setUp() {
        listener = mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(new JFrame());
        helper.setListener(listener);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent keyEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "Enter Key");
        helper.keyPressed(keyEvent);
        verify(listener).enter(helper.getComponent());
    }
    
}