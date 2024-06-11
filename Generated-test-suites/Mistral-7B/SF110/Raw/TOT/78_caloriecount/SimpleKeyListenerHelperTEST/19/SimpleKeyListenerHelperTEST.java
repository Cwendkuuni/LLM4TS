package com.lts.event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListener;
    private Component component = mock(Component.class);

    @Before
    public void setUp() {
        simpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testEnterKeyEvent() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 10, 1, 0, new KeyEvent.KeySpec(KeyEvent.VK_ENTER, 0, 0, 0)));
        verify(simpleKeyListener).enter(component);
    }
    
}