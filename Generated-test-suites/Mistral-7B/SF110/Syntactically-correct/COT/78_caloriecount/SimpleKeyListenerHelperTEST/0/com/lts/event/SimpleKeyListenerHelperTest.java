package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // Any valid Component
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNotNull(helper);
    }

    @Test
    public void testDetachMethod() {
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnterKeyCode() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.fire(10); // Simulate key press event
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsertKeyCode() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.fire(155); // Simulate key press event
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDeleteKeyCode() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.fire(127); // Simulate key press event
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTabKeyCode() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.fire(9); // Simulate tab key press event
        verify(listener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyListenerWithNullListener() {
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = null;
        helper.notifyListener(listener, 10, null); // Simulate enter key press event
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerWithInvalidKeyCode() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 255, null); // Simulate key press event with an invalid key code
    }

    @Test
    public void testKeyPressedMethodWithValidKeyCodes() {
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.keyPressed(new KeyEvent(component, 10, 0, 0)); // Simulate enter key press event
        helper.keyPressed(new KeyEvent(component, 127, 0, 0)); // Simulate delete key press event
        helper.keyPressed(new KeyEvent(component, 155, 0, 0)); // Simulate insert key press event
        helper.keyPressed(new KeyEvent(component, 9, 0, 0)); // Simulate tab key press event
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressedMethodWithInvalidKeyCode() {
        Component component = new JFrame();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.keyPressed(new KeyEvent(component, 255, 0, 0)); // Simulate key press event with an invalid key code
    }
}