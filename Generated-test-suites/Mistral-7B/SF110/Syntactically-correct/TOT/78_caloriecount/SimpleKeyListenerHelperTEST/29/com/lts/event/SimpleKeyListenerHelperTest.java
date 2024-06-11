package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListener;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        simpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(simpleKeyListener);
    }

    @Test
    public void testEnterKeyEvent() {
        KeyEvent keyEvent = new KeyEvent(component, 10, true, KeyEvent.KEY_PRESSED, 0, 0, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(simpleKeyListener).enter(component);
    }
    
}