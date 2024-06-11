package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame());
        helper.detach();
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListener_Enter_KeyEvent() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()) {
            @Override
            public void fire(int keyCode) {
                listener.enter(this.component);
            }
        };
        KeyEvent keyEvent = new KeyEvent(new JFrame(), KeyEvent.KEY_PRESSED, 1, 'A');
        helper.keyPressed(keyEvent);
        verify(listener).enter(helper.component);
    }
    
}