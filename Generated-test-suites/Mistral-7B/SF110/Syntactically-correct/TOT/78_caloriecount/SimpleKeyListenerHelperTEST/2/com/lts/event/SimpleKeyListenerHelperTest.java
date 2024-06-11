package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testKeyListenerAttached() {
        helper.keyPressed(new KeyEvent(component, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_CODE_ENTER, ""));
        verify(listener).enter(component);
    }

    @After
    public void tearDown() {
        helper.detach();
        helper = null;
        listener = null;
        component = null;
    }
    
}