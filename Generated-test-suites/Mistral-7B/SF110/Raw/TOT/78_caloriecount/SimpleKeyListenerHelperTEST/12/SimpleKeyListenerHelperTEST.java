package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListener;
    private Component component;
    
    @Before
    public void setUp() {
        simpleKeyListener = mock(SimpleKeyListener.class);
        component = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.attach();
    }
    
    @Test
    public void testKeyPressedEnter() {
        KeyEvent keyEvent = new KeyEvent(component, 10, true, false, ' ', 0);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(simpleKeyListener).enter(component);
    }
    
    @Test
    public void testKeyPressedTab() {
        KeyEvent keyEvent = new KeyEvent(component, 9, true, false, ' ', 0);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(simpleKeyListener).tab(component);
    }
    
    @Test
    public void testKeyPressedDelete() {
        KeyEvent keyEvent = new KeyEvent(component, 127, true, false, ' ', 0);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(simpleKeyListener).delete(component);
    }
    
    @Test
    public void testKeyPressedInsert() {
        KeyEvent keyEvent = new KeyEvent(component, 155, true, false, ' ', 0);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        verify(simpleKeyListener).insert(component);
    }
    
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        simpleKeyListenerHelper = null;
        // Verify that key listener is removed and component is null
    }
    
    
}