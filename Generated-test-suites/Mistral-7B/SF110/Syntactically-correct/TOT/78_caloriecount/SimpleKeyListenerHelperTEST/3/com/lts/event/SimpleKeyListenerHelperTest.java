package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockListener;

    @Before
    public void setUp() {
        mockListener = mock(SimpleKeyListener.class);
    }

    @Test
    public void testEnterKeyEvent() {
        SimpleComponent component = new SimpleComponent();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(mockListener);
        KeyEvent enterEvent = new KeyEvent(component, 10, true, KeyEvent.CHAR_UNDEFINED, 0L);
        simpleKeyListenerHelper.keyPressed(enterEvent);
        verify(mockListener).enter(component);
    }
    
}