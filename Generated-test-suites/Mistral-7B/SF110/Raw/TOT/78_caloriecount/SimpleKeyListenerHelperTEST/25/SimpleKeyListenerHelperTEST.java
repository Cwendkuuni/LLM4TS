package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    
    @Test
    public void testEnterKey() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = mock(SimpleKeyListener.class);

        helper.notifyListener(listener, 10, null);
        verify(listener).enter(component);

        helper.detach();
    }

    
}