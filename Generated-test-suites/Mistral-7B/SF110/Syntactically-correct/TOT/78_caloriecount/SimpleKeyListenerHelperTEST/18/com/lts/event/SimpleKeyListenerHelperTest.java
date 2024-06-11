package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListenerMock;

    @Before
    public void setUp() {
        simpleKeyListenerMock = mock(SimpleKeyListener.class);
    }

    @Test
    public void testEnterKeyEvent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame()) {
            {
                addKeyListener(simpleKeyListenerMock);
            }
        };
        KeyEvent keyEvent = new KeyEvent(new WindowPeer(), 0, KeyEvent.KEY_PRESSED, ' ');
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(simpleKeyListenerMock).enter(eq(simpleKeyListenerHelper.component));
    }
    
}