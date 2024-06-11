package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setup() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(component);
        helper.addListener(listener);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent enterEvent = new KeyEvent(component, 10, true, false, KeyEvent.KEY_ENTER, 'Enter');
        helper.keyPressed(enterEvent);
        verify(listener).enter(component);
    }

    
}