package com.lts.event;

import org.junit.Assert;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    // Test case for constructor
    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        SimpleKeyListenerHelper helper = new SimpleKeyKeyListenerHelper(new JFrame());
        Assert.assertNotNull(helper);
    }

    // Test case for keyPressed method with valid key codes
    @Test
    public void testKeyPressedValidKeyCodes() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame());
        KeyEvent keyEvent = new KeyEvent(new JFrame(), 1, KeyEvent.KEY_PRESSED, 0);
        helper.keyPressed(keyEvent);
        helper.fire(keyEvent.getKeyCode());
    }

    // Test case for keyPressed method with invalid key code
    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressedInvalidKeyCode() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame());
        KeyEvent keyEvent = new KeyEvent(new JFrame(), 1, KeyEvent.KEY_PRESSED, 0);
        helper.keyPressed(new KeyEvent(new JFrame(), 1, KeyEvent.KEY_PRESSED, 0, (short) 256, ' '));
    }

    // Test case for detach method
    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame());
        helper.detach();
        Assert.assertNull(helper.component);
    }
}