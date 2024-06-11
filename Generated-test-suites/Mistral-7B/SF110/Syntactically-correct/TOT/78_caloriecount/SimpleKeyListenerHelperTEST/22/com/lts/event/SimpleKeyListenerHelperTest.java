package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockListener;

    @Before
    public void setup() {
        mockListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
        simpleKeyListenerHelper.addListener(mockListener);
    }

    @Test
    public void testEnter() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED, 0, false));
        verify(mockListener).enter(simpleKeyListenerHelper.getComponent());
    }
    
}