package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    
    @Test
    public void testKeyListenersAttach() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
        assert(mockComponent.getKeyListeners()[0] instanceof SimpleKeyListenerHelper);
        simpleKeyListenerHelper.detach();
        assert(mockComponent.getKeyListeners().length == 0);
    }
    
    @Test
    public void testEnterKeyEvent() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
        simpleKeyListenerHelper.fire(10);
        verify(mockListener).enter(mockComponent);
    }
    
    
}