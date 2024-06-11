package com.lts.event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {
    
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockSimpleKeyListener;

    @Before
    public void setUp() {
        mockSimpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame()) {
            @Override
            public void fire(int keyCode) {
                mockSimpleKeyListener.keyEventReceived(keyCode);
            }
        };
        simpleKeyListenerHelper.attach();
    }

    @After
    public void tearDown() {
        simpleKeyListenerHelper.detach();
    }

    @Test
    public void testEnterKey() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(new Component() {}, 10, 0, 0, 0, KeyEvent.KEY_PRESSED));
        verify(mockSimpleKeyListener).enter(simpleKeyListenerHelper.component);
    }

    
}