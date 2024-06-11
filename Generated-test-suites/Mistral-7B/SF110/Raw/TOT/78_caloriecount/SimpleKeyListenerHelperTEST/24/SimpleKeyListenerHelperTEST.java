package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    
    @Test
    public void testKeyListenerHelper() {
        Component component = new JFrame(); // Assuming JFrame is a valid component type for this test
        SimpleKeyListenerHelper keyListenerHelper = new SimpleKeyListenerHelper(component);
        keyListenerHelper.detach();
        assertNull(keyListenerHelper.component);
    }

    @Test
    public void testKeyPressedEnter() {
        Component component = new JFrame();
        SimpleKeyListenerHelper keyListenerHelper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(new Component(){}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0);
        keyListenerHelper.keyPressed(keyEvent);
        assertTrue(keyListenerHelper.component.isFocusOwnerForFocusTraversal()); // Assuming focus is a desirable state for the component
        keyListenerHelper.detach();
    }

    @Test
    public void testKeyPressedDelete() {
        Component component = new JFrame();
        SimpleKeyListenerHelper keyListenerHelper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(new Component(){}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, 0);
        keyListenerHelper.keyPressed(keyEvent);
        assertEquals(KeyEvent.CHAR_UNDEFINED, keyEvent.getKeyChar()); // Test for the default behavior when no character is defined for a specific key
        keyListenerHelper.detach();
    }

    @Test
    public void testKeyPressedTab() {
        Component component1 = new JFrame();
        Component component2 = new JFrame();
        SimpleKeyListenerHelper keyListenerHelper1 = new SimpleKeyListenerHelper(component1);
        SimpleKeyListenerHelper keyListenerHelper2 = new SimpleKeyListenerHelper(component2);
        KeyEvent keyEvent = new KeyEvent(new Component(){}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, 0);
        keyListenerHelper1.keyPressed(keyEvent);
        assertFalse(component1.isFocusOwnerForFocusTraversal());
        keyListenerHelper2.component.requestFocus();
        keyListenerHelper2.keyPressed(keyEvent);
        assertTrue(component2.isFocusOwnerForFocusTraversal());
        keyListenerHelper1.detach();
        keyListenerHelper2.detach();
    }

    
}