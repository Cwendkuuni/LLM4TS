package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListener;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        simpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testEnterKey() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 10, true, false, ' '));
        verify(simpleKeyListener).enter(component);
    }

    @After
    public void tearDown() {
        simpleKeyListenerHelper.detach();
    }
    
}